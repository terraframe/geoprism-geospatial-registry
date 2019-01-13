package net.geoprism.georegistry.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.FilenameUtils;
import org.commongeoregistry.adapter.dataaccess.GeoObject;
import org.commongeoregistry.adapter.metadata.AttributeBooleanType;
import org.commongeoregistry.adapter.metadata.AttributeDateType;
import org.commongeoregistry.adapter.metadata.GeoObjectType;
import org.geotools.data.FeatureSource;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.GeometryDescriptor;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.runwaysdk.RunwayException;
import com.runwaysdk.business.SmartException;
import com.runwaysdk.constants.VaultProperties;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.session.Request;
import com.runwaysdk.session.RequestType;
import com.runwaysdk.system.gis.geo.GeoEntity;
import com.runwaysdk.system.gis.geo.GeoEntityQuery;
import com.runwaysdk.system.gis.geo.Universal;

import net.geoprism.georegistry.io.GeoObjectConfiguration;
import net.geoprism.georegistry.shapefile.GeoObjectShapefileExporter;
import net.geoprism.georegistry.shapefile.GeoObjectShapefileImporter;
import net.geoprism.georegistry.shapefile.NullLogger;
import net.geoprism.gis.geoserver.SessionPredicate;

public class ShapefileService
{
  @Request(RequestType.SESSION)
  public JsonObject getShapefileConfiguration(String sessionId, String type, String fileName, InputStream fileStream)
  {
    // Save the file to the file system
    try
    {
      GeoObjectType geoObjectType = ServiceFactory.getAdapter().getMetadataCache().getGeoObjectType(type).get();

      String name = SessionPredicate.generateId();

      File root = new File(new File(VaultProperties.getPath("vault.default"), "files"), name);
      root.mkdirs();

      File directory = new File(root, FilenameUtils.getBaseName(fileName));
      directory.mkdirs();

      this.extract(fileStream, directory);

      File[] dbfs = directory.listFiles(new FilenameFilter()
      {
        @Override
        public boolean accept(File dir, String name)
        {
          return name.endsWith(".dbf");
        }
      });

      if (dbfs.length > 0)
      {
        JsonObject object = new JsonObject();
        object.add("type", this.getType(geoObjectType));
        object.add("sheet", this.getSheetInformation(dbfs[0]));
        object.addProperty("directory", root.getName());
        object.addProperty("filename", fileName);

        return object;
      }
      else
      {
        // TODO Change exception type
        throw new ProgrammingErrorException("Zip file does not contain a valid shapefile");
      }

    }
    catch (RunwayException | SmartException e)
    {
      throw e;
    }
    catch (Exception e)
    {
      throw new ProgrammingErrorException(e);
    }
  }

  private JsonObject getType(GeoObjectType geoObjectType)
  {
    JsonObject type = geoObjectType.toJSON();
    JsonArray attributes = type.get("attributes").getAsJsonArray();

    for (int i = 0; i < attributes.size(); i++)
    {
      JsonObject attribute = attributes.get(i).getAsJsonObject();
      String attributeType = attribute.get("type").getAsString();

      attribute.addProperty("baseType", GeoObjectConfiguration.getBaseType(attributeType));
    }

    return type;
  }

  private JsonObject getSheetInformation(File dbf)
  {
    try
    {
      ShapefileDataStore store = new ShapefileDataStore(dbf.toURI().toURL());

      try
      {
        String[] typeNames = store.getTypeNames();

        if (typeNames.length > 0)
        {
          String typeName = typeNames[0];

          FeatureSource<SimpleFeatureType, SimpleFeature> source = store.getFeatureSource(typeName);

          SimpleFeatureType schema = source.getSchema();

          List<AttributeDescriptor> descriptors = schema.getAttributeDescriptors();

          JsonObject attributes = new JsonObject();
          attributes.add(AttributeBooleanType.TYPE, new JsonArray());
          attributes.add(GeoObjectConfiguration.TEXT, new JsonArray());
          attributes.add(GeoObjectConfiguration.NUMERIC, new JsonArray());
          attributes.add(AttributeDateType.TYPE, new JsonArray());

          for (AttributeDescriptor descriptor : descriptors)
          {
            if (! ( descriptor instanceof GeometryDescriptor ))
            {
              String name = descriptor.getName().getLocalPart();
              String baseType = GeoObjectConfiguration.getBaseType(descriptor.getType());

              attributes.get(baseType).getAsJsonArray().add(name);
            }
          }

          JsonObject sheet = new JsonObject();
          sheet.addProperty("name", typeName);
          sheet.add("attributes", attributes);

          return sheet;
        }
        else
        {
          // TODO Change exception type
          throw new ProgrammingErrorException("Shapefile does not contain any types");
        }
      }
      finally
      {
        store.dispose();
      }
    }
    catch (RuntimeException e)
    {
      throw e;
    }
    catch (Exception e)
    {
      throw new ProgrammingErrorException(e);
    }
  }

  public void extract(InputStream iStream, File directory)
  {
    // create a buffer to improve copy performance later.
    byte[] buffer = new byte[2048];

    try
    {
      ZipInputStream zstream = new ZipInputStream(iStream);

      ZipEntry entry;

      while ( ( entry = zstream.getNextEntry() ) != null)
      {
        File file = new File(directory, entry.getName());

        try (FileOutputStream output = new FileOutputStream(file))
        {
          int len = 0;

          while ( ( len = zstream.read(buffer) ) > 0)
          {
            output.write(buffer, 0, len);
          }
        }
      }
    }
    catch (IOException e1)
    {
      throw new ProgrammingErrorException(e1);
    }
  }

  @Request(RequestType.SESSION)
  public JsonObject importShapefile(String sessionId, String config)
  {
    GeoObjectConfiguration configuration = GeoObjectConfiguration.parse(config);

    String dir = configuration.getDirectory();
    String fname = configuration.getFilename();

    File root = new File(new File(VaultProperties.getPath("vault.default"), "files"), dir);
    root.mkdirs();

    File directory = new File(root, FilenameUtils.getBaseName(fname));
    directory.mkdirs();

    File[] dbfs = directory.listFiles(new FilenameFilter()
    {
      @Override
      public boolean accept(File dir, String name)
      {
        return name.endsWith(".dbf");
      }
    });

    if (dbfs.length > 0)
    {
      this.importShapefile(configuration, dbfs[0]);
    }

    return configuration.toJson();
  }

  @Transaction
  private void importShapefile(GeoObjectConfiguration configuration, File shapefile)
  {
    try
    {
      GeoObjectShapefileImporter importer = new GeoObjectShapefileImporter(shapefile, configuration);
      importer.run(new NullLogger());
    }
    catch (MalformedURLException | InvocationTargetException e)
    {
      throw new ProgrammingErrorException(e);
    }
  }

  @Request(RequestType.SESSION)
  public InputStream exportShapefile(String sessionId, String code)
  {
    return this.exportShapefile(code);
  }

  @Transaction
  private InputStream exportShapefile(String code)
  {
    GeoObjectType type = ServiceFactory.getAdapter().getMetadataCache().getGeoObjectType(code).get();
    List<GeoObject> objects = this.getObjects(type);

    GeoObjectShapefileExporter exporter = new GeoObjectShapefileExporter(type, objects);
    return exporter.export();
  }

  private List<GeoObject> getObjects(GeoObjectType type)
  {
    List<GeoObject> objects = new LinkedList<>();

    Universal universal = ServiceFactory.getConversionService().geoObjectTypeToUniversal(type);

    GeoEntityQuery query = new GeoEntityQuery(new QueryFactory());
    query.WHERE(query.getUniversal().EQ(universal));
    query.ORDER_BY_ASC(query.getGeoId());

    OIterator<? extends GeoEntity> it = query.getIterator();

    try
    {
      List<? extends GeoEntity> entities = it.getAll();

      for (GeoEntity entity : entities)
      {
        objects.add(ServiceFactory.getUtilities().getGeoObjectByCode(entity.getGeoId(), type.getCode()));
      }
    }
    finally
    {
      it.close();
    }

    return objects;
  }

}

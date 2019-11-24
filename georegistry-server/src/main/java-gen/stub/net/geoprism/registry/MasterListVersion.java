package net.geoprism.registry;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TimeZone;

import org.commongeoregistry.adapter.Term;
import org.commongeoregistry.adapter.constants.DefaultAttribute;
import org.commongeoregistry.adapter.constants.GeometryType;
import org.commongeoregistry.adapter.dataaccess.LocalizedValue;
import org.commongeoregistry.adapter.metadata.AttributeBooleanType;
import org.commongeoregistry.adapter.metadata.AttributeCharacterType;
import org.commongeoregistry.adapter.metadata.AttributeDateType;
import org.commongeoregistry.adapter.metadata.AttributeFloatType;
import org.commongeoregistry.adapter.metadata.AttributeIntegerType;
import org.commongeoregistry.adapter.metadata.AttributeLocalType;
import org.commongeoregistry.adapter.metadata.AttributeTermType;
import org.commongeoregistry.adapter.metadata.AttributeType;
import org.commongeoregistry.adapter.metadata.GeoObjectType;
import org.commongeoregistry.adapter.metadata.HierarchyType;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.runwaysdk.ComponentIF;
import com.runwaysdk.business.Business;
import com.runwaysdk.business.BusinessFacade;
import com.runwaysdk.business.BusinessQuery;
import com.runwaysdk.business.rbac.Operation;
import com.runwaysdk.business.rbac.RoleDAO;
import com.runwaysdk.constants.BusinessInfo;
import com.runwaysdk.constants.IndexTypes;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeCharacterInfo;
import com.runwaysdk.constants.MdAttributeConcreteInfo;
import com.runwaysdk.constants.MdAttributeDoubleInfo;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.MdTableInfo;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdAttributeMomentDAOIF;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.cache.DataNotFoundException;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.dataaccess.metadata.MdAttributeCharacterDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeUUIDDAO;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.metadata.SupportedLocaleDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.gis.dataaccess.MdAttributePointDAOIF;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.session.Session;
import com.runwaysdk.system.gis.metadata.MdAttributeGeometry;
import com.runwaysdk.system.gis.metadata.MdAttributeLineString;
import com.runwaysdk.system.gis.metadata.MdAttributeMultiLineString;
import com.runwaysdk.system.gis.metadata.MdAttributeMultiPoint;
import com.runwaysdk.system.gis.metadata.MdAttributeMultiPolygon;
import com.runwaysdk.system.gis.metadata.MdAttributePoint;
import com.runwaysdk.system.gis.metadata.MdAttributePolygon;
import com.runwaysdk.system.metadata.MdAttribute;
import com.runwaysdk.system.metadata.MdAttributeBoolean;
import com.runwaysdk.system.metadata.MdAttributeCharacter;
import com.runwaysdk.system.metadata.MdAttributeConcrete;
import com.runwaysdk.system.metadata.MdAttributeDateTime;
import com.runwaysdk.system.metadata.MdAttributeDouble;
import com.runwaysdk.system.metadata.MdAttributeIndices;
import com.runwaysdk.system.metadata.MdAttributeLong;
import com.runwaysdk.system.metadata.MdBusiness;

import net.geoprism.DefaultConfiguration;
import net.geoprism.localization.LocalizationFacade;
import net.geoprism.ontology.Classifier;
import net.geoprism.registry.conversion.LocalizedValueConverter;
import net.geoprism.registry.io.GeoObjectConfiguration;
import net.geoprism.registry.masterlist.MasterListAttributeComparator;
import net.geoprism.registry.masterlist.TableMetadata;
import net.geoprism.registry.model.LocationInfo;
import net.geoprism.registry.model.ServerGeoObjectIF;
import net.geoprism.registry.model.ServerGeoObjectType;
import net.geoprism.registry.model.ServerHierarchyType;
import net.geoprism.registry.progress.Progress;
import net.geoprism.registry.progress.ProgressService;
import net.geoprism.registry.query.graph.VertexGeoObjectQuery;
import net.geoprism.registry.service.LocaleSerializer;
import net.geoprism.registry.service.ServiceFactory;

public class MasterListVersion extends MasterListVersionBase
{
  private static final long serialVersionUID = -351397872;

  public static String      PREFIX           = "ml_";

  public static String      ORIGINAL_OID     = "originalOid";

  public static String      LEAF             = "leaf";

  public static String      TYPE_CODE        = "typeCode";

  public static String      ATTRIBUTES       = "attributes";

  public static String      NAME             = "name";

  public static String      LABEL            = "label";

  public static String      VALUE            = "value";

  public static String      TYPE             = "type";

  public static String      BASE             = "base";

  public static String      DEPENDENCY       = "dependency";

  public static String      DEFAULT_LOCALE   = "DefaultLocale";

  public MasterListVersion()
  {
    super();
  }

  private String getTableName()
  {
    int count = 0;

    MdBusiness mdBusiness = this.getMasterlist().getUniversal().getMdBusiness();

    String name = PREFIX + count + mdBusiness.getTableName();

    while (Database.tableExists(name))
    {
      count++;

      name = PREFIX + count + mdBusiness.getTableName();
    }

    return name;
  }

  private boolean isValid(AttributeType attributeType)
  {
    if (attributeType.getName().equals(DefaultAttribute.UID.getName()))
    {
      return false;
    }

    if (attributeType.getName().equals(DefaultAttribute.SEQUENCE.getName()))
    {
      return false;
    }

    if (attributeType.getName().equals(DefaultAttribute.STATUS.getName()))
    {
      return false;
    }

    if (attributeType.getName().equals(DefaultAttribute.LAST_UPDATE_DATE.getName()))
    {
      return false;
    }

    if (attributeType.getName().equals(DefaultAttribute.CREATE_DATE.getName()))
    {
      return false;
    }

    if (attributeType.getName().equals(DefaultAttribute.TYPE.getName()))
    {
      return false;
    }

    return true;
  }

  public boolean isValid(MdAttributeConcreteDAOIF mdAttribute)
  {
    if (mdAttribute.isSystem() || mdAttribute.definesAttribute().equals(DefaultAttribute.UID.getName()))
    {
      return false;
    }

    if (mdAttribute.definesAttribute().equals(DefaultAttribute.SEQUENCE.getName()))
    {
      return false;
    }

    if (mdAttribute.definesAttribute().equals(DefaultAttribute.LAST_UPDATE_DATE.getName()))
    {
      return false;
    }

    if (mdAttribute.definesAttribute().equals(DefaultAttribute.CREATE_DATE.getName()))
    {
      return false;
    }

    if (mdAttribute.definesAttribute().equals(DefaultAttribute.TYPE.getName()))
    {
      return false;
    }

    if (mdAttribute.definesAttribute().equals(DefaultAttribute.STATUS.getName()))
    {
      return false;
    }

    if (mdAttribute.definesAttribute().equals(ORIGINAL_OID))
    {
      return false;
    }

    // if (mdAttribute.definesAttribute().endsWith("Oid"))
    // {
    // return false;
    // }

    if (mdAttribute.definesAttribute().equals(RegistryConstants.GEOMETRY_ATTRIBUTE_NAME))
    {
      return false;
    }

    if (mdAttribute.definesAttribute().equals(BusinessInfo.OWNER))
    {
      return false;
    }

    if (mdAttribute.definesAttribute().equals(BusinessInfo.KEY))
    {
      return false;
    }

    if (mdAttribute.definesAttribute().equals(BusinessInfo.DOMAIN))
    {
      return false;
    }

    return true;
  }

  public void createMdAttributeFromAttributeType(ServerGeoObjectType type, AttributeType attributeType, List<Locale> locales)
  {
    TableMetadata metadata = new TableMetadata();
    metadata.setMdBusiness(this.getMdBusiness());

    this.createMdAttributeFromAttributeType(metadata, attributeType, type, locales);

    Map<MdAttribute, MdAttribute> pairs = metadata.getPairs();

    Set<Entry<MdAttribute, MdAttribute>> entries = pairs.entrySet();

    for (Entry<MdAttribute, MdAttribute> entry : entries)
    {
      MasterListAttributeGroup.create(this, entry.getValue(), entry.getKey());
    }
  }

  public void createMdAttributeFromAttributeType(TableMetadata metadata, AttributeType attributeType, ServerGeoObjectType type, List<Locale> locales)
  {
    MdBusiness mdBusiness = metadata.getMdBusiness();

    if (! ( attributeType instanceof AttributeTermType || attributeType instanceof AttributeLocalType ))
    {
      MdAttributeConcrete mdAttribute = null;

      if (attributeType.getType().equals(AttributeCharacterType.TYPE))
      {
        mdAttribute = new MdAttributeCharacter();
        MdAttributeCharacter mdAttributeCharacter = (MdAttributeCharacter) mdAttribute;
        mdAttributeCharacter.setDatabaseSize(MdAttributeCharacterInfo.MAX_CHARACTER_SIZE);
      }
      else if (attributeType.getType().equals(AttributeDateType.TYPE))
      {
        mdAttribute = new MdAttributeDateTime();
      }
      else if (attributeType.getType().equals(AttributeIntegerType.TYPE))
      {
        mdAttribute = new MdAttributeLong();
      }
      else if (attributeType.getType().equals(AttributeFloatType.TYPE))
      {
        AttributeFloatType attributeFloatType = (AttributeFloatType) attributeType;

        mdAttribute = new MdAttributeDouble();
        mdAttribute.setValue(MdAttributeDoubleInfo.LENGTH, Integer.toString(attributeFloatType.getPrecision()));
        mdAttribute.setValue(MdAttributeDoubleInfo.DECIMAL, Integer.toString(attributeFloatType.getScale()));
      }
      else if (attributeType.getType().equals(AttributeBooleanType.TYPE))
      {
        mdAttribute = new MdAttributeBoolean();
      }
      else
      {
        throw new UnsupportedOperationException("Unsupported type [" + attributeType.getType() + "]");
      }

      mdAttribute.setAttributeName(attributeType.getName());

      LocalizedValueConverter.populate(mdAttribute.getDisplayLabel(), attributeType.getLabel());
      LocalizedValueConverter.populate(mdAttribute.getDescription(), attributeType.getDescription());

      mdAttribute.setDefiningMdClass(mdBusiness);
      mdAttribute.apply();
    }
    else if (attributeType instanceof AttributeTermType)
    {
      MdAttributeCharacter cloneAttribute = new MdAttributeCharacter();
      cloneAttribute.setValue(MdAttributeConcreteInfo.NAME, attributeType.getName());
      cloneAttribute.setValue(MdAttributeCharacterInfo.SIZE, "255");
      cloneAttribute.addIndexType(MdAttributeIndices.NON_UNIQUE_INDEX);
      LocalizedValueConverter.populate(cloneAttribute.getDisplayLabel(), attributeType.getLabel());
      LocalizedValueConverter.populate(cloneAttribute.getDescription(), attributeType.getDescription());
      cloneAttribute.setDefiningMdClass(mdBusiness);
      cloneAttribute.apply();

      metadata.addPair(cloneAttribute, cloneAttribute);

      MdAttributeCharacter mdAttributeDefaultLocale = new MdAttributeCharacter();
      mdAttributeDefaultLocale.setValue(MdAttributeCharacterInfo.NAME, attributeType.getName() + DEFAULT_LOCALE);
      mdAttributeDefaultLocale.setValue(MdAttributeCharacterInfo.SIZE, "255");
      mdAttributeDefaultLocale.setDefiningMdClass(mdBusiness);
      LocalizedValueConverter.populate(mdAttributeDefaultLocale.getDisplayLabel(), attributeType.getLabel(), " (defaultLocale)");
      LocalizedValueConverter.populate(mdAttributeDefaultLocale.getDescription(), attributeType.getDescription(), " (defaultLocale)");
      mdAttributeDefaultLocale.apply();

      metadata.addPair(mdAttributeDefaultLocale, cloneAttribute);

      for (Locale locale : locales)
      {
        MdAttributeCharacter mdAttributeLocale = new MdAttributeCharacter();
        mdAttributeLocale.setValue(MdAttributeCharacterInfo.NAME, attributeType.getName() + locale.toString());
        mdAttributeLocale.setValue(MdAttributeCharacterInfo.SIZE, "255");
        mdAttributeLocale.setDefiningMdClass(mdBusiness);
        LocalizedValueConverter.populate(mdAttributeLocale.getDisplayLabel(), attributeType.getLabel(), " (" + locale.toString() + ")");
        LocalizedValueConverter.populate(mdAttributeLocale.getDescription(), attributeType.getDescription());
        mdAttributeLocale.apply();

        metadata.addPair(mdAttributeLocale, cloneAttribute);
      }

      // MdAttributeUUID mdAttributeOid = new MdAttributeUUID();
      // mdAttributeOid.setValue(MdAttributeConcreteInfo.NAME,
      // attributeType.getName() + "Oid");
      // AbstractBuilder.populate(mdAttributeOid.getDisplayLabel(),
      // attributeType.getLabel());
      // AbstractBuilder.populate(mdAttributeOid.getDescription(),
      // attributeType.getDescription());
      // mdAttributeOid.setDefiningMdClass(mdBusiness);
      // mdAttributeOid.apply();
    }
    else if (attributeType instanceof AttributeLocalType)
    {
      boolean isDisplayLabel = attributeType.getName().equals(DefaultAttribute.DISPLAY_LABEL.getName());

      MdAttributeCharacter mdAttributeDefaultLocale = new MdAttributeCharacter();
      mdAttributeDefaultLocale.setValue(MdAttributeCharacterInfo.NAME, attributeType.getName() + DEFAULT_LOCALE);
      mdAttributeDefaultLocale.setValue(MdAttributeCharacterInfo.SIZE, "255");
      mdAttributeDefaultLocale.setDefiningMdClass(mdBusiness);
      LocalizedValueConverter.populate(mdAttributeDefaultLocale.getDisplayLabel(), isDisplayLabel ? type.getLabel() : attributeType.getLabel(), " (defaultLocale)");
      LocalizedValueConverter.populate(mdAttributeDefaultLocale.getDescription(), attributeType.getDescription(), " (defaultLocale)");
      mdAttributeDefaultLocale.apply();

      for (Locale locale : locales)
      {
        MdAttributeCharacter mdAttributeLocale = new MdAttributeCharacter();
        mdAttributeLocale.setValue(MdAttributeCharacterInfo.NAME, attributeType.getName() + locale.toString());
        mdAttributeLocale.setValue(MdAttributeCharacterInfo.SIZE, "255");
        mdAttributeLocale.setDefiningMdClass(mdBusiness);
        LocalizedValueConverter.populate(mdAttributeLocale.getDisplayLabel(), isDisplayLabel ? type.getLabel() : attributeType.getLabel(), " (" + locale.toString() + ")");
        LocalizedValueConverter.populate(mdAttributeLocale.getDescription(), attributeType.getDescription());
        mdAttributeLocale.apply();
      }
    }
  }

  public void createMdAttributeFromAttributeType(MdBusiness mdBusiness, GeometryType attributeType)
  {
    MdAttributeGeometry mdAttribute = null;

    if (attributeType.equals(GeometryType.POINT))
    {
      mdAttribute = new MdAttributePoint();
    }
    else if (attributeType.equals(GeometryType.MULTIPOINT))
    {
      mdAttribute = new MdAttributeMultiPoint();
    }
    else if (attributeType.equals(GeometryType.LINE))
    {
      mdAttribute = new MdAttributeLineString();
    }
    else if (attributeType.equals(GeometryType.MULTILINE))
    {
      mdAttribute = new MdAttributeMultiLineString();
    }
    else if (attributeType.equals(GeometryType.POLYGON))
    {
      mdAttribute = new MdAttributePolygon();
    }
    else if (attributeType.equals(GeometryType.MULTIPOLYGON))
    {
      mdAttribute = new MdAttributeMultiPolygon();
    }

    mdAttribute.setAttributeName(RegistryConstants.GEOMETRY_ATTRIBUTE_NAME);
    mdAttribute.getDisplayLabel().setValue(RegistryConstants.GEOMETRY_ATTRIBUTE_NAME);
    mdAttribute.setDefiningMdClass(mdBusiness);
    mdAttribute.setSrid(4326);
    mdAttribute.apply();
  }

  private TableMetadata createTable()
  {
    MasterList masterlist = this.getMasterlist();

    TableMetadata metadata = new TableMetadata();

    Locale currentLocale = Session.getCurrentLocale();

    String viewName = this.getTableName();

    // Create the MdTable
    MdBusinessDAO mdTableDAO = MdBusinessDAO.newInstance();
    mdTableDAO.setValue(MdTableInfo.NAME, viewName);
    mdTableDAO.setValue(MdTableInfo.PACKAGE, RegistryConstants.TABLE_PACKAGE);
    mdTableDAO.setStructValue(MdTableInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, masterlist.getDisplayLabel().getValue());
    mdTableDAO.setValue(MdTableInfo.TABLE_NAME, viewName);
    mdTableDAO.setValue(MdTableInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdTableDAO.apply();

    MdBusiness mdBusiness = (MdBusiness) BusinessFacade.get(mdTableDAO);

    MdAttributeUUIDDAO mdAttributeOriginalId = MdAttributeUUIDDAO.newInstance();
    mdAttributeOriginalId.setValue(MdAttributeCharacterInfo.NAME, ORIGINAL_OID);
    mdAttributeOriginalId.setValue(MdAttributeCharacterInfo.DEFINING_MD_CLASS, mdTableDAO.getOid());
    mdAttributeOriginalId.setStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Original oid");
    mdAttributeOriginalId.apply();

    metadata.setMdBusiness(mdBusiness);

    List<Locale> locales = SupportedLocaleDAO.getSupportedLocales();

    ServerGeoObjectType type = ServerGeoObjectType.get(masterlist.getUniversal());

    this.createMdAttributeFromAttributeType(mdBusiness, type.getGeometryType());

    Collection<AttributeType> attributeTypes = type.getAttributeMap().values();

    for (AttributeType attributeType : attributeTypes)
    {
      if (this.isValid(attributeType))
      {
        this.createMdAttributeFromAttributeType(metadata, attributeType, type, locales);
      }
    }

    JsonArray hierarchies = masterlist.getHierarchiesAsJson();

    for (int i = 0; i < hierarchies.size(); i++)
    {
      JsonObject hierarchy = hierarchies.get(i).getAsJsonObject();

      List<String> pCodes = masterlist.getParentCodes(hierarchy);

      if (pCodes.size() > 0)
      {
        String hCode = hierarchy.get("code").getAsString();

        HierarchyType hierarchyType = ServiceFactory.getAdapter().getMetadataCache().getHierachyType(hCode).get();
        String hierarchyLabel = hierarchyType.getLabel().getValue(currentLocale);

        for (String pCode : pCodes)
        {
          ServerGeoObjectType got = ServerGeoObjectType.get(pCode);
          String typeLabel = got.getLabel().getValue(currentLocale);
          String attributeName = hCode.toLowerCase() + pCode.toLowerCase();
          String label = typeLabel + " (" + hierarchyLabel + ")";

          String codeDescription = LocalizationFacade.getFromBundles("masterlist.code.description");
          codeDescription = codeDescription.replaceAll("\\{typeLabel\\}", typeLabel);
          codeDescription = codeDescription.replaceAll("\\{hierarchyLabel\\}", hierarchyLabel);

          String labelDescription = LocalizationFacade.getFromBundles("masterlist.label.description");
          labelDescription = labelDescription.replaceAll("\\{typeLabel\\}", typeLabel);
          labelDescription = labelDescription.replaceAll("\\{hierarchyLabel\\}", hierarchyLabel);

          MdAttributeCharacterDAO mdAttributeCode = MdAttributeCharacterDAO.newInstance();
          mdAttributeCode.setValue(MdAttributeCharacterInfo.NAME, attributeName);
          mdAttributeCode.setValue(MdAttributeCharacterInfo.DEFINING_MD_CLASS, mdTableDAO.getOid());
          mdAttributeCode.setValue(MdAttributeCharacterInfo.SIZE, "255");
          mdAttributeCode.setStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, label);
          mdAttributeCode.addItem(MdAttributeCharacterInfo.INDEX_TYPE, IndexTypes.NON_UNIQUE_INDEX.getOid());
          mdAttributeCode.setStructValue(MdAttributeCharacterInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, codeDescription);
          mdAttributeCode.apply();

          MdAttributeCharacterDAO mdAttributeDefaultLocale = MdAttributeCharacterDAO.newInstance();
          mdAttributeDefaultLocale.setValue(MdAttributeCharacterInfo.NAME, attributeName + DEFAULT_LOCALE);
          mdAttributeDefaultLocale.setValue(MdAttributeCharacterInfo.DEFINING_MD_CLASS, mdTableDAO.getOid());
          mdAttributeDefaultLocale.setValue(MdAttributeCharacterInfo.SIZE, "255");
          mdAttributeDefaultLocale.setStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, label + " (defaultLocale)");
          mdAttributeDefaultLocale.setStructValue(MdAttributeCharacterInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, labelDescription.replaceAll("\\{locale\\}", "default"));
          mdAttributeDefaultLocale.apply();

          for (Locale locale : locales)
          {
            MdAttributeCharacterDAO mdAttributeLocale = MdAttributeCharacterDAO.newInstance();
            mdAttributeLocale.setValue(MdAttributeCharacterInfo.NAME, attributeName + locale.toString());
            mdAttributeLocale.setValue(MdAttributeCharacterInfo.DEFINING_MD_CLASS, mdTableDAO.getOid());
            mdAttributeLocale.setValue(MdAttributeCharacterInfo.SIZE, "255");
            mdAttributeLocale.setStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, label + " (" + locale + ")");
            mdAttributeLocale.setStructValue(MdAttributeCharacterInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, labelDescription.replaceAll("\\{locale\\}", locale.toString()));
            mdAttributeLocale.apply();
          }
        }
      }
    }

    return metadata;
  }

  @Override
  @Transaction
  public void delete()
  {
    MasterListAttributeGroup.deleteAll(this);

    MdBusiness mdTable = this.getMdBusiness();

    super.delete();

    if (mdTable != null)
    {
      MdBusinessDAO mdBusiness = MdBusinessDAO.get(this.getMdBusinessOid()).getBusinessDAO();
      mdBusiness.deleteAllRecords();

      mdTable.delete();
    }
  }

  @Transaction
  public JsonObject publish()
  {
    this.lock();

    try
    {
      MasterList masterlist = this.getMasterlist();

      MdBusinessDAO mdBusiness = MdBusinessDAO.get(this.getMdBusinessOid()).getBusinessDAO();
      mdBusiness.deleteAllRecords();

      ServerGeoObjectType type = ServerGeoObjectType.get(masterlist.getUniversal());

      List<Locale> locales = SupportedLocaleDAO.getSupportedLocales();

      // Add the type ancestor fields
      Map<HierarchyType, List<GeoObjectType>> ancestorMap = masterlist.getAncestorMap(type);
      Collection<AttributeType> attributes = type.getAttributeMap().values();

      // ServerGeoObjectService service = new ServerGeoObjectService();
      // ServerGeoObjectQuery query = service.createQuery(type,
      // this.getPeriod());
      VertexGeoObjectQuery query = new VertexGeoObjectQuery(type, this.getForDate());

      Long count = query.getCount();
      long current = 0;

      ProgressService.put(this.getOid(), new Progress(0L, count, ""));

      try
      {
        List<ServerGeoObjectIF> results = query.getResults();

        for (ServerGeoObjectIF result : results)
        {
          if (result.getStatus().equals(GeoObjectStatus.ACTIVE))
          {
            Business business = new Business(mdBusiness.definesType());

            publish(result, business, attributes, ancestorMap, locales);
          }

          ProgressService.put(this.getOid(), new Progress(current++, count, ""));
        }

        // this.setPublishDate(new Date());
        this.apply();

        return this.toJSON();
      }
      finally
      {
        ProgressService.remove(this.getOid());
      }
    }
    finally
    {
      this.unlock();
    }
  }

  private void publish(ServerGeoObjectIF object, Business business, Collection<AttributeType> attributes, Map<HierarchyType, List<GeoObjectType>> ancestorMap, List<Locale> locales)
  {
    business.setValue(RegistryConstants.GEOMETRY_ATTRIBUTE_NAME, object.getGeometry());

    for (AttributeType attribute : attributes)
    {
      String name = attribute.getName();

      business.setValue(ORIGINAL_OID, object.getRunwayId());

      if (this.isValid(attribute))
      {
        Object value = object.getValue(name);

        if (value != null)
        {

          if (attribute instanceof AttributeTermType)
          {
            Classifier classifier = (Classifier) value;

            Term term = ( (AttributeTermType) attribute ).getTermByCode(classifier.getClassifierId()).get();
            LocalizedValue label = term.getLabel();

            this.setValue(business, name, term.getCode());
            this.setValue(business, name + DEFAULT_LOCALE, label.getValue(LocalizedValue.DEFAULT_LOCALE));

            for (Locale locale : locales)
            {
              this.setValue(business, name + locale.toString(), label.getValue(locale));
            }
          }
          else if (attribute instanceof AttributeLocalType)
          {
            LocalizedValue label = (LocalizedValue) value;

            this.setValue(business, name + DEFAULT_LOCALE, label.getValue(LocalizedValue.DEFAULT_LOCALE));

            for (Locale locale : locales)
            {
              this.setValue(business, name + locale.toString(), label.getValue(locale));
            }
          }
          else
          {
            this.setValue(business, name, value);
          }
        }
      }
    }

    Set<Entry<HierarchyType, List<GeoObjectType>>> entries = ancestorMap.entrySet();

    for (Entry<HierarchyType, List<GeoObjectType>> entry : entries)
    {
      ServerHierarchyType hierarchy = ServerHierarchyType.get(entry.getKey());

      // List<GeoObjectType> parents = entry.getValue();
      Map<String, LocationInfo> map = object.getAncestorMap(hierarchy);

      Set<Entry<String, LocationInfo>> locations = map.entrySet();

      for (Entry<String, LocationInfo> location : locations)
      {
        String pCode = location.getKey();
        LocationInfo vObject = location.getValue();

        if (vObject != null)
        {
          String attributeName = hierarchy.getCode().toLowerCase() + pCode.toLowerCase();

          this.setValue(business, attributeName, vObject.getCode());
          this.setValue(business, attributeName + DEFAULT_LOCALE, vObject.getLabel());

          for (Locale locale : locales)
          {
            this.setValue(business, attributeName + locale.toString(), vObject.getLabel(locale));
          }
        }
      }
    }

    business.apply();
  }

  private void setValue(Business business, String name, Object value)
  {
    if (business.hasAttribute(name))
    {
      business.setValue(name, value);
    }
  }

  @Transaction
  public void updateRecord(ServerGeoObjectIF object)
  {
    MasterList masterlist = this.getMasterlist();
    MdBusinessDAO mdBusiness = MdBusinessDAO.get(this.getMdBusinessOid()).getBusinessDAO();
    List<Locale> locales = SupportedLocaleDAO.getSupportedLocales();

    // Add the type ancestor fields
    ServerGeoObjectType type = ServerGeoObjectType.get(masterlist.getUniversal());
    Map<HierarchyType, List<GeoObjectType>> ancestorMap = masterlist.getAncestorMap(type);
    Collection<AttributeType> attributes = type.getAttributeMap().values();

    BusinessQuery query = new QueryFactory().businessQuery(mdBusiness.definesType());
    query.WHERE(query.aCharacter(DefaultAttribute.CODE.getName()).EQ(object.getCode()));

    List<Business> records = query.getIterator().getAll();

    for (Business record : records)
    {
      try
      {
        record.appLock();

        this.publish(object, record, attributes, ancestorMap, locales);
      }
      finally
      {
        record.unlock();
      }
    }
  }

  @Transaction
  public void publishRecord(ServerGeoObjectIF object)
  {
    MasterList masterlist = this.getMasterlist();
    MdBusinessDAO mdBusiness = MdBusinessDAO.get(this.getMdBusinessOid()).getBusinessDAO();
    List<Locale> locales = SupportedLocaleDAO.getSupportedLocales();

    // Add the type ancestor fields
    ServerGeoObjectType type = ServerGeoObjectType.get(masterlist.getUniversal());
    Map<HierarchyType, List<GeoObjectType>> ancestorMap = masterlist.getAncestorMap(type);
    Collection<AttributeType> attributes = type.getAttributeMap().values();

    Business business = new Business(mdBusiness.definesType());

    this.publish(object, business, attributes, ancestorMap, locales);
  }

  public JsonObject toJSON()
  {
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    format.setTimeZone(TimeZone.getTimeZone("GMT"));

    Locale locale = Session.getCurrentLocale();
    LocaleSerializer serializer = new LocaleSerializer(locale);
    MasterList masterlist = this.getMasterlist();

    ServerGeoObjectType type = ServerGeoObjectType.get(masterlist.getUniversal());

    JsonObject object = new JsonObject();

    if (this.isAppliedToDB())
    {
      object.addProperty(MasterListVersion.OID, this.getOid());
    }

    object.addProperty(MasterListVersion.TYPE_CODE, type.getCode());
    object.addProperty(MasterListVersion.LEAF, type.isLeaf());
    object.addProperty(MasterListVersion.MASTERLIST, masterlist.getOid());
    object.addProperty(MasterListVersion.FORDATE, format.format(this.getForDate()));
    object.addProperty(MasterListVersion.CREATEDATE, format.format(this.getCreateDate()));
    object.add(MasterListVersion.ATTRIBUTES, this.getAttributesAsJson());

    return object;
  }

  private JsonArray getAttributesAsJson()
  {
    List<Locale> locales = SupportedLocaleDAO.getSupportedLocales();
    MasterList list = this.getMasterlist();

    Map<String, JsonArray> dependencies = new HashMap<String, JsonArray>();
    Map<String, String> baseAttributes = new HashMap<String, String>();
    List<String> attributesOrder = new LinkedList<String>();

    JsonArray hierarchies = list.getHierarchiesAsJson();

    for (int i = 0; i < hierarchies.size(); i++)
    {
      JsonObject hierarchy = hierarchies.get(i).getAsJsonObject();

      List<String> pCodes = list.getParentCodes(hierarchy);

      if (pCodes.size() > 0)
      {
        String hCode = hierarchy.get(DefaultAttribute.CODE.getName()).getAsString();

        String previous = null;

        for (String pCode : pCodes)
        {
          String attributeName = hCode.toLowerCase() + pCode.toLowerCase();

          baseAttributes.put(attributeName, attributeName);
          baseAttributes.put(attributeName + DEFAULT_LOCALE, attributeName);

          for (Locale locale : locales)
          {
            baseAttributes.put(attributeName + locale.toString(), attributeName);
          }

          attributesOrder.add(attributeName);
          attributesOrder.add(attributeName + DEFAULT_LOCALE);

          for (Locale locale : locales)
          {
            attributesOrder.add(attributeName + locale.toString());
          }

          if (previous != null)
          {
            addDependency(dependencies, attributeName, previous);
            addDependency(dependencies, attributeName + DEFAULT_LOCALE, previous);

            for (Locale locale : locales)
            {
              addDependency(dependencies, attributeName + locale.toString(), previous);
            }
          }

          previous = attributeName;
        }

        if (previous != null)
        {
          addDependency(dependencies, DefaultAttribute.CODE.getName(), previous);
          addDependency(dependencies, DefaultAttribute.DISPLAY_LABEL.getName() + DEFAULT_LOCALE, previous);

          for (Locale locale : locales)
          {
            addDependency(dependencies, DefaultAttribute.DISPLAY_LABEL.getName() + locale.toString(), previous);
          }
        }
      }
    }

    baseAttributes.put(DefaultAttribute.CODE.getName(), DefaultAttribute.CODE.getName());
    baseAttributes.put(DefaultAttribute.DISPLAY_LABEL.getName() + DEFAULT_LOCALE, DefaultAttribute.CODE.getName());

    for (Locale locale : locales)
    {
      baseAttributes.put(DefaultAttribute.DISPLAY_LABEL.getName() + locale.toString(), DefaultAttribute.CODE.getName());
    }

    attributesOrder.add(DefaultAttribute.CODE.getName());
    attributesOrder.add(DefaultAttribute.DISPLAY_LABEL.getName() + DEFAULT_LOCALE);

    for (Locale locale : locales)
    {
      attributesOrder.add(DefaultAttribute.DISPLAY_LABEL.getName() + locale.toString());
    }

    JsonArray attributes = new JsonArray();
    String mdBusinessId = this.getMdBusinessOid();

    if (mdBusinessId != null && mdBusinessId.length() > 0)
    {
      MdBusinessDAOIF mdBusiness = MdBusinessDAO.get(mdBusinessId);
      List<? extends MdAttributeConcreteDAOIF> mdAttributes = mdBusiness.definesAttributesOrdered();

      Collections.sort(mdAttributes, new MasterListAttributeComparator(attributesOrder, mdAttributes));

      MdAttributeConcreteDAOIF mdGeometry = mdBusiness.definesAttribute(RegistryConstants.GEOMETRY_ATTRIBUTE_NAME);

      if (mdGeometry instanceof MdAttributePointDAOIF)
      {
        JsonObject longitude = new JsonObject();
        longitude.addProperty(NAME, "longitude");
        longitude.addProperty(LABEL, LocalizationFacade.getFromBundles(GeoObjectConfiguration.LONGITUDE_KEY));
        longitude.addProperty(TYPE, "none");

        attributes.add(longitude);

        JsonObject latitude = new JsonObject();
        latitude.addProperty(NAME, "latitude");
        latitude.addProperty(LABEL, LocalizationFacade.getFromBundles(GeoObjectConfiguration.LATITUDE_KEY));
        latitude.addProperty(TYPE, "none");

        attributes.add(latitude);
      }

      for (MdAttributeConcreteDAOIF mdAttribute : mdAttributes)
      {
        if (this.isValid(mdAttribute))
        {
          String attributeName = mdAttribute.definesAttribute();

          try
          {
            MasterListAttributeGroup group = MasterListAttributeGroup.getByKey(mdAttribute.getOid());

            if (group != null)
            {
              baseAttributes.put(mdAttribute.definesAttribute(), group.getSourceAttribute().getAttributeName());
            }
          }
          catch (DataNotFoundException e)
          {
            // Ignore
          }

          JsonObject attribute = new JsonObject();
          attribute.addProperty(NAME, attributeName);
          attribute.addProperty(LABEL, mdAttribute.getDisplayLabel(Session.getCurrentLocale()));
          attribute.addProperty(TYPE, baseAttributes.containsKey(attributeName) ? "list" : "input");
          attribute.addProperty(BASE, baseAttributes.containsKey(attributeName) ? baseAttributes.get(attributeName) : attributeName);
          attribute.add(DEPENDENCY, dependencies.containsKey(attributeName) ? dependencies.get(attributeName) : new JsonArray());

          if (mdAttribute instanceof MdAttributeMomentDAOIF)
          {
            attribute.addProperty(TYPE, "date");
            attribute.add(VALUE, new JsonObject());
          }

          attributes.add(attribute);
        }
      }
    }

    return attributes;
  }

  private void addDependency(Map<String, JsonArray> dependencies, String attributeName, String dependency)
  {
    if (!dependencies.containsKey(attributeName))
    {
      dependencies.put(attributeName, new JsonArray());
    }

    dependencies.get(attributeName).add(dependency);
  }

  public void removeAttributeType(AttributeType attributeType)
  {
    TableMetadata metadata = new TableMetadata();
    metadata.setMdBusiness(this.getMdBusiness());

    this.removeAttributeType(metadata, attributeType);
  }

  public void removeAttributeType(TableMetadata metadata, AttributeType attributeType)
  {
    List<Locale> locales = SupportedLocaleDAO.getSupportedLocales();

    MdBusinessDAOIF mdBusiness = MdBusinessDAO.get(metadata.getMdBusiness().getOid());

    if (! ( attributeType instanceof AttributeTermType || attributeType instanceof AttributeLocalType ))
    {
      removeAttribute(mdBusiness, attributeType.getName());
    }
    else if (attributeType instanceof AttributeTermType)
    {
      removeAttribute(mdBusiness, attributeType.getName());
      removeAttribute(mdBusiness, attributeType.getName() + DEFAULT_LOCALE);

      for (Locale locale : locales)
      {
        removeAttribute(mdBusiness, attributeType.getName() + locale.toString());
      }
    }
    else if (attributeType instanceof AttributeLocalType)
    {
      removeAttribute(mdBusiness, attributeType.getName() + DEFAULT_LOCALE);

      for (Locale locale : locales)
      {
        removeAttribute(mdBusiness, attributeType.getName() + locale.toString());
      }
    }
  }

  private void removeAttribute(MdBusinessDAOIF mdBusiness, String name)
  {
    MdAttributeConcreteDAOIF mdAttribute = mdBusiness.definesAttribute(name);

    if (mdAttribute != null)
    {
      MasterListAttributeGroup.remove(mdAttribute);

      mdAttribute.getBusinessDAO().delete();
    }
  }

  @Transaction
  public static MasterListVersion create(MasterList list, Date forDate)
  {
    MasterListVersion version = new MasterListVersion();
    version.setMasterlist(list);
    version.setForDate(forDate);

    TableMetadata metadata = null;

    if (version.isNew())
    {
      metadata = version.createTable();

      version.setMdBusiness(metadata.getMdBusiness());
    }

    version.apply();

    if (metadata != null)
    {
      Map<MdAttribute, MdAttribute> pairs = metadata.getPairs();

      Set<Entry<MdAttribute, MdAttribute>> entries = pairs.entrySet();

      for (Entry<MdAttribute, MdAttribute> entry : entries)
      {
        MasterListAttributeGroup.create(version, entry.getValue(), entry.getKey());
      }
    }

    if (version.isNew())
    {
      MasterListVersion.assignDefaultRolePermissions(version.getMdBusiness());
    }

    return version;
  }

  private static void assignDefaultRolePermissions(ComponentIF component)
  {
    RoleDAO adminRole = RoleDAO.findRole(DefaultConfiguration.ADMIN).getBusinessDAO();
    adminRole.grantPermission(Operation.CREATE, component.getOid());
    adminRole.grantPermission(Operation.DELETE, component.getOid());
    adminRole.grantPermission(Operation.WRITE, component.getOid());
    adminRole.grantPermission(Operation.WRITE_ALL, component.getOid());

    RoleDAO maintainer = RoleDAO.findRole(RegistryConstants.REGISTRY_MAINTAINER_ROLE).getBusinessDAO();
    maintainer.grantPermission(Operation.CREATE, component.getOid());
    maintainer.grantPermission(Operation.DELETE, component.getOid());
    maintainer.grantPermission(Operation.WRITE, component.getOid());
    maintainer.grantPermission(Operation.WRITE_ALL, component.getOid());

    RoleDAO consumer = RoleDAO.findRole(RegistryConstants.API_CONSUMER_ROLE).getBusinessDAO();
    consumer.grantPermission(Operation.READ, component.getOid());
    consumer.grantPermission(Operation.READ_ALL, component.getOid());

    RoleDAO contributor = RoleDAO.findRole(RegistryConstants.REGISTRY_CONTRIBUTOR_ROLE).getBusinessDAO();
    contributor.grantPermission(Operation.READ, component.getOid());
    contributor.grantPermission(Operation.READ_ALL, component.getOid());
  }

}

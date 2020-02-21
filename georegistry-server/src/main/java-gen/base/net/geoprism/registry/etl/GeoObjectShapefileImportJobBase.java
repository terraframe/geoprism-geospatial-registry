package net.geoprism.registry.etl;

@com.runwaysdk.business.ClassSignature(hash = -1949057370)
/**
 * This class is generated automatically.
 * DO NOT MAKE CHANGES TO IT - THEY WILL BE OVERWRITTEN
 * Custom business logic should be added to GeoObjectShapefileImportJob.java
 *
 * @author Autogenerated by RunwaySDK
 */
public abstract class GeoObjectShapefileImportJobBase extends com.runwaysdk.system.scheduler.ExecutableJob
{
  public final static String CLASS = "net.geoprism.registry.etl.GeoObjectShapefileImportJob";
  private static final long serialVersionUID = -1949057370;
  
  public GeoObjectShapefileImportJobBase()
  {
    super();
  }
  
  protected String getDeclaredType()
  {
    return CLASS;
  }
  
  public static GeoObjectShapefileImportJobQuery getAllInstances(String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {
    GeoObjectShapefileImportJobQuery query = new GeoObjectShapefileImportJobQuery(new com.runwaysdk.query.QueryFactory());
    com.runwaysdk.business.Entity.getAllInstances(query, sortAttribute, ascending, pageSize, pageNumber);
    return query;
  }
  
  public static GeoObjectShapefileImportJob get(String oid)
  {
    return (GeoObjectShapefileImportJob) com.runwaysdk.business.Business.get(oid);
  }
  
  public static GeoObjectShapefileImportJob getByKey(String key)
  {
    return (GeoObjectShapefileImportJob) com.runwaysdk.business.Business.get(CLASS, key);
  }
  
  public static GeoObjectShapefileImportJob lock(java.lang.String oid)
  {
    GeoObjectShapefileImportJob _instance = GeoObjectShapefileImportJob.get(oid);
    _instance.lock();
    
    return _instance;
  }
  
  public static GeoObjectShapefileImportJob unlock(java.lang.String oid)
  {
    GeoObjectShapefileImportJob _instance = GeoObjectShapefileImportJob.get(oid);
    _instance.unlock();
    
    return _instance;
  }
  
  public String toString()
  {
    if (this.isNew())
    {
      return "New: "+ this.getClassDisplayLabel();
    }
    else
    {
      return super.toString();
    }
  }
}

package net.geoprism.registry.etl;

@com.runwaysdk.business.ClassSignature(hash = -1354524281)
/**
 * This class is generated automatically.
 * DO NOT MAKE CHANGES TO IT - THEY WILL BE OVERWRITTEN
 * Custom business logic should be added to ImportHistory.java
 *
 * @author Autogenerated by RunwaySDK
 */
public class ImportHistoryQueryDTO extends com.runwaysdk.system.scheduler.JobHistoryQueryDTO
{
private static final long serialVersionUID = -1354524281;

  protected ImportHistoryQueryDTO(String type)
  {
    super(type);
  }

@SuppressWarnings("unchecked")
public java.util.List<? extends net.geoprism.registry.etl.ImportHistoryDTO> getResultSet()
{
  return (java.util.List<? extends net.geoprism.registry.etl.ImportHistoryDTO>)super.getResultSet();
}
}

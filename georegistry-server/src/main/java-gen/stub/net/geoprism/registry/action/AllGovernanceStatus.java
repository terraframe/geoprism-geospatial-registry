package net.geoprism.registry.action;

/**
 * This class is generated automatically.
 * DO NOT MAKE CHANGES TO IT - THEY WILL BE OVERWRITTEN
 *
 * @author Autogenerated by RunwaySDK
 */
@com.runwaysdk.business.ClassSignature(hash = 742970704)
public enum AllGovernanceStatus implements com.runwaysdk.business.BusinessEnumeration
{
  ACCEPTED(),
  
  PENDING(),
  
  REJECTED();
  
  public static final java.lang.String CLASS = "net.geoprism.registry.action.AllGovernanceStatus";
  private net.geoprism.registry.action.GovernanceStatus enumeration;
  
  private synchronized void loadEnumeration()
  {
    net.geoprism.registry.action.GovernanceStatus enu = net.geoprism.registry.action.GovernanceStatus.getEnumeration(this.name());
    setEnumeration(enu);
  }
  
  private synchronized void setEnumeration(net.geoprism.registry.action.GovernanceStatus enumeration)
  {
    this.enumeration = enumeration;
  }
  
  public java.lang.String getOid()
  {
    loadEnumeration();
    return enumeration.getOid();
  }
  
  public java.lang.String getEnumName()
  {
    loadEnumeration();
    return enumeration.getEnumName();
  }
  
  public java.lang.String getDisplayLabel()
  {
    loadEnumeration();
    return enumeration.getDisplayLabel().getValue(com.runwaysdk.session.Session.getCurrentLocale());
  }
  
  public static AllGovernanceStatus get(String oid)
  {
    for (AllGovernanceStatus e : AllGovernanceStatus.values())
    {
      if (e.getOid().equals(oid))
      {
        return e;
      }
    }
    return null;
  }
  
}
package net.geoprism.registry.roles;

@com.runwaysdk.business.ClassSignature(hash = -1153230333)
/**
 * This class is generated automatically.
 * DO NOT MAKE CHANGES TO IT - THEY WILL BE OVERWRITTEN
 * Custom business logic should be added to ReadHierarchyPermissionException.java
 *
 * @author Autogenerated by RunwaySDK
 */
public abstract class ReadHierarchyPermissionExceptionBase extends com.runwaysdk.business.SmartException
{
  public final static String CLASS = "net.geoprism.registry.roles.ReadHierarchyPermissionException";
  public static java.lang.String OID = "oid";
  public static java.lang.String ORGANIZATION = "organization";
  private static final long serialVersionUID = -1153230333;
  
  public ReadHierarchyPermissionExceptionBase()
  {
    super();
  }
  
  public ReadHierarchyPermissionExceptionBase(java.lang.String developerMessage)
  {
    super(developerMessage);
  }
  
  public ReadHierarchyPermissionExceptionBase(java.lang.String developerMessage, java.lang.Throwable cause)
  {
    super(developerMessage, cause);
  }
  
  public ReadHierarchyPermissionExceptionBase(java.lang.Throwable cause)
  {
    super(cause);
  }
  
  public String getOid()
  {
    return getValue(OID);
  }
  
  public void validateOid()
  {
    this.validateAttribute(OID);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeUUIDDAOIF getOidMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.registry.roles.ReadHierarchyPermissionException.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeUUIDDAOIF)mdClassIF.definesAttribute(OID);
  }
  
  public String getOrganization()
  {
    return getValue(ORGANIZATION);
  }
  
  public void validateOrganization()
  {
    this.validateAttribute(ORGANIZATION);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeTextDAOIF getOrganizationMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.registry.roles.ReadHierarchyPermissionException.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeTextDAOIF)mdClassIF.definesAttribute(ORGANIZATION);
  }
  
  public void setOrganization(String value)
  {
    if(value == null)
    {
      setValue(ORGANIZATION, "");
    }
    else
    {
      setValue(ORGANIZATION, value);
    }
  }
  
  protected String getDeclaredType()
  {
    return CLASS;
  }
  
  public java.lang.String localize(java.util.Locale locale)
  {
    java.lang.String message = super.localize(locale);
    message = replace(message, "{oid}", this.getOid());
    message = replace(message, "{organization}", this.getOrganization());
    return message;
  }
  
}
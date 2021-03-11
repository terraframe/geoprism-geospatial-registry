package net.geoprism.registry.etl.export.dhis2;

@com.runwaysdk.business.ClassSignature(hash = -574178350)
/**
 * This class is generated automatically.
 * DO NOT MAKE CHANGES TO IT - THEY WILL BE OVERWRITTEN
 * Custom business logic should be added to MultipleLevelOneOrgUnitException.java
 *
 * @author Autogenerated by RunwaySDK
 */
public abstract class MultipleLevelOneOrgUnitExceptionBase extends com.runwaysdk.business.SmartException
{
  public final static String CLASS = "net.geoprism.registry.etl.export.dhis2.MultipleLevelOneOrgUnitException";
  public static java.lang.String OID = "oid";
  private static final long serialVersionUID = -574178350;
  
  public MultipleLevelOneOrgUnitExceptionBase()
  {
    super();
  }
  
  public MultipleLevelOneOrgUnitExceptionBase(java.lang.String developerMessage)
  {
    super(developerMessage);
  }
  
  public MultipleLevelOneOrgUnitExceptionBase(java.lang.String developerMessage, java.lang.Throwable cause)
  {
    super(developerMessage, cause);
  }
  
  public MultipleLevelOneOrgUnitExceptionBase(java.lang.Throwable cause)
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
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.registry.etl.export.dhis2.MultipleLevelOneOrgUnitException.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeUUIDDAOIF)mdClassIF.definesAttribute(OID);
  }
  
  protected String getDeclaredType()
  {
    return CLASS;
  }
  
  public java.lang.String localize(java.util.Locale locale)
  {
    java.lang.String message = super.localize(locale);
    message = replace(message, "{oid}", this.getOid());
    return message;
  }
  
}
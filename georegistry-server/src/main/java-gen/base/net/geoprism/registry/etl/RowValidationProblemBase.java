/**
 * Copyright (c) 2019 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Geoprism Registry(tm).
 *
 * Geoprism Registry(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Geoprism Registry(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Geoprism Registry(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
package net.geoprism.registry.etl;

@com.runwaysdk.business.ClassSignature(hash = 440758212)
/**
 * This class is generated automatically.
 * DO NOT MAKE CHANGES TO IT - THEY WILL BE OVERWRITTEN
 * Custom business logic should be added to RowValidationProblem.java
 *
 * @author Autogenerated by RunwaySDK
 */
public abstract class RowValidationProblemBase extends net.geoprism.registry.etl.ValidationProblem
{
  public final static String CLASS = "net.geoprism.registry.etl.RowValidationProblem";
  public static java.lang.String EXCEPTIONJSON = "exceptionJson";
  private static final long serialVersionUID = 440758212;
  
  public RowValidationProblemBase()
  {
    super();
  }
  
  public String getExceptionJson()
  {
    return getValue(EXCEPTIONJSON);
  }
  
  public void validateExceptionJson()
  {
    this.validateAttribute(EXCEPTIONJSON);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeTextDAOIF getExceptionJsonMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.registry.etl.RowValidationProblem.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeTextDAOIF)mdClassIF.definesAttribute(EXCEPTIONJSON);
  }
  
  public void setExceptionJson(String value)
  {
    if(value == null)
    {
      setValue(EXCEPTIONJSON, "");
    }
    else
    {
      setValue(EXCEPTIONJSON, value);
    }
  }
  
  protected String getDeclaredType()
  {
    return CLASS;
  }
  
  public static RowValidationProblem get(String oid)
  {
    return (RowValidationProblem) com.runwaysdk.business.Business.get(oid);
  }
  
  public static RowValidationProblem getByKey(String key)
  {
    return (RowValidationProblem) com.runwaysdk.business.Business.get(CLASS, key);
  }
  
  public static RowValidationProblem lock(java.lang.String oid)
  {
    RowValidationProblem _instance = RowValidationProblem.get(oid);
    _instance.lock();
    
    return _instance;
  }
  
  public static RowValidationProblem unlock(java.lang.String oid)
  {
    RowValidationProblem _instance = RowValidationProblem.get(oid);
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
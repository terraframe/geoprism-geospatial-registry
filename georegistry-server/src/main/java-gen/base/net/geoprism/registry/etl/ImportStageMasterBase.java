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

@com.runwaysdk.business.ClassSignature(hash = 2130705567)
/**
 * This class is generated automatically.
 * DO NOT MAKE CHANGES TO IT - THEY WILL BE OVERWRITTEN
 * Custom business logic should be added to ImportStageMaster.java
 *
 * @author Autogenerated by RunwaySDK
 */
public abstract class ImportStageMasterBase extends com.runwaysdk.system.EnumerationMaster
{
  public final static String CLASS = "net.geoprism.registry.etl.ImportStageMaster";
  private static final long serialVersionUID = 2130705567;
  
  public ImportStageMasterBase()
  {
    super();
  }
  
  protected String getDeclaredType()
  {
    return CLASS;
  }
  
  public static ImportStageMasterQuery getAllInstances(String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {
    ImportStageMasterQuery query = new ImportStageMasterQuery(new com.runwaysdk.query.QueryFactory());
    com.runwaysdk.business.Entity.getAllInstances(query, sortAttribute, ascending, pageSize, pageNumber);
    return query;
  }
  
  public static ImportStageMaster get(String oid)
  {
    return (ImportStageMaster) com.runwaysdk.business.Business.get(oid);
  }
  
  public static ImportStageMaster getByKey(String key)
  {
    return (ImportStageMaster) com.runwaysdk.business.Business.get(CLASS, key);
  }
  
  public static ImportStageMaster getEnumeration(String enumName)
  {
    return (ImportStageMaster) com.runwaysdk.business.Business.getEnumeration(net.geoprism.registry.etl.ImportStageMaster.CLASS ,enumName);
  }
  
  public static ImportStageMaster lock(java.lang.String oid)
  {
    ImportStageMaster _instance = ImportStageMaster.get(oid);
    _instance.lock();
    
    return _instance;
  }
  
  public static ImportStageMaster unlock(java.lang.String oid)
  {
    ImportStageMaster _instance = ImportStageMaster.get(oid);
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
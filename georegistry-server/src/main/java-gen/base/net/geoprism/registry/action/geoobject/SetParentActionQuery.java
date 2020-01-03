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
package net.geoprism.registry.action.geoobject;

@com.runwaysdk.business.ClassSignature(hash = 940930911)
/**
 * This class is generated automatically.
 * DO NOT MAKE CHANGES TO IT - THEY WILL BE OVERWRITTEN
 * Custom business logic should be added to SetParentAction.java
 *
 * @author Autogenerated by RunwaySDK
 */
public  class SetParentActionQuery extends net.geoprism.registry.action.AbstractActionQuery

{

  public SetParentActionQuery(com.runwaysdk.query.QueryFactory componentQueryFactory)
  {
    super(componentQueryFactory);
    if (this.getComponentQuery() == null)
    {
      com.runwaysdk.business.BusinessQuery businessQuery = componentQueryFactory.businessQuery(this.getClassType());

       this.setBusinessQuery(businessQuery);
    }
  }

  public SetParentActionQuery(com.runwaysdk.query.ValueQuery valueQuery)
  {
    super(valueQuery);
    if (this.getComponentQuery() == null)
    {
      com.runwaysdk.business.BusinessQuery businessQuery = new com.runwaysdk.business.BusinessQuery(valueQuery, this.getClassType());

       this.setBusinessQuery(businessQuery);
    }
  }

  public String getClassType()
  {
    return net.geoprism.registry.action.geoobject.SetParentAction.CLASS;
  }
  public com.runwaysdk.query.SelectableChar getChildCode()
  {
    return getChildCode(null);

  }
 
  public com.runwaysdk.query.SelectableChar getChildCode(String alias)
  {
    return (com.runwaysdk.query.SelectableChar)this.getComponentQuery().get(net.geoprism.registry.action.geoobject.SetParentAction.CHILDCODE, alias, null);

  }
 
  public com.runwaysdk.query.SelectableChar getChildCode(String alias, String displayLabel)
  {
    return (com.runwaysdk.query.SelectableChar)this.getComponentQuery().get(net.geoprism.registry.action.geoobject.SetParentAction.CHILDCODE, alias, displayLabel);

  }
  public com.runwaysdk.query.SelectableChar getChildTypeCode()
  {
    return getChildTypeCode(null);

  }
 
  public com.runwaysdk.query.SelectableChar getChildTypeCode(String alias)
  {
    return (com.runwaysdk.query.SelectableChar)this.getComponentQuery().get(net.geoprism.registry.action.geoobject.SetParentAction.CHILDTYPECODE, alias, null);

  }
 
  public com.runwaysdk.query.SelectableChar getChildTypeCode(String alias, String displayLabel)
  {
    return (com.runwaysdk.query.SelectableChar)this.getComponentQuery().get(net.geoprism.registry.action.geoobject.SetParentAction.CHILDTYPECODE, alias, displayLabel);

  }
  public com.runwaysdk.query.SelectableChar getJson()
  {
    return getJson(null);

  }
 
  public com.runwaysdk.query.SelectableChar getJson(String alias)
  {
    return (com.runwaysdk.query.SelectableChar)this.getComponentQuery().get(net.geoprism.registry.action.geoobject.SetParentAction.JSON, alias, null);

  }
 
  public com.runwaysdk.query.SelectableChar getJson(String alias, String displayLabel)
  {
    return (com.runwaysdk.query.SelectableChar)this.getComponentQuery().get(net.geoprism.registry.action.geoobject.SetParentAction.JSON, alias, displayLabel);

  }
  /**  
   * Returns an iterator of Business objects that match the query criteria specified
   * on this query object. 
   * @return iterator of Business objects that match the query criteria specified
   * on this query object.
   */
  public com.runwaysdk.query.OIterator<? extends SetParentAction> getIterator()
  {
    this.checkNotUsedInValueQuery();
    String sqlStmt;
    if (_limit != null && _skip != null)
    {
      sqlStmt = this.getComponentQuery().getSQL(_limit, _skip);
    }
    else
    {
      sqlStmt = this.getComponentQuery().getSQL();
    }
    java.util.Map<String, com.runwaysdk.query.ColumnInfo> columnInfoMap = this.getComponentQuery().getColumnInfoMap();

    java.sql.ResultSet results = com.runwaysdk.dataaccess.database.Database.query(sqlStmt);
    return new com.runwaysdk.business.BusinessIterator<SetParentAction>(this.getComponentQuery().getMdEntityIF(), columnInfoMap, results);
  }


/**
 * Interface that masks all type unsafe query methods and defines all type safe methods.
 * This type is used when a join is performed on this class as a reference.
 **/
  public interface SetParentActionQueryReferenceIF extends net.geoprism.registry.action.AbstractActionQuery.AbstractActionQueryReferenceIF
  {

    public com.runwaysdk.query.SelectableChar getChildCode();
    public com.runwaysdk.query.SelectableChar getChildCode(String alias);
    public com.runwaysdk.query.SelectableChar getChildCode(String alias, String displayLabel);
    public com.runwaysdk.query.SelectableChar getChildTypeCode();
    public com.runwaysdk.query.SelectableChar getChildTypeCode(String alias);
    public com.runwaysdk.query.SelectableChar getChildTypeCode(String alias, String displayLabel);
    public com.runwaysdk.query.SelectableChar getJson();
    public com.runwaysdk.query.SelectableChar getJson(String alias);
    public com.runwaysdk.query.SelectableChar getJson(String alias, String displayLabel);

    public com.runwaysdk.query.BasicCondition EQ(net.geoprism.registry.action.geoobject.SetParentAction setParentAction);

    public com.runwaysdk.query.BasicCondition NE(net.geoprism.registry.action.geoobject.SetParentAction setParentAction);

  }

/**
 * Implements type safe query methods.
 * This type is used when a join is performed on this class as a reference.
 **/
  public static class SetParentActionQueryReference extends net.geoprism.registry.action.AbstractActionQuery.AbstractActionQueryReference
 implements SetParentActionQueryReferenceIF

  {

  public SetParentActionQueryReference(com.runwaysdk.dataaccess.MdAttributeRefDAOIF mdAttributeIF, String attributeNamespace, String definingTableName, String definingTableAlias, com.runwaysdk.dataaccess.MdBusinessDAOIF referenceMdBusinessIF, String referenceTableAlias, com.runwaysdk.query.ComponentQuery rootQuery, java.util.Set<com.runwaysdk.query.Join> tableJoinSet, String alias, String displayLabel)
  {
    super(mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, referenceMdBusinessIF, referenceTableAlias, rootQuery, tableJoinSet, alias, displayLabel);

  }


    public com.runwaysdk.query.BasicCondition EQ(net.geoprism.registry.action.geoobject.SetParentAction setParentAction)
    {
      if(setParentAction == null) return this.EQ((java.lang.String)null);
      return this.EQ(setParentAction.getOid());
    }

    public com.runwaysdk.query.BasicCondition NE(net.geoprism.registry.action.geoobject.SetParentAction setParentAction)
    {
      if(setParentAction == null) return this.NE((java.lang.String)null);
      return this.NE(setParentAction.getOid());
    }

  public com.runwaysdk.query.SelectableChar getChildCode()
  {
    return getChildCode(null);

  }
 
  public com.runwaysdk.query.SelectableChar getChildCode(String alias)
  {
    return (com.runwaysdk.query.SelectableChar)this.get(net.geoprism.registry.action.geoobject.SetParentAction.CHILDCODE, alias, null);

  }
 
  public com.runwaysdk.query.SelectableChar getChildCode(String alias, String displayLabel)
  {
    return (com.runwaysdk.query.SelectableChar)this.get(net.geoprism.registry.action.geoobject.SetParentAction.CHILDCODE, alias, displayLabel);

  }
  public com.runwaysdk.query.SelectableChar getChildTypeCode()
  {
    return getChildTypeCode(null);

  }
 
  public com.runwaysdk.query.SelectableChar getChildTypeCode(String alias)
  {
    return (com.runwaysdk.query.SelectableChar)this.get(net.geoprism.registry.action.geoobject.SetParentAction.CHILDTYPECODE, alias, null);

  }
 
  public com.runwaysdk.query.SelectableChar getChildTypeCode(String alias, String displayLabel)
  {
    return (com.runwaysdk.query.SelectableChar)this.get(net.geoprism.registry.action.geoobject.SetParentAction.CHILDTYPECODE, alias, displayLabel);

  }
  public com.runwaysdk.query.SelectableChar getJson()
  {
    return getJson(null);

  }
 
  public com.runwaysdk.query.SelectableChar getJson(String alias)
  {
    return (com.runwaysdk.query.SelectableChar)this.get(net.geoprism.registry.action.geoobject.SetParentAction.JSON, alias, null);

  }
 
  public com.runwaysdk.query.SelectableChar getJson(String alias, String displayLabel)
  {
    return (com.runwaysdk.query.SelectableChar)this.get(net.geoprism.registry.action.geoobject.SetParentAction.JSON, alias, displayLabel);

  }
  }

/**
 * Interface that masks all type unsafe query methods and defines all type safe methods.
 * This type is used when a join is performed on this class as a reference.
 **/
  public interface SetParentActionQueryMultiReferenceIF extends net.geoprism.registry.action.AbstractActionQuery.AbstractActionQueryMultiReferenceIF
  {

    public com.runwaysdk.query.SelectableChar getChildCode();
    public com.runwaysdk.query.SelectableChar getChildCode(String alias);
    public com.runwaysdk.query.SelectableChar getChildCode(String alias, String displayLabel);
    public com.runwaysdk.query.SelectableChar getChildTypeCode();
    public com.runwaysdk.query.SelectableChar getChildTypeCode(String alias);
    public com.runwaysdk.query.SelectableChar getChildTypeCode(String alias, String displayLabel);
    public com.runwaysdk.query.SelectableChar getJson();
    public com.runwaysdk.query.SelectableChar getJson(String alias);
    public com.runwaysdk.query.SelectableChar getJson(String alias, String displayLabel);

    public com.runwaysdk.query.Condition containsAny(net.geoprism.registry.action.geoobject.SetParentAction ... setParentAction);
    public com.runwaysdk.query.Condition notContainsAny(net.geoprism.registry.action.geoobject.SetParentAction ... setParentAction);
    public com.runwaysdk.query.Condition containsAll(net.geoprism.registry.action.geoobject.SetParentAction ... setParentAction);
    public com.runwaysdk.query.Condition notContainsAll(net.geoprism.registry.action.geoobject.SetParentAction ... setParentAction);
    public com.runwaysdk.query.Condition containsExactly(net.geoprism.registry.action.geoobject.SetParentAction ... setParentAction);
  }

/**
 * Implements type safe query methods.
 * This type is used when a join is performed on this class as a reference.
 **/
  public static class SetParentActionQueryMultiReference extends net.geoprism.registry.action.AbstractActionQuery.AbstractActionQueryMultiReference
 implements SetParentActionQueryMultiReferenceIF

  {

  public SetParentActionQueryMultiReference(com.runwaysdk.dataaccess.MdAttributeMultiReferenceDAOIF mdAttributeIF, String attributeNamespace, String definingTableName, String definingTableAlias, String mdMultiReferenceTableName, com.runwaysdk.dataaccess.MdBusinessDAOIF referenceMdBusinessIF, String referenceTableAlias, com.runwaysdk.query.ComponentQuery rootQuery, java.util.Set<com.runwaysdk.query.Join> tableJoinSet, String alias, String displayLabel)
  {
    super(mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, mdMultiReferenceTableName, referenceMdBusinessIF, referenceTableAlias, rootQuery, tableJoinSet, alias, displayLabel);

  }



    public com.runwaysdk.query.Condition containsAny(net.geoprism.registry.action.geoobject.SetParentAction ... setParentAction)  {

      String[] itemIdArray = new String[setParentAction.length]; 

      for (int i=0; i<setParentAction.length; i++)
      {
        itemIdArray[i] = setParentAction[i].getOid();
      }

      return this.containsAny(itemIdArray);
  }

    public com.runwaysdk.query.Condition notContainsAny(net.geoprism.registry.action.geoobject.SetParentAction ... setParentAction)  {

      String[] itemIdArray = new String[setParentAction.length]; 

      for (int i=0; i<setParentAction.length; i++)
      {
        itemIdArray[i] = setParentAction[i].getOid();
      }

      return this.notContainsAny(itemIdArray);
  }

    public com.runwaysdk.query.Condition containsAll(net.geoprism.registry.action.geoobject.SetParentAction ... setParentAction)  {

      String[] itemIdArray = new String[setParentAction.length]; 

      for (int i=0; i<setParentAction.length; i++)
      {
        itemIdArray[i] = setParentAction[i].getOid();
      }

      return this.containsAll(itemIdArray);
  }

    public com.runwaysdk.query.Condition notContainsAll(net.geoprism.registry.action.geoobject.SetParentAction ... setParentAction)  {

      String[] itemIdArray = new String[setParentAction.length]; 

      for (int i=0; i<setParentAction.length; i++)
      {
        itemIdArray[i] = setParentAction[i].getOid();
      }

      return this.notContainsAll(itemIdArray);
  }

    public com.runwaysdk.query.Condition containsExactly(net.geoprism.registry.action.geoobject.SetParentAction ... setParentAction)  {

      String[] itemIdArray = new String[setParentAction.length]; 

      for (int i=0; i<setParentAction.length; i++)
      {
        itemIdArray[i] = setParentAction[i].getOid();
      }

      return this.containsExactly(itemIdArray);
  }
  public com.runwaysdk.query.SelectableChar getChildCode()
  {
    return getChildCode(null);

  }
 
  public com.runwaysdk.query.SelectableChar getChildCode(String alias)
  {
    return (com.runwaysdk.query.SelectableChar)this.get(net.geoprism.registry.action.geoobject.SetParentAction.CHILDCODE, alias, null);

  }
 
  public com.runwaysdk.query.SelectableChar getChildCode(String alias, String displayLabel)
  {
    return (com.runwaysdk.query.SelectableChar)this.get(net.geoprism.registry.action.geoobject.SetParentAction.CHILDCODE, alias, displayLabel);

  }
  public com.runwaysdk.query.SelectableChar getChildTypeCode()
  {
    return getChildTypeCode(null);

  }
 
  public com.runwaysdk.query.SelectableChar getChildTypeCode(String alias)
  {
    return (com.runwaysdk.query.SelectableChar)this.get(net.geoprism.registry.action.geoobject.SetParentAction.CHILDTYPECODE, alias, null);

  }
 
  public com.runwaysdk.query.SelectableChar getChildTypeCode(String alias, String displayLabel)
  {
    return (com.runwaysdk.query.SelectableChar)this.get(net.geoprism.registry.action.geoobject.SetParentAction.CHILDTYPECODE, alias, displayLabel);

  }
  public com.runwaysdk.query.SelectableChar getJson()
  {
    return getJson(null);

  }
 
  public com.runwaysdk.query.SelectableChar getJson(String alias)
  {
    return (com.runwaysdk.query.SelectableChar)this.get(net.geoprism.registry.action.geoobject.SetParentAction.JSON, alias, null);

  }
 
  public com.runwaysdk.query.SelectableChar getJson(String alias, String displayLabel)
  {
    return (com.runwaysdk.query.SelectableChar)this.get(net.geoprism.registry.action.geoobject.SetParentAction.JSON, alias, displayLabel);

  }
  }
}
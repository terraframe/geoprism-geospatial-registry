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
package net.geoprism.registry;

@com.runwaysdk.business.ClassSignature(hash = -1602916596)
/**
 * This class is generated automatically.
 * DO NOT MAKE CHANGES TO IT - THEY WILL BE OVERWRITTEN
 * Custom business logic should be added to ChangeFrequencyMaster.java
 *
 * @author Autogenerated by RunwaySDK
 */
public  class ChangeFrequencyMasterQuery extends com.runwaysdk.system.EnumerationMasterQuery

{

  public ChangeFrequencyMasterQuery(com.runwaysdk.query.QueryFactory componentQueryFactory)
  {
    super(componentQueryFactory);
    if (this.getComponentQuery() == null)
    {
      com.runwaysdk.business.BusinessQuery businessQuery = componentQueryFactory.businessQuery(this.getClassType());

       this.setBusinessQuery(businessQuery);
    }
  }

  public ChangeFrequencyMasterQuery(com.runwaysdk.query.ValueQuery valueQuery)
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
    return net.geoprism.registry.ChangeFrequencyMaster.CLASS;
  }
  /**  
   * Returns an iterator of Business objects that match the query criteria specified
   * on this query object. 
   * @return iterator of Business objects that match the query criteria specified
   * on this query object.
   */
  public com.runwaysdk.query.OIterator<? extends ChangeFrequencyMaster> getIterator()
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
    return new com.runwaysdk.business.BusinessIterator<ChangeFrequencyMaster>(this.getComponentQuery().getMdEntityIF(), columnInfoMap, results);
  }


/**
 * 
 **/

  public com.runwaysdk.query.Condition enum_ChangeFrequency()
  {
    com.runwaysdk.query.QueryFactory queryFactory = this.getQueryFactory();
    com.runwaysdk.business.RelationshipQuery relationshipQuery = queryFactory.relationshipQuery(com.runwaysdk.system.metadata.EnumerationAttributeItem.CLASS);

    com.runwaysdk.business.BusinessQuery businessQuery = queryFactory.businessQuery(com.runwaysdk.system.metadata.MdEnumeration.CLASS);
    com.runwaysdk.dataaccess.MdEnumerationDAOIF mdEnumerationIF = com.runwaysdk.dataaccess.metadata.MdEnumerationDAO.getMdEnumerationDAO(net.geoprism.registry.ChangeFrequency.CLASS); 
    businessQuery.WHERE(businessQuery.oid().EQ(mdEnumerationIF.getOid()));

    relationshipQuery.WHERE(relationshipQuery.hasParent(businessQuery));

    return this.getBusinessQuery().isChildIn(relationshipQuery);
  }


/**
 * 
 **/

  public com.runwaysdk.query.Condition notEnum_ChangeFrequency()
  {
    com.runwaysdk.query.QueryFactory queryFactory = this.getQueryFactory();
    com.runwaysdk.business.RelationshipQuery relationshipQuery = queryFactory.relationshipQuery(com.runwaysdk.system.metadata.EnumerationAttributeItem.CLASS);

    com.runwaysdk.business.BusinessQuery businessQuery = queryFactory.businessQuery(com.runwaysdk.system.metadata.MdEnumeration.CLASS);
    com.runwaysdk.dataaccess.MdEnumerationDAOIF mdEnumerationIF = com.runwaysdk.dataaccess.metadata.MdEnumerationDAO.getMdEnumerationDAO(net.geoprism.registry.ChangeFrequency.CLASS); 
    businessQuery.WHERE(businessQuery.oid().EQ(mdEnumerationIF.getOid()));

    relationshipQuery.WHERE(relationshipQuery.hasParent(businessQuery));

    return this.getBusinessQuery().isNotChildIn(relationshipQuery);
  }


/**
 * Interface that masks all type unsafe query methods and defines all type safe methods.
 * This type is used when a join is performed on this class as a reference.
 **/
  public interface ChangeFrequencyMasterQueryReferenceIF extends com.runwaysdk.system.EnumerationMasterQuery.EnumerationMasterQueryReferenceIF
  {


    public com.runwaysdk.query.BasicCondition EQ(net.geoprism.registry.ChangeFrequencyMaster changeFrequencyMaster);

    public com.runwaysdk.query.BasicCondition NE(net.geoprism.registry.ChangeFrequencyMaster changeFrequencyMaster);

  }

/**
 * Implements type safe query methods.
 * This type is used when a join is performed on this class as a reference.
 **/
  public static class ChangeFrequencyMasterQueryReference extends com.runwaysdk.system.EnumerationMasterQuery.EnumerationMasterQueryReference
 implements ChangeFrequencyMasterQueryReferenceIF

  {

  public ChangeFrequencyMasterQueryReference(com.runwaysdk.dataaccess.MdAttributeRefDAOIF mdAttributeIF, String attributeNamespace, String definingTableName, String definingTableAlias, com.runwaysdk.dataaccess.MdBusinessDAOIF referenceMdBusinessIF, String referenceTableAlias, com.runwaysdk.query.ComponentQuery rootQuery, java.util.Set<com.runwaysdk.query.Join> tableJoinSet, String alias, String displayLabel)
  {
    super(mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, referenceMdBusinessIF, referenceTableAlias, rootQuery, tableJoinSet, alias, displayLabel);

  }


    public com.runwaysdk.query.BasicCondition EQ(net.geoprism.registry.ChangeFrequencyMaster changeFrequencyMaster)
    {
      if(changeFrequencyMaster == null) return this.EQ((java.lang.String)null);
      return this.EQ(changeFrequencyMaster.getOid());
    }

    public com.runwaysdk.query.BasicCondition NE(net.geoprism.registry.ChangeFrequencyMaster changeFrequencyMaster)
    {
      if(changeFrequencyMaster == null) return this.NE((java.lang.String)null);
      return this.NE(changeFrequencyMaster.getOid());
    }

  }

/**
 * Interface that masks all type unsafe query methods and defines all type safe methods.
 * This type is used when a join is performed on this class as an enumeration.
 **/
  public interface ChangeFrequencyMasterQueryEnumerationIF extends com.runwaysdk.system.EnumerationMasterQuery.EnumerationMasterQueryEnumerationIF
  {


  }

/**
 * Implements type safe query methods.
 * This type is used when a join is performed on this class as an enumeration.
 **/
  public static class ChangeFrequencyMasterQueryEnumeration extends com.runwaysdk.system.EnumerationMasterQuery.EnumerationMasterQueryEnumeration
 implements ChangeFrequencyMasterQueryEnumerationIF
  {

  public ChangeFrequencyMasterQueryEnumeration(com.runwaysdk.dataaccess.MdAttributeEnumerationDAOIF mdAttributeIF, String attributeNamespace, String definingTableName, String definingTableAlias, String mdEnumerationTableName,com.runwaysdk.dataaccess.MdBusinessDAOIF masterMdBusinessIF, String masterTableAlias, com.runwaysdk.query.ComponentQuery rootQuery, java.util.Set<com.runwaysdk.query.Join> tableJoinSet, String alias, String displayLabel)
  {
    super(mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, mdEnumerationTableName, masterMdBusinessIF, masterTableAlias, rootQuery, tableJoinSet, alias, displayLabel);

  }

  }

/**
 * Specifies type safe query methods for the enumeration net.geoprism.registry.ChangeFrequency.
 * This type is used when a join is performed on this class as an enumeration.
 **/
  public interface ChangeFrequencyQueryIF extends ChangeFrequencyMasterQueryEnumerationIF  {

    public com.runwaysdk.query.Condition containsAny(net.geoprism.registry.ChangeFrequency ... changeFrequency);
    public com.runwaysdk.query.Condition notContainsAny(net.geoprism.registry.ChangeFrequency ... changeFrequency);
    public com.runwaysdk.query.Condition containsAll(net.geoprism.registry.ChangeFrequency ... changeFrequency);
    public com.runwaysdk.query.Condition notContainsAll(net.geoprism.registry.ChangeFrequency ... changeFrequency);
    public com.runwaysdk.query.Condition containsExactly(net.geoprism.registry.ChangeFrequency ... changeFrequency);
  }

/**
 * Implements type safe query methods for the enumeration net.geoprism.registry.ChangeFrequency.
 * This type is used when a join is performed on this class as an enumeration.
 **/
  public static class ChangeFrequencyQuery extends ChangeFrequencyMasterQueryEnumeration implements  ChangeFrequencyQueryIF
  {
  public ChangeFrequencyQuery(com.runwaysdk.dataaccess.MdAttributeEnumerationDAOIF mdAttributeIF, String attributeNamespace, String definingTableName, String definingTableAlias, String mdEnumerationTableName,com.runwaysdk.dataaccess.MdBusinessDAOIF masterMdBusinessIF, String masterTableAlias, com.runwaysdk.query.ComponentQuery rootQuery, java.util.Set<com.runwaysdk.query.Join> tableJoinSet, String alias, String displayLabel)
  {
    super(mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, mdEnumerationTableName, masterMdBusinessIF, masterTableAlias, rootQuery, tableJoinSet, alias, displayLabel);

  }

    public com.runwaysdk.query.Condition containsAny(net.geoprism.registry.ChangeFrequency ... changeFrequency)  {

      String[] enumIdArray = new String[changeFrequency.length]; 

      for (int i=0; i<changeFrequency.length; i++)
      {
        enumIdArray[i] = changeFrequency[i].getOid();
      }

      return this.containsAny(enumIdArray);
  }

    public com.runwaysdk.query.Condition notContainsAny(net.geoprism.registry.ChangeFrequency ... changeFrequency)  {

      String[] enumIdArray = new String[changeFrequency.length]; 

      for (int i=0; i<changeFrequency.length; i++)
      {
        enumIdArray[i] = changeFrequency[i].getOid();
      }

      return this.notContainsAny(enumIdArray);
  }

    public com.runwaysdk.query.Condition containsAll(net.geoprism.registry.ChangeFrequency ... changeFrequency)  {

      String[] enumIdArray = new String[changeFrequency.length]; 

      for (int i=0; i<changeFrequency.length; i++)
      {
        enumIdArray[i] = changeFrequency[i].getOid();
      }

      return this.containsAll(enumIdArray);
  }

    public com.runwaysdk.query.Condition notContainsAll(net.geoprism.registry.ChangeFrequency ... changeFrequency)  {

      String[] enumIdArray = new String[changeFrequency.length]; 

      for (int i=0; i<changeFrequency.length; i++)
      {
        enumIdArray[i] = changeFrequency[i].getOid();
      }

      return this.notContainsAll(enumIdArray);
  }

    public com.runwaysdk.query.Condition containsExactly(net.geoprism.registry.ChangeFrequency ... changeFrequency)  {

      String[] enumIdArray = new String[changeFrequency.length]; 

      for (int i=0; i<changeFrequency.length; i++)
      {
        enumIdArray[i] = changeFrequency[i].getOid();
      }

      return this.containsExactly(enumIdArray);
  }
  }
/**
 * Interface that masks all type unsafe query methods and defines all type safe methods.
 * This type is used when a join is performed on this class as a reference.
 **/
  public interface ChangeFrequencyMasterQueryMultiReferenceIF extends com.runwaysdk.system.EnumerationMasterQuery.EnumerationMasterQueryMultiReferenceIF
  {


    public com.runwaysdk.query.Condition containsAny(net.geoprism.registry.ChangeFrequencyMaster ... changeFrequencyMaster);
    public com.runwaysdk.query.Condition notContainsAny(net.geoprism.registry.ChangeFrequencyMaster ... changeFrequencyMaster);
    public com.runwaysdk.query.Condition containsAll(net.geoprism.registry.ChangeFrequencyMaster ... changeFrequencyMaster);
    public com.runwaysdk.query.Condition notContainsAll(net.geoprism.registry.ChangeFrequencyMaster ... changeFrequencyMaster);
    public com.runwaysdk.query.Condition containsExactly(net.geoprism.registry.ChangeFrequencyMaster ... changeFrequencyMaster);
  }

/**
 * Implements type safe query methods.
 * This type is used when a join is performed on this class as a reference.
 **/
  public static class ChangeFrequencyMasterQueryMultiReference extends com.runwaysdk.system.EnumerationMasterQuery.EnumerationMasterQueryMultiReference
 implements ChangeFrequencyMasterQueryMultiReferenceIF

  {

  public ChangeFrequencyMasterQueryMultiReference(com.runwaysdk.dataaccess.MdAttributeMultiReferenceDAOIF mdAttributeIF, String attributeNamespace, String definingTableName, String definingTableAlias, String mdMultiReferenceTableName, com.runwaysdk.dataaccess.MdBusinessDAOIF referenceMdBusinessIF, String referenceTableAlias, com.runwaysdk.query.ComponentQuery rootQuery, java.util.Set<com.runwaysdk.query.Join> tableJoinSet, String alias, String displayLabel)
  {
    super(mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, mdMultiReferenceTableName, referenceMdBusinessIF, referenceTableAlias, rootQuery, tableJoinSet, alias, displayLabel);

  }



    public com.runwaysdk.query.Condition containsAny(net.geoprism.registry.ChangeFrequencyMaster ... changeFrequencyMaster)  {

      String[] itemIdArray = new String[changeFrequencyMaster.length]; 

      for (int i=0; i<changeFrequencyMaster.length; i++)
      {
        itemIdArray[i] = changeFrequencyMaster[i].getOid();
      }

      return this.containsAny(itemIdArray);
  }

    public com.runwaysdk.query.Condition notContainsAny(net.geoprism.registry.ChangeFrequencyMaster ... changeFrequencyMaster)  {

      String[] itemIdArray = new String[changeFrequencyMaster.length]; 

      for (int i=0; i<changeFrequencyMaster.length; i++)
      {
        itemIdArray[i] = changeFrequencyMaster[i].getOid();
      }

      return this.notContainsAny(itemIdArray);
  }

    public com.runwaysdk.query.Condition containsAll(net.geoprism.registry.ChangeFrequencyMaster ... changeFrequencyMaster)  {

      String[] itemIdArray = new String[changeFrequencyMaster.length]; 

      for (int i=0; i<changeFrequencyMaster.length; i++)
      {
        itemIdArray[i] = changeFrequencyMaster[i].getOid();
      }

      return this.containsAll(itemIdArray);
  }

    public com.runwaysdk.query.Condition notContainsAll(net.geoprism.registry.ChangeFrequencyMaster ... changeFrequencyMaster)  {

      String[] itemIdArray = new String[changeFrequencyMaster.length]; 

      for (int i=0; i<changeFrequencyMaster.length; i++)
      {
        itemIdArray[i] = changeFrequencyMaster[i].getOid();
      }

      return this.notContainsAll(itemIdArray);
  }

    public com.runwaysdk.query.Condition containsExactly(net.geoprism.registry.ChangeFrequencyMaster ... changeFrequencyMaster)  {

      String[] itemIdArray = new String[changeFrequencyMaster.length]; 

      for (int i=0; i<changeFrequencyMaster.length; i++)
      {
        itemIdArray[i] = changeFrequencyMaster[i].getOid();
      }

      return this.containsExactly(itemIdArray);
  }
  }
}
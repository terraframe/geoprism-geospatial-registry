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
package net.geoprism.registry.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;
import java.util.TimeZone;

import org.commongeoregistry.adapter.constants.DefaultAttribute;
import org.commongeoregistry.adapter.constants.DefaultTerms;
import org.commongeoregistry.adapter.dataaccess.AttributeGeometry;
import org.commongeoregistry.adapter.dataaccess.GeoObjectOverTime;
import org.commongeoregistry.adapter.dataaccess.ValueOverTimeCollectionDTO;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.runwaysdk.dataaccess.graph.attributes.ValueOverTime;
import com.runwaysdk.session.Request;
import com.vividsolutions.jts.geom.Geometry;

import junit.framework.Assert;
import net.geoprism.registry.GeoObjectStatus;
import net.geoprism.registry.model.ServerGeoObjectIF;
import net.geoprism.registry.test.FastTestDataset;
import net.geoprism.registry.test.TestGeoObjectInfo;

public class RegistryVersionTest
{
  protected static FastTestDataset               testData;

  @BeforeClass
  public static void setUpClass()
  {
    testData = FastTestDataset.newTestData();
    testData.setUpMetadata();
  }
  
  @AfterClass
  public static void cleanUpClass()
  {
    testData.tearDownMetadata();
  }
  
  @Before
  public void setUp()
  {
    testData.setUpInstanceData();
    
    testData.logIn(testData.USER_CGOV_RA);
  }

  @After
  public void tearDown()
  {
    testData.logOut();
    
    testData.tearDownInstanceData();
  }
  
  @Test
  public void testGetGeoObjectOverTimeByCode()
  {
    this.addVersionData(testData.PROV_CENTRAL);
    
    GeoObjectOverTime goTime = testData.adapter.getGeoObjectOverTimeByCode(testData.PROV_CENTRAL.getCode(), testData.PROV_CENTRAL.getGeoObjectType().getCode());
    
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
    
    try
    {
      Assert.assertEquals(testData.PROV_CENTRAL.getCode(), goTime.getCode());
      Assert.assertEquals(DefaultTerms.GeoObjectStatusTerm.INACTIVE.code, goTime.getStatus(dateFormat.parse("1990-01-01")).getCode());
      Assert.assertEquals(DefaultTerms.GeoObjectStatusTerm.NEW.code, goTime.getStatus(dateFormat.parse("1990-02-01")).getCode());
      Assert.assertEquals(DefaultTerms.GeoObjectStatusTerm.PENDING.code, goTime.getStatus(dateFormat.parse("1990-03-01")).getCode());
      
      ValueOverTimeCollectionDTO allStatus = goTime.getAllValues(DefaultAttribute.STATUS.getName());
      Assert.assertEquals("1990-01-01", dateFormat.format(allStatus.get(0).getStartDate()));
      Assert.assertEquals("1990-01-31", dateFormat.format(allStatus.get(0).getEndDate()));
      Assert.assertEquals("1990-02-01", dateFormat.format(allStatus.get(1).getStartDate()));
      Assert.assertEquals("1990-02-28", dateFormat.format(allStatus.get(1).getEndDate()));
      Assert.assertEquals("1990-03-01", dateFormat.format(allStatus.get(2).getStartDate()));
      Assert.assertEquals(dateFormat.format(ValueOverTime.INFINITY_END_DATE), dateFormat.format(allStatus.get(2).getEndDate()));
      
      Geometry expectedGeom = testData.PROV_CENTRAL.fetchGeoObject().getGeometry();
      Geometry actualGeom = ( (AttributeGeometry) goTime.getAttributeOnDate(DefaultAttribute.GEOMETRY.getName(), new Date()) ).getValue();
      Assert.assertTrue(expectedGeom.equalsTopo(actualGeom));
    }
    catch (ParseException e)
    {
      throw new RuntimeException(e);
    }
  }
  
//  @Test
  //  public void testSetAttributeVersions()
//  {
//    String attributeName = "status";
//    AttributeType type = getAttributeType(attributeName);
//    
//    ServerGeoObjectIF serverObj = testData.PROV_CENTRAL.getServerObject();
//    
//    Assert.assertEquals(0, serverObj.getValuesOverTime(attributeName).size());
//    
//    ValueOverTimeCollectionDTO dto = buildVersionDTO(type);
//    
//    testData.adapter.setAttributeVersions(testData.PROV_CENTRAL.getCode(), testData.PROV_CENTRAL.getGeoObjectType().getCode(), "status", dto);
//    
//    assertVersionData(ValueOverTimeConverter.colToDTO(serverObj.getValuesOverTime(attributeName), type));
//  }
//  
//  private void assertVersionData(ValueOverTimeCollectionDTO dto)
//  {
//    SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
//    dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
//    
//    Assert.assertEquals(3, dto.size());
//   
//    try
//    {
//      Assert.assertEquals(dto.get(0).getValue(), GeoObjectStatus.INACTIVE);
//      Assert.assertEquals(dto.get(0).getStartDate(), dateFormat.parse("01-01-1990"));
//      Assert.assertEquals(dto.get(0).getEndDate(), dateFormat.parse("01-31-1990"));
//      
//      Assert.assertEquals(dto.get(1).getValue(), GeoObjectStatus.NEW);
//      Assert.assertEquals(dto.get(1).getStartDate(), dateFormat.parse("02-01-1990"));
//      Assert.assertEquals(dto.get(1).getEndDate(), dateFormat.parse("02-28-1990"));
//      
//      Assert.assertEquals(dto.get(2).getValue(), GeoObjectStatus.PENDING);
//      Assert.assertEquals(dto.get(2).getStartDate(), dateFormat.parse("03-01-1990"));
//      Assert.assertEquals(dto.get(2).getEndDate(), ValueOverTime.INFINITY_END_DATE);
//    }
//    catch (ParseException e)
//    {
//      throw new RuntimeException(e);
//    }
//  }
//  
  @Request
  @SuppressWarnings("unchecked")
  private void addVersionData(TestGeoObjectInfo geoObj)
  {
    SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
    dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
    
    try
    {
      ServerGeoObjectIF serverObj = geoObj.getServerObject();
      serverObj.getValuesOverTime("status").clear();
      
      serverObj.setStatus(GeoObjectStatus.INACTIVE, dateFormat.parse("01-01-1990"), null);
      Assert.assertEquals(GeoObjectStatus.INACTIVE.getOid(), ((Set<String>)serverObj.getValue("status", dateFormat.parse("01-01-1990"))).iterator().next());
      
      serverObj.setStatus(GeoObjectStatus.NEW, dateFormat.parse("02-01-1990"), null);
      Assert.assertEquals(GeoObjectStatus.INACTIVE.getOid(), ((Set<String>)serverObj.getValue("status", dateFormat.parse("01-01-1990"))).iterator().next());
      
      serverObj.setStatus(GeoObjectStatus.PENDING, dateFormat.parse("03-01-1990"), null);
      
      Assert.assertEquals(GeoObjectStatus.INACTIVE.getOid(), ((Set<String>)serverObj.getValue("status", dateFormat.parse("01-01-1990"))).iterator().next());
      
      serverObj.apply(false);
      
      Assert.assertEquals(GeoObjectStatus.INACTIVE.getOid(), ((Set<String>)serverObj.getValue("status", dateFormat.parse("01-01-1990"))).iterator().next());
    }
    catch (ParseException e)
    {
      throw new RuntimeException(e);
    }
  }
//  
//  private ValueOverTimeCollectionDTO buildVersionDTO(AttributeType type)
//  {
//    SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
//    dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
//    
//    ValueOverTimeCollectionDTO dto = new ValueOverTimeCollectionDTO();
//    
//    try
//    {
//      dto.add(new ValueOverTimeDTO(dateFormat.parse("01-01-1990"), null, GeoObjectStatus.INACTIVE, type));
//      dto.add(new ValueOverTimeDTO(dateFormat.parse("02-01-1990"), null, GeoObjectStatus.NEW, type));
//      dto.add(new ValueOverTimeDTO(dateFormat.parse("03-01-1990"), null, GeoObjectStatus.PENDING, type));
//    }
//    catch (ParseException e)
//    {
//      throw new RuntimeException(e);
//    }
//    
//    return dto;
//  }
//  
//  @Request
//  private AttributeType getAttributeType(String attributeName)
//  {
//    return testData.PROV_CENTRAL.getGeoObjectType().getGeoObjectType().getAttribute(attributeName).get();
//  }
}

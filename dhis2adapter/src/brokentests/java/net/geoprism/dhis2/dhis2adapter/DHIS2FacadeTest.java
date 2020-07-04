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
package net.geoprism.dhis2.dhis2adapter;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.http.NameValuePair;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.JsonObject;

import net.geoprism.dhis2.dhis2adapter.exception.HTTPException;
import net.geoprism.dhis2.dhis2adapter.exception.InvalidLoginException;
import net.geoprism.dhis2.dhis2adapter.exception.UnexpectedResponseException;
import net.geoprism.dhis2.dhis2adapter.response.DHIS2ImportResponse;
import net.geoprism.dhis2.dhis2adapter.response.DHIS2Response;
import net.geoprism.dhis2.dhis2adapter.response.TypeReportResponse;
import net.geoprism.dhis2.dhis2adapter.response.model.ErrorReport;

/**
 * Tests the DHIS2 Facade by talking to play.dhis2.org
 */
public class DHIS2FacadeTest
{
  private DHIS2Facade facade;
  
  @Before
  public void setUp()
  {
    HTTPConnector connector = new HTTPConnector();
    connector.setCredentials(Constants.USERNAME, Constants.PASSWORD);
    connector.setServerUrl(Constants.DHIS2_URL);
    
    facade = new DHIS2Facade(connector, Constants.API_VERSION);
  }
  
  @Test
  public void testSystemInfo() throws InvalidLoginException, HTTPException
  {
    DHIS2Response resp = facade.systemInfo();
    
    Assert.assertEquals(200, resp.getStatusCode());
    
    JsonObject jo = resp.getJsonObject();
    
    Assert.assertEquals(Constants.DHIS2_VERSION, jo.get("version").getAsString());
    Assert.assertEquals(Constants.DHIS2_URL, jo.get("contextPath").getAsString());
  }
  
//  @Test
//  public void testMetadataGet() throws Exception
//  {
//    List<NameValuePair> params = new ArrayList<NameValuePair>();
//    params.add(new BasicNameValuePair("organisationUnits", "true"));
//    params.add(new BasicNameValuePair("code", "OU_525"));
//    
//    DHIS2Response resp = facade.metadataGet(params);
//    
//    System.out.println(resp.getResponse());
//  }
  
  @Test
  public void testEntityIdGet() throws Exception
  {
    List<NameValuePair> params = new ArrayList<NameValuePair>();
//    params.add(new BasicNameValuePair("organisationUnits", "true"));
//    params.add(new BasicNameValuePair("code", "OU_525"));
    
    DHIS2Response resp = facade.entityIdGet("organisationUnits", "ImspTQPwCqd", params);
    
//    System.out.println(resp.getResponse());
  }
  
  @Test
  public void testMetadataPost() throws InvalidLoginException, HTTPException
  {
    // Payload taken from https://docs.dhis2.org/2.34/en/dhis2_developer_manual/web-api.html#metadata-import
    final String payload = "{\n" + 
        "  \"dataElements\": [\n" + 
        "    {\n" + 
        "      \"name\": \"EPI - IPV 3 doses given\",\n" + 
        "      \"shortName\": \"EPI - IPV 3 doses given\",\n" + 
        "      \"aggregationType\": \"SUM\",\n" + 
        "      \"domainType\": \"AGGREGATE\",\n" + 
        "      \"valueType\": \"INTEGER_ZERO_OR_POSITIVE\"\n" + 
        "    }\n" + 
        "  ]\n" + 
        "}";
    
    List<NameValuePair> params = new ArrayList<NameValuePair>();
    params.add(new BasicNameValuePair("importMode", "VALIDATE"));
    
    DHIS2ImportResponse resp = facade.metadataPost(params, new StringEntity(payload, Charset.forName("UTF-8")));
    
    Assert.assertEquals(200, resp.getStatusCode());
    
    JsonObject jo = resp.getJsonObject();
    
    Assert.assertEquals("OK", jo.get("status").getAsString());
  }
  
  @Test
  public void testGetDhis2Id() throws HTTPException, InvalidLoginException, UnexpectedResponseException
  {
    Set<String> set = new HashSet<String>();
    
    final int fetchSize = Dhis2IdCache.FETCH_NUM * 3 - 300;
    
    for (int i = 0; i < fetchSize; ++i)
    {
      String id = facade.getDhis2Id();
      
      Assert.assertNotNull(id);
      
      Assert.assertEquals(11, id.length());
      
      Assert.assertTrue(set.add(id));
    }
    
    Assert.assertEquals(new Integer((Dhis2IdCache.FETCH_NUM * 3) - (Dhis2IdCache.FETCH_NUM * 3 - 300)), facade.idCache.getNumIds());
  }
  
  @Test
  public void testEntityTranslations() throws HTTPException, InvalidLoginException, UnexpectedResponseException
  {
    final String sierraLeoneId = "ImspTQPwCqd";
    
    final String payload = "{\n" + 
        "  \"translations\": [\n" + 
        "    {\n" + 
        "      \"property\": \"NAME\",\n" + 
        "      \"locale\": \"km\",\n" + 
        "      \"value\": \"Sierra Leone km\"\n" + 
        "    },\n" + 
        "    {\n" + 
        "      \"property\": \"SHORT_NAME\",\n" + 
        "      \"locale\": \"km\",\n" + 
        "      \"value\": \"Sierra Leone km\"\n" + 
        "    }\n" + 
        "  ]\n" + 
        "}";
    
    List<NameValuePair> params = new ArrayList<NameValuePair>();
    params.add(new BasicNameValuePair("importMode", "VALIDATE"));
    
    TypeReportResponse resp = facade.entityTranslations("organisationUnits", sierraLeoneId, params, new StringEntity(payload, Charset.forName("UTF-8")));
    
    Assert.assertNull(resp.getResponse());
    
    Assert.assertFalse(resp.hasErrorReports());
    
    Assert.assertEquals(204, resp.getStatusCode());
  }
  
  /**
   * This test posts to the Entity Translations DHIS2 play endpoint, however it does so with a bad payload.
   * This payload is missing required parameters (specifically, value).
   */
  @Test
  public void testBadEntityTranslations() throws HTTPException, InvalidLoginException, UnexpectedResponseException
  {
    final String sierraLeoneId = "ImspTQPwCqd";
    
    final String payload = "{\n" + 
        "  \"translations\": [\n" + 
        "    {\n" + 
        "      \"property\": \"NAME\",\n" + 
        "      \"locale\": \"km\",\n" + 
        "      \"value\": \"Sierra Leone km\"\n" + 
        "    },\n" + 
        "    {\n" + 
        "      \"property\": \"SHORT_NAME\",\n" + 
        "      \"locale\": \"km\"\n" + 
        "    }\n" + 
        "  ]\n" + 
        "}";
    
    List<NameValuePair> params = new ArrayList<NameValuePair>();
    params.add(new BasicNameValuePair("importMode", "VALIDATE"));
    
    TypeReportResponse resp = facade.entityTranslations("organisationUnits", sierraLeoneId, params, new StringEntity(payload, Charset.forName("UTF-8")));
    
    Assert.assertEquals(409, resp.getStatusCode());
    
    Assert.assertNotNull(resp.getResponse());
    
    Assert.assertTrue(resp.hasErrorReports());
    
    Assert.assertFalse(resp.isSuccess());
    
    List<ErrorReport> reports = resp.getErrorReports();
    
    Assert.assertEquals(1, reports.size());
    Assert.assertEquals(ErrorCodes.MISSING_REQUIRED_PROPERTY, reports.get(0).getErrorCode());
    Assert.assertEquals("Missing required property `value`.", reports.get(0).getMessage());
  }
  
  // TODO : It says in their docs that they should support this, however it doesn't work on any DHIS2 server I've tried. (server responds 404)
  // https://docs.dhis2.org/2.34/en/dhis2_developer_manual/web-api.html#translations
//  @Test
//  public void testTranslationsPost() throws InvalidLoginException, HTTPException
//  {
//    JsonObject payload = new JsonObject();
//    payload.addProperty("objectId", "ImspTQPwCqd"); // We're currently hardcoded to the playstore SierraLeone object id. Let's hope this never changes.
//    payload.addProperty("className", "OrganisationUnit");
//    payload.addProperty("locale", "es");
//    payload.addProperty("property", "name");
//    payload.addProperty("value", "Spanish Test");
//    
//    List<NameValuePair> params = new ArrayList<NameValuePair>();
//    params.add(new BasicNameValuePair("importMode", "VALIDATE"));
//    
//    HTTPResponse resp = facade.translationsPost(params, new StringEntity(payload.toString(), Charset.forName("UTF-8")));
//    
//    System.out.println(resp.getResponse());
//    
//    Assert.assertEquals(200, resp.getStatusCode());
//  }
}
package net.geoprism.registry.service;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import net.geoprism.GeoprismUserDTO;
import net.geoprism.registry.OrganizationAndRoleTest;
import net.geoprism.registry.controller.RegistryAccountController;
import net.geoprism.registry.test.TestDataSet;

import org.commongeoregistry.adapter.metadata.RegistryRole;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.gson.JsonObject;
import com.runwaysdk.ClientSession;
import com.runwaysdk.Pair;
import com.runwaysdk.constants.ClientRequestIF;
import com.runwaysdk.constants.CommonProperties;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.mvc.RestResponse;
import com.runwaysdk.session.Request;

public class AccountServiceControllerTest
{
  public static RegistryAccountController           controller;
  
  public static ClientSession                       systemSession                   = null;

  public static ClientRequestIF                     clientRequest                   = null;
  
  public static String                              TEST_USER_NAME                  = "testRegistryUser";
  
  public static String                              TEST_USER_PASSWORD              = "abc123";
  
  public static String                              ORG_MOI                         = "MOI";
  
  public static String                              DISTRICT                        = "District";
  
  public static String                              VILLAGE                         = "Village";

  
  @BeforeClass
  public static void classSetUp()
  {
    controller = new RegistryAccountController();
    
    systemSession = ClientSession.createUserSession("default", TestDataSet.ADMIN_USER_NAME, TestDataSet.ADMIN_PASSWORD, new Locale[] { CommonProperties.getDefaultLocale() });
    clientRequest = systemSession.getRequest();
    
    classTearDownRequest();
    classSetUpRequest();
  }

  @Request
  public static void classSetUpRequest()
  {
    classSetUpTransaction();
  }
  
  @Transaction
  public static void classSetUpTransaction()
  {
    OrganizationAndRoleTest.createOrganization(ORG_MOI);
    OrganizationAndRoleTest.createGeoObjectType(ORG_MOI, DISTRICT);
    OrganizationAndRoleTest.createGeoObjectType(ORG_MOI, VILLAGE);
  }
  
  @AfterClass
  public static void classTearDown()
  {
    systemSession.logout();
    
    classTearDownRequest();
  }
  
  @Request
  public static void classTearDownRequest()
  {
    classTearDownTransaction();
  }

  @Transaction
  public static void classTearDownTransaction()
  {
    OrganizationAndRoleTest.deleteGeoObjectType(DISTRICT);
    OrganizationAndRoleTest.deleteGeoObjectType(VILLAGE);
    OrganizationAndRoleTest.deleteOrganization(ORG_MOI);
  }
  
  /** 
   * Test returning possible roles that can be assigned to a person for a given organization.
   */
  @Test
  public void createAndApplyUserWithOrgRoles()
  {
    RestResponse response = (RestResponse)controller.newInstance(clientRequest, ORG_MOI);
    @SuppressWarnings("rawtypes")
    Pair userPair = (Pair)response.getAttribute("user");
    GeoprismUserDTO user = (GeoprismUserDTO)userPair.getFirst();

    @SuppressWarnings("rawtypes")
    Pair rolesPair = (Pair)response.getAttribute("roles");
    
    JSONArray roleJSONArray = (JSONArray)rolesPair.getFirst();

    user.setFirstName("John");
    user.setLastName("Doe");
    user.setUsername("jdoe6");
    user.setEmail("john6@doe.com");
    user.setPassword("123456");
    
    String rmDistrictRole = RegistryRole.Type.getRM_RoleName(ORG_MOI, DISTRICT);
    String rmVillageRole = RegistryRole.Type.getRM_RoleName(ORG_MOI, VILLAGE);
    
    response = (RestResponse)controller.apply(clientRequest, user, rmDistrictRole+","+rmVillageRole);
    userPair = (Pair)response.getAttribute("user");
    user = (GeoprismUserDTO)userPair.getFirst();

    try
    {
      Pair rolePair = (Pair)response.getAttribute("roles");
      roleJSONArray = (JSONArray)rolePair.getFirst();
    
      Assert.assertEquals(2, roleJSONArray.length());
  
      Set<String> rolesFoundSet = new HashSet<String>();
      rolesFoundSet.add(RegistryRole.Type.getRM_RoleName(ORG_MOI, DISTRICT));
      rolesFoundSet.add(RegistryRole.Type.getRM_RoleName(ORG_MOI, VILLAGE));
  
      for (int i = 0; i < roleJSONArray.length(); i++)
      {
        JSONObject json = (JSONObject)roleJSONArray.get(i);
        RegistryRole registryRole = RegistryRole.fromJSON(json.toString());
 System.out.println(registryRole.getName()+" "+registryRole.getOrganizationLabel().getValue()+" "+registryRole.getGeoObjectTypeLabel().getValue()+" "+registryRole.isAssigned());       
 
        rolesFoundSet.remove(registryRole.getName());
      }
    
      Assert.assertEquals("Not all related roles were returned from the server", 0, rolesFoundSet.size());
    }
    finally
    {
      controller.remove(clientRequest, user.getOid());
    }
  }
  
  /** 
   * Test returning possible roles that can be assigned to a person for a given organization.
   */
  @Test
  public void createUserWithOrgRoles()
  {
    RestResponse response = (RestResponse)controller.newInstance(clientRequest, ORG_MOI);
    
    Pair pair = (Pair)response.getAttribute("roles");
    
    JSONArray roleJSONArray = (JSONArray)pair.getFirst();
    
    Assert.assertEquals(7, roleJSONArray.length());
    
    Set<String> rolesFoundSet = new HashSet<String>();
    
    rolesFoundSet.add(RegistryRole.Type.getRA_RoleName(ORG_MOI));
    rolesFoundSet.add(RegistryRole.Type.getRM_RoleName(ORG_MOI, DISTRICT));
    rolesFoundSet.add(RegistryRole.Type.getRC_RoleName(ORG_MOI, DISTRICT));
    rolesFoundSet.add(RegistryRole.Type.getAC_RoleName(ORG_MOI, DISTRICT));
    rolesFoundSet.add(RegistryRole.Type.getRM_RoleName(ORG_MOI, VILLAGE));
    rolesFoundSet.add(RegistryRole.Type.getRC_RoleName(ORG_MOI, VILLAGE));
    rolesFoundSet.add(RegistryRole.Type.getAC_RoleName(ORG_MOI, VILLAGE));
    
    for (int i = 0; i < roleJSONArray.length(); i++)
    {
      JSONObject json = (JSONObject)roleJSONArray.get(i);
      RegistryRole registryRole = RegistryRole.fromJSON(json.toString());
      rolesFoundSet.remove(registryRole.getName());
    }
    
    Assert.assertEquals("Not all related roles were returned from the server", 0, rolesFoundSet.size());
  }
  
  /** 
   * Test returning all possible roles that can be assigned to a person by passing in an empty string for the organizations.
   */
  @Test
  public void createUserWithRolesEmptyOrgString()
  {
    RestResponse response = (RestResponse)controller.newInstance(clientRequest, "");
    
    createUserWithRoles(response);
  }
  
  /** 
   * Test returning all possible roles that can be assigned to a person by passing in an empty string for the organizations.
   */
  @Test
  public void createUserWithRolesEmptyOrgNull()
  {
    RestResponse response = (RestResponse)controller.newInstance(clientRequest, null);
    
    createUserWithRoles(response);
  }
  
  private void createUserWithRoles(RestResponse response)
  {
    Pair pair = (Pair)response.getAttribute("roles");
    
    JSONArray roleJSONArray = (JSONArray)pair.getFirst();
    
    Assert.assertEquals(8, roleJSONArray.length());
    
    Set<String> rolesFoundSet = new HashSet<String>();
    rolesFoundSet.add(RegistryRole.Type.getSRA_RoleName());
    rolesFoundSet.add(RegistryRole.Type.getRA_RoleName(ORG_MOI));
    rolesFoundSet.add(RegistryRole.Type.getRM_RoleName(ORG_MOI, DISTRICT));
    rolesFoundSet.add(RegistryRole.Type.getRC_RoleName(ORG_MOI, DISTRICT));
    rolesFoundSet.add(RegistryRole.Type.getAC_RoleName(ORG_MOI, DISTRICT));
    rolesFoundSet.add(RegistryRole.Type.getRM_RoleName(ORG_MOI, VILLAGE));
    rolesFoundSet.add(RegistryRole.Type.getRC_RoleName(ORG_MOI, VILLAGE));
    rolesFoundSet.add(RegistryRole.Type.getAC_RoleName(ORG_MOI, VILLAGE));
    
    for (int i = 0; i < roleJSONArray.length(); i++)
    {
      JSONObject json = (JSONObject)roleJSONArray.get(i);
      RegistryRole registryRole = RegistryRole.fromJSON(json.toString());
      rolesFoundSet.remove(registryRole.getName());
    }
    
    Assert.assertEquals("Not all related roles were returned from the server", 0, rolesFoundSet.size());
  }
}

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
package net.geoprism.registry.etl.export;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.commongeoregistry.adapter.dataaccess.GeoObject;
import org.commongeoregistry.adapter.dataaccess.LocalizedValue;
import org.commongeoregistry.adapter.metadata.GeoObjectType;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.runwaysdk.LocalizationFacade;

import net.geoprism.dhis2.dhis2adapter.DHIS2Facade;
import net.geoprism.dhis2.dhis2adapter.exception.HTTPException;
import net.geoprism.dhis2.dhis2adapter.exception.InvalidLoginException;
import net.geoprism.dhis2.dhis2adapter.exception.UnexpectedResponseException;
import net.geoprism.registry.AdapterUtilities;
import net.geoprism.registry.conversion.VertexGeoObjectStrategy;
import net.geoprism.registry.graph.ExternalSystem;
import net.geoprism.registry.model.ServerGeoObjectIF;
import net.geoprism.registry.model.ServerGeoObjectType;
import net.geoprism.registry.model.ServerHierarchyType;
import net.geoprism.registry.model.ServerParentTreeNode;
import net.geoprism.registry.model.graph.VertexServerGeoObject;

public class DHIS2GeoObjectJsonAdapters
{
  public static class DHIS2Serializer implements JsonSerializer<GeoObject>
  {
    private ServerHierarchyType hierarchyType;
    
    private ServerGeoObjectType got;
    
    private Integer depth;
    
    private ExternalSystem ex;
    
    private DHIS2Facade dhis2;
    
    public DHIS2Serializer(DHIS2Facade dhis2, ServerGeoObjectType got, ServerHierarchyType hierarchyType, ExternalSystem ex)
    {
      this.got = got;
      this.hierarchyType = hierarchyType;
      this.dhis2 = dhis2;
      this.ex = ex;
      
      this.calculateDepth();
    }
    
    private String getExternalId(ServerGeoObjectIF serverGo)
    {
      String externalId = serverGo.getExternalId(this.ex);
      
      if (externalId == null || externalId.length() == 0)
      {
        try
        {
          externalId = this.dhis2.getDhis2Id();
        }
        catch (HTTPException | InvalidLoginException | UnexpectedResponseException e)
        {
          ExportRemoteException remoteEx = new ExportRemoteException();
          remoteEx.setRemoteError(e.getLocalizedMessage()); // TODO : Pull this error message from DHIS2
          throw remoteEx;
        }
        
        serverGo.createExternalId(this.ex, externalId);
      }
      
      return externalId;
    }
    
    @Override
    public JsonElement serialize(GeoObject go, Type typeOfSrc, JsonSerializationContext context)
    {
      VertexServerGeoObject serverGo = new VertexGeoObjectStrategy(this.got).getGeoObjectByCode(go.getCode());
      
      JsonObject jo = new JsonObject();
      
      jo.addProperty("code", go.getCode());
      
      jo.addProperty("level", this.depth);
      
      jo.addProperty("created", formatDate(go.getCreateDate()));
      
      jo.addProperty("lastUpdated", formatDate(go.getLastUpdateDate()));
      
      jo.addProperty("name", go.getDisplayLabel().getValue(LocalizedValue.DEFAULT_LOCALE));
      
      jo.addProperty("id", this.getExternalId(serverGo));
      
      jo.addProperty("shortName", go.getDisplayLabel().getValue(LocalizedValue.DEFAULT_LOCALE));
      
      jo.addProperty("path", calculatePath(serverGo));
      
      // TODO : openingDate ?
      
      ServerGeoObjectIF goParent = getParent(serverGo, this.hierarchyType.getCode());
      if (goParent != null)
      {
        JsonObject parent = new JsonObject();
        parent.addProperty("id", this.getExternalId(goParent)); // TODO : Is this the correct id?
        jo.add("parent", parent); // TODO Don't always submit parent information?
      }
      
      // TODO : attributeValues ?
      
      JsonArray translations = new JsonArray();
      LocalizedValue lv = go.getDisplayLabel();
      
      List<Locale> locales = LocalizationFacade.getInstalledLocales();
      for (Locale locale : locales)
      {
        if (lv.contains(locale))
        {
          JsonObject joLocaleShort = new JsonObject();
          joLocaleShort.addProperty("property", "SHORT_NAME");
          joLocaleShort.addProperty("locale", locale.toString());
          joLocaleShort.addProperty("value", lv.getValue(locale));
          translations.add(joLocaleShort);
          
          JsonObject joLocaleName = new JsonObject();
          joLocaleName.addProperty("property", "NAME");
          joLocaleName.addProperty("locale", locale.toString());
          joLocaleName.addProperty("value", lv.getValue(locale));
          translations.add(joLocaleName);
        }
      }
      
      return jo;
    }
    
    public static ServerGeoObjectIF getParent(ServerGeoObjectIF serverGo, String hierarchyCode)
    {
      ServerParentTreeNode sptn = serverGo.getParentGeoObjects(null, false);
      
      List<ServerParentTreeNode> parents = sptn.getParents();
      
      for (ServerParentTreeNode parent : parents)
      {
        if (hierarchyCode == null || parent.getHierarchyType().getCode().equals(hierarchyCode))
        {
          return parent.getGeoObject();
        }
      }
      
      return null;
    }
    
    private String formatDate(Date date)
    {
      SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
      
      return format.format(date);
    }
    
    public String calculatePath(VertexServerGeoObject serverGo)
    {
      List<String> ancestorExternalIds = new ArrayList<String>();
      
      List<VertexServerGeoObject> ancestors = serverGo.getAncestors(this.hierarchyType);
      
      Collections.reverse(ancestors);
      
      ancestors.forEach(ancestor -> {
        ancestorExternalIds.add(this.getExternalId(ancestor));
      });
      
      return "/" + StringUtils.join(ancestorExternalIds, "/");
    }
    
    public void calculateDepth()
    {
      if (got.getUniversal().getParents(hierarchyType.getUniversalType()).getAll().size() > 1)
      {
        throw new UnsupportedOperationException("Multiple GeoObjectType parents not supported.");
      }
      
      List<GeoObjectType> ancestors = AdapterUtilities.getInstance().getTypeAncestors(this.got, this.hierarchyType.getCode());
      
      this.depth = ancestors.size();
    }
  }
}
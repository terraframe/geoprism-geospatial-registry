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

import org.commongeoregistry.adapter.metadata.GeoObjectType;
import org.json.JSONArray;
import org.json.JSONObject;

import com.runwaysdk.session.Session;

import net.geoprism.localization.LocalizationFacade;
import net.geoprism.registry.geoobject.ServerGeoObjectService;
import net.geoprism.registry.model.ServerGeoObjectIF;
import net.geoprism.registry.model.ServerGeoObjectType;
import net.geoprism.registry.permission.GeoObjectPermissionService;
import net.geoprism.registry.permission.GeoObjectPermissionServiceIF;
import net.geoprism.registry.service.ServiceFactory;
import net.geoprism.registry.view.ServerParentTreeNodeOverTime;

public class SetParentAction extends SetParentActionBase
{
  private static final long            serialVersionUID           = 876924243;

  private GeoObjectPermissionServiceIF geoObjectPermissionService = new GeoObjectPermissionService();

  public SetParentAction()
  {
    super();
  }

  @Override
  public boolean isVisible()
  {
    if (Session.getCurrentSession() != null && Session.getCurrentSession().getUser() != null)
    {
      try
      {
        // Important to remember that the child may or may not exist at this point (so we can't fetch it from the DB here)
        
        ServerGeoObjectType type = ServiceFactory.getMetadataCache().getGeoObjectType(this.getChildTypeCode()).get();

        return geoObjectPermissionService.canWrite(type.getOrganization().getCode(), type);
      }
      catch (Exception e)
      {

      }
    }

    return false;
  }

  @Override
  public void execute()
  {
    ServerGeoObjectService service = new ServerGeoObjectService();
    ServerGeoObjectIF child = service.getGeoObjectByCode(this.getChildCode(), this.getChildTypeCode());

    ServerParentTreeNodeOverTime ptnOt = ServerParentTreeNodeOverTime.fromJSON(child.getType(), this.getJson());

    child.setParents(ptnOt);
  }

  @Override
  public void apply()
  {
    // Important to remember that the child may or may not exist at this point (so we can't fetch it from the DB here)
    
    ServerGeoObjectType type = ServiceFactory.getMetadataCache().getGeoObjectType(this.getChildTypeCode()).get();

    ServerParentTreeNodeOverTime ptnOt = ServerParentTreeNodeOverTime.fromJSON(type, this.getJson());

    ptnOt.enforceUserHasPermissionSetParents(this.getChildTypeCode(), true);

    super.apply();
  }

  @Override
  public JSONObject serialize()
  {
    JSONObject jo = super.serialize();
    jo.put(SetParentAction.JSON, new JSONArray(this.getJson()));
    jo.put(SetParentAction.CHILDTYPECODE, this.getChildTypeCode());
    jo.put(SetParentAction.CHILDCODE, this.getChildCode());

    return jo;
  }

  @Override
  public void buildFromJson(JSONObject joAction)
  {
    super.buildFromJson(joAction);

    this.setChildTypeCode(joAction.getString(SetParentAction.CHILDTYPECODE));
    this.setChildCode(joAction.getString(SetParentAction.CHILDCODE));
    this.setJson(joAction.getJSONArray(SetParentAction.JSON).toString());
  }

  @Override
  protected String getMessage()
  {
    ServerGeoObjectType childType = ServerGeoObjectType.get(this.getChildTypeCode());

    String message = LocalizationFacade.getFromBundles("change.request.email.set.parent");
    message = message.replaceAll("\\{0\\}", childType.getLabel().getValue(Session.getCurrentLocale()));
    message = message.replaceAll("\\{1\\}", this.getChildCode());

    return message;
  }

}

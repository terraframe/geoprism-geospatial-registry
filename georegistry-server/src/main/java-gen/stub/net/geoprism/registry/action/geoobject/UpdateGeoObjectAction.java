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

import org.commongeoregistry.adapter.action.AbstractActionDTO;
import org.commongeoregistry.adapter.action.geoobject.UpdateGeoObjectActionDTO;
import org.commongeoregistry.adapter.dataaccess.GeoObjectOverTime;
import org.commongeoregistry.adapter.metadata.GeoObjectType;
import org.json.JSONObject;

import com.runwaysdk.business.rbac.Operation;
import com.runwaysdk.session.Session;

import net.geoprism.localization.LocalizationFacade;
import net.geoprism.registry.geoobject.GeoObjectPermissionService;
import net.geoprism.registry.geoobject.GeoObjectPermissionServiceIF;
import net.geoprism.registry.geoobject.ServerGeoObjectService;
import net.geoprism.registry.model.ServerGeoObjectType;
import net.geoprism.registry.service.ServiceFactory;

public class UpdateGeoObjectAction extends UpdateGeoObjectActionBase
{
  private static final long serialVersionUID = 2090460439;
  
  private GeoObjectPermissionServiceIF geoObjectPermissionService = new GeoObjectPermissionService();

  public UpdateGeoObjectAction()
  {
    super();
  }

  @Override
  public void execute()
  {
    String sJson = this.getGeoObjectJson();

    GeoObjectOverTime goTime = GeoObjectOverTime.fromJSON(ServiceFactory.getAdapter(), sJson);

    ServerGeoObjectService builder = new ServerGeoObjectService(new GeoObjectPermissionService());
    builder.apply(goTime, false, false);
  }
  
  @Override
  public void apply()
  {
    String sJson = this.getGeoObjectJson();

    GeoObjectOverTime geoObject = GeoObjectOverTime.fromJSON(ServiceFactory.getAdapter(), sJson);
    
    ServerGeoObjectType type = ServerGeoObjectType.get(geoObject.getType());
    
    geoObjectPermissionService.enforceCanWriteCR(Session.getCurrentSession().getUser(), type);
    
    super.apply();
  }

  @Override
  protected void buildFromDTO(AbstractActionDTO dto)
  {
    super.buildFromDTO(dto);

    UpdateGeoObjectActionDTO castedDTO = (UpdateGeoObjectActionDTO) dto;

    this.setGeoObjectJson(castedDTO.getGeoObject().toString());
  }

  @Override
  public JSONObject serialize()
  {
    JSONObject object = super.serialize();
    object.put(UpdateGeoObjectAction.GEOOBJECTJSON, new JSONObject(this.getGeoObjectJson()));
    addGeoObjectType(object);
    return object;
  }

  private void addGeoObjectType(JSONObject object)
  {
    GeoObjectOverTime go = GeoObjectOverTime.fromJSON(ServiceFactory.getAdapter(), this.getGeoObjectJson());
    GeoObjectType got = go.getType();

    object.put("geoObjectType", new JSONObject(got.toJSON().toString()));
  }

  @Override
  public void buildFromJson(JSONObject joAction)
  {
    super.buildFromJson(joAction);

    this.setGeoObjectJson(joAction.getJSONObject(UpdateGeoObjectAction.GEOOBJECTJSON).toString());
  }

  @Override
  protected String getMessage()
  {
    GeoObjectOverTime go = GeoObjectOverTime.fromJSON(ServiceFactory.getAdapter(), this.getGeoObjectJson());
    GeoObjectType got = go.getType();

    String message = LocalizationFacade.getFromBundles("change.request.email.update.object");
    message = message.replaceAll("\\{0\\}", go.getCode());
    message = message.replaceAll("\\{1\\}", got.getLabel().getValue(Session.getCurrentLocale()));

    return message;
  }

}

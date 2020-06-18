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
package net.geoprism.registry.geoobject;

import com.runwaysdk.business.rbac.SingleActorDAOIF;

public interface GeoObjectRelationshipPermissionServiceIF
{
  public boolean canAddChild(SingleActorDAOIF actor, String orgCode, String parentGeoObjectTypeCode, String childGeoObjectTypeCode);

  public void enforceCanAddChild(SingleActorDAOIF actor, String orgCode, String parentGeoObjectTypeCode, String childGeoObjectTypeCode);
  
  public boolean canViewChild(SingleActorDAOIF actor, String orgCode, String parentGeoObjectTypeCode, String childGeoObjectTypeCode);
  
  public void enforceCanViewChild(SingleActorDAOIF actor, String orgCode, String parentGeoObjectTypeCode, String childGeoObjectTypeCode);

  public boolean canAddChildCR(SingleActorDAOIF actor, String orgCode, String parentGeoObjectTypeCode, String childGeoObjectTypeCode);

  public void enforceCanAddChildCR(SingleActorDAOIF actor, String orgCode, String parentGeoObjectTypeCode, String childGeoObjectTypeCode);
  
  public boolean canRemoveChild(SingleActorDAOIF actor, String orgCode, String parentGeoObjectTypeCode, String childGeoObjectTypeCode);

  public void enforceCanRemoveChild(SingleActorDAOIF actor, String orgCode, String parentGeoObjectTypeCode, String childGeoObjectTypeCode);

  public boolean canRemoveChildCR(SingleActorDAOIF actor, String orgCode, String parentGeoObjectTypeCode, String childGeoObjectTypeCode);

  public void enforceCanRemoveChildCR(SingleActorDAOIF actor, String orgCode, String parentGeoObjectTypeCode, String childGeoObjectTypeCode);
}

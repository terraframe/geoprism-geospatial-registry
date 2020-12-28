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
package net.geoprism.registry.view;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeMap;

import org.commongeoregistry.adapter.dataaccess.GeoObject;
import org.commongeoregistry.adapter.metadata.GeoObjectType;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.runwaysdk.dataaccess.ProgrammingErrorException;

import net.geoprism.registry.geoobject.ServerGeoObjectService;
import net.geoprism.registry.model.ServerGeoObjectIF;
import net.geoprism.registry.model.ServerGeoObjectType;
import net.geoprism.registry.model.ServerHierarchyType;
import net.geoprism.registry.model.ServerParentTreeNode;
import net.geoprism.registry.permission.AllowAllGeoObjectPermissionService;
import net.geoprism.registry.service.ServiceFactory;

public class ServerParentTreeNodeOverTime
{
  public static final String JSON_HIERARCHY_CODE = "code";
  
  public static final String JSON_HIERARCHY_LABEL = "label";
  
  public static final String JSON_HIERARCHY_TYPES = "types";
  
  public static final String JSON_HIERARCHY_ENTRIES = "entries";
  
  public static final String JSON_TYPE_CODE = "code";
  
  public static final String JSON_TYPE_LABEL = "label";
  
  public static final String JSON_ENTRY_STARTDATE = "startDate";
  
  public static final String JSON_ENTRY_ENDDATE = "endDate";
  
  public static final String JSON_ENTRY_PARENTS = "parents";
  
  public static final String JSON_ENTRY_PARENT_GEOOBJECT = "geoObject";
  
  public static final String JSON_ENTRY_PARENT_TEXT = "text";
  
  private static class Hierarchy
  {
    private ServerHierarchyType        type;

    private List<ServerParentTreeNode> nodes;

    public Hierarchy(ServerHierarchyType type)
    {
      this.type = type;
      this.nodes = new LinkedList<ServerParentTreeNode>();
    }

    public ServerHierarchyType getType()
    {
      return type;
    }

    public void add(ServerParentTreeNode node)
    {
      this.nodes.add(node);
    }

    public List<ServerParentTreeNode> getNodes()
    {
      return nodes;
    }
  }

  private Map<String, Hierarchy> hierarchies;

  private ServerGeoObjectType    type;

  public ServerParentTreeNodeOverTime(ServerGeoObjectType type)
  {
    this.type = type;
    this.hierarchies = new TreeMap<String, ServerParentTreeNodeOverTime.Hierarchy>();
  }

  public void add(ServerHierarchyType type)
  {
    if (!this.hierarchies.containsKey(type.getCode()))
    {
      this.hierarchies.put(type.getCode(), new Hierarchy(type));
    }
  }

  public void add(ServerHierarchyType type, ServerParentTreeNode node)
  {
    if (!this.hierarchies.containsKey(type.getCode()))
    {
      this.hierarchies.put(type.getCode(), new Hierarchy(type));
    }

    this.hierarchies.get(type.getCode()).add(node);
  }

  public Hierarchy remove(ServerHierarchyType type)
  {
    return this.hierarchies.remove(type.getCode());
  }

  public Collection<ServerHierarchyType> getHierarchies()
  {
    final LinkedList<ServerHierarchyType> list = new LinkedList<ServerHierarchyType>();

    this.hierarchies.forEach((key, value) -> {
      list.add(value.getType());
    });

    return list;
  }

  public boolean hasEntries(ServerHierarchyType type)
  {
    return this.hierarchies.containsKey(type.getCode());
  }

  public List<ServerParentTreeNode> getEntries(ServerHierarchyType type)
  {
    return this.hierarchies.get(type.getCode()).getNodes();
  }

  public void enforceUserHasPermissionSetParents(String childCode, boolean asCR)
  {
    final Collection<ServerHierarchyType> hierarchyTypes = this.getHierarchies();

    ServerGeoObjectType childType = ServerGeoObjectType.get(childCode);

    for (ServerHierarchyType hierarchyType : hierarchyTypes)
    {
      final List<ServerParentTreeNode> entries = this.getEntries(hierarchyType);

      for (ServerParentTreeNode entry : entries)
      {
        final ServerGeoObjectIF parent = entry.getGeoObject();

        if (asCR)
        {
          ServiceFactory.getGeoObjectRelationshipPermissionService().enforceCanAddChildCR(hierarchyType.getOrganization().getCode(), parent.getType(), childType);
        }
        else
        {
          ServiceFactory.getGeoObjectRelationshipPermissionService().enforceCanAddChild(hierarchyType.getOrganization().getCode(), parent.getType(), childType);
        }
      }
    }
  }

  public boolean isSame(ServerParentTreeNodeOverTime other, ServerGeoObjectIF exclude)
  {
    Set<Entry<String, Hierarchy>> entries = this.hierarchies.entrySet();

    for (Entry<String, Hierarchy> entry : entries)
    {
      Hierarchy hierarchy = entry.getValue();
      ServerHierarchyType ht = hierarchy.getType();

      if (other.hasEntries(ht) && hierarchy.nodes.size() == other.getEntries(ht).size())
      {
        for (int i = 0; i < hierarchy.nodes.size(); i++)
        {
          ServerParentTreeNode node = hierarchy.nodes.get(i);
          ServerParentTreeNode oNode = other.getEntries(ht).get(i);

          if (!node.isSame(oNode, exclude))
          {
            return false;
          }
        }
      }
      else
      {
        return false;
      }
    }

    return true;
  }

  public JsonArray toJSON()
  {
    SimpleDateFormat format = getDateFormat();

    final JsonArray response = new JsonArray();

    final Set<Entry<String, Hierarchy>> entrySet = this.hierarchies.entrySet();

    for (Entry<String, Hierarchy> entry : entrySet)
    {
      final Hierarchy hierarchy = entry.getValue();
      final ServerHierarchyType ht = hierarchy.getType();

      List<GeoObjectType> parentTypes = this.type.getTypeAncestors(ht, false);

      // Populate a "types" array with all ancestors of the GOT they passed us
      JsonArray types = new JsonArray();
      for (GeoObjectType parent : parentTypes)
      {
        ServerGeoObjectType pType = ServerGeoObjectType.get(parent);

        if (!pType.getCode().equals(this.type.getCode()))
        {
          JsonObject pObject = new JsonObject();
          pObject.addProperty(JSON_TYPE_CODE, pType.getCode());
          pObject.addProperty(JSON_TYPE_LABEL, pType.getLabel().getValue());

          types.add(pObject);
        }
      }

      // Populate an "entries" array with all the parents per time period
      final JsonArray entries = new JsonArray();
      final List<ServerParentTreeNode> nodes = hierarchy.getNodes();
      for (ServerParentTreeNode node : nodes)
      {
        JsonObject pArray = new JsonObject();

        for (GeoObjectType parent : parentTypes)
        {
          ServerGeoObjectType pType = ServerGeoObjectType.get(parent);

          if (!pType.getCode().equals(this.type.getCode()))
          {
            final List<ServerParentTreeNode> ptns = node.findParentOfType(pType.getCode());

            if (ptns.size() > 0)
            {
              final ServerGeoObjectIF sGeoObject = ptns.get(0).getGeoObject();
              final GeoObject geoObject = sGeoObject.toGeoObject();
              geoObject.setGeometry(null);

              JsonObject pObject = new JsonObject();
              pObject.add(JSON_ENTRY_PARENT_GEOOBJECT, geoObject.toJSON());
              pObject.addProperty(JSON_ENTRY_PARENT_TEXT, sGeoObject.getDisplayLabel().getValue() + " : " + sGeoObject.getCode());

              pArray.add(pType.getCode(), pObject);
            }
          }
        }

        JsonObject object = new JsonObject();
        object.addProperty(JSON_ENTRY_STARTDATE, format.format(node.getDate()));

        if (node.getEndDate() != null)
        {
          object.addProperty(JSON_ENTRY_ENDDATE, format.format(node.getEndDate()));
        }

        object.add(JSON_ENTRY_PARENTS, pArray);

        entries.add(object);
      }

      JsonObject object = new JsonObject();
      object.addProperty(JSON_HIERARCHY_CODE, ht.getCode());
      object.addProperty(JSON_HIERARCHY_LABEL, ht.getDisplayLabel().getValue());
      object.add(JSON_HIERARCHY_TYPES, types);
      object.add(JSON_HIERARCHY_ENTRIES, entries);

      response.add(object);
    }

    return response;
  }
  
  public static SimpleDateFormat getDateFormat()
  {
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    format.setTimeZone(TimeZone.getTimeZone("GMT"));
    
    return format;
  }

  public static ServerParentTreeNodeOverTime fromJSON(ServerGeoObjectType type, String sPtn)
  {
    SimpleDateFormat format = getDateFormat();

    final ServerParentTreeNodeOverTime node = new ServerParentTreeNodeOverTime(type);

    final JsonArray array = JsonParser.parseString(sPtn).getAsJsonArray();

    for (int i = 0; i < array.size(); i++)
    {
      final JsonObject hJSON = array.get(i).getAsJsonObject();
      final String hierarchyCode = hJSON.get(JSON_HIERARCHY_CODE).getAsString();
      final JsonArray types = hJSON.get(JSON_HIERARCHY_TYPES).getAsJsonArray();
      final ServerHierarchyType ht = ServerHierarchyType.get(hierarchyCode);

      final JsonArray entries = hJSON.get(JSON_HIERARCHY_ENTRIES).getAsJsonArray();

      for (int j = 0; j < entries.size(); j++)
      {
        final JsonObject entry = entries.get(j).getAsJsonObject();
        final String startDate = entry.get(JSON_ENTRY_STARTDATE).getAsString();
        final JsonObject parents = entry.get(JSON_ENTRY_PARENTS).getAsJsonObject();

        try
        {
          Date date = format.parse(startDate);

          final ServerParentTreeNode parent = parseParent(ht, types, parents, date);

          if (parent != null)
          {
            node.add(ht, parent);
          }
        }
        catch (ParseException e)
        {
          throw new ProgrammingErrorException(e);
        }
      }
    }

    return node;
  }

  private static ServerParentTreeNode parseParent(final ServerHierarchyType ht, final JsonArray types, final JsonObject parents, final Date date)
  {
    for (int k = ( types.size() - 1 ); k >= 0; k--)
    {
      final JsonObject type = types.get(k).getAsJsonObject();
      final String code = type.get(JSON_TYPE_CODE).getAsString();
      final JsonObject parent = parents.get(code).getAsJsonObject();

      if (parent.has(JSON_ENTRY_PARENT_GEOOBJECT))
      {
        final JsonObject go = parent.get(JSON_ENTRY_PARENT_GEOOBJECT).getAsJsonObject();

        GeoObject geoObject = GeoObject.fromJSON(ServiceFactory.getAdapter(), go.toString());
        final ServerGeoObjectIF pSGO = new ServerGeoObjectService(new AllowAllGeoObjectPermissionService()).getGeoObjectByCode(geoObject.getCode(), code);

        return new ServerParentTreeNode(pSGO, ht, date);
      }
    }

    return null;
  }
}

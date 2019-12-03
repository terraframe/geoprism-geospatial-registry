package net.geoprism.registry.graph;

public abstract class GeoVertex extends GeoVertexBase
{
  private static final long  serialVersionUID       = 765825100;

  public static final String START_DATE             = "startDate";

  public static final String END_DATE               = "endDate";

  public static final String HAS_SYNONYM = "net.geoprism.registry.graph.GeoVertexHasSynonym";

  public GeoVertex()
  {
    super();
  }

}

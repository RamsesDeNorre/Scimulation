package core.graph.visualize

import scala.collection.mutable.Map
import core.graph.Edge
import core.graph.Graph
import core.graph.Vertex
import org.ubiety.ubigraph.UbigraphClient
import monitor.Subscriber

class UbiGraphVisualizer[V <: Vertex[V] with Color, E <: Edge[V]]
  extends Subscriber[V, E] {

  override def update(graph: Graph[V, E]) {
    visualize(graph);
  }

  def visualize(graph: Graph[V, E]) {
    val ubiClient = init()
    val verticesmap = Map.empty[String, Int]

    graph foreach addVertex(ubiClient, verticesmap)
    graph.edges foreach addEdge(ubiClient, verticesmap)
  }

  private[this] def init(): UbigraphClient = {
    val ubiClient = new UbigraphClient()
    ubiClient.clear()
    ubiClient.setVertexStyleAttribute(0, "shape", "sphere")
    ubiClient.setVertexStyleAttribute(0, "size", "0.5")
    ubiClient.setEdgeStyleAttribute(0, "arrow", "true")
    ubiClient
  }

  private[this] def addVertex
  (ubiGraph: UbigraphClient, map: Map[String, Int])(v: V) {
    map.put(v.id, ubiGraph.newVertex())
    ubiGraph.setVertexAttribute(map(v.id), "label", v.id)
    ubiGraph.setVertexAttribute(map(v.id), "color", v.color)
  }

  private[this] def addEdge
  (ubiGraph: UbigraphClient, map: Map[String, Int])(e: E) {
    ubiGraph.newEdge(map(e.from.id), map(e.to.id))
  }
}

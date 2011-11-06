package core.graph.persistence

import xml.XML
import core.graph.{BaseVertex, Edge, Vertex, Graph}

/**
 * Created by Ramses de Norre
 * Date: 04/11/11
 * Time: 17:26
 */
object GraphRepository {

  def persistGraph[V <: Vertex, E <: Edge[V]]
      (graph: Graph[V, E], file: String) {
    XML.save(file, graph.toXML, "UTF-8", true, null)
  }

  def getGraph[V <: Vertex, E <: Edge[V]](file: String): Graph[V, E] = {
    val node = XML.loadFile(file)
    val vertices = (node \ "vertices" \ "vertex") map Vertex.fromXML[V]
    val vertexMap: Map[String, V] = getVertexMap(vertices)
    val edges = (node \ "edges" \ "edge") map Edge.fromXML[V, E](vertexMap)
    Graph.fromXML[V, E]((node \ "class").text, vertices, edges)
  }

  private[this] def getVertexMap[V <: Vertex](vertices: Seq[V]) = {
    var map = Map.empty[String, V]
    vertices foreach {v => map += (v.id -> v)}
    map
  }
}

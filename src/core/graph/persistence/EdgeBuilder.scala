package core.graph.persistence

import core.graph.{Vertex, Edge}

/**
 * Created by Ramses de Norre
 * Date: 03/11/11
 * Time: 22:49
 */
class EdgeBuilder extends GenericBuilder {
  def create[V <: Vertex, E <: Edge[V]]
      (edgeClass: String, from: V, to: V, weight: Double): E = {
    New[E](edgeClass)(from, to, weight)
  }
}

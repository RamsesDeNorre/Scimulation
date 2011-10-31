package core.graph

import scala.collection.Seq
import scala.collection.Iterator

/**
 * Directed edge which connects two vertices and has some weight.
 * Created by Ramses de Norre
 * Date: 24/10/11
 * Time: 16:41
 */
abstract case class Edge[V <: Vertex](from: V, to: V)
  extends Seq[V] with Ordered[Edge[V]] {

  override val length = 2
  val weight: Double

  /**
   * Return the other node, or None if the given node is not contained in
   * this edge
   */
  def other(vertex: V) = vertex match {
    case `from` => Some(to)
    case `to`   => Some(from)
    case _      => None
  }

  override def apply(idx: Int) = idx match {
    case 0 => from
    case 1 => to
    case _ => throw new IndexOutOfBoundsException
  }


  override def reverse = construct(to, from)

  protected def construct(from: V, to: V): Edge[V]

  override def iterator = Iterator(from, to)

  def compare(that: Edge[V]) = this.weight compare that.weight
}

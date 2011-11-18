package core.statistics


import core.graph.Graph
import core.graph.Edge
import core.graph.Vertex
import scala.collection.mutable.HashMap
import monitor.Subscriber

/*
 * De bedoeling is dus dat iedere simulatie hier een implementatie van maakt met zijn eigen update 
 * Mss moeten die definities die hieronder staan helemaal niet hier staan? Of zijn die mss ook gewoon abstract?
 */

abstract class StatisticsManager[V <: Vertex[V], E <: Edge[V]]extends Subscriber[V, E] {
  
	//Is er maar 1 van, is dat een object?
	val stat = new Statistics[V,E]
	//1 Per simulatie, is dat een abstracte klasse of een trait?
    val store = new StatisticsStore[V,E]
    //Kan worden ingevuld per simulatie met onderstaande methodes
	
  override
  def update(graph: Graph[V, E])
    
	
	//Zodat de echte graaf niet wordt meegegeven naar Statisticsstore, nieuwe thread aanmaken?
  def storeInfoOnNetwork(graph:Graph[V, E],t: Int){
    val graph1 = graph.deepCopy
    store.nbofedges (t) -> stat.nbOfEdges(graph1)
    store.nbofvertices (t) -> stat.nbOfEdges(graph1)
    store.avnbofedgespervertex (t) -> stat.avNbOfEdgesPerVertex(graph1)
    store.attrbmap (t) -> stat.createAttrMap(graph1)
  }
  	
  
    //Voor deze twee onderstaande functies wordt geen gebruik gemaakt 
  	//van de graaf maar van de reeds opgeslagen gegevens
  def storeDensityOfNetwork(t:Int){
    store.densityOfNetwork(t)
  }
  
  
  // Bij implementatie voor 1 simulatie invullen attr en value, naar gelang dewelke opgeslagen moeten worden
  def storeNbOfVerticesWithSameAttribute(t: Int, attr:String, value: Char){
    store.nbOfVerticesWithSameAttribute(t: Int, attr:String, value: Char)
  }
  
 
  
}
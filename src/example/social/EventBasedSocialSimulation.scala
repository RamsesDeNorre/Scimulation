package example.social
import core.graph.UndirectedGraph
import core.persistence.GraphRepository
import engine.EventBasedEngine
import engine.Event
import core.graph.Graph
import core.visualize.UbiGraphVisualizer
import engine.SnapshotCreator



object EventBasedSocialSimulation extends App {

  testEngine()

  def testEngine() {
    val graph = new UndirectedGraph[SocialActor, SocialEdge[SocialActor]]

    GraphRepository.loadGraph(graph,"social.xml")

    val engine = new EventBasedEngine(graph, 28)

    engine.addEvent(new Peace(1,graph,graph.getVertex("2").get,graph.getVertex("0").get))
    engine.addEvent(new Peace(1,graph,graph.getVertex("4").get,graph.getVertex("0").get))
    engine.addEvent(new Peace(2,graph,graph.getVertex("0").get,graph.getVertex("5").get))
    engine.addEvent(new Peace(2,graph,graph.getVertex("6").get,graph.getVertex("1").get))
    engine.addEvent(new Peace(3,graph,graph.getVertex("4").get,graph.getVertex("14").get))
    engine.addEvent(new Peace(3,graph,graph.getVertex("9").get,graph.getVertex("13").get))
    engine.addEvent(new Peace(3,graph,graph.getVertex("12").get,graph.getVertex("11").get))
    engine.addEvent(new Peace(3,graph,graph.getVertex("4").get,graph.getVertex("11").get))
    engine.addEvent(new Peace(4,graph,graph.getVertex("7").get,graph.getVertex("9").get))
    engine.addEvent(new Peace(4,graph,graph.getVertex("9").get,graph.getVertex("0").get))
    engine.addEvent(new Peace(4,graph,graph.getVertex("10").get,graph.getVertex("0").get))
    engine.addEvent(new Peace(5,graph,graph.getVertex("3").get,graph.getVertex("5").get))
    engine.addEvent(new Peace(5,graph,graph.getVertex("7").get,graph.getVertex("5").get))
    engine.addEvent(new Peace(6,graph,graph.getVertex("4").get,graph.getVertex("12").get))
    engine.addEvent(new Peace(6,graph,graph.getVertex("11").get,graph.getVertex("5").get))    
    engine.addEvent(new Peace(7,graph,graph.getVertex("14").get,graph.getVertex("7").get))
    engine.addEvent(new Peace(7,graph,graph.getVertex("2").get,graph.getVertex("8").get))
    engine.addEvent(new Peace(8,graph,graph.getVertex("8").get,graph.getVertex("9").get))
    engine.addEvent(new Peace(8,graph,graph.getVertex("1").get,graph.getVertex("10").get))
    engine.addEvent(new Peace(9,graph,graph.getVertex("1").get,graph.getVertex("11").get))
    engine.addEvent(new Peace(9,graph,graph.getVertex("1").get,graph.getVertex("0").get))    
    engine.addEvent(new Peace(10,graph,graph.getVertex("4").get,graph.getVertex("3").get))
    engine.addEvent(new Peace(11,graph,graph.getVertex("3").get,graph.getVertex("2").get))
    engine.addEvent(new Peace(11,graph,graph.getVertex("9").get,graph.getVertex("6").get))    
    
    engine.addEvent(new War(12,graph,graph.getVertex("9").get,graph.getVertex("6").get))   
    engine.addEvent(new War(12,graph,graph.getVertex("3").get,graph.getVertex("2").get))
    engine.addEvent(new War(12,graph,graph.getVertex("4").get,graph.getVertex("3").get))
    engine.addEvent(new War(13,graph,graph.getVertex("1").get,graph.getVertex("0").get))    
    engine.addEvent(new War(13,graph,graph.getVertex("1").get,graph.getVertex("11").get))
    engine.addEvent(new War(14,graph,graph.getVertex("1").get,graph.getVertex("10").get))
    engine.addEvent(new War(15,graph,graph.getVertex("8").get,graph.getVertex("9").get))
    engine.addEvent(new War(15,graph,graph.getVertex("2").get,graph.getVertex("8").get))
    engine.addEvent(new War(16,graph,graph.getVertex("14").get,graph.getVertex("7").get))
    engine.addEvent(new War(17,graph,graph.getVertex("11").get,graph.getVertex("5").get))    
    engine.addEvent(new War(17,graph,graph.getVertex("4").get,graph.getVertex("12").get))
    engine.addEvent(new War(17,graph,graph.getVertex("7").get,graph.getVertex("5").get))
    engine.addEvent(new War(18,graph,graph.getVertex("3").get,graph.getVertex("5").get))
    engine.addEvent(new War(19,graph,graph.getVertex("10").get,graph.getVertex("0").get))
    engine.addEvent(new War(20,graph,graph.getVertex("9").get,graph.getVertex("0").get))
    engine.addEvent(new War(22,graph,graph.getVertex("7").get,graph.getVertex("9").get))
    engine.addEvent(new War(23,graph,graph.getVertex("4").get,graph.getVertex("11").get))
    engine.addEvent(new War(23,graph,graph.getVertex("12").get,graph.getVertex("11").get))
    engine.addEvent(new War(24,graph,graph.getVertex("9").get,graph.getVertex("13").get))
    engine.addEvent(new War(25,graph,graph.getVertex("4").get,graph.getVertex("14").get))
    engine.addEvent(new War(25,graph,graph.getVertex("6").get,graph.getVertex("1").get))
    engine.addEvent(new War(26,graph,graph.getVertex("0").get,graph.getVertex("5").get))
    engine.addEvent(new War(27,graph,graph.getVertex("4").get,graph.getVertex("0").get))
    engine.addEvent(new War(27,graph,graph.getVertex("2").get,graph.getVertex("0").get))

    val vis = new UbiGraphVisualizer[SocialActor, SocialEdge[SocialActor]]
    vis.subscribeTo(engine)
    
    val snapshots = new SnapshotCreator[SocialActor, SocialEdge[SocialActor]](7,"EvSocout")
    snapshots.subscribeTo(engine)
    
    val stats = new SocialStatistics[SocialActor, SocialEdge[SocialActor]]
    stats.subscribeTo(engine)

    engine.run()
        GraphRepository.persistGraph(graph,"EventSocialOut.xml")
  }

}

case class Peace(t:Int,graph:Graph[SocialActor, SocialEdge[SocialActor]],s1:SocialActor,s2:SocialActor) extends Event[SocialActor](t){
	  override def execute(){
	    println("Peace between "+s1.getName+" and "+s2.getName)
	    graph.addEdge(new SocialEdge(s1,s2))
	  }
}

case class War(t:Int,graph:Graph[SocialActor, SocialEdge[SocialActor]],s1:SocialActor,s2:SocialActor) extends Event[SocialActor](t){
	  override def execute(){
	    println("War between "+s1.getName+" and "+s2.getName)
	    graph.edges.foreach{ e=>
	      if(e.from == s1 && e.to ==s2 || e.to == s2 && e.from == s1){
	        graph.removeEdge(e)
	      }
	    }
	    
	  }
}

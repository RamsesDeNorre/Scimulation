package example.virus

import scala.collection.mutable.Map
import example.virus.Status._
import core.graph.Vertex
import engine.Action
import engine.Multiverse
import core.visualize.Color

// zouden graag with action[] eruit trekken zodat de TurnbasedVirusSimulation
// gebruiker een nieuw kind maakt die extend virusactor with Action
// idem voor roundVirusactor, die dan grotendeels zou wegsmelten
// hadden errors met overerving
class RoundVirusActor(inId: String, inMap: Map[String, Any])
  extends Vertex[RoundVirusActor] with Multiverse[RoundVirusActor] with
    Action[RoundVirusActor] with Ordered[RoundVirusActor]with Color{

  override def calcPrior() = 0

  def compare(that: RoundVirusActor) = this.calcPrior compare that.calcPrior

  override lazy val id = inId
  override lazy val params = inMap

  override def color: String = {
    if (getStatus == Status.I.toString()) {
      "#ff0000"
    } else if (getStatus == Status.S.toString()) {
      "#ff00ff"
    } else if (getName == "Dave") {
      "#0000ff"
    } else {
      "#" + (255 - getProgSkill).toHexString + "ff" + (255 - getProgSkill).toHexString
    }
  }

  object AllDone extends Exception { }

  override def execute() {
    if (getName == "Dave")
      setProgSkill(255)
    else if (getStatus == Status.S.toString()) {
	  neighbours.foreach{
	    e =>
	    if(e.getStatus==Status.I.toString())
	      infectious()
	  }
    }
    else if (getStatus == Status.NI.toString()) {
	  neighbours.foreach{
	    e => {
	      if(e.getStatus==Status.I.toString()){
	        sick()
	      }
	    }
	  }
    }
    else if (getStatus == Status.I.toString() && getGender == Gender.Female.toString()) {
      heal()
    }
    if (getStatus == Status.NI.toString()) {
      neighbours.foreach{
	    e =>
	    if(e.getProgSkill > 200)
	      incProgSkill()
	  }
    }
  }

  def getStatus = params.get("status") getOrElse "unknown"

  def getGender = params.get("gender") getOrElse "unknown"

  def getProbability = params.get("probability") getOrElse "unknown"

  def getName = params.get("name") getOrElse "unknown"

  def getProgSkill: Int = {
    val skill = params.get("progSkill") getOrElse "0"
    Integer.parseInt(skill.toString)
  }

  def setStatus(newStatus: Status) {
    params += ("status" -> newStatus.toString)
  }

  def setName(name: String) {
    params += ("name" -> name)
  }

  def setProgSkill(level: Int) {
	params += ("progSkill" -> level)
  }

  def sick() {
    setStatus(Status.S)
    println("Actor: " + id + "  with status " + getStatus + " and gender " +
            getGender + " just got sick");
  }

  def heal() {
    setStatus(Status.NI)
    println("Actor: " + id + "  with status " + getStatus + " and gender " +
            getGender + " just healed");
  }

  def infectious() {
    setStatus(Status.I)
    println("Actor: " + id + "  with status " + getStatus + " and gender " +
            getGender + " just became infectious");
  }

  def incProgSkill() {
    val newSkill = getProgSkill + 10
    if(newSkill < 256)
      setProgSkill(newSkill)
    else
      setProgSkill(255)
  }
}

object RoundVirusActor {
  def apply(id: String, params: Map[String, Any]) =
    new RoundVirusActor(id, params)
}


import MomKidExample.Mom.{Ask, _}
import MomKidExample.FussyKid.{HAPPY, KidAccept, KidReject, SAD}
import akka.actor.{Actor, ActorRef, ActorSystem, Props}

object MomKidExample {

  object FussyKid {
    case object KidAccept
    case object KidReject
    val HAPPY = "happy"
    val SAD = "sad"
  }

  object Mom {
    case class MomStart(kidRef: ActorRef)
    case class Food(food: String)
    case class Ask(message: String)
    val VEGETABLE = "vegetable"
    val CHOCOLATE = "chocolate"
  }

  class FussyKid extends Actor {

    var state = HAPPY

    override def receive: Receive = {
      case Food(VEGETABLE) => state = SAD
      case Food(CHOCOLATE) => state = HAPPY
      case Ask(_) =>
        if(state == HAPPY) sender()  ! KidAccept
        else sender() ! KidReject

      case x => println(s"My state is $state")
    }
  }

  class StatelessFussyKid extends Actor {

    override def receive: Receive= happyReceive

    def happyReceive: Receive = {
      case Food(VEGETABLE) => {
        context.become(sadReceive, discardOld = false)
        println("happyReceive vegetable")
      }
      case Food(CHOCOLATE) => println("happyReceive chocolate")
      case Ask(_) => sender() ! KidAccept
    }

    def sadReceive: Receive = {
      case Food(VEGETABLE) => {
        context.become(sadReceive, discardOld = false)
        println("sadReceive vegetable")
      }
      case Food(CHOCOLATE) => {
        context.unbecome()
        println("sadReceive chocolate")
      }
      case Ask(_) => sender() ! KidReject
    }

  }

  class Mom extends Actor {

    override def receive: Receive = {
      case MomStart(ref) =>
        ref ! Food(VEGETABLE)
        ref ! Food(CHOCOLATE)
        ref ! Ask("do you want to play ?")
      case KidAccept => println("My kid accepted")
      case KidReject => println("My kid rejected")
    }
  }

  def main(args: Array[String]): Unit = {

    println("\n----Mom Kid Actor------")

    val momKidActorSystem = ActorSystem("momKidActorSystem")
    val momActor = momKidActorSystem.actorOf(Props[Mom])
    val kidActor = momKidActorSystem.actorOf(Props[FussyKid])

    momActor ! MomStart(kidActor)
    kidActor ! "state"
    Thread.sleep(100)
    kidActor ! "state"

    Thread.sleep(1000)
    println("\n-----STATELESS------")

    val kidActorStateLess = momKidActorSystem.actorOf(Props[StatelessFussyKid])
    momActor ! MomStart(kidActorStateLess)


  }

}

import ChildrenActors.Parent.{GetMessage, Kill, StartChild, StopChild}
import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, Props}

import scala.concurrent.duration.DurationInt
import scala.language.postfixOps

object ChildrenActors {

  object Parent {
    case class StartChild(name: String)
    case class StopChild(name: String)
    case class GetMessage(name: String)
    case object Kill
  }

  class Parent extends Actor with ActorLogging {

    override def receive: Receive = withChildren(Map())

    def withChildren(children: Map[String, ActorRef]) : Receive = {
      case StartChild(name) =>
        log.info(s"Starting child $name")
        context.become(withChildren(children+ (name-> context.actorOf(Props[Child], name))))
      case StopChild(name) =>
        log.info(s"Stopping child with the name $name")
        val childOption = children.get(name)
        childOption.foreach(childRef => context.stop(childRef))
      case GetMessage(name) =>
        val childOption = children.get(name)
        childOption.get ! "test"
      case Kill => context.stop(self)
    }
  }

  class Child extends Actor with ActorLogging{
    override def receive: Receive= {
      case message => log.info(message.toString)
    }
  }

  class LifecycleActor extends Actor with ActorLogging {

    override def preStart(): Unit = log.info("I am starting")
    override def postStop(): Unit = log.info("I have stopped")

    override def receive: Receive = {
      case StartChild => context.actorOf(Props[LifecycleActor], "child")
      case Kill => context.stop(self)
    }
  }

  def main(args: Array[String]): Unit = {
    println("\n----Children actors------")
    val actorSystem = ActorSystem("actorSystem")
    val actor = actorSystem.actorOf(Props[Parent])

    actor ! StartChild("child")
    actor ! GetMessage("child")
    actor ! StopChild("child")
    actor ! Kill

    Thread.sleep(1000)
    println("\n----Starting and stopping actors and children------")
    val system = ActorSystem("LifecycleActor")
    val parent = system.actorOf(Props[LifecycleActor], "parent")

    parent ! StartChild
    parent ! Kill

    Thread.sleep(1000)
    println("\n----Starting scheduled actors------")
    val schedulerSystem = ActorSystem("schedulerSystem")
    val schedulerActor = system.actorOf(Props[Child], "schedulerActor")

    schedulerSystem.scheduler.scheduleOnce(5 second){
      schedulerActor ! "test"
    }(system.dispatcher)

  }
}

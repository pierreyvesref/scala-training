import Akka.Test
import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.event.Logging
import com.typesafe.config.ConfigFactory

object Akka {

  case class SayHiTo(ref: ActorRef)

  class SimpleActor extends Actor {
    val logger = Logging(context.system, this)
    override def receive: Receive = {
      case "Hi!" => sender() ! "Hello, there!" //2
      case message: String => logger.info(s"I have received : $message") //3
      case SayHiTo(ref) => ref! "Hi!" // 1
      case (a, b) => logger.info("Two things: {} and {}", a, b)

    }
  }


    object Test {
      def props = Props(new SimpleActor)
    }

    def main(args: Array[String]): Unit = {
      println("\n----SIMPLE ACTOR------")
      val actorSystem= ActorSystem("firstActorSystem")
      val simpleActor = actorSystem.actorOf(Props[SimpleActor], "simpleActor")
      val anotherSimpleActor= actorSystem.actorOf(Props[SimpleActor], "anotherSimpleActor")

      //1ere methode
      //simpleActor ! "A first message"
      //anotherSimpleActor ! "Another message ezrzet azer"
      simpleActor ! SayHiTo(anotherSimpleActor)

      simpleActor ! (1, 2)

      //2eme methode
      //val test = actorSystem.actorOf(Test.props)
      //test ! "test zefds f"

      Thread.sleep(1000)
      println("\n----WITH CONFIG------")

      val actorConfig= ConfigFactory.load().getConfig("mySpecialConfig")
      val actorSystemWithConfig = ActorSystem("actorSystemWithConfig", actorConfig)
      val actorWithConfig = actorSystemWithConfig.actorOf(Props[SimpleActor], "actorWithConfig")
      actorWithConfig ! "Hello"

    }

}

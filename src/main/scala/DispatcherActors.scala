import akka.actor.{Actor, ActorLogging, ActorSystem, Props}

object DispatcherActors {

  class Counter extends Actor with ActorLogging {
    var count = 0

    override def receive: Receive = {
      case message => count += 1
        log.info(s"[$count] $message i am $self")
    }

  }

  def main(args: Array[String]): Unit = {
    val system = ActorSystem("DispatchersDemo")

    val actors = for(i <- 1 to 10) yield system.actorOf(Props[Counter].withDispatcher("my-dispatcher"), s"counter_$i")

    /*val masterclassActor = system.actorOf(Props[Counter], "masterclass")
    for(i <- 1 to 10) yield masterclassActor ! "test"*/
  }

}

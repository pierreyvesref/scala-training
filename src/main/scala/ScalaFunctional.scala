import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import scala.language.postfixOps
import scala.util._
import scala.util.matching.Regex

object ScalaFunctional {

  def main(args: Array[String]): Unit = {

    //fonctions anonymes
    println("\n----------FONCTIONS ANONYMES------------")
    val numbers: List[Int] = List(4, 3, 5, 6, 19)
    println(numbers.reduce((a: Int, b: Int) => a + b + 1))
    println(numbers.reduce(_ + _ + 1))

    //Collections
    println("\n----------COLLECTIONS------------")
    println("\n------SEQUENCES--------")
    val seq: Seq[Int] = Seq(1, 2, 3, 4)
    println(seq)
    println(seq.reverse)
    println(seq ++ Seq(5))
    println(seq.sorted)

    //Range
    println("\n------RANGE--------")
    val aRange: Seq[Int] = 1 until 10
    println(aRange)
    aRange.foreach(x => println("Hello"))

    //Array (no toString available)
    println("\n------ARRAY--------")
    println(Array.ofDim[Int](3).mkString(","))
    val ar1: Array[Int] = Array.ofDim(4)
    println(ar1.mkString(","))
    val arStr = Array.ofDim[String](5)
    println(arStr.mkString(","))
    arStr.update(1, "test")
    println(arStr.mkString(","))

    val anArray: Array[Int] = Array(1, 2, 3, 4)
    println(anArray.mkString)
    anArray.update(0, 99)
    println(anArray.mkString(","))
    println((anArray ++ Array(100)).mkString(","))
    println((anArray :+ 34).mkString(","))
    println((0 +: anArray).mkString(","))

    //Vector
    println("\n------VECTOR--------")
    val vector: Vector[Int] = Vector(1, 2, 3)
    println(vector)

    //List
    println("\n------LIST--------")
    val list: List[String] = List("test")
    println(list)
    val list2: List[String] = List.fill(5)("apple")
    println(list2)

    //List
    println("\n------TUPLES--------")
    val aTuple: Tuple2[Int, String] = (2, "Scala")
    val aTuple2 = new Tuple2[Int, String](2, "Scala")
    println(aTuple)
    println(aTuple2)

    println(aTuple._1)
    println(aTuple.copy(_2 = "goodbye Java"))
    println(aTuple.swap) // ("Scala", 2)

    //Map
    println("\n------MAP--------")
    val aMap: Map[String, Int] = Map("Test" -> 4, ("Test2", 9)).withDefaultValue(-1)
    println(aMap)
    println(aMap("example")) // -1

    val tuple2: Tuple2[String, Int] = "example" -> 10
    val aMap2 = aMap + tuple2
    println(aMap2)

    val transformedMap: Map[String, Int] = aMap2.map(element => element._1.toLowerCase -> (element._2 + 2))
    println(transformedMap)

    println(transformedMap.filter(x => x._1.startsWith("t")))
    println(transformedMap.map(number => "0245-" + number._2))

    val names = List("Bob", "James", "Angela", "Mary", "Daniel", "Jim")
    println(names.groupBy(name => name.charAt(0).toLower))

    //Options
    println("\n------OPTIONS--------")
    val option: Option[Int] = Some(2)
    val noOption: Option[Int] = None

    println(option)
    println(noOption.getOrElse(0))

    def unsafeMethod(): String = null

    println(Option(unsafeMethod()))
    println(noOption.isEmpty)

    println(option.map(_ * 2))

    //Pattern Matching
    println("\n------PATTERN MATCHING--------")

    val random = new Random
    val x = random.nextInt(5)

    val description = x match {
      case 1 => "test"
      case 2 => "test 2"
      case _ => "test autre"
    }
    println(description + "\n")

    val mapTest: Map[Any, Int] = Map((2 -> 4), (1 -> 4), ("test" -> 6))

    mapTest foreach ({
      case (something, 4) => println(s"$something and 4")
      case ("test", _) => println(s"test")
    })
    println

    val aList= List(1, 2, 3, 42)
    aList match {
      case List(1, _, _, _) => println("first") // extractor
      case List(1, _*) => println("second")  // list of arbitrary length
      case 1 :: List(_) => println("third") // infix pattern
      case List(1, 2, 3) :+ 42 => println("fourth") // infix pattern
      case _ => println("other")
    }
    println

    val unknown: Any= 2
    val unknownMatch= unknown match {
      case _ : List[Int] => println("List")
      case _ : Int => println("Integer")
      case _ => println("other")
    }

    //Threads
    println("\n----------THREADS------------")

    val threadSleeping = Future[Int] {
      Thread.sleep(1000)
      42
    }

    /*Await.ready(threadSleeping, 2 second) onComplete {
      case Success(x) => println(s"success : $x")
      case Failure(e) => println(s"failure : $e")
    }*/

    val result = for {
      r1 <- threadSleeping
      r2 <- threadSleeping
      r3 <- threadSleeping
    } yield r1 + r2 + r3

    Await.ready(result, 2 second) onComplete {
      case Success(x) => println(s"success : $x")
      case Failure(e) => println(s"failure : $e")
    }

    val test: String = "MUM-23 feat: sdfhqsdfj"
    val commitPattern: Regex = """^([A-Z0-9]+-[A-Z0-9]+.*)""".r.unanchored

    test match {
      case commitPattern(project) => println("match")
      case _ => println("no match")
    }

  }
}

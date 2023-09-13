import model.{Abc, Book, Employee}

import java.net.URL
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.io.StdIn.readLine
import scala.util.Try

object HelloWorld {

  def main(args: Array[String]): Unit = {

    //fonctions anonymes
    println("\n----------FONCTIONS ANONYMES------------")
    val numbers: List[Int] = List(1, 2, 3)
    val equalTwo = (i: Int) => i == 2
    val equalTwoBis : (Int) => Boolean = (i: Int) => i == 2
    val equalTwoBiss : (Int) => Boolean = _ == 2

    println(numbers.find(equalTwo).getOrElse(10))
    println(numbers.filter(equalTwo))
    println(numbers.filter(equalTwoBis))
    println(numbers.filter(equalTwoBiss))

    //blocks
    println("\n----------BOCKS------------")
    val area = {
      val PI = 3.1;
      println(s"Inside scope 1, PI = $PI");
      {
        val PI = 3
        println(s"Inside scope 2, PI = $PI");
      }
      PI * 10 * 10
    }

    println(area)

    //Expressions
    println("\n----------EXPRESSIONS------------")
    val cond: Boolean = true
    val i = if (cond) 42 else 0
    println(i)

    //String interpolation methods
    println("\n----------STRING PARSING------------")
    val a = 5
    println(s"1 + 1 = ${1 + 1} and 5 = $a")

    val height = 22.89
    val name = "PY"
    println(f"$name%s is $height%20.10f meters tall")

    //type inference
    println("\n----------TYPE INFERENCE------------")
    val y : String  = 2 + " thing"
    println(y)

    //function
    println("\n----------FUNCTIONS------------")
    val getArea = (x: Int) => {
      val PI: Double = 3.14d
      PI * 2 + x
    }: Double

    //ou
    val getArea2 = (x: Int) => 3.14d * 2 + x: Double

    println(s"${getArea(5)} == ${getArea2(5)}")

    //method
    println("\n----------METHODS------------")
    def method(x: Int): Double = {
      val PI: Double = 3.14d
      PI * 2 + x
    }

    println(method(5))

    //Class
    println("\n----------CLASS------------")
    val employee: Employee = new Employee("test", "test", 12, "hello")
    employee.set_first_name("Jack")

    val employee2: Employee = new Employee()

    println(employee)
    println(employee2)

    employee.ditBonjour()

    val abc : Abc = new Abc()
    val abc2 : Abc = new Abc(a="test override")

    val livre = Book("22-332-54")

    //Flatmap
    println("\n----------FLATMAP------------")
    val list: List[String] = List("Je suis chez Macq", "Il fait pas beau", "BX est une belle ville")
    println(list.map(_.split(" ").mkString(",")).mkString("|"))
    println(list.flatMap(_.split(" ")).mkString(","))

    //Option
    println("\n----------OPTION------------")
    val numbersList : List[Int] = List(4,3,5,6,19)
    val result : Int = numbersList.find(_ == 1).getOrElse(0)
    print(result)

    //yield
    println("\n----------YIELD------------")
    val numberList : List[Int] = List(4,3,5,6,19)
    val chars: List[Char] = List('a', 'b', 'c')
    val colors: List[String] = List("red", "blue", "yellow")

    val forCombinations = for {
      n <- numberList if n % 2 == 0 // use if to filter
      c <- chars
      color <- colors
    } yield c.toString + n + "-" + color // result is concatenated to an initially empty list with yield
    println(forCombinations)

    val forTest = for(n <- numberList if n > 5) yield n
    println(forTest)

    val forTest2 = for {
      n <- numberList
      c <- chars
    } yield n + c.toString

    println(forTest2)

    //Update array
    println("\n----------ARRAYS------------")
    val anArray = Array(56, 25, 3, 44)
    println(anArray.mkString(","))
    anArray.update(1, 0)
    println(anArray.mkString(","))
    anArray(2) = 5
    println(anArray.mkString(","))

    //Exceptions A REVOIR
    println("\n----------EXCEPTIONS------------")
    case class DivideZeroException(private val message: String = "You can't divide by 0!") extends Exception(message)

    def divideFunction(i: Int, j: Int) : Try[Int] = {
      Try(i/j)
    }

    val resultDivide = divideFunction(4, 0) recover {
      case _: ArithmeticException => DivideZeroException().getMessage
    }

    println(s"${resultDivide.get} \n")

    def parsedUrl(url: String): Try[URL] = Try(new URL(url))

    val url = parsedUrl("https://www.google.com/")
    println(s"Is success - ${url.isSuccess}")

    println(parsedUrl("mauvais url").getOrElse("https://www.google.com/"))



    //Curried function
    println("\n----------CURRIED FUNCTION------------")
    def curriedFormatter(c: String)(x: Double): String = c.format(x)
    val format1: (Double => String) = curriedFormatter("%4.2f")
    val format2: (Double => String) = curriedFormatter("%10.8f")

    println(curriedFormatter("%4.2f")(Math.PI))
    println(format1(Math.PI))
    println(format2 (Math.PI))

    //Implicit
    println("\n----------IMPLICIT------------")
    println("\n----IMPLICIT FUNCTION------")
    case class Animal(n: String){
      def name = n

      def boire() = {
        println("Je bois")
      }
    }

    implicit def fromStringToAnimal(s : String): Animal = Animal(s)

    println(fromStringToAnimal("Jaguar").name)
    println("Jaguar".name)
    "Jaguar".boire()

    println("\n----IMPLICIT PARAMETERS------")

    def sendText(body: String)(implicit from: String) : String = s"$body, from $from"
    implicit def sender : String = "Jack"
    println(sendText("Hello !"))

    println("\n----IMPLICIT CLASSES------")

    object Playground {
      implicit class MyString(x: String){
        def mirror: String = x.reverse
      }
      implicit class Other(x: String){
        def addThings: String = x+"abcdefg"
      }
    }

    object PlaygroundBis {
      trait MyTrait {
        val number: Int
        def transform(number: Int): String
      }

      implicit object MyObject extends MyTrait {
        val number: Int = 1
        def transform(number: Int): String = number.toString
      }

      def doSomething(implicit theTrait: MyTrait) = theTrait.transform(theTrait.number)

    }

    import Playground._
    import PlaygroundBis._

    println("1234".mirror)
    println("1234".addThings)

    println(doSomething)

  }

}

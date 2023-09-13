package model

class Employee (f : String, s : String, a : Int, var bonjour: String) extends Person(a) {
  private var first_name: String = f
  private var surname: String = s

  def this() = {
    this("foo", "bar", 20, "hello")
  }

  def this(name: String) = {
    this("name", "bar", 20, "hello")
  }

  def set_first_name(n: String): Unit = {
    first_name= n
  }

  def get_first_name(): String = {
    first_name
  }

  override def toString: String = {
    s"$first_name $surname $age"
  }


  override def ditBonjour(): Unit = {
    println(s"Je dis $bonjour")
  }

}

package model

abstract class Person(a : Int) extends Humain {

    var age: Int = a

    def get_age(): Int = {
        age
    }

}


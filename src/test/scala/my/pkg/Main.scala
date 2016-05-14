package my.pkg

import org.slf4j._

import scala.trace.{Debug, _}


/**
  * Created by johnreed on 3/23/16. Run with sbt test:run
  */
object Main {

  def main(args: Array[String]) {

    import scala.trace.Log
    import scala.trace._

    val list = List(-1,0,5,1,5,3,4,3)

    var currentMax = Int.MinValue


    val listofStrings = List("foo", "bar", "baz")

    def filter(t: String) : Boolean = {
      var found = false;
      for(s <- listofStrings) {
        if ( t.contains(s)) {
          found = true
        }
      }
      found
    }


    def getMaxIndex(array: Array[Int]): Int = {
      var max = Int.MinValue
      var maxIndex = Int.MinValue
      for {
        index <- 0 until array.length
        element <- array
      } {
        if (element > max) {
          max = element
          maxIndex = index
        }
      }
      maxIndex
    }
  }
}

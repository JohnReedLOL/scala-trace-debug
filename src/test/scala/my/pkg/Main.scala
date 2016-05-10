package my.pkg

import org.slf4j._

import scala.trace.{Debug, _}


/**
  * Created by johnreed on 3/23/16. Run with sbt test:run
  */
object Main {

  def merge(left: List[Int], right: List[Int]): List[Int] = {
    val combinedList = (left ++ right)
    combinedList.sorted
  }

  def split(input: List[Int]): (List[Int], List[Int]) = {
    val left = input.slice(0, input.length/2)
    val right = input.slice(input.length/2, input.length)
    (left, right)
  }

  /**
    * @param list unsorted list
    * @return sorted list
    */
  def mergeSort(list: List[Int]): List[Int] = {
    // do a split
    val (left, right) = split(list)
    left.length match {
      case 1 => left // base case
      case _ => merge( mergeSort(left), mergeSort(right) ) // recursive step
    }
  }

  /**
    * @param list unsorted list
    * @return sorted list
    */
  def mergeSort2(list: List[Int]): List[Int] = {
    // do a split
    val (left, right) = split(list)
    right.length match {
      case 0 => throw new RuntimeException("Should never happen")
      case 1 => merge(left, right) // base case
      case _ => merge( mergeSort(left), mergeSort(right) ) // recursive step
    }
  }

  def main(args: Array[String]) {

    import scala.trace.Log
    import scala.trace.implicitlyFormatable
    println( ("This is a really really really really really really really really really really really " +
      "really really really really really really really really really really really really really " +
      "really really really really really really really really" +
      "long string that needs to be formatted because it is longer than " + Format.getLineWrap
      + " chars").formatted)
    println( ("This is a really,really,really,really,really,really,really,really,really,really,really," +
      ",really,really,really,really,really," +
      ",really,really,really,really,really,," +
      "long,string,that,needs,to,be,formatted,because,it,is,longer,than," + Format.getLineWrap
      + ",chars").formatted(",") )

    println("an error message" + Pos())
    println(Log.find("foo" + 2 + "bar"))
    SDebug.traceCode("foo" + 2 + "bar")
    SDebug.traceExpression("foo" + 2 + "bar")
    SDebug.traceContents(List(1,2,3))
    println(Log.find(List(1,2,3)))
  }
}

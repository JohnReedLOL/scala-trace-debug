package main

import info.collaboration_station.debug._

// wildcard import for implicit trace/assert/print functionality
/**
  * Created by johnreed on 3/23/16. Run with sbt test:run
  */
object Main {
  def main(args: Array[String]) {

    "foooooooooo0".traceStdOut
    "foooooooooo1".trace

    Debug.traceCode({
      // print out the code as it appears in the source
      val myVal = 4;
      1 + 2 + myVal
    }, 1) // 1 lines of stack trace

    Debug.traceCode({
      // print out the code as it appears in the source
      val s = List(1, 2, 3)
      (s).toString
    }, 2) // 2 lines of stack trace

    Debug.assertNonFatalCode({
      val three = 3
      three + 2 == 0
    }, 2)

    Debug.assertCode {
      val one = 1
      one + 2 == 3
    }
    val m = Map[String, Int]("hello" -> 2) //  TraversableLike[A, Traversable[A]]

    Thread.sleep(20)

    // You can use this with a logger
    Debug.traceErrOff_! // just get the String
    val collectionString = Debug.traceContents(List(1, 2, 3))
    println(collectionString)

    Debug.traceOutOn_!
    Debug.traceContentsStdOut(Map("1" -> 1, "2" -> 2, "3" -> 3))

    Debug.enableEverything_!
    Debug.traceContents(List(1, 2, 3), numElements = 2, numLines = 2)


    Thread.sleep(20)

    /*
    val ll = new java.util.List() // interface Iterable<T>
    val lll = new java.util.Set() // Iterable<T>
    val llll = new java.util.Map() // Map<K, V>
    val x = new ConcurrentHashMap[String, Int]() // AbstractMap<K, V>
    val list = new scala.collection.mutable.ArrayBuffer[Int]()
val m = Map[String, Int]("hello" -> 2) //  TraversableLike[A, Traversable[A]]
val r = Array.apply(1,2,3) //  Array[T]
    val l = List(1,2,3) // A template trait for traversable collections of type Traversable[A]. This is a base trait of all kinds of Scala collections.
    */
    Debug.trace("Hello World") // 1 line of stack trace
    Debug.trace("Hello World 2", numLines = 2) // 2 lines of stack trace
    "Hello World".trace(0) // 0 lines of stack trace

    Debug.traceExpression {
      // trace the expression ("foo" + "bar")
      val foo = "foo";
      foo + "bar"
    }
    Debug.assert(1 + 1 == 2, "One plus one must equal two") // fatal assertion (kills application)
    1.assertNonFatalEquals(3, "One must equal three") // non-fatal assertion, 2 lines of stack trace [BROKEN]

    Thread.sleep(10) // sleep to prevent print statements from getting mixed up

    Debug.traceStdOut("Hey0")
    Debug.traceStdOutExpression {
      "Hey3"
    }
    Debug.traceStdOutExpression({
      val myVal = 5; 1 + 2 + myVal
    }, 0) // 0 lines of stack trace
    Debug.traceStdOutExpression("Hey4", 2)
    Debug.traceStackStdOutExpression {
      val myVal = 6; 1 + 2 + myVal
    }

    Thread.sleep(10) // sleep to prevent print statements from getting mixed up

    Debug.traceExpression {
      "Hey5"
    }
    Debug.traceExpression {
      val myVal = 3; 1 + 2 + myVal
    }
    Debug.traceExpression("Hey6", 2)
    Debug.traceStackExpression {
      val myVal = 4; 1 + 2 + myVal
    }
    Debug.traceExpression {
      val one = 1;
      val two = 2;
      val three = 3;
      val four = 4;
      val five = 5;
      one * two + three / four + five;
    }
    System.err.println("Assertions return strings: " + Debug.assertNonFatalExpression({
      val someVal = 2; 1 + someVal == 4
    }, 3))
    Debug.assertNonFatalExpression {
      val someVal = 2; 1 + someVal == 4
    }
    Debug.assertExpression({
      val one = 1; one + 1 == 2
    }, 0) // 0 lines of stack trace
    Debug.assertExpression {
      val myVal = 3; 1 + 2 == myVal
    }
    Debug.assertExpression(1 + 2 == 3)
    Debug.assertNonFatalExpression({
      val noStack = "No stack trace is generated"; noStack.equals("foo")
    }, 0) // no lines of stack trace
    Debug.assertNonFatal(1 == 2, "No stack trace is printed", 0)

    Debug.enableEverything_!()
    System.err.println(Debug.trace(77, numLines = 2)) // this should return a String
    System.err.println(Debug.assertNonFatal(2 == 3, "foo", numLines = 2)) // this should return a String
    val fooVar = "foo"
    val barVar = "bar"
    Debug.traceCode[String] {
      fooVar + barVar
    }
    Debug.traceCode[String]({
      fooVar + barVar
    }, 3)
    Debug.traceStackCode[String] {
      fooVar + barVar
    }
    val trueVar = true
    val falseVar = false
    System.err.println("assertCode")
    // Debug.assertCode{(trueVar || falseVar || false) && false} // "trueVar || falseVar || false -> false" in thread run-main-4:
    System.err.println("End")

    Debug.enableEverything_!()
    "foo".assertNotEquals(2, "ERROR! foo == 2")
    "foo".assertEquals("foo", "ERROR! foo == foo")
  }
}

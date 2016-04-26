package main

import scala.trace.debug._
import ch.qos.logback.classic.LoggerContext
import ch.qos.logback.core.util.StatusPrinter
import org.slf4j._

import scala.trace.debug.Debug


// wildcard import for implicit trace/assert/print functionality



/**
  * Created by johnreed on 3/23/16. Run with sbt test:run
  */
object Main {

  def main(args: Array[String]) {
    import scala.trace.debug.Log

    // Easy to locate log statements
    Debug.traceContents(List(1, 2, 3) map ( 2.* ))
    Debug.traceContents(List(1, 2, 3) map ( _*2 ))
  }
}


    /*
val logger = LoggerFactory.getLogger("Logger");
logger.warn(Log.pos("foobar")) // append position
logger.warn(Log.find("foo" + 2 + "bar"))
logger.warn(Log.find(Array("foo", "bar")))
val list = List(0, 1, 2)
logger.warn(Log.find(list, 2)) // first 2 elements
logger.warn(Log.find(Map("foo" -> 2)))


    println()
    println()
    println()
    logger.warn(Log.find(Array("foo", "bar", "baz")))
    logger.warn(Log.find(Array("foo", "bar", "baz"), 1))
    logger.warn(Log.find(Array("foo", "bar", "baz"), 0, 2))






    Debug.traceContents(List(0, 1, 2, 3), numElements = 3)

    //val file = sourcecode.File()
    //assert(file.endsWith("/sourcecode/shared/src/test/scala/sourcecode/Tests.scala"))

    /*
    Debug.traceCode(file)

    val line = implicitly[sourcecode.Line]

    Debug.traceCode(line)

    val s = sourcecode.FullName.Machine()

    Debug.traceCode( implicitly[sourcecode.Name] )
    Debug.traceCode( implicitly[sourcecode.FullName.Machine] )

    Thread.sleep(10)
    println()
    val parseFile: (String => String) = (s: String) => {
      val parsedFileLinuxMac = s.split("/")
      // val parsedFileWindows = s.split("\\") // java.util.regex.PatternSyntaxException: Unexpected internal error
      parsedFileLinuxMac(parsedFileLinuxMac.length-1)
    }
    println( sourcecode.FullName.Machine() + "(" + parseFile( sourcecode.File() ) + ":" + sourcecode.Line() + ")" )
    println( sourcecode.Name.Machine() + "(" + sourcecode.File() + ":" + sourcecode.Line() + ")" )
    println( sourcecode.Enclosing.Machine() + "(" + sourcecode.File() + ":" + sourcecode.Line() + ")" )
    println()
    */
    Thread.sleep(10)

    System.err.println("Main.main(Main.scala:30)")

    logger.debug("Hello world.");
    logger.debug("Hello world.");
    logger.debug("Hello world.");
    Debug.disableEverythingSE()
    logger.debug(Debug.trace("Foo bar is awesome!"))
    logger.debug(Debug.trace("Foo bar is awesome!"))
    logger.debug(Debug.trace("Foo bar is awesome!"))
    logger.debug("foo", new RuntimeException("re"))
    Debug.enableEverythingSE()
    Debug.traceContents(List(1, 2, 3, 4, 5, 6, 7))

    val temp = LoggerFactory.getILoggerFactory();
    val lc = temp.asInstanceOf[LoggerContext]
    StatusPrinter.print(lc);

    Thread.sleep(10)

    /*
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

    Debug.checkCode({
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
    Debug.traceErrOffSE // just get the String
    val collectionString = Debug.traceContents(List(1, 2, 3))
    println(collectionString)

    Debug.traceOutOnSE
    Debug.traceContentsStdOut(Map("1" -> 1, "2" -> 2, "3" -> 3))

    Debug.enableEverythingSE
    Debug.traceContents(List(1, 2, 3), numElements = 2, numLines = 2)


    Thread.sleep(20)
    */

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
    "Hello World".trace(numLines = 0) // 0 lines of stack trace

    Debug.traceExpression {
      // trace the expression ("foo" + "bar")
      val foo = "foo";
      foo + "bar"
    }
    Debug.assert(1 + 1 == 2, "One plus one must equal two") // fatal assertion (kills application)
    1.checkEquals(3, "One must equal three", numLines = 2) // non-fatal assertion, 2 lines of stack trace

    Thread.sleep(10) // sleep to prevent print statements from getting mixed up

    Debug.traceStdOut("Hey0")
    Debug.traceStdOutExpression {
      "Hey3"
    }
    Debug.traceStdOutExpression({
      val myVal = 5;
      1 + 2 + myVal
    }, 0) // 0 lines of stack trace
    Debug.traceStdOutExpression("Hey4", 2)
    Debug.traceStackStdOutExpression {
      val myVal = 6;
      1 + 2 + myVal
    }

    Thread.sleep(10) // sleep to prevent print statements from getting mixed up

    Debug.traceExpression {
      "Hey5"
    }
    Debug.traceExpression {
      val myVal = 3;
      1 + 2 + myVal
    }
    Debug.traceExpression("Hey6", 2)
    Debug.traceStackExpression {
      val myVal = 4;
      1 + 2 + myVal
    }
    Debug.traceExpression {
      val one = 1;
      val two = 2;
      val three = 3;
      val four = 4;
      val five = 5;
      one * two + three / four + five;
    }
    System.err.println("Assertions return strings: " + Debug.checkExpression({
      val someVal = 2;
      1 + someVal == 4
    }, 3))
    Debug.checkExpression {
      val someVal = 2;
      1 + someVal == 4
    }
    Debug.assertExpression({
      val one = 1;
      one + 1 == 2
    }, 0) // 0 lines of stack trace
    Debug.assertExpression {
      val myVal = 3;
      1 + 2 == myVal
    }
    Debug.assertExpression(1 + 2 == 3)
    Debug.checkExpression({
      val noStack = "No stack trace is generated";
      noStack.equals("foo")
    }, 0) // no lines of stack trace
    Debug.check(1 == 2, "No stack trace is printed", 0)

    Debug.enableEverythingSE()
    System.err.println(Debug.trace(77, numLines = 2)) // this should return a String
    System.err.println(Debug.check(2 == 3, "foo", numLines = 20)) // this should return a String
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
  }
}
*/

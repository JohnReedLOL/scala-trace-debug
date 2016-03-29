package main
import scala.reflect.macros.blackbox.Context
import info.collaboration_station.debug._ // wildcard import for implicit trace/assert/print functionality
/**
  * Created by johnreed on 3/23/16. Run with sbt test:run
  */
object Main {
  def main(args: Array[String]) {

    Debug.trace("Hello World")                  // 1 line of stack trace
    Debug.trace("Hello World 2", numLines = 2)  // 2 lines of stack trace
    "Hello World".trace(numLines = 0)           // 0 lines of stack trace

    Debug.traceExpression{ // trace the expression ("foo" + "bar")
      val foo = "foo";
      foo + "bar"
    }
    Debug.assert(1 + 1 == 2, "One plus one must equal two")         // fatal assertion (kills application)
    1.assertNonFatalEquals(3, "One must equal three", numLines = 2) // non-fatal assertion, 2 lines of stack trace

    Thread.sleep(10) // sleep to prevent print statements from getting mixed up

    Debug.traceStdOut("Hey0")
    Debug.traceStdOutExpression{"Hey3"}
    Debug.traceStdOutExpression{val myVal = 5; 1 + 2 + myVal}
    Debug.traceStdOutExpression("Hey4", 2)
    Debug.traceStackStdOutExpression{val myVal = 6; 1 + 2 + myVal}

    Thread.sleep(10) // sleep to prevent print statements from getting mixed up

    Debug.traceExpression{"Hey5"}
    Debug.traceExpression{val myVal = 3; 1 + 2 + myVal}
    Debug.traceExpression("Hey6", 2)
    Debug.traceStackExpression{val myVal = 4; 1 + 2 + myVal}
    Debug.traceExpression{
      val one = 1;
      val two = 2;
      val three = 3;
      val four = 4;
      val five = 5;
      one * two + three / four + five;
    }
    Debug.assertNonFatalExpression({val someVal = 2; 1  + someVal == 4}, 3) // 3 lines of stack trace
    Debug.assertNonFatalExpression{val someVal = 2; 1  + someVal == 4}
    Debug.assertExpression({val one = 1; one + 1 == 2}, 0) // 0 lines of stack trace
    Debug.assertExpression{val myVal = 3; 1 + 2 == myVal}
    Debug.assertExpression(1+2 == 3)
    Debug.assertNonFatalExpression({val noStack = "No stack trace is generated"; noStack.equals("foo")}, 0) // no lines of stack trace
    Debug.assertNonFatal(1 == 2, "No stack trace is printed", 0)

    Debug.disableEverything_!()
    System.err.println( Debug.trace(77, numLines = 2) ) // this should return a String
    System.err.println( Debug.assertNonFatal(2 == 3, "foo", numLines = 2) ) // this should return a String
  }
}

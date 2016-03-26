package main
import scala.reflect.macros.blackbox.Context
import info.collaboration_station.debug._
/**
  * Created by johnreed on 3/23/16. Run with sbt test:run
  */
object Main {
  def main(args: Array[String]) {

    Debug.trace("Hello World")      // 1 line of stack trace
    Debug.trace("Hello World 2", 2) // 2 lines of stack trace (max)
    "Hello World".trace(0)          // 0 lines of stack trace

    Debug.traceExpression{val foo = "foo"; foo + "bar"}     // trace that which produces foo + bar

    Debug.assert(1 + 1 == 2, "One plus one must equal two") // fatal assertion (kills application)
    1.assertNonFatalEquals(3, "One must equal three")       // non-fatal assertion


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

    Debug.assertNonFatalExpression{val someVal = 2; 1  + someVal == 4}
    Debug.assertExpression{val myVal = 3; 1 + 2 == myVal}
    Debug.assertExpression(1+2 == 3)

  }
}

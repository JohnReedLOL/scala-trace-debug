package main
import info.collaboration_station.debug._
import scala.reflect.macros.blackbox.Context
/**
  * Created by johnreed on 3/23/16.
  */
object Main {
  def main(args: Array[String]) {

    Debug.traceStdOut("Hey0")
    Debug.traceStdOutExpression{"Hey3"}
    Debug.traceStdOutExpression{val myVal = 5; 1 + 2 + myVal}
    Debug.traceStdOutExpression("Hey4", 2)
    Debug.traceStackStdOutExpression{val myVal = 6; 1 + 2 + myVal}
/*
    Debug.traceExpression{"Hey5"}
    Debug.traceExpression{val myVal = 3; 1 + 2 + myVal}
    Debug.traceExpression("Hey6", 2)
    Debug.traceStackExpression{val myVal = 4; 1 + 2 + myVal}

    Debug.assertExpression{val myVal = 3; 1 + 2 == myVal}
    Debug.assertExpression(1+2 == 3)
*/
  }
}

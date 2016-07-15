package scala.trace.conversions

import scala.language.implicitConversions
import scala.trace.internal.Printer

/**
  * Created by johnreed on 4/12/16.
  */
final class ImplicitTrace[MyType](val me: MyType) extends AnyVal {

  /** Prints out this object with 1 lines of stack trace to standard error
    *
    * @return The thing that was just printed
    */
  final def err: MyType = {
    Printer.traceInternal(me, 1); me
  }

  /** Prints out this object to standard error with a user specified number of lines of stack trace
    *
    * @param numLines The number of lines of stack trace
    * @return The thing that was just printed
    */
  final def err(numLines: Int = 1): MyType = {
    Printer.traceInternal(me, numLines); me
  }

  /** Prints out this object with 1 lines of stack trace to standard out
    *
    * @return The thing that was just printed
    */
  final def out: MyType = {
    Printer.traceInternal(me, 1, usingStdOut = true); me
  }

  /** Prints out this object to standard out with a user specified number of lines of stack trace
    *
    * @param numLines The number of lines of stack trace
    * @return The thing that was just printed
    */
  final def out(numLines: Int): MyType = {
    Printer.traceInternal(me, numLines, usingStdOut = true); me
  }

}

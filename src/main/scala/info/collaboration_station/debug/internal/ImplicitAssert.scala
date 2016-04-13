package info.collaboration_station.debug.internal

import info.collaboration_station.debug.Debug

import scala.language.implicitConversions

/**
  * Created by johnreed on 4/12/16.
  */
final case class ImplicitAssert[MyType](val me: MyType) {

  /** A fatal assertion, but with the function name after the object.
    * Terminates the program with exit code "7"
    *
    * @param assertion the assertion that must be true for the program to run
    * @param message   the message to be printed to standard error on assertion failure
    * @param numLines  the number of lines of stack trace
    * @example 1.assert( _ + 2 = 3, "Error: one plus two does not equal three.")
    * @note this (and other assertions not marked "nonFatal") are fatal. To disable, please call "Debug.fatalAssertOff_!()"
    */
  final def assert(assertion: (MyType) => Boolean, message: String, numLines: Int = Int.MaxValue): MyType = {
    Printer.traceInternalAssert(message, numLines, assertionTrue_? = assertion(me), isFatal_? = true)
    me
  }

  /** A fatal assertion, but with the function name after the object.
    * Terminates the program with exit code "7"
    *
    * @param assertion the assertion that must be true for the program to run
    * @param message   the message to be printed  to standard out on assertion failure
    * @param numLines  the number of lines of stack trace
    * @example 1.assertStdOut( _ + 2 = 3, "Error: one plus two does not equal three.")
    * @note this (and other assertions not marked "nonFatal") are fatal. To disable, please call "Debug.fatalAssertOff_!()"
    */
  final def assertStdOut(assertion: (MyType) => Boolean, message: String, numLines: Int = Int.MaxValue): MyType = {
    if (!assertion(me) && Debug.fatalAssertOn_?) {
      Printer.traceInternalAssert(message, numLines, useStdOut_? = true, assertionTrue_? = assertion(me), isFatal_? = true)
      System.exit(7)
    }
    me
  }

  /** Compares this object with another object of the same type for equality using the ".equals()" method.
    * Terminates the program with exit code "7" in case of assertion failure
    *
    * @param other    the thing that this must be equal to
    * @param message  the message to be printed  to standard error on assertion failure
    * @param numLines the max number of lines of stack trace to show on assertion failure. Defaults to all lines
    * @example "foo".assertEquals("bar", "Error: foo does not equal bar")
    * @note this (and other assertions not marked "nonFatal") are fatal. To disable, please call "Debug.fatalAssertOff_!()"
    */
  final def assertEquals(other: MyType, message: String, numLines: Int = Int.MaxValue): MyType = {
    Printer.traceInternalAssert(message, numLines, useStdOut_? = false, assertionTrue_? = me.equals(other), isFatal_? = true)
    me
  }

  /**
    * Same as ImplicitTrace.assertEquals(), but it uses StdOut instead of StdErr.
    *
    * @note this (and other assertions not marked "nonFatal") are fatal. To disable, please call "Debug.fatalAssertOff_!()"
    */
  final def assertEqualsStdOut(other: MyType, message: String, numLines: Int = Int.MaxValue): MyType = {
    Printer.traceInternalAssert(message, numLines, useStdOut_? = true, assertionTrue_? = me.equals(other), isFatal_? = true)
    me
  }

  /**
    * Same as ImplicitTrace[MyType].assert(), but it does not kill anything (not even the current thread)
    */
  final def assertNonFatal(assertion: (MyType) => Boolean, message: String, numLines: Int = Int.MaxValue): MyType = {
    Printer.traceInternalAssert(message, numLines, useStdOut_? = false, assertionTrue_? = assertion(me), isFatal_? = false)
    me
  }

  /**
    * Same as ImplicitTrace[MyType].assertStdOut(), but it does not kill anything (not even the current thread)
    */
  final def assertNonFatalStdOut(assertion: (MyType) => Boolean, message: String, numLines: Int = Int.MaxValue): MyType = {
    Printer.traceInternalAssert(message, numLines, useStdOut_? = true, assertionTrue_? = assertion(me), isFatal_? = false)
    me
  }

  /**
    * Same as ImplicitTrace[MyType].assertEquals(), but it does not kill anything (not even the current thread)
    */
  final def assertNonFatalEquals(other: MyType, message: String, numLines: Int = Int.MaxValue): MyType = {
    Printer.traceInternalAssert(message, numLines, useStdOut_? = false, assertionTrue_? = me.equals(other), isFatal_? = false)
    me
  }

  /**
    * Same as ImplicitTrace[MyType].assertEqualsStdOut(), but it does not kill anything (not even the current thread)
    */
  final def assertNonFatalEqualsStdOut(other: MyType, message: String, numLines: Int = Int.MaxValue): MyType = {
    Printer.traceInternalAssert(message, numLines, useStdOut_? = true, assertionTrue_? = me.equals(other), isFatal_? = false)
    me
  }
}

object ImplicitAssert {

}
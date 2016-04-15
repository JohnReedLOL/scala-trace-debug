package info.collaboration_station

import info.collaboration_station.debug.internal.old.ImplicitAssert
import info.collaboration_station.debug.internal.{ImplicitAssertMacros, ImplicitPrintMacros, ImplicitTraceMacros, Printer}

/**
  * Created by johnreed on 3/12/16. Contains implicit debug functions. Import with "import info.collaboration_station.debug._"
  */
package object debug {

  import scala.language.implicitConversions
  import scala.language.experimental.macros

  /**
    * Import this to add print functionality to the current scope
    */
  final implicit class ImplicitPrint[MyType](val me: MyType) {
    /**
      * Prints this to standard out and then returns me
      */
    def print(): MyType = macro ImplicitPrintMacros.print[MyType]

    /**
      * Prints this to standard out with newline and then returns me
      */
    def println(): MyType = macro ImplicitPrintMacros.println[MyType]

    /**
      * Prints this to standard error and then returns me
      */
    def printStdErr(): MyType = macro ImplicitPrintMacros.printStdErr[MyType]

    /**
      * Prints this to standard error with newline and then returns me
      */
    def printlnStdErr(): MyType = macro ImplicitPrintMacros.printlnStdErr[MyType]
  }

  /**
    * Import this to add trace functionality to the current scope
    */
  final implicit class ImplicitTrace[MyType](val me: MyType) {
    /**
      * Prints this to standard error with one line of stack trace
      */
    def trace(): MyType = macro ImplicitTraceMacros.trace[MyType]
    /**
      * Prints this to standard error with n lines of stack trace
      */
    def trace(numLines: Int): MyType = macro ImplicitTraceMacros.traceLines[MyType]

    /**
      * Prints to standard error this along with a stack trace
      */
    def traceStack(): MyType = macro ImplicitTraceMacros.traceStack[MyType]

    /**
      * Prints this to standard out with one line of stack trace
      */
    def traceStdOut(): MyType = macro ImplicitTraceMacros.traceStdOut[MyType]

    /**
      * Prints this to standard out with n lines of stack trace
      */
    def traceStdOut(numLines: Int): MyType = macro ImplicitTraceMacros.traceLinesStdOut[MyType]

    /**
      * Prints to standard out this along with a stack trace
      */
    def traceStackStdOut(): MyType = macro ImplicitTraceMacros.traceStackStdOut[MyType]
  }

  /**
    * Import this to add assert functionality to the current scope
    */
  final implicit class ImplicitAssert[MyType](val me: MyType) {

    // numLines: Int = Int.MaxValue should be adjustable.

    /** A fatal assertion, but with the function name after the object.
      * Terminates the program with exit code "7"
      *
      * @param assertion the assertion that must be true for the program to run
      * @param message   the message to be printed to standard error on assertion failure
      * @example 1.assert( _ + 2 = 3, "Error: one plus two does not equal three.")
      * @note this (and other assertions not marked "nonFatal") are fatal. To disable, please call "Debug.fatalAssertOff_!()"
      */
    final def assert(assertion: (MyType) => Boolean, message: String): MyType =
      macro ImplicitAssertMacros.assert[MyType]

    /** A fatal assertion, but with the function name after the object.
      * Terminates the program with exit code "7"
      *
      * @param assertion the assertion that must be true for the program to run
      * @param message   the message to be printed  to standard out on assertion failure
      * @example 1.assertStdOut( _ + 2 = 3, "Error: one plus two does not equal three.")
      * @note this (and other assertions not marked "nonFatal") are fatal. To disable, please call "Debug.fatalAssertOff_!()"
      */
    final def assertStdOut(assertion: (MyType) => Boolean, message: String): MyType =
      macro ImplicitAssertMacros.assertStdOut[MyType]

    /** Compares this object with another object of the same type for equality using the ".equals()" method.
      * Terminates the program with exit code "7" in case of assertion failure
      *
      * @param other    the thing that this must be equal to
      * @param message  the message to be printed  to standard error on assertion failure
      * @example "foo".assertEquals("bar", "Error: foo does not equal bar")
      * @note this (and other assertions not marked "nonFatal") are fatal. To disable, please call "Debug.fatalAssertOff_!()"
      */
    final def assertEquals[OtherType](other: OtherType, message: String): MyType =
      macro ImplicitAssertMacros.assertEquals[MyType, OtherType]

    /**
      * Same as ImplicitTrace.assertEquals(), but it uses StdOut instead of StdErr.
      *
      * @note this (and other assertions not marked "nonFatal") are fatal. To disable, please call "Debug.fatalAssertOff_!()"
      */
    final def assertEqualsStdOut[OtherType](other: OtherType, message: String): MyType =
      macro ImplicitAssertMacros.assertEqualsStdOut[MyType, OtherType]

    /**
      * Same as assertEquals, but checks for inequality instead of equality.
      */
    final def assertNotEquals[OtherType](other: OtherType, message: String): MyType =
      macro ImplicitAssertMacros.assertNotEquals[MyType, OtherType]

    /**
      * Same as assertEqualsStdOut, but checks for inequality instead of equality.
      */
    final def assertNotEqualsStdOut[OtherType](other: OtherType, message: String): MyType =
      macro ImplicitAssertMacros.assertNotEqualsStdOut[MyType, OtherType]

    /**
      * Same as ImplicitTrace[MyType].assert(), but it does not kill anything (not even the current thread)
      */
    final def assertNonFatal(assertion: (MyType) => Boolean, message: String): MyType =
      macro ImplicitAssertMacros.assertNonFatal[MyType]

    /**
      * Same as ImplicitTrace[MyType].assertStdOut(), but it does not kill anything (not even the current thread)
      */
    final def assertNonFatalStdOut(assertion: (MyType) => Boolean, message: String): MyType =
      macro ImplicitAssertMacros.assertNonFatalStdOut[MyType]

    /**
      * Same as ImplicitTrace[MyType].assertEquals(), but it does not kill anything (not even the current thread)
      */
    final def assertNonFatalEquals[OtherType](other: OtherType, message: String): MyType =
      macro ImplicitAssertMacros.assertNonFatalEquals[MyType, OtherType]

    /**
      * Same as ImplicitTrace[MyType].assertEqualsStdOut(), but it does not kill anything (not even the current thread)
      */
    final def assertNonFatalEqualsStdOut[OtherType](other: OtherType, message: String): MyType =
      macro ImplicitAssertMacros.assertNonFatalEqualsStdOut[MyType, OtherType]

    /**
      * Same as assertNonFatalEquals, but checks for inequality instead of equality.
      */
    final def assertNonFatalNotEquals[OtherType](other: OtherType, message: String): MyType =
      macro ImplicitAssertMacros.assertNonFatalNotEquals[MyType, OtherType]

    /**
      * Same as assertNonFatalEqualsStdOut, but checks for inequality instead of equality.
      */
    final def assertNonFatalNotEqualsStdOut[OtherType](other: OtherType, message: String): MyType =
      macro ImplicitAssertMacros.assertNonFatalNotEqualsStdOut[MyType, OtherType]
  }

}
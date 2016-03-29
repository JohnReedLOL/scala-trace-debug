package info.collaboration_station

/**
  * Created by johnreed on 3/12/16. Contains implicit debug functions. Import with "import info.collaboration_station.debug._"
  */
package object debug {

  import scala.language.implicitConversions

  // Warning: implicit conversions language feature

  /** Wrapper class for a value that can be traced. Uses the implicit conversion feature to append print, trace, and assert functionality onto a value
    *
    * @param me the original value inside the wrapper (before the conversion)
    * @tparam MyType the original type of the value inside the wrapper
    */
  implicit final class ImplicitTrace[MyType](val me: MyType) {
    /**
      * Same as System.out.print(this), but with the function name after the object
      * @return the thing that was just printed
      * @example if(foo.print) { /* do something with foo */ }
      */
    final def print(): MyType = { System.out.print(me); me }

    /**
      * Same as System.out.println(this), but with the function name after the object
      * @return the thing that was just printed
      * @example if(foo.println) { /* do something with foo */ }
      */
    final def println(): MyType = { System.out.println(me); me }

    /**
      * Same as System.err.print(this), but with the function name after the object
      * @return the thing that was just printed
      * @example if(foo.printStdErr) { /* do something with foo */ }
      */
    final def printStdErr(): MyType = { System.err.print(me); me }

    /**
      * Same as System.err.println(this), but with the function name after the object
      * @return the thing that was just printed
      * @example if(foo.printlnStdErr) { /* do something with foo */ }
      */
    final def printlnStdErr(): MyType = { System.err.println(me); me }

    /** Prints out this object with 1 lines of stack trace to standard error
      *
      * @return The thing that was just printed
      */
    final def trace: MyType = { ImplicitTraceObject.traceInternal(me, 1); me}

    /** Prints out this object to standard error with a user specified number of lines of stack trace
      *
      * @param numLines The number of lines of stack trace
      * @return The thing that was just printed
      */
    final def trace(numLines: Int = 1): MyType = { ImplicitTraceObject.traceInternal(me, numLines); me }

    /** Prints out this object to standard error along with the entire stack trace
      *
      * @return The thing that was just printed
      */
    final def traceStack: MyType = { ImplicitTraceObject.traceInternal(me, Int.MaxValue); me }

    /** Prints out this object with 1 lines of stack trace to standard out
      *
      * @return The thing that was just printed
      */
    final def traceStdOut: MyType = { ImplicitTraceObject.traceInternal(me, 1, useStdOut_? = true); me }

    /** Prints out this object to standard out with a user specified number of lines of stack trace
      *
      * @param numLines The number of lines of stack trace
      * @return The thing that was just printed
      */
    final def traceStdOut(numLines: Int): MyType = { ImplicitTraceObject.traceInternal(me, numLines, useStdOut_? = true); me }

    /** Prints out this object to standard out along with the entire stack trace
      *
      * @return The thing that was just printed
      */
    final def traceStackStdOut: MyType = { ImplicitTraceObject.traceInternal(me, Int.MaxValue, useStdOut_? = true); me }

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
      if (!assertion(me) && Debug.fatalAssertOn_?) {
        ImplicitTraceObject.traceInternalAssert(message, numLines) // trace the max number of lines of stack trace to std error
        System.exit(7)
      }
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
        ImplicitTraceObject.traceInternalAssert(message, numLines, useStdOut_? = true) // trace the max number of lines of stack trace to std out
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
      val assertionTrue_? = (me.equals(other))
      if (!assertionTrue_? && Debug.fatalAssertOn_?) {
        ImplicitTraceObject.traceInternalAssert(message, numLines) // trace the max number of lines of stack trace to std error
        System.exit(7)
      }
      me
    }

    /**
      * Same as ImplicitTrace.assertEquals(), but it uses StdOut instead of StdErr.
      *
      * @note this (and other assertions not marked "nonFatal") are fatal. To disable, please call "Debug.fatalAssertOff_!()"
      */
    final def assertEqualsStdOut(other: MyType, message: String, numLines: Int = Int.MaxValue): MyType = {
      val assertionTrue_? = (me.equals(other))
      if (!assertionTrue_? && Debug.fatalAssertOn_?) {
        ImplicitTraceObject.traceInternalAssert(message, numLines, useStdOut_? = true) // trace the max number of lines of stack trace to std out
        System.exit(7)
      }
      me
    }

    /**
      * Same as ImplicitTrace[MyType].assert(), but it does not kill anything (not even the current thread)
      */
    final def assertNonFatal(assertion: (MyType) => Boolean, message: String, numLines: Int = Int.MaxValue): MyType = {
      val assertionTrue_? = assertion(me)
      if (!assertionTrue_? && Debug.nonFatalAssertOn_?) {
        ImplicitTraceObject.traceInternalAssert(message, numLines) // trace the max number of lines of stack trace to std error
      }
      me
    }

    /**
      * Same as ImplicitTrace[MyType].assertStdOut(), but it does not kill anything (not even the current thread)
      */
    final def assertNonFatalStdOut(assertion: (MyType) => Boolean, message: String, numLines: Int = Int.MaxValue): MyType = {
      val assertionTrue_? = assertion(me)
      if (!assertionTrue_? && Debug.nonFatalAssertOn_?) {
        ImplicitTraceObject.traceInternalAssert(message, numLines, useStdOut_? = true) // trace the max number of lines of stack trace to std out
      }
      me
    }

    /**
      * Same as ImplicitTrace[MyType].assertEquals(), but it does not kill anything (not even the current thread)
      */
    final def assertNonFatalEquals(other: MyType, message: String, numLines: Int = Int.MaxValue): MyType = {
      val assertionTrue_? = (me.equals(other))
      if (!assertionTrue_? && Debug.nonFatalAssertOn_?) {
        ImplicitTraceObject.traceInternalAssert(message, numLines) // trace the max number of lines of stack trace to std error
      }
      me
    }

    /**
      * Same as ImplicitTrace[MyType].assertEqualsStdOut(), but it does not kill anything (not even the current thread)
      */
    final def assertNonFatalEqualsStdOut(other: MyType, message: String, numLines: Int = Int.MaxValue): MyType = {
      val assertionTrue_? = (me.equals(other))
      if (!assertionTrue_? && Debug.nonFatalAssertOn_?) {
        ImplicitTraceObject.traceInternalAssert(message, numLines, useStdOut_? = true) // trace the max number of lines of stack trace to std out
      }
      me
    }
  }

  /**
    * Contains static methods for ImplicitTrace. All the real printing is done here.
    */
  protected[debug] object ImplicitTraceObject {

    /**
      * Ensures that no two threads can print at the same time
      */
    private object PrintLocker

    /** The offset of the first line from the base of the stack trace
      * The +1 is necessary because the method call traceInternal adds one to the offset of the stack trace
      */
    protected[debug] val newStackOffset = Debug.stackOffset + 1

    /** Prints out the object with N lines of stack trace. Do not use with assertions
      *
      * @param toPrintOutNullable    the object to print out. May be "null"
      * @param numStackLinesIntended N, the number of lines of stack trace intended. Defaults to zero actual lines of stack trace for negative values
      * @param useStdOut_?           Whether to use standard out for trace (as opposed to std error). Uses standard error by default
      * @return The string that would have been printed out if printing were enabled and the string that was printed out because printing was enabled.
      */
    protected[debug] final def traceInternal[A](toPrintOutNullable: A, numStackLinesIntended: Int,
        useStdOut_? : Boolean = false): String = {
      val numStackLines = if (numStackLinesIntended > 0) {
        numStackLinesIntended // the number of lines must be positive or zero
      } else {
        0
      }
      val stack = Thread.currentThread().getStackTrace
      val toPrintOut: String = if (toPrintOutNullable == null) {
        "null"
      } else {
        toPrintOutNullable.toString
      }
      var toPrint = "\"" + toPrintOut + "\"" + " in thread " + Thread.currentThread().getName + ":"
      for (row <- 0 to Math.min(numStackLines - 1, stack.length - 1 - newStackOffset)) {
        val lineNumber = newStackOffset + row
        val stackLine = stack(lineNumber)
        // The java stack traces use a tab character, not a space
        val tab = "\t"
        toPrint += "\n" + tab + "at " + stackLine
      }
      toPrint += "\n"

      if (!useStdOut_? && !Debug.traceErrOn_? ) {
        return toPrint // if we are using standard error and tracing to standard error is off, return
      }
      if (useStdOut_? && !Debug.traceOutOn_? ) {
        return toPrint // if we are using standard out and tracing to standard out is off, return
      }

      if (useStdOut_?) {
        PrintLocker.synchronized{ System.out.println(toPrint) }
      } else {
        PrintLocker.synchronized{ System.err.println(toPrint) }
      }
      toPrint
    }

    /** Prints out the object with N lines of stack trace. Meant to be used only for asserts
      *
      * @param toPrintOutNullable    the object to print out. May be "null"
      * @param numStackLinesIntended N, the number of lines of stack trace intended. Defaults to zero actual lines of stack trace for negative values
      * @param useStdOut_?           Whether to use standard out for trace (as opposed to std error). Uses standard error by default
      * @return The string that would have been printed out if printing were enabled and the string that was printed out because printing was enabled.
      */
    protected[debug] final def traceInternalAssert[A](toPrintOutNullable: A, numStackLinesIntended: Int,
        useStdOut_? : Boolean = false): String = {
      // Disabling trace does not also disable assert. They are two separate things
      //if( !Debug.traceErrOn_? && !useStdOut_?) {
      //  return toPrintOutNullable // if tracing to standard error is off and we trace to standard error, return
      //}
      //if( !Debug.traceOutOn_? && useStdOut_?) {
      //  return toPrintOutNullable // if tracing to standard out is off and we trace to out, return
      //}
      // ^ Disabling is being taken care of in the assert function. There is no connection between turning off trace and turning off assert.
      val numStackLines = if (numStackLinesIntended > 0) {
        numStackLinesIntended // the number of lines must be positive or zero
      } else {
        0
      }
      val stack = Thread.currentThread().getStackTrace
      val toPrintOut: String = if (toPrintOutNullable == null) {
        "null"
      } else {
        toPrintOutNullable.toString
      }
      var toPrint = "\"" + toPrintOut + "\"" + " in thread " + Thread.currentThread().getName + ":"
      for (row <- 0 to Math.min(numStackLines - 1, stack.length - 1 - newStackOffset)) {
        val lineNumber = newStackOffset + row
        val stackLine = stack(lineNumber)
        // The java stack traces use a tab character, not a space
        val tab = "\t"
        toPrint += "\n" + tab + "at " + stackLine
      }
      toPrint += "\n" + "^ The above stack trace leads to an assertion failure. ^" + "\n"
      if (useStdOut_?) {
        PrintLocker.synchronized{ System.out.println(toPrint) }
      } else {
        PrintLocker.synchronized{ System.err.println(toPrint) }
      }
      toPrint
    }
  }

}

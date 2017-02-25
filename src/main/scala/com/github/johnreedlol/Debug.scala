package com.github.johnreedlol

import scala.trace.internal.Printer

/**
  * Contains static debug functions for Java and Scala.
  * https://github.com/JohnReedLOL/scala-trace-debug
  */
object Debug {

  /**
    * Stack offset is 2 because the first row in the stack trace is Thread and the second row is internal call
    */
  protected[trace] val stackOffset = 2

  @volatile private var isTraceOutOn_ = true
  @volatile private var isTraceErrOn_ = true
  @volatile private var isFatalAssertOn_ = true
  @volatile private var isNonFatalAssertOn_ = true
  @volatile private var elementsPerRow_ = 10 // for easy counting

  /**
    * Uses Java notation for Java users
    *
    * @return the number of elements per row for container printing
    */
  def getElementsPerRow(): Int = {
    elementsPerRow_
  }

  /**
    * Uses Java notation for Java users
    *
    * @param elementsPerRow the number of elements per row for container printing
    */
  def setElementsPerRow(elementsPerRow: Int): Unit = {
    require(elementsPerRow > 0)
    elementsPerRow_ = elementsPerRow
  }

  /** Tells you whether tracing to standard out is on or off
    * Note that disabling the "traceStdOut" feature does not disable the "assertStdOut" feature
    */
  def isTraceOutOn: Boolean = isTraceOutOn_

  /** Tells you whether tracing to standard error is on or off
    * Note that disabling the "trace" feature does not disable the "assert" feature
    */
  def isTraceErrOn: Boolean = isTraceErrOn_

  /**
    * Tells you whether fatal asserts are on or off
    */
  def isFatalAssertOn: Boolean = isFatalAssertOn_

  /**
    * Tells you whether non-fatal asserts are on or off
    */
  def isNonFatalAssertOn: Boolean = isNonFatalAssertOn_

  /**
    * Enables tracing and asserts, including fatal assertions.
    *
    */
  def enableEverything(): Unit = {
    traceErrOn()
    traceOutOn()
    fatalAssertOn()
    nonFatalAssertOn()
  }

  /**
    * Enables tracing to standard error. Has no effect on "print" or "println", only on "trace" methods
    *
    */
  def traceErrOn(): Unit = {
    isTraceErrOn_ = true
  }

  /**
    * Enables tracing to standard out. Has no effect on "print" or "println", only on "traceStdOut" methods
    *
    */
  def traceOutOn(): Unit = {
    isTraceOutOn_ = true
  }

  /**
    * Enables fatal assertions. Has no effect on "check", only on "assert" and other fatal assert methods (assertEquals, etc.)
    *
    */
  def fatalAssertOn(): Unit = {
    isFatalAssertOn_ = true
  }

  /**
    * Enables non-fatal assertions. Has no effect on "assert" and other fatal assert methods (assertEquals, etc.)
    *
    */
  def nonFatalAssertOn(): Unit = {
    isNonFatalAssertOn_ = true
  }

  /**
    * Disables tracing and asserts. Both fatal and non-fatal assertions are disabled. Does not disable print or println
    *
    */
  def disableEverything(): Unit = {
    traceErrOff()
    traceOutOff()
    fatalAssertOff()
    nonFatalAssertOff()
  }

  /**
    * Disables tracing to standard error. Has no effect on "print" or "println", only on "trace" methods
    *
    */
  def traceErrOff(): Unit = {
    isTraceErrOn_ = false
  }

  /**
    * Disables tracing to standard out. Has no effect on "print" or "println", only on "traceStdOut" methods
    *
    */
  def traceOutOff(): Unit = {
    isTraceOutOn_ = false
  }

  /**
    * Disables fatal assertions. Has no effect on "check", only on "assert" and other fatal assert methods (assertEquals, etc.)
    *
    */
  def fatalAssertOff(): Unit = {
    isFatalAssertOn_ = false
  }

  /**
    * Disables non-fatal assertions. Has no effect on "assert" and other fatal assert methods (assertEquals, etc.)
    *
    */
  def nonFatalAssertOff(): Unit = {
    isNonFatalAssertOn_ = false
  }

  /**
    * Traces to standard error with a one line stack trace.
    *
    * @param toPrint whatever it is you want to print.
    * @return the string containing what was printed or what would have been printed if printing was enabled. You can pass this string into a logger.
    */
  def err(toPrint: Any): String = Printer.traceInternal(toPrint.toString, 1)

  /**
    * Traces to standard error with a N line stack trace.
    *
    * @param toPrint  whatever it is you want to print.
    * @param numLines the number of lines to trace
    * @return the string containing what was printed or what would have been printed if printing was enabled. You can pass this string into a logger.
    */
  def err(toPrint: Any, numLines: Int): String = Printer.traceInternal(toPrint.toString, numLines)

  /**
    * Same as Debug.err, but prints to standard out instead of standard error
    *
    * @return the string containing what was printed or what would have been printed if printing was enabled. You can pass this string into a logger.
    */
  def out(toPrint: Any): String = Printer.traceInternal(toPrint.toString, 1, usingStdOut = true)

  /**
    * Same as Debug.err(toPrint: Any, numLines: Int), but prints to standard out instead of standard error
    *
    * @return the string containing what was printed or what would have been printed if printing was enabled. You can pass this string into a logger.
    */
  def out(toPrint: Any, numLines: Int): String = Printer.traceInternal(toPrint.toString, numLines, usingStdOut = true)

  /** A fatal assertion.
    * Terminates the program with exit code "7"
    * Note: "assert" is a reserved keyword in Java, use "assrt" instead.
    *
    * @param assertion the assertion that must be true for the program to run.
    * @param message   the message to be printed to standard error on assertion failure
    * @example Debug.assert( 1 + 2 == 4, "Error: one plus two is not equal to four" )
    * @note this (and other assertions not marked "nonFatal") are fatal. To disable, please call "Debug.fatalAssertOffSE()"
    * @return the string containing what was printed or what would have been printed if printing was enabled. You can pass this string into a logger.
    */
  def assert(assertion: Boolean, message: String, numLines: Int = Int.MaxValue): String = {
    Printer.internalAssert(message, numLines, usingStdOut = false, assertionTrue_? = assertion, isFatal_? = true) // trace the max number of lines of stack trace to std error
  }

  /** A fatal assertion.
    * Terminates the program with exit code "7"
    *
    * @param assertion the assertion that must be true for the program to run.
    * @param message   the message to be printed to standard out on assertion failure
    * @example Debug.assertOut( 1 + 2 == 4, "Error: one plus two is not equal to four" )
    * @note this (and other assertions not marked "nonFatal") are fatal. To disable, please call "Debug.fatalAssertOffSE()"
    * @return the string containing what was printed or what would have been printed if printing was enabled. You can pass this string into a logger.
    */
  def assertOut(assertion: Boolean, message: String, numLines: Int = Int.MaxValue): String = {
    Printer.internalAssert(message, numLines, usingStdOut = true, assertionTrue_? = assertion, isFatal_? = true)
  }

  /**
    * Like Debug.assert(), but does not terminate the application
    *
    * @return the string containing what was printed or what would have been printed if printing was enabled. You can pass this string into a logger.
    */
  def check(assertion: Boolean, message: String, numLines: Int = Int.MaxValue): String = {
    Printer.internalAssert(message, numLines, usingStdOut = false, assertionTrue_? = assertion, isFatal_? = false)
  }

  /**
    * Like Debug.assertOut(), but does not terminate the application
    *
    * @return the string containing what was printed or what would have been printed if printing was enabled. You can pass this string into a logger.
    */
  def checkOut(assertion: Boolean, message: String, numLines: Int = Int.MaxValue): String = {
    Printer.internalAssert(message, numLines, usingStdOut = true, assertionTrue_? = assertion, isFatal_? = false)
  }

  /**
    * Gets the collection as a string of n elements from start to start + numElements
    */
  protected[trace] def getArrayAsString[T](coll: Array[T], start: Int = 0, numElements: Int = Int.MaxValue): String = {
    var toPrint = ""
    val iterator: Iterator[T] = coll.toIterator
    var currentElement: Long = 0L // Long to prevent overflow
    val end: Long = start.toLong + numElements.toLong // Long to prevent overflow
    // first increment the iterator to start
    while (currentElement < start) {
      if (iterator.hasNext) {
        // Skip this element
        iterator.next()
        currentElement += 1L
      } else {
        // get out of loop
        currentElement = Long.MaxValue
      }
    }
    val startElement: Long = currentElement
    // Now currentElement = start
    // then do real printing
    while (currentElement < end) {
      if (iterator.hasNext) {
        toPrint = toPrint + iterator.next() + ", "
        currentElement += 1L
        if ((currentElement - startElement) % Debug.getElementsPerRow() == 0) {
          toPrint += "\n" // newline every "numElementsPerRow" elements
        }
      } else {
        // get out of loop
        currentElement = Long.MaxValue
      }
    }
    toPrint = toPrint.trim // no trailing whitespace

    if (toPrint.length() > 0 && toPrint.charAt(toPrint.length() - 1) == ',') {
      toPrint = toPrint.substring(0, toPrint.length() - 1) // remove trailing comma
    }

    " " + toPrint + "\n"
  }

  /**
    * Same as Macro.containerErr[ContainedT], but for Java Arrays (callable from Java Code)
    */
  def arrayErr[T](coll: Array[T], start: Int = 0, numElements: Int = Int.MaxValue, numLines: Int = 1)
  : String = {
    val toPrint: String = getArrayAsString(coll, start, numElements)
    Printer.traceInternal(toPrint, numStackLinesIntended = numLines, usingStdOut = false)
  }


  /**
    * Same as Macro.containerOut[ContainedT], but for Java Arrays (callable from Java Code)
    */
  def arrayOut[T](coll: Array[T], start: Int = 0, numElements: Int = Int.MaxValue, numLines: Int = 1)
  : String = {
    val toPrint: String = getArrayAsString(coll, start, numElements)
    Printer.traceInternal(toPrint, numStackLinesIntended = numLines, usingStdOut = true)
  }

}

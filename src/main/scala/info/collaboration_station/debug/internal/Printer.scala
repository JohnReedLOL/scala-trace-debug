package info.collaboration_station.debug.internal

import info.collaboration_station.debug.Debug

/**
  * Created by johnreed on 4/12/16.
  */
object Printer {

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
    val toPrintOut: String = if (toPrintOutNullable == null) {
      "null"
    } else {
      toPrintOutNullable.toString
    }
    var toPrint = "\"" + toPrintOut + "\"" + " in thread " + Thread.currentThread().getName + ":"
    if (numStackLinesIntended > 0) {
      val stack = Thread.currentThread().getStackTrace
      for (row <- 0 to Math.min(numStackLinesIntended - 1, stack.length - 1 - newStackOffset)) {
        val lineNumber = newStackOffset + row
        val stackLine = stack(lineNumber)
        // The java stack traces use a tab character, not a space
        val tab = "\t"
        toPrint += "\n" + tab + "at " + stackLine
      }
    } else {
      // do not make a call to Thread.currentThread().getStackTrace
    }
    toPrint += "\n"
    if (!useStdOut_? && !Debug.traceErrOn_?) {
      return toPrint // if we are using standard error and tracing to standard error is off, return
    }
    if (useStdOut_? && !Debug.traceOutOn_?) {
      return toPrint // if we are using standard out and tracing to standard out is off, return
    }

    if (useStdOut_?) {
      PrintLocker.synchronized {
        System.out.println(toPrint)
      }
    } else {
      PrintLocker.synchronized {
        System.err.println(toPrint)
      }
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
                                                    useStdOut_? : Boolean = false, assertionTrue_? : Boolean, isFatal_? : Boolean): String = {
    if (assertionTrue_?) {
      return "" // If assertion is true, print nothing and return empty string.
    }
    val toPrintOut: String = if (toPrintOutNullable == null) {
      "null"
    } else {
      toPrintOutNullable.toString // calling toString on null is bad
    }
    var toPrint = "\"" + toPrintOut + "\"" + " in thread " + Thread.currentThread().getName + ":"
    if (numStackLinesIntended > 0) {
      // Only make call to Thread.currentThread().getStackTrace if there is a stack to print
      val stack = Thread.currentThread().getStackTrace
      for (row <- 0 to Math.min(numStackLinesIntended - 1, stack.length - 1 - newStackOffset)) {
        val lineNumber = newStackOffset + row
        val stackLine = stack(lineNumber)
        // The java stack traces use a tab character, not a space
        val tab = "\t"
        toPrint += "\n" + tab + "at " + stackLine
      }
      toPrint += "\n" + "^ The above stack trace leads to an assertion failure. ^" + "\n"
    } else {
      toPrint += "\n" + "^ An assertion failure has occured. ^" + "\n"
    }
    if (!isFatal_? && !Debug.nonFatalAssertOn_?) {
      return toPrint // If it is nonfatal and nonFatalAssert is off, return the string without printing (so that the logger can print it)
    }
    if (isFatal_? && !Debug.fatalAssertOn_?) {
      return toPrint // If it is fatal and fatalAssert is off, return the string without printing (so that the logger can print it)
    }
    if (useStdOut_?) {
      PrintLocker.synchronized {
        System.out.println(toPrint)
      }
    } else {
      PrintLocker.synchronized {
        System.err.println(toPrint)
      }
    }
    if (isFatal_? && Debug.fatalAssertOn_?) {
      System.exit(7) // if the assertion is fatal and fatal assert is on, exit with system code 7
    }
    toPrint
  }

  /**
    * Ensures that no two threads can print at the same time
    */
  private object PrintLocker
}

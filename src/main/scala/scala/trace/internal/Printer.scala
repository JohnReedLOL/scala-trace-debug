package scala.trace.internal

import scala.annotation.tailrec
import scala.trace.Debug

/**
  * Created by johnreed on 4/12/16 for https://github.com/JohnReedLOL/scala-trace-debug
  */
protected[trace] object Printer {

  object Color {
    val escape: Char = 27.toChar
    val clear: String = escape + "[0m"
    val red: String = "[31m"
    val black: String = "[30m"

    /**
      * Uses ANSI color sequences to make the output yellow.
      */
    def red(str: String): String = {
      escape + red + str + clear
    }

    def black(str: String): String = {
      escape + black + str + clear
    }
  }

  private val mySystemProperty = "ENABLE_TRACE_DEBUG"

  /**
    * Disabling of traces, asserts, etc. can be specified through the system property
    * "ENABLE_TRACE_DEBUG" or the environment variable
    * "ENABLE_TRACE_DEBUG". The system property takes precedence over the
    * environment variable. See: https://github.com/adamw/scala-macro-debug
    */
  val isDebugDisabled : Boolean = {

    // Holds true or false if debugging has been enabled or disabled through
    // a system property
    val systemProperty = {
      val tmp = System.getProperty(mySystemProperty)
      if (tmp == null) null
      else {
        tmp.trim().toLowerCase() match {
          case "true" => true
          case "false" => false
          case _ => null
        }
      }
    }

    // Holds true or false if debugging has been enabled or disabled through
    // an environment variable
    val environmentProperty = {
      val tmp = System.getenv(mySystemProperty)
      if (tmp == null) null
      else {
        tmp.trim().toLowerCase() match {
          case "true" => true
          case "false" => false
          case _ => null
        }
      }
    }

    // The resulting value
    systemProperty match {
      case b: Boolean => !b
      case _ =>
        environmentProperty match {
          case b: Boolean => !b
          case _ => false
        }
    }
  }

  /** The offset of the first line from the base of the stack trace
    * The +1 is necessary because the method call traceInternal adds one to the offset of the stack trace
    */
  protected[trace] val newStackOffset = Debug.stackOffset + 1

  /** Prints out the object with N lines of stack trace. Do not use with assertions
    *
    * @param toPrintOutNullable    the object to print out. May be "null"
    * @param numStackLinesIntended N, the number of lines of stack trace intended. Defaults to zero actual lines of stack trace for negative values
    * @param usingStdOut           Whether to use standard out for trace (as opposed to std error). Uses standard error by default
    * @return The string that would have been printed out if printing were enabled and the string that was printed out because printing was enabled.
    */
  protected[trace] final def traceInternal[A](toPrintOutNullable: A, numStackLinesIntended: Int,
                                              usingStdOut: Boolean = false): String = {
    if(isDebugDisabled) {
      return ""
    }

    val toPrintOut: String = if (toPrintOutNullable == null) {
      "null"
    } else {
      toPrintOutNullable.toString
    }
    var toPrint = "\n" + "\"" + toPrintOut + "\"" + " in thread " + Thread.currentThread().getName + ":"
    if (numStackLinesIntended > 0) {
      val stack = Thread.currentThread().getStackTrace
      for (row <- 0 to Math.min(numStackLinesIntended - 1, stack.length - 1 - newStackOffset)) {
        val lineNumber = newStackOffset + row
        val stackLine: StackTraceElement = stack(lineNumber)
        val packageName = getPackageName(stackLine)
        val myPackageName = if(packageName.equals("")) {""} else {" [" + packageName + "]"}
        // The java stack traces use a tab character, not a space
        val tab = "\t"
        toPrint += "\n" + tab + "at " + stackLine + myPackageName
      }
    } else {
      // do not make a call to Thread.currentThread().getStackTrace
    }
    // toPrint += "\n"
    if (!usingStdOut && !Debug.isTraceErrOn) {
      return toPrint // if we are using standard error and tracing to standard error is off, return
    }
    if (usingStdOut && !Debug.isTraceOutOn) {
      return toPrint // if we are using standard out and tracing to standard out is off, return
    }

    if (usingStdOut) {
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

  /**
    * Gets the package name
    */
  protected[internal] def getPackageName(stackLine: StackTraceElement): String = {
    try {
      val className = Class.forName(stackLine.getClassName)
      val stringLocation = if(className != null) {
        val packageName = PackagingDataCalculator.getCodeLocation(className)
        if(packageName.endsWith(".jar") ) {
          packageName
        } else {
          ""
        }
      } else {
        ""
      }
      stringLocation
    } catch {
      case _:java.lang.Exception => { "" }
    }
  }

  protected[trace] final def internalAssert[A](toPrintOutNullable: A, numStackLinesIntended: Int
    , usingStdOut: Boolean = false, isAssertTrue : Boolean, isFatal : Boolean): String = {
    if (isDebugDisabled || isAssertTrue ) {
      // Intentional decision to break style rules by using an explicit "return".
      return "" // If assertion is true, print nothing and return empty string.
    }
    val toPrintOut: String = if (toPrintOutNullable == null) {
      "null"
    } else {
      toPrintOutNullable.toString
    }
    val toPrintRaw: String = {
      val firstLine = "\n" + "\"" + toPrintOut + "\"" + " in thread " + Thread.currentThread().getName + ":"
      if (numStackLinesIntended <= 0) {
        firstLine + "\n" + "^ An assertion failure has occured. ^"
      } else {
        val stackTrace = Thread.currentThread().getStackTrace
        val stackTraceLines = {
          var traced = ""
          val numRows = Math.min(numStackLinesIntended - 1, stackTrace.length - 1 - newStackOffset)
          for (row <- 0 until numRows) {
            val stackRowNumber = newStackOffset + row
            val stackTraceRow = stackTrace(stackRowNumber)
            val packageNameRaw = getPackageName(stackTraceRow)
            val packageName = if(packageNameRaw.equals("")) {""} else {" [" + packageNameRaw + "]"}
            val indent = "\t" // Java stack traces uses a tab character for indentation
            traced = traced + "\n" + indent + "at " + stackTraceRow + packageName
          }
          traced
        }
        val lastLine = "\n" + "^ The above stack trace leads to an assertion failure. ^"
        firstLine + stackTraceLines + lastLine
      }
    }
    val toPrint = Color.red(toPrintRaw)
    if ( (!isFatal && !Debug.isNonFatalAssertOn) || (isFatal && !Debug.isFatalAssertOn) ) {
      toPrint // If it is nonfatal and nonFatalAssert is off, return the string without printing (so that the logger can print it)
    } else {
      if (usingStdOut) {
        PrintLocker.synchronized {
          System.out.println(toPrint)
        }
      } else {
        PrintLocker.synchronized {
          System.err.println(toPrint)
        }
      }
      if (isFatal && Debug.isFatalAssertOn) {
        System.exit(7) // if the assertion is fatal and fatal assert is on, exit with system code 7
      }
      toPrint
    }
  }

  /**
    * Ensures that no two threads can print at the same time
    */
  private object PrintLocker

}

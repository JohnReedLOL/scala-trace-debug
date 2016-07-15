package scala.trace.internal

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
  val debugDisabled_? : Boolean = {

    // Holds true or false if debugging has been enabled or disabled through
    // a system property
    val systemProperty: Any = {
      val tmp: String = System.getProperty(mySystemProperty)
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
    val environmentProperty: Any = {
      val tmp: String = System.getenv(mySystemProperty)
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
  protected[trace] val newStackOffset: Int = Debug.stackOffset + 1

  /** Prints out the object with N lines of stack trace. Do not use with assertions
    *
    * @param toPrintOutNullable    the object to print out. May be "null"
    * @param numStackLinesIntended N, the number of lines of stack trace intended. Defaults to zero actual lines of stack trace for negative values
    * @param usingStdOut           Whether to use standard out for trace (as opposed to std error). Uses standard error by default
    * @return The string that would have been printed out if printing were enabled and the string that was printed out because printing was enabled.
    */
  protected[trace] final def traceInternal[A](toPrintOutNullable: A, numStackLinesIntended: Int
                                              , usingStdOut: Boolean = false): String = {
    if (debugDisabled_?) {
      return ""
    }
    val toPrintOut: String = if (toPrintOutNullable == null) {
      "null"
    } else {
      toPrintOutNullable.toString
    }
    var toPrint: String = "\n" + "\"" + toPrintOut + "\"" + " in thread " + Thread.currentThread().getName + ":"
    if (numStackLinesIntended > 0) {
      val stack: Array[StackTraceElement] = Thread.currentThread().getStackTrace
      for (row <- 0 to Math.min(numStackLinesIntended - 1, stack.length - 1 - newStackOffset)) {
        val lineNumber: Int = newStackOffset + row
        val stackLine: StackTraceElement = stack(lineNumber)
        val packageName: String = getPackageName(stackLine)
        val myPackageName: String = if (packageName.equals("")) {
          ""
        } else {
          " [" + packageName + "]"
        }
        // The java stack traces use a tab character, not a space
        val tab = "\t"
        toPrint += "\n" + tab + "at " + stackLine + myPackageName
      }
    }
    if ((!usingStdOut && !Debug.isTraceErrOn) || (usingStdOut && !Debug.isTraceOutOn)) {
      // if we are using standard error and tracing to standard error is off, don't print anything
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
    }
    toPrint
  }

  /**
    * Gets the package name
    */
  protected[internal] def getPackageName(stackLine: StackTraceElement): String = {
    try {
      val className: Class[_] = Class.forName(stackLine.getClassName)
      val stringLocation: String = if (className != null) {
        val packageName: String = PackagingDataCalculator.getCodeLocation(className)
        if (packageName.endsWith(".jar")) {
          packageName
        } else {
          ""
        }
      } else {
        ""
      }
      stringLocation
    } catch {
      case _: java.lang.Exception => {
        ""
      }
    }
  }

  /** Prints out the object with N lines of stack trace. Meant to be used only for asserts
    *
    * @param toPrintOutNullable    the object to print out. May be "null"
    * @param numStackLinesIntended N, the number of lines of stack trace intended. Defaults to zero actual lines of stack trace for negative values
    * @param usingStdOut           Whether to use standard out for trace (as opposed to std error). Uses standard error by default
    * @return The string that would have been printed out if printing were enabled and the string that was printed out because printing was enabled.
    */
  protected[trace] final def internalAssert[A](toPrintOutNullable: A, numStackLinesIntended: Int
                                               , usingStdOut: Boolean = false, assertionTrue_? : Boolean, isFatal_? : Boolean): String = {
    if (debugDisabled_? || assertionTrue_?) {
      return "" // If assertion is true, print nothing and return empty string.
    }
    val toPrintOut: String = if (toPrintOutNullable == null) {
      "null"
    } else {
      toPrintOutNullable.toString // calling toString on null is bad
    }
    var toPrint: String = "\n" + "\"" + toPrintOut + "\"" + " in thread " + Thread.currentThread().getName + ":"
    if (numStackLinesIntended > 0) {
      // Only make call to Thread.currentThread().getStackTrace if there is a stack to print
      val stack: Array[StackTraceElement] = Thread.currentThread().getStackTrace
      for (row <- 0 to Math.min(numStackLinesIntended - 1, stack.length - 1 - newStackOffset)) {
        val lineNumber: Int = newStackOffset + row
        val stackLine: StackTraceElement = stack(lineNumber)
        val packageName: String = getPackageName(stackLine)
        val myPackageName: String = if (packageName.equals("")) {
          ""
        } else {
          " [" + packageName + "]"
        }
        // The java stack traces use a tab character, not a space
        val tab = "\t"
        toPrint += "\n" + tab + "at " + stackLine + myPackageName
      }
      toPrint += "\n" + "^ The above stack trace leads to an assertion failure. ^"
    } else {
      toPrint += "\n" + "^ An assertion failure has occured. ^"
    }
    toPrint = Color.red(toPrint)
    if ((!isFatal_? && !Debug.isNonFatalAssertOn) || (isFatal_? && !Debug.isFatalAssertOn)) {
      // If it is nonfatal and nonFatalAssert is off, don't print anything
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
      if (isFatal_? && Debug.isFatalAssertOn) {
        System.exit(7) // if the assertion is fatal and fatal assert is on, exit with system code 7
      }
    }
    toPrint
  }

  /**
    * Ensures that no two threads can print at the same time
    */
  private object PrintLocker

}

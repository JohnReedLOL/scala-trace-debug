package info.collaboration_station.debug

import scala.language.experimental.macros // There will be macros
import scala.reflect.macros.blackbox.Context

/**
  * Created by johnreed on 3/12/16
  */
object Debug {

  /**
    * Stack offset is 2 because the first row in the stack trace is Thread and the second row is internal call
    */
  protected[debug] val stackOffset = 2

  @volatile private var _traceOutOn_? = true

  /** Tells you whether tracing to standard out is on or off
    * Note that disabling the "traceStdOut" feature does not disable the "assertStdOut" feature
    */
  def traceOutOn_? = _traceOutOn_?

  @volatile private var _traceErrOn_? = true

  /** Tells you whether tracing to standard error is on or off
    * Note that disabling the "trace" feature does not disable the "assert" feature
    */
  def traceErrOn_? = _traceErrOn_?

  @volatile private var _fatalAssertOn_? = true

  /**
    * Tells you whether fatal asserts are on or off
    */
  def fatalAssertOn_? = _fatalAssertOn_?

  @volatile private var _nonFatalAssertOn_? = true

  /**
    * Tells you whether non-fatal asserts are on or off
    */
  def nonFatalAssertOn_? = _nonFatalAssertOn_?

  // these lines disable and enable particular features

  /**
    * Enables tracing to standard error. Has no effect on "print" or "println", only on "trace" methods
    */
  def traceErrOn_!() = {
    _traceErrOn_? = true
  }

  /**
    * Disables tracing to standard error. Has no effect on "print" or "println", only on "trace" methods
    */
  def traceErrOff_!() = {
    _traceErrOn_? = false
  }

  /**
    * Enables tracing to standard out. Has no effect on "print" or "println", only on "traceStdOut" methods
    */
  def traceOutOn_!() = {
    _traceOutOn_? = true
  }

  /**
    * Disables tracing to standard out. Has no effect on "print" or "println", only on "traceStdOut" methods
    */
  def traceOutOff_!() = {
    _traceOutOn_? = false
  }

  /**
    * Enables fatal assertions. Has no effect on "assertNonFatal", only on "assert" and other fatal assert methods (assertEquals, etc.)
    */
  def fatalAssertOn_!() = {
    _fatalAssertOn_? = true
  }

  /**
    * Disables fatal assertions. Has no effect on "assertNonFatal", only on "assert" and other fatal assert methods (assertEquals, etc.)
    */
  def fatalAssertOff_!() = {
    _fatalAssertOn_? = false
  }

  /**
    * Enables non-fatal assertions. Has no effect on "assert" and other fatal assert methods (assertEquals, etc.)
    */
  def nonFatalAssertOn_!() = {
    _nonFatalAssertOn_? = true
  }

  /**
    * Disables non-fatal assertions. Has no effect on "assert" and other fatal assert methods (assertEquals, etc.)
    */
  def nonFatalAssertOff_!() = {
    _nonFatalAssertOn_? = false
  }

  /**
    * Enables tracing and asserts, including fatal assertions
    */
  def enableEverything_!() = {
    traceErrOn_!()
    traceOutOn_!()
    fatalAssertOn_!()
    nonFatalAssertOn_!()
  }

  /**
    * Disables tracing and asserts. Both fatal and non-fatal assertions are disabled. Does not disable print or println
    */
  def disableEverything_!() = {
    traceErrOff_!()
    traceOutOff_!()
    fatalAssertOff_!()
    nonFatalAssertOff_!()
  }

  final def trace[T](block: => T): Unit = ImplicitTraceObject.traceInternal(block.toString, 1)

  final def traceExpression[T](block: => T): Unit = macro traceExpressionImpl

  final def traceExpressionImpl(c: Context)(block: c.Tree): c.Tree = {
    import c.universe._
    val toTraceString = showCode(block) + " -> "
    val arg1 = q"$toTraceString + ({$block}.toString)"
    val args = List(arg1)
    val toReturn = q"""
        info.collaboration_station.debug.Debug.trace(..$args);
    """
    toReturn
  }

  final def trace[T](block: => T, lines: Int): Unit = ImplicitTraceObject.traceInternal(block.toString, lines)

  final def traceExpression[T](block: => T, lines: Int): Unit = macro traceLinesExpressionImpl

  final def traceLinesExpressionImpl(c: Context)(block: c.Tree, lines: c.Tree): c.Tree = {
    import c.universe._
    val toTraceString = showCode(block) + " -> "
    val arg1 = q"$toTraceString + ({$block}.toString)"
    val arg2 = q"$lines"
    val args = List(arg1, arg2)
    val toReturn = q"""
        info.collaboration_station.debug.Debug.trace(..$args);
    """
    toReturn
  }

  final def traceStack[T](block: => T): Unit = ImplicitTraceObject.traceInternal(block.toString, Int.MaxValue)

  final def traceStackExpression[T](block: => T): Unit = macro traceStackExpressionImpl

  final def traceStackExpressionImpl(c: Context)(block: c.Tree): c.Tree = {
    import c.universe._
    val toTraceString = showCode(block) + " -> "
    val arg1 = q"$toTraceString + ({$block}.toString)"
    val args = List(arg1)
    val toReturn = q"""
        info.collaboration_station.debug.Debug.traceStack(..$args);
    """
    toReturn
  }

  final def traceStdOut[T](block: => T): Unit = ImplicitTraceObject.traceInternal(block.toString, 1, useStdOut_? = true)

  final def traceStdOutExpression[T](block: => T): Unit = macro traceStdOutExpressionImpl

  final def traceStdOutExpressionImpl(c: Context)(block: c.Tree): c.Tree = {
    import c.universe._
    val toTraceString = showCode(block) + " -> "
    val arg1 = q"$toTraceString + ({$block}.toString)"
    val args = List(arg1)
    val toReturn = q"""
        info.collaboration_station.debug.Debug.traceStdOut(..$args);
    """
    toReturn
  }

  final def traceStdOut[T](block: => T, lines: Int): Unit = ImplicitTraceObject.traceInternal(block.toString, lines, useStdOut_? = true)

  final def traceStdOutExpression[T](block: => T, lines: Int): Unit = macro traceLinesStdOutExpressionImpl

  final def traceLinesStdOutExpressionImpl(c: Context)(block: c.Tree, lines: c.Tree): c.Tree = {
    import c.universe._
    val toTraceString = showCode(block) + " -> "
    val arg1 = q"$toTraceString + ({$block}.toString)"
    val arg2 = q"$lines"
    val args = List(arg1, arg2)
    val toReturn = q"""
        info.collaboration_station.debug.Debug.traceStdOut(..$args);
    """
    toReturn
  }

  final def traceStackStdOut[T](block: => T): Unit = ImplicitTraceObject.traceInternal(block.toString, Int.MaxValue, useStdOut_? = true)

  final def traceStackStdOutExpression[T](block: => T): Unit = macro traceStackStdOutExpressionImpl

  final def traceStackStdOutExpressionImpl(c: Context)(block: c.Tree): c.Tree = {
    import c.universe._
    val toTraceString = showCode(block) + " -> "
    val arg1 = q"$toTraceString + ({$block}.toString)"
    val args = List(arg1)
    val toReturn = q"""
        info.collaboration_station.debug.Debug.traceStackStdOut(..$args);
    """
    toReturn
  }

  /** A fatal assertion.
    * Terminates the program with exit code "7"
    *
    * @param assertion the assertion that must be true for the program to run. Can be a value or a function
    * @param message   the message to be printed to standard error on assertion failure
    * @example Debug.assert( 1 + 2 == 4, "Error: one plus two is not equal to four" )
    * @note this (and other assertions not marked "nonFatal") are fatal. To disable, please call "Debug.fatalAssertOff_!()"
    */
  final def assert(assertion: => Boolean, message: String, maxLines: Int = Int.MaxValue): Unit = {
    if (!assertion && Debug.fatalAssertOn_?) {
      ImplicitTraceObject.traceInternalAssert(message, maxLines) // trace the max number of lines of stack trace to std error
      System.exit(7)
    }
  }

  // You can't pass in : =>Boolean without getting "java.lang.IllegalArgumentException: Could not find proxy for val myVal"
  // You also cannot use default parameters. Boo.

  final def assertInternal(assertion: Boolean, message: String): Unit = {
    if (!assertion && Debug.fatalAssertOn_?) {
      ImplicitTraceObject.traceInternalAssert(message, Int.MaxValue) // trace the max number of lines of stack trace to std error
      System.exit(7)
    }
  }

  final def assertExpression(assertion: Boolean): Unit = macro assertExpressionImpl

  final def assertExpressionImpl(c: Context)(assertion: c.Tree): c.Tree = {
    import c.universe._
    val assertionString = showCode(assertion) + " -> "
    val arg1 = q"$assertion"
    val arg2 = q"$assertionString + ({$assertion}.toString)"
    val args = List(arg1, arg2)
    val toReturn = q"""
        info.collaboration_station.debug.Debug.assertInternal(..$args);
    """
    toReturn
  }

  /** A fatal assertion.
    * Terminates the program with exit code "7"
    *
    * @param assertion the assertion that must be true for the program to run. Can be a value or a function
    * @param message   the message to be printed to standard out on assertion failure
    * @example Debug.assertStdOut( 1 + 2 == 4, "Error: one plus two is not equal to four" )
    * @note this (and other assertions not marked "nonFatal") are fatal. To disable, please call "Debug.fatalAssertOff_!()"
    */
  final def assertStdOut(assertion: => Boolean, message: String, maxLines: Int = Int.MaxValue): Unit = {
    if (!assertion && Debug.fatalAssertOn_?) {
      ImplicitTraceObject.traceInternalAssert(message, maxLines, useStdOut_? = true) // trace the max number of lines of stack trace to std out
      System.exit(7)
    }
  }

  /**
    * Like Debug.assert(), but does not terminate the application
    */
  final def assertNonFatal(assertion: => Boolean, message: String, maxLines: Int = Int.MaxValue): Unit = {
    if (!assertion && Debug.nonFatalAssertOn_?) {
      ImplicitTraceObject.traceInternalAssert(message, maxLines) // trace the max number of lines of stack trace to std error
    }
  }

  /**
    * Like Debug.assertStdOut(), but does not terminate the application
    */
  final def assertNonFatalStdOut(assertion: => Boolean, message: String, maxLines: Int = Int.MaxValue): Unit = {
    if (!assertion && Debug.nonFatalAssertOn_?) {
      ImplicitTraceObject.traceInternalAssert(message, maxLines, useStdOut_? = true) // trace the max number of lines of stack trace to std out
    }
  }
}
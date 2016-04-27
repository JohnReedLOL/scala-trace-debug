package scala.trace

import scala.trace.internal.Printer
import scala.trace.internal.Helpers.MacroHelperMethod
import scala.language.experimental.macros
import scala.language.existentials

/**
  * Created by johnreed on 3/12/16. Contains static debug functions. https://github.com/JohnReedLOL/scala-trace-debug
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
    * @return the number of elements per row for container printing
    */
  def getElementsPerRow(): Int = {
    elementsPerRow_
  }

  /**
    * Uses Java notation for Java users
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
  def trace[T](toPrint: T): String = Printer.traceInternal(toPrint.toString, 1)

  /**
    * Traces to standard error with a N line stack trace.
    *
    * @param toPrint whatever it is you want to print.
    * @param numLines the number of lines to trace
    * @return the string containing what was printed or what would have been printed if printing was enabled. You can pass this string into a logger.
    */
  def trace[T](toPrint: T, numLines: Int): String = Printer.traceInternal(toPrint.toString, numLines)

  /**
    * Traces to standard error with a full length stack trace.
    *
    * @param toPrint whatever it is you want to print.
    * @return the string containing what was printed or what would have been printed if printing was enabled. You can pass this string into a logger.
    */
  def traceStack[T](toPrint: T): String = Printer.traceInternal(toPrint.toString, Int.MaxValue)

  /**
    * Same as Debug.trace, but prints to standard out instead of standard error
    *
    * @return the string containing what was printed or what would have been printed if printing was enabled. You can pass this string into a logger.
    */
  def traceStdOut[T](toPrint: T): String = Printer.traceInternal(toPrint.toString, 1, usingStdOut = true)

  /**
    * Same as Debug.trace(toPrint: T, numLines: Int), but prints to standard out instead of standard error
    *
    * @return the string containing what was printed or what would have been printed if printing was enabled. You can pass this string into a logger.
    */
  def traceStdOut[T](toPrint: T, numLines: Int): String = Printer.traceInternal(toPrint.toString, numLines, usingStdOut = true)

  /**
    * Same as traceStack, but prints to StdOut instead of StdError
    *
    * @return the string containing what was printed or what would have been printed if printing was enabled. You can pass this string into a logger.
    */
  def traceStackStdOut[T](toPrint: T): String = Printer.traceInternal(toPrint.toString, Int.MaxValue, usingStdOut = true)

  /** A fatal assertion.
    * Terminates the program with exit code "7"
    * Note: "assert" is a reserved keyword in Java, use "assrt" instead.
    * @param assertion the assertion that must be true for the program to run.
    * @param message   the message to be printed to standard error on assertion failure
    * @example Debug.assrt( 1 + 2 == 4, "Error: one plus two is not equal to four" )
    * @note this (and other assertions not marked "nonFatal") are fatal. To disable, please call "Debug.fatalAssertOffSE()"
    * @return the string containing what was printed or what would have been printed if printing was enabled. You can pass this string into a logger.
    */
  def assrt(assertion: Boolean, message: String, numLines: Int = Int.MaxValue): String = {
    Printer.internalAssert(message, numLines, usingStdOut = false, assertionTrue_? = assertion, isFatal_? = true) // trace the max number of lines of stack trace to std error
  }

  /** A fatal assertion.
    * Terminates the program with exit code "7"
    *
    * @param assertion the assertion that must be true for the program to run.
    * @param message   the message to be printed to standard out on assertion failure
    * @example Debug.assrtStdOut( 1 + 2 == 4, "Error: one plus two is not equal to four" )
    * @note this (and other assertions not marked "nonFatal") are fatal. To disable, please call "Debug.fatalAssertOffSE()"
    * @return the string containing what was printed or what would have been printed if printing was enabled. You can pass this string into a logger.
    */
  def assertStdOut(assertion: Boolean, message: String, numLines: Int = Int.MaxValue): String = {
    Printer.internalAssert(message, numLines, usingStdOut = true, assertionTrue_? = assertion, isFatal_? = true)
  }

  /**
    * Like Debug.assrt(), but does not terminate the application
    *
    * @return the string containing what was printed or what would have been printed if printing was enabled. You can pass this string into a logger.
    */
  def check(assertion: Boolean, message: String, numLines: Int = Int.MaxValue): String = {
    Printer.internalAssert(message, numLines, usingStdOut = false, assertionTrue_? = assertion, isFatal_? = false)
  }

  /**
    * Like Debug.assrtStdOut(), but does not terminate the application
    *
    * @return the string containing what was printed or what would have been printed if printing was enabled. You can pass this string into a logger.
    */
  def checkStdOut(assertion: Boolean, message: String, numLines: Int = Int.MaxValue): String = {
    Printer.internalAssert(message, numLines, usingStdOut = true, assertionTrue_? = assertion, isFatal_? = false)
  }

  import scala.reflect.runtime.universe.WeakTypeTag

  /**
    * Gets the collection as a string of n elements from start to start + numElements
    */
  def getCollectionAsString(coll: collection.GenTraversableOnce[_], start: Int = 0, numElements: Int = Int.MaxValue): String = {
    var toPrint = ""
    val iterator = coll.toIterator
    var currentElement: Long = 0L // Long to prevent overflow
    val end = start.toLong + numElements.toLong  // Long to prevent overflow
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
    val startElement = currentElement
    // Now currentElement = start
    // then do real printing
    while(currentElement < end) {
      if (iterator.hasNext) {
        toPrint = toPrint + iterator.next() + ", "
        currentElement += 1L
        if( (currentElement - startElement) % Debug.getElementsPerRow() == 0) {
          toPrint += "\n" // newline every "numElementsPerRow" elements
        }
      } else {
        // get out of loop
        currentElement = Long.MaxValue
      }
    }
    toPrint = toPrint.trim // no trailing whitespace

    if (toPrint.length() > 0 && toPrint.charAt(toPrint.length()-1)==',') {
      toPrint = toPrint.substring(0, toPrint.length()-1) // remove trailing comma
    }

    " " + toPrint + "\n"
  }

  /**
    * Same as traceContents[CollectionType], but for Java Arrays (callable from Java Code)
    */
  def traceArray[T](coll: Array[T], start: Int = 0, numElements: Int = Int.MaxValue, numLines: Int = 1)
  : String = {
    val toPrint = getCollectionAsString(coll, start, numElements)
    Printer.traceInternal(toPrint, numStackLinesIntended = numLines, usingStdOut = false)
  }


  /**
    * Same as traceContentsStdOut[CollectionType], but for Java Arrays (callable from Java Code)
    */
  def traceArrayStdOut[T](coll: Array[T], start: Int = 0, numElements: Int = Int.MaxValue, numLines: Int = 1)
  : String = {
    val toPrint = getCollectionAsString(coll, start, numElements)
    Printer.traceInternal(toPrint, numStackLinesIntended = numLines, usingStdOut = true)
  }

  /**
    * Traces the contents of a Scala container to standard error. To convert a Java container into a Scala container, import collection.JavaConversions._
    * Note: The maximum number of elements it can print is 2 to the 32 elements.
    *
    * @param coll  the Scala collection. TraversableLike is a base trait of all kinds of Scala collections.
    * @param start the index of the first element to print. To work on all containers, index is counted using an iterator.
    * @param numElements the number of elements you want to trace. Defaults to all elements in the collection (or 2 to the 32 elements)
    * @param numLines    the number of lines of stack trace.
    * @return the string containing what was printed or what would have been printed if printing was enabled.
    */
  def traceContents[ContainedT](coll: collection.GenTraversableOnce[ContainedT], start: Int = 0, numElements: Int = Int.MaxValue, numLines: Int = 1)
  (implicit tag: WeakTypeTag[ContainedT]): String = {
    val collectionType = tag.tpe
    val toPrint = "Contains: " + collectionType.toString + getCollectionAsString(coll, start, numElements)
    Printer.traceInternal(toPrint, numStackLinesIntended = numLines, usingStdOut = false)
  }

  /**
    * Traces the contents of a Scala container to standard out. To convert a Java container into a Scala container, import collection.JavaConversions._
    * Note: The maximum number of elements it can print is 2 to the 32 elements.
    *
    * @param coll  the Scala collection. TraversableLike is a base trait of all kinds of Scala collections.
    * @param start the index of the first element to print. To work on all containers, index is counted using an iterator.
    * @param numElements the number of elements you want to trace. Defaults to all elements in the collection (or 2 to the 32 elements)
    * @param numLines    the number of lines of stack trace.
    * @return the string containing what was printed or what would have been printed if printing was enabled.
    */
  def traceContentsStdOut[ContainedT](coll: collection.GenTraversableOnce[ContainedT], start: Int = 0, numElements: Int = Int.MaxValue, numLines: Int = 1)
  (implicit tag: WeakTypeTag[ContainedT]): String = {
    val collectionType = tag.tpe
    val toPrint = "Contains: " + collectionType.toString + getCollectionAsString(coll, start, numElements)
    Printer.traceInternal(toPrint, numStackLinesIntended = numLines, usingStdOut = true)
  }

  /**
    * Same as trace, but prints the code in the block, not just the result
    *
    * @example myVal = 3; Debug.traceCode{1 + 2 + myVal}
    * @example myVal = 3; Debug.traceCode(1 + 2 + myVal}, 3) // 3 lines of stack trace
    * @return the string containing what was printed or what would have been printed if printing was enabled. You can pass this string into a logger.
    */
  object traceCode {
    def traceCodeImpl[T](c: Compat.Context)(toPrint: c.Expr[T]): c.Expr[String] = {
      import c.universe._
      val blockString = (new MacroHelperMethod[c.type](c)).getSourceCode(toPrint.tree)
      val arg1 = q""" "(" + $blockString + ") -> " + ({$toPrint}.toString) """
      val args = List(arg1)
      val toReturn =
        q"""
        _root_.scala.trace.Debug.trace(..$args);
    """
      c.Expr[String](toReturn)
    }

    def traceLinesCodeImpl[T](c: Compat.Context)(toPrint: c.Expr[T], numLines: c.Expr[Int]): c.Expr[String] = {
      import c.universe._
      val blockString = (new MacroHelperMethod[c.type](c)).getSourceCode(toPrint.tree)
      val arg1 = q""" "(" + $blockString + ") -> " + ({$toPrint}.toString) """
      val arg2 = q"$numLines"
      val args = List(arg1, arg2)
      val toReturn =
        q"""
        _root_.scala.trace.Debug.trace(..$args);
    """
      c.Expr[String](toReturn)
    }

    def apply[T](toPrint: T): String = macro traceCodeImpl[T]

    def apply[T](toPrint: T, numLines: Int): String = macro traceLinesCodeImpl[T]
  }

  // You can't pass in : =>Boolean without getting "java.lang.IllegalArgumentException: Could not find proxy for val myVal"
  // You also cannot use default parameters. Boo.

  /**
    * Same as traceStack, but prints the source code in the block, not just the result
    *
    * @example myVal = 3; Debug.traceStackCode{1 + 2 + myVal}
    * @return the string containing what was printed or what would have been printed if printing was enabled. You can pass this string into a logger.
    */
  object traceStackCode {
    def traceStackCodeImpl[T](c: Compat.Context)(toPrint: c.Expr[T]): c.Expr[String] = {
      import c.universe._
      val blockString = (new MacroHelperMethod[c.type](c)).getSourceCode(toPrint.tree)
      val arg1 = q""" "(" + $blockString + ") -> " + ({$toPrint}.toString) """
      val args = List(arg1)
      val toReturn =
        q"""
        _root_.scala.trace.Debug.traceStack(..$args);
    """
      c.Expr[String](toReturn)
    }

    def apply[T](toPrint: T): String = macro traceStackCodeImpl[T]
  }

  /**
    * Same as trace, but prints the entire expression, not just the result
    *
    * @example Debug.traceExpression{val myVal = 3; 1 + 2 + myVal}
    * @example Debug.traceExpression({val myVal = 3; 1 + 2 + myVal}, 3) // 3 lines of stack trace
    * @return the string containing what was printed or what would have been printed if printing was enabled. You can pass this string into a logger.
    */
  object traceExpression {
    def traceExpressionImpl[T](c: Compat.Context)(toPrint: c.Expr[T]): c.Expr[String] = {
      import c.universe._
      val toTraceString = (toPrint.tree).toString + " -> "
      val arg1 = q"$toTraceString + ({$toPrint}.toString)"
      val args = List(arg1)
      val toReturn =
        q"""
        _root_.scala.trace.Debug.trace(..$args);
    """
      c.Expr[String](toReturn)
    }

    def traceLinesExpressionImpl[T](c: Compat.Context)(toPrint: c.Expr[T], numLines: c.Expr[Int]): c.Expr[String] = {
      import c.universe._
      val toTraceString = (toPrint.tree).toString + " -> "
      val arg1 = q"$toTraceString + ({$toPrint}.toString)"
      val arg2 = q"$numLines"
      val args = List(arg1, arg2)
      val toReturn =
        q"""
        _root_.scala.trace.Debug.trace(..$args);
    """
      c.Expr[String](toReturn)
    }

    def apply[T](toPrint: T): String = macro traceExpressionImpl[T]

    def apply[T](toPrint: T, numLines: Int): String = macro traceLinesExpressionImpl[T]
  }

  /**
    * Same as traceStack, but prints the entire expression, not just the result
    *
    * @example Debug.traceStackExpression{val myVal = 3; 1 + 2 + myVal}
    * @return the string containing what was printed or what would have been printed if printing was enabled. You can pass this string into a logger.
    */
  object traceStackExpression {
    def traceStackExpressionImpl[T](c: Compat.Context)(toPrint: c.Expr[T]): c.Expr[String] = {
      import c.universe._
      val toTraceString = (toPrint.tree).toString + " -> "
      val arg1 = q"$toTraceString + ({$toPrint}.toString)"
      val args = List(arg1)
      val toReturn =
        q"""
        _root_.scala.trace.Debug.traceStack(..$args);
    """
      c.Expr[String](toReturn)
    }

    def apply[T](toPrint: T): String = macro traceStackExpressionImpl[T]
  }

  /**
    * Same as Debug.traceStdOut, but prints the whole expression not just its result
    *
    * @example Debug.traceStdOutExpression{val myVal = 3; 1 + 2 + myVal}
    * @example Debug.traceStdOutExpression({val myVal = 3; 1 + 2 + myVal}, 3) // 3 lines of stack trace
    * @return the string containing what was printed or what would have been printed if printing was enabled. You can pass this string into a logger.
    */
  object traceStdOutExpression {

    def traceStdOutExpressionImpl[T](c: Compat.Context)(toPrint: c.Expr[T]): c.Expr[String] = {
      import c.universe._
      val toTraceString = (toPrint.tree).toString + " -> "
      val arg1 = q"$toTraceString + ({$toPrint}.toString)"
      val args = List(arg1)
      val toReturn =
        q"""
        _root_.scala.trace.Debug.traceStdOut(..$args);
    """
      c.Expr[String](toReturn)
    }

    def traceLinesStdOutExpressionImpl[T](c: Compat.Context)(toPrint: c.Expr[T], numLines: c.Expr[Int]): c.Expr[String] = {
      import c.universe._
      val toTraceString = (toPrint.tree).toString + " -> "
      val arg1 = q"$toTraceString + ({$toPrint}.toString)"
      val arg2 = q"$numLines"
      val args = List(arg1, arg2)
      val toReturn =
        q"""
        _root_.scala.trace.Debug.traceStdOut(..$args);
    """
      c.Expr[String](toReturn)
    }

    def apply[T](toPrint: T): String = macro traceStdOutExpressionImpl[T]

    def apply[T](toPrint: T, numLines: Int): String = macro traceLinesStdOutExpressionImpl[T]
  }

  /**
    * Same as traceStackStdOut, but prints the whole expression not just the result
    *
    * @example Debug.traceStackStdOutExpression{val myVal = 3; 1 + 2 + myVal}
    * @return the string containing what was printed or what would have been printed if printing was enabled. You can pass this string into a logger.
    */
  object traceStackStdOutExpression {
    def traceStackStdOutExpressionImpl[T](c: Compat.Context)(toPrint: c.Expr[T]): c.Expr[String] = {
      import c.universe._
      val toTraceString = (toPrint.tree).toString + " -> "
      val arg1 = q"$toTraceString + ({$toPrint}.toString)"
      val args = List(arg1)
      val toReturn =
        q"""
        _root_.scala.trace.Debug.traceStackStdOut(..$args);
    """
      c.Expr[String](toReturn)
    }

    def apply[T](toPrint: T): String = macro traceStackStdOutExpressionImpl[T]
  }

  /**
    * Same as assert, but prints the whole expression instead of an error message
    *
    * @example Debug.assrtExpression{val one = 1; one + 1 == 2}
    * @example Debug.assrtExpression({val one = 1; one + 1 == 2}, 0) // 0 lines of stack trace
    * @return the string containing what was printed or what would have been printed if printing was enabled. You can pass this string into a logger.
    */
  object assertExpression {
    def apply(assertion: Boolean): String = macro assertExpressionImpl

    def assertExpressionImpl(c: Compat.Context)(assertion: c.Expr[Boolean]): c.Expr[String] = {
      import c.universe._
      val assertionString = (assertion.tree).toString + " -> "
      //val arg1 = q"$assertion"
      val arg2 = q"$assertionString + ({$assertion}.toString)"
      val arg3 = q"Int.MaxValue"
      val args = List(arg2, arg3)
      val toReturn =
        q"""
        val assertBoolean = $assertion;
        _root_.scala.trace.Debug.assrt(assertBoolean, ..$args);
    """
      c.Expr[String](toReturn)
    }

    def apply(assertion: Boolean, numLines: Int): String = macro assertLinesExpressionImpl

    def assertLinesExpressionImpl(c: Compat.Context)(assertion: c.Expr[Boolean], numLines: c.Expr[Int]): c.Expr[String] = {
      import c.universe._
      val assertionString = (assertion.tree).toString + " -> "
      //val arg1 = q"$assertion"
      val arg2 = q"$assertionString + ({$assertion}.toString)"
      val arg3 = q"$numLines"
      val args = List(arg2, arg3)
      val toReturn =
        q"""
        val assertBoolean = $assertion;
        _root_.scala.trace.Debug.assrt(assertBoolean, ..$args);
    """
      c.Expr[String](toReturn)
    }
  }

  /**
    * Same as assert, but prints the code instead of an error message.
    *
    * @example val one = 1; Debug.assrtCode{one + 1 == 2}
    * @example val one = 1; Debug.assrtCode({one + 1 == 2}, 0) // 0 lines of stack trace
    * @return the string containing what was printed or what would have been printed if printing was enabled. You can pass this string into a logger.
    */
  object assertCode {
    def apply(assertion: Boolean): String = macro assertCodeImpl

    def assertCodeImpl(c: Compat.Context)(assertion: c.Expr[Boolean]): c.Expr[String] = {
      import c.universe._
      val sourceCode: c.Tree = (new MacroHelperMethod[c.type](c)).getSourceCode(assertion.tree)
      val arg2 = q""" "(" + $sourceCode + ") -> " + ({$assertion}.toString) """
      val arg3 = q"Int.MaxValue"
      val args = List(arg2, arg3)
      val toReturn =
        q"""
        val assertBoolean = $assertion;
        _root_.scala.trace.Debug.assrt(assertBoolean, ..$args);
    """
      c.Expr[String](toReturn)
    }

    def apply(assertion: Boolean, numLines: Int): String = macro assertLinesCodeImpl

    def assertLinesCodeImpl(c: Compat.Context)(assertion: c.Expr[Boolean], numLines: c.Expr[Int]): c.Expr[String] = {
      import c.universe._
      val sourceCode: c.Tree = (new MacroHelperMethod[c.type](c)).getSourceCode(assertion.tree)
      val arg2 = q""" "(" + $sourceCode + ") -> " + ({$assertion}.toString) """
      val arg3 = q"$numLines"
      val args = List(arg2, arg3)
      val toReturn =
        q"""
        val assertBoolean = $assertion;
        _root_.scala.trace.Debug.assrt(assertBoolean, ..$args);
    """
      c.Expr[String](toReturn)
    }
  }

  import scala.collection.TraversableLike
  import scala.reflect.runtime.universe._

  // for WeakTypeTag

  /**
    * Same as check, but prints the whole expression instead of an error message
    *
    * @example Debug.checkExpression{val one = 1; one + 1 == 2}
    * @example Debug.checkExpression({val one = 1; one + 1 == 2}, 0) // 0 lines of stack trace
    * @return the string containing what was printed or what would have been printed if printing was enabled. You can pass this string into a logger.
    */
  object checkExpression {

    def apply(assertion: Boolean): String = macro checkExpressionImpl

    def checkExpressionImpl(c: Compat.Context)(assertion: c.Expr[Boolean]): c.Expr[String] = {
      import c.universe._
      val assertionString = (assertion.tree).toString + " -> "
      //val arg1 = q"$assertion"
      val arg2 = q"$assertionString + ({$assertion}.toString)"
      val arg3 = q"Int.MaxValue"
      val args = List(arg2, arg3)
      val toReturn =
        q"""
        val assertBoolean = $assertion;
        _root_.scala.trace.Debug.check(assertBoolean, ..$args);
    """
      c.Expr[String](toReturn)
    }

    def apply(assertion: Boolean, numLines: Int): String = macro checkLinesNonFatalExpressionImpl

    def checkLinesNonFatalExpressionImpl(c: Compat.Context)(assertion: c.Expr[Boolean], numLines: c.Expr[Int]): c.Expr[String] = {
      import c.universe._
      val assertionString = (assertion.tree).toString + " -> "
      //val arg1 = q"$assertion"
      val arg2 = q"$assertionString + ({$assertion}.toString)"
      val arg3 = q"$numLines"
      val args = List(arg2, arg3)
      val toReturn =
        q"""
        val assertBoolean = $assertion;
        _root_.scala.trace.Debug.check(assertBoolean, ..$args);
    """
      c.Expr[String](toReturn)
    }
  }

  /**
    * Same as check, but prints the code instead of an error message.
    *
    * @example val one = 1; Debug.checkCode{one + 1 == 2}
    * @example val one = 1; Debug.checkCode({one + 1 == 2}, 0) // 0 lines of stack trace
    * @return the string containing what was printed or what would have been printed if printing was enabled. You can pass this string into a logger.
    */
  object checkCode {
    def apply(assertion: Boolean): String = macro checkCodeImpl

    def checkCodeImpl(c: Compat.Context)(assertion: c.Expr[Boolean]): c.Expr[String] = {
      import c.universe._
      val sourceCode: c.Tree = (new MacroHelperMethod[c.type](c)).getSourceCode(assertion.tree)
      val arg2 = q""" "(" + $sourceCode + ") -> " + ({$assertion}.toString) """
      val arg3 = q"Int.MaxValue"
      val args = List(arg2, arg3)
      val toReturn =
        q"""
        val assertBoolean = $assertion;
        _root_.scala.trace.Debug.check(assertBoolean, ..$args);
    """
      c.Expr[String](toReturn)
    }

    def apply(assertion: Boolean, numLines: Int): String = macro checkLinesCodeImpl

    def checkLinesCodeImpl(c: Compat.Context)(assertion: c.Expr[Boolean], numLines: c.Expr[Int]): c.Expr[String] = {
      import c.universe._
      val sourceCode: c.Tree = (new MacroHelperMethod[c.type](c)).getSourceCode(assertion.tree)
      val arg2 = q""" "(" + $sourceCode + ") -> " + ({$assertion}.toString) """
      val arg3 = q"$numLines"
      val args = List(arg2, arg3)
      val toReturn =
        q"""
        val assertBoolean = $assertion;
        _root_.scala.trace.Debug.check(assertBoolean, ..$args);
    """
      c.Expr[String](toReturn)
    }
  }

}

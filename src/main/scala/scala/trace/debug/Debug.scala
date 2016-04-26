package scala.trace.debug


import scala.trace.debug.Helpers.MacroHelperMethod

import scala.language.experimental.macros
import scala.trace.debug.internal.Printer

/**
  * Created by johnreed on 3/12/16. Contains static debug functions. https://github.com/JohnReedLOL/scala-trace-debug
  */
object Debug {

  /**
    * Stack offset is 2 because the first row in the stack trace is Thread and the second row is internal call
    */
  protected[debug] val stackOffset = 2

  @volatile private var isTraceOutOn_ = true
  @volatile private var isTraceErrOn_ = true
  @volatile private var isFatalAssertOn_ = true
  @volatile private var isNonFatalAssertOn_ = true

  /** Tells you whether tracing to standard out is on or off
    * Note that disabling the "traceStdOut" feature does not disable the "assertStdOut" feature
    */
  def isTraceOutOn = isTraceOutOn_

  /** Tells you whether tracing to standard error is on or off
    * Note that disabling the "trace" feature does not disable the "assert" feature
    */
  def isTraceErrOn = isTraceErrOn_

  /**
    * Tells you whether fatal asserts are on or off
    */
  def isFatalAssertOn = isFatalAssertOn_

  /**
    * Tells you whether non-fatal asserts are on or off
    */
  def isNonFatalAssertOn = isNonFatalAssertOn_

  // these lines disable and enable particular features

  /**
    * Enables tracing and asserts, including fatal assertions.
    * SE stands for "Side Effecting"
    */
  def enableEverythingSE() = {
    traceErrOnSE()
    traceOutOnSE()
    fatalAssertOnSE()
    nonFatalAssertOnSE()
  }

  /**
    * Enables tracing to standard error. Has no effect on "print" or "println", only on "trace" methods
    * SE stands for "Side Effecting"
    */
  def traceErrOnSE() = {
    isTraceErrOn_ = true
  }

  /**
    * Enables tracing to standard out. Has no effect on "print" or "println", only on "traceStdOut" methods
    * SE stands for "Side Effecting"
    */
  def traceOutOnSE() = {
    isTraceOutOn_ = true
  }

  /**
    * Enables fatal assertions. Has no effect on "check", only on "assert" and other fatal assert methods (assertEquals, etc.)
    * SE stands for "Side Effecting"
    */
  def fatalAssertOnSE() = {
    isFatalAssertOn_ = true
  }

  /**
    * Enables non-fatal assertions. Has no effect on "assert" and other fatal assert methods (assertEquals, etc.)
    * SE stands for "Side Effecting"
    */
  def nonFatalAssertOnSE() = {
    isNonFatalAssertOn_ = true
  }

  /**
    * Disables tracing and asserts. Both fatal and non-fatal assertions are disabled. Does not disable print or println
    * SE stands for "Side Effecting"
    */
  def disableEverythingSE() = {
    traceErrOffSE()
    traceOutOffSE()
    fatalAssertOffSE()
    nonFatalAssertOffSE()
  }

  /**
    * Disables tracing to standard error. Has no effect on "print" or "println", only on "trace" methods
    * SE stands for "Side Effecting"
    */
  def traceErrOffSE() = {
    isTraceErrOn_ = false
  }

  /**
    * Disables tracing to standard out. Has no effect on "print" or "println", only on "traceStdOut" methods
    * SE stands for "Side Effecting"
    */
  def traceOutOffSE() = {
    isTraceOutOn_ = false
  }

  /**
    * Disables fatal assertions. Has no effect on "check", only on "assert" and other fatal assert methods (assertEquals, etc.)
    * SE stands for "Side Effecting"
    */
  def fatalAssertOffSE() = {
    isFatalAssertOn_ = false
  }

  /**
    * Disables non-fatal assertions. Has no effect on "assert" and other fatal assert methods (assertEquals, etc.)
    * SE stands for "Side Effecting"
    */
  def nonFatalAssertOffSE() = {
    isNonFatalAssertOn_ = false
  }

  /**
    * Traces to standard error with a one line stack trace.
    *
    * @param block this block contains or returns whatever it is to be traced.
    * @tparam T the return type of the block
    * @return the string containing what was printed or what would have been printed if printing was enabled. You can pass this string into a logger.
    */
  def trace[T](block: => T): String = Printer.traceInternal(block.toString, 1)

  /**
    * Traces to standard error with a N line stack trace.
    *
    * @param block    this block contains or returns whatever it is to be traced.
    * @param numLines the number of lines to trace
    * @tparam T the return type of the block
    * @return the string containing what was printed or what would have been printed if printing was enabled. You can pass this string into a logger.
    */
  def trace[T](block: => T, numLines: Int): String = Printer.traceInternal(block.toString, numLines)

  /**
    * Traces to standard error with a full length stack trace.
    *
    * @param block this block contains or returns whatever it is to be traced.
    * @tparam T the return type of the block
    * @return the string containing what was printed or what would have been printed if printing was enabled. You can pass this string into a logger.
    */
  def traceStack[T](block: => T): String = Printer.traceInternal(block.toString, Int.MaxValue)

  /**
    * Same as Debug.trace, but prints to standard out instead of standard error
    *
    * @return the string containing what was printed or what would have been printed if printing was enabled. You can pass this string into a logger.
    */
  def traceStdOut[T](block: => T): String = Printer.traceInternal(block.toString, 1, usingStdOut = true)

  /**
    * Same as Debug.trace(block: => T, numLines: Int), but prints to standard out instead of standard error
    *
    * @return the string containing what was printed or what would have been printed if printing was enabled. You can pass this string into a logger.
    */
  def traceStdOut[T](block: => T, numLines: Int): String = Printer.traceInternal(block.toString, numLines, usingStdOut = true)

  /**
    * Same as traceStack, but prints to StdOut instead of StdError
    *
    * @return the string containing what was printed or what would have been printed if printing was enabled. You can pass this string into a logger.
    */
  def traceStackStdOut[T](block: => T): String = Printer.traceInternal(block.toString, Int.MaxValue, usingStdOut = true)

  /** A fatal assertion.
    * Terminates the program with exit code "7"
    *
    * @param assertion the assertion that must be true for the program to run. Can be a value or a function
    * @param message   the message to be printed to standard error on assertion failure
    * @example Debug.assert( 1 + 2 == 4, "Error: one plus two is not equal to four" )
    * @note this (and other assertions not marked "nonFatal") are fatal. To disable, please call "Debug.fatalAssertOffSE()"
    * @return the string containing what was printed or what would have been printed if printing was enabled. You can pass this string into a logger.
    */
  def assert(assertion: => Boolean, message: String, numLines: Int = Int.MaxValue): String = {
    Printer.internalAssert(message, numLines, usingStdOut = false, assertionTrue_? = assertion, isFatal_? = true) // trace the max number of lines of stack trace to std error
  }

  /** A fatal assertion.
    * Terminates the program with exit code "7"
    *
    * @param assertion the assertion that must be true for the program to run. Can be a value or a function
    * @param message   the message to be printed to standard out on assertion failure
    * @example Debug.assertStdOut( 1 + 2 == 4, "Error: one plus two is not equal to four" )
    * @note this (and other assertions not marked "nonFatal") are fatal. To disable, please call "Debug.fatalAssertOffSE()"
    * @return the string containing what was printed or what would have been printed if printing was enabled. You can pass this string into a logger.
    */
  def assertStdOut(assertion: => Boolean, message: String, numLines: Int = Int.MaxValue): String = {
    Printer.internalAssert(message, numLines, usingStdOut = true, assertionTrue_? = assertion, isFatal_? = true)
  }

  /**
    * Like Debug.assert(), but does not terminate the application
    *
    * @return the string containing what was printed or what would have been printed if printing was enabled. You can pass this string into a logger.
    */
  def check(assertion: => Boolean, message: String, numLines: Int = Int.MaxValue): String = {
    Printer.internalAssert(message, numLines, usingStdOut = false, assertionTrue_? = assertion, isFatal_? = false)
  }

  /**
    * Like Debug.assertStdOut(), but does not terminate the application
    *
    * @return the string containing what was printed or what would have been printed if printing was enabled. You can pass this string into a logger.
    */
  def checkStdOut(assertion: => Boolean, message: String, numLines: Int = Int.MaxValue): String = {
    Printer.internalAssert(message, numLines, usingStdOut = true, assertionTrue_? = assertion, isFatal_? = false)
  }

  import scala.collection.TraversableLike
  import scala.reflect.runtime.universe.WeakTypeTag

  // def printAll(coll: collection.GenTraversable[_]) = coll.foreach(println)

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
    // Now currentElement = start
    // then do real printing
    while(currentElement < end) {
      if (iterator.hasNext) {
        toPrint = toPrint + iterator.next() + " "
        currentElement += 1L
      } else {
        // get out of loop
        currentElement = Long.MaxValue
      }
    }
    toPrint = toPrint.trim // no trailing whitespace
    toPrint
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
  def traceContents[CollectionType](coll: collection.GenTraversableOnce[CollectionType], start: Int = 0, numElements: Int = Int.MaxValue, numLines: Int = 1)
  (implicit tag: WeakTypeTag[CollectionType]): String = {
    val collectionType = tag.tpe
    val toPrint = collectionType.toString + " " + getCollectionAsString(coll, start, numElements)
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
  def traceContentsStdOut[CollectionType](coll: collection.GenTraversableOnce[CollectionType], start: Int = 0, numElements: Int = Int.MaxValue, numLines: Int = 1)
  (implicit tag: WeakTypeTag[CollectionType]): String = {
    val collectionType = tag.tpe
    val toPrint = collectionType.toString + " " + getCollectionAsString(coll, start, numElements)
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
    def traceCodeImpl[T](c: Compat.Context)(block: c.Expr[T]): c.Expr[String] = {
      import c.universe._
      import scala.language.existentials
      val blockString = (new MacroHelperMethod[c.type](c)).getSourceCode(block.tree)
      val arg1 = q""" "(" + $blockString + ") -> " + ({$block}.toString) """
      val args = List(arg1)
      val toReturn =
        q"""
        _root_.scala.trace.debug.Debug.trace(..$args);
    """
      c.Expr[String](toReturn)
    }

    def traceLinesCodeImpl[T](c: Compat.Context)(block: c.Expr[T], numLines: c.Expr[Int]): c.Expr[String] = {
      import c.universe._
      import scala.language.existentials
      val blockString = (new MacroHelperMethod[c.type](c)).getSourceCode(block.tree)
      val arg1 = q""" "(" + $blockString + ") -> " + ({$block}.toString) """
      val arg2 = q"$numLines"
      val args = List(arg1, arg2)
      val toReturn =
        q"""
        _root_.scala.trace.debug.Debug.trace(..$args);
    """
      c.Expr[String](toReturn)
    }

    def apply[T](block: => T): String = macro traceCodeImpl[T]

    def apply[T](block: => T, numLines: Int): String = macro traceLinesCodeImpl[T]
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
    def traceStackCodeImpl[T](c: Compat.Context)(block: c.Expr[T]): c.Expr[String] = {
      import c.universe._
      val blockTree = block.tree
      val blockSource = new String(blockTree.pos.source.content)
      // apply case tree => tree.pos.startOrPoint to each subtree on which the function is defined and collect the results.
      val listOfTreePositions: List[Int] = blockTree.collect { case tree => tree.pos.startOrPoint }
      val start: Int = listOfTreePositions.min
      import scala.language.existentials
      val globalContext = c.asInstanceOf[reflect.macros.runtime.Context].global // inferred existential
      val codeParser = globalContext.newUnitParser(code = blockSource.drop(start))
      codeParser.expr()
      val end = codeParser.in.lastOffset
      val blockString = blockSource.slice(start, start + end)
      val arg1 = q""" "(" + $blockString + ") -> " + ({$block}.toString) """
      // System.err.println(arg1)
      // At compile time prints: "(".$plus("fooVar + barVar").$plus(") -> ").$plus(fooVar.+(barVar).toString)
      val args = List(arg1)
      val toReturn =
        q"""
        _root_.scala.trace.debug.Debug.traceStack(..$args);
    """
      c.Expr[String](toReturn)
    }

    def apply[T](block: => T): String = macro traceStackCodeImpl[T]
  }

  /**
    * Same as trace, but prints the entire expression, not just the result
    *
    * @example Debug.traceExpression{val myVal = 3; 1 + 2 + myVal}
    * @example Debug.traceExpression({val myVal = 3; 1 + 2 + myVal}, 3) // 3 lines of stack trace
    * @return the string containing what was printed or what would have been printed if printing was enabled. You can pass this string into a logger.
    */
  object traceExpression {
    def traceExpressionImpl[T](c: Compat.Context)(block: c.Expr[T]): c.Expr[String] = {
      import c.universe._
      val toTraceString = (block.tree).toString + " -> "
      val arg1 = q"$toTraceString + ({$block}.toString)"
      val args = List(arg1)
      val toReturn =
        q"""
        _root_.scala.trace.debug.Debug.trace(..$args);
    """
      c.Expr[String](toReturn)
    }

    def traceLinesExpressionImpl[T](c: Compat.Context)(block: c.Expr[T], numLines: c.Expr[Int]): c.Expr[String] = {
      import c.universe._
      val toTraceString = (block.tree).toString + " -> "
      val arg1 = q"$toTraceString + ({$block}.toString)"
      val arg2 = q"$numLines"
      val args = List(arg1, arg2)
      val toReturn =
        q"""
        _root_.scala.trace.debug.Debug.trace(..$args);
    """
      c.Expr[String](toReturn)
    }

    def apply[T](block: => T): String = macro traceExpressionImpl[T]

    def apply[T](block: => T, numLines: Int): String = macro traceLinesExpressionImpl[T]
  }

  /**
    * Same as traceStack, but prints the entire expression, not just the result
    *
    * @example Debug.traceStackExpression{val myVal = 3; 1 + 2 + myVal}
    * @return the string containing what was printed or what would have been printed if printing was enabled. You can pass this string into a logger.
    */
  object traceStackExpression {
    def traceStackExpressionImpl[T](c: Compat.Context)(block: c.Expr[T]): c.Expr[String] = {
      import c.universe._
      val toTraceString = (block.tree).toString + " -> "
      val arg1 = q"$toTraceString + ({$block}.toString)"
      val args = List(arg1)
      val toReturn =
        q"""
        _root_.scala.trace.debug.Debug.traceStack(..$args);
    """
      c.Expr[String](toReturn)
    }

    def apply[T](block: => T): String = macro traceStackExpressionImpl[T]
  }

  /**
    * Same as Debug.traceStdOut, but prints the whole expression not just its result
    *
    * @example Debug.traceStdOutExpression{val myVal = 3; 1 + 2 + myVal}
    * @example Debug.traceStdOutExpression({val myVal = 3; 1 + 2 + myVal}, 3) // 3 lines of stack trace
    * @return the string containing what was printed or what would have been printed if printing was enabled. You can pass this string into a logger.
    */
  object traceStdOutExpression {

    def traceStdOutExpressionImpl[T](c: Compat.Context)(block: c.Expr[T]): c.Expr[String] = {
      import c.universe._
      val toTraceString = (block.tree).toString + " -> "
      val arg1 = q"$toTraceString + ({$block}.toString)"
      val args = List(arg1)
      val toReturn =
        q"""
        _root_.scala.trace.debug.Debug.traceStdOut(..$args);
    """
      c.Expr[String](toReturn)
    }

    def traceLinesStdOutExpressionImpl[T](c: Compat.Context)(block: c.Expr[T], numLines: c.Expr[Int]): c.Expr[String] = {
      import c.universe._
      val toTraceString = (block.tree).toString + " -> "
      val arg1 = q"$toTraceString + ({$block}.toString)"
      val arg2 = q"$numLines"
      val args = List(arg1, arg2)
      val toReturn =
        q"""
        _root_.scala.trace.debug.Debug.traceStdOut(..$args);
    """
      c.Expr[String](toReturn)
    }

    def apply[T](block: => T): String = macro traceStdOutExpressionImpl[T]

    def apply[T](block: => T, numLines: Int): String = macro traceLinesStdOutExpressionImpl[T]
  }

  /**
    * Same as traceStackStdOut, but prints the whole expression not just the result
    *
    * @example Debug.traceStackStdOutExpression{val myVal = 3; 1 + 2 + myVal}
    * @return the string containing what was printed or what would have been printed if printing was enabled. You can pass this string into a logger.
    */
  object traceStackStdOutExpression {
    def traceStackStdOutExpressionImpl[T](c: Compat.Context)(block: c.Expr[T]): c.Expr[String] = {
      import c.universe._
      val toTraceString = (block.tree).toString + " -> "
      val arg1 = q"$toTraceString + ({$block}.toString)"
      val args = List(arg1)
      val toReturn =
        q"""
        _root_.scala.trace.debug.Debug.traceStackStdOut(..$args);
    """
      c.Expr[String](toReturn)
    }

    def apply[T](block: => T): String = macro traceStackStdOutExpressionImpl[T]
  }

  /**
    * Same as assert, but prints the whole expression instead of an error message
    *
    * @example Debug.assertExpression{val one = 1; one + 1 == 2}
    * @example Debug.assertExpression({val one = 1; one + 1 == 2}, 0) // 0 lines of stack trace
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
        _root_.scala.trace.debug.Debug.assert(assertBoolean, ..$args);
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
        _root_.scala.trace.debug.Debug.assert(assertBoolean, ..$args);
    """
      c.Expr[String](toReturn)
    }
  }

  /**
    * Same as assert, but prints the code instead of an error message.
    *
    * @example val one = 1; Debug.assertCode{one + 1 == 2}
    * @example val one = 1; Debug.assertCode({one + 1 == 2}, 0) // 0 lines of stack trace
    * @return the string containing what was printed or what would have been printed if printing was enabled. You can pass this string into a logger.
    */
  object assertCode {
    def apply(assertion: Boolean): String = macro assertCodeImpl

    def assertCodeImpl(c: Compat.Context)(assertion: c.Expr[Boolean]): c.Expr[String] = {
      import c.universe._
      val assertionTree = assertion.tree
      val assertionSource = new String(assertionTree.pos.source.content)
      // apply case tree => tree.pos.startOrPoint to each subtree on which the function is defined and collect the results.
      val listOfTreePositions: List[Int] = assertionTree.collect { case tree => tree.pos.startOrPoint }
      val start: Int = listOfTreePositions.min
      import scala.language.existentials
      val globalContext = c.asInstanceOf[reflect.macros.runtime.Context].global // inferred existential
      val codeParser = globalContext.newUnitParser(code = assertionSource.drop(start))
      codeParser.expr()
      val end = codeParser.in.lastOffset
      val blockString = assertionSource.slice(start, start + end)
      val assertionString = blockString + " -> "
      val arg2 = q"$assertionString + ({$assertion}.toString)"
      val arg3 = q"Int.MaxValue"
      val args = List(arg2, arg3)
      val toReturn =
        q"""
        val assertBoolean = $assertion;
        _root_.scala.trace.debug.Debug.assert(assertBoolean, ..$args);
    """
      c.Expr[String](toReturn)
    }

    def apply(assertion: Boolean, numLines: Int): String = macro assertLinesCodeImpl

    def assertLinesCodeImpl(c: Compat.Context)(assertion: c.Expr[Boolean], numLines: c.Expr[Int]): c.Expr[String] = {
      import c.universe._
      val assertionTree = assertion.tree
      val assertionSource = new String(assertionTree.pos.source.content)
      // apply case tree => tree.pos.startOrPoint to each subtree on which the function is defined and collect the results.
      val listOfTreePositions: List[Int] = assertionTree.collect { case tree => tree.pos.startOrPoint }
      val start: Int = listOfTreePositions.min
      import scala.language.existentials
      val globalContext = c.asInstanceOf[reflect.macros.runtime.Context].global // inferred existential
      val codeParser = globalContext.newUnitParser(code = assertionSource.drop(start))
      codeParser.expr()
      val end = codeParser.in.lastOffset
      val blockString = assertionSource.slice(start, start + end)
      val assertionString = blockString + " -> "
      val arg2 = q"$assertionString + ({$assertion}.toString)"
      val arg3 = q"$numLines"
      val args = List(arg2, arg3)
      val toReturn =
        q"""
        val assertBoolean = $assertion;
        _root_.scala.trace.debug.Debug.assert(assertBoolean, ..$args);
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
        _root_.scala.trace.debug.Debug.check(assertBoolean, ..$args);
    """
      c.Expr[String](toReturn)
    }

    def apply(assertion: Boolean, numLines: Int): String = macro assertLinesNonFatalExpressionImpl

    def assertLinesNonFatalExpressionImpl(c: Compat.Context)(assertion: c.Expr[Boolean], numLines: c.Expr[Int]): c.Expr[String] = {
      import c.universe._
      val assertionString = (assertion.tree).toString + " -> "
      //val arg1 = q"$assertion"
      val arg2 = q"$assertionString + ({$assertion}.toString)"
      val arg3 = q"$numLines"
      val args = List(arg2, arg3)
      val toReturn =
        q"""
        val assertBoolean = $assertion;
        _root_.scala.trace.debug.Debug.check(assertBoolean, ..$args);
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
    def apply(assertion: Boolean): String = macro assertCodeImpl

    def assertCodeImpl(c: Compat.Context)(assertion: c.Expr[Boolean]): c.Expr[String] = {
      import c.universe._
      val assertionTree = assertion.tree
      val assertionSource = new String(assertionTree.pos.source.content)
      // apply case tree => tree.pos.startOrPoint to each subtree on which the function is defined and collect the results.
      val listOfTreePositions: List[Int] = assertionTree.collect { case tree => tree.pos.startOrPoint }
      val start: Int = listOfTreePositions.min
      import scala.language.existentials
      val globalContext = c.asInstanceOf[reflect.macros.runtime.Context].global // inferred existential
      val codeParser = globalContext.newUnitParser(code = assertionSource.drop(start))
      codeParser.expr()
      val end = codeParser.in.lastOffset
      val blockString = assertionSource.slice(start, start + end)
      val assertionString = blockString + " -> "
      val arg2 = q"$assertionString + ({$assertion}.toString)"
      val arg3 = q"Int.MaxValue"
      val args = List(arg2, arg3)
      val toReturn =
        q"""
        val assertBoolean = $assertion;
        _root_.scala.trace.debug.Debug.check(assertBoolean, ..$args);
    """
      c.Expr[String](toReturn)
    }

    def apply(assertion: Boolean, numLines: Int): String = macro assertLinesCodeImpl

    def assertLinesCodeImpl(c: Compat.Context)(assertion: c.Expr[Boolean], numLines: c.Expr[Int]): c.Expr[String] = {
      import c.universe._
      val assertionTree = assertion.tree
      val assertionSource = new String(assertionTree.pos.source.content)
      // apply case tree => tree.pos.startOrPoint to each subtree on which the function is defined and collect the results.
      val listOfTreePositions: List[Int] = assertionTree.collect { case tree => tree.pos.startOrPoint }
      val start: Int = listOfTreePositions.min
      import scala.language.existentials
      val globalContext = c.asInstanceOf[reflect.macros.runtime.Context].global // inferred existential
      val codeParser = globalContext.newUnitParser(code = assertionSource.drop(start))
      codeParser.expr()
      val end = codeParser.in.lastOffset
      val blockString = assertionSource.slice(start, start + end)
      val assertionString = blockString + " -> "
      val arg2 = q"$assertionString + ({$assertion}.toString)"
      val arg3 = q"$numLines"
      val args = List(arg2, arg3)
      val toReturn =
        q"""
        val assertBoolean = $assertion;
        _root_.scala.trace.debug.Debug.check(assertBoolean, ..$args);
    """
      c.Expr[String](toReturn)
    }
  }

}
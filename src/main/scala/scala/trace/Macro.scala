package scala.trace

import scala.trace.internal.Printer
import scala.trace.internal.Helpers.MacroHelperMethod
import scala.language.experimental.macros
import scala.language.existentials

/**
  * Scala specific debug methods.
  */
object Macro {


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
  def traceContentsOut[ContainedT](coll: collection.GenTraversableOnce[ContainedT], start: Int = 0, numElements: Int = Int.MaxValue, numLines: Int = 1)
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
        _root_.scala.trace.Debug.err(..$args);
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
        _root_.scala.trace.Debug.err(..$args);
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
      val args: List[c.universe.Tree] = List(arg1)
      val toReturn =
        q"""
        _root_.scala.trace.Debug.errStack(..$args);
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
      val toTraceString: String = (toPrint.tree).toString + " -> "
      val arg1 = q"$toTraceString + ({$toPrint}.toString)"
      val args: List[c.universe.Tree] = List(arg1)
      val toReturn =
        q"""
        _root_.scala.trace.Debug.err(..$args);
    """
      c.Expr[String](toReturn)
    }

    def traceLinesExpressionImpl[T](c: Compat.Context)(toPrint: c.Expr[T], numLines: c.Expr[Int]): c.Expr[String] = {
      import c.universe._
      val toTraceString: String = (toPrint.tree).toString + " -> "
      val arg1 = q"$toTraceString + ({$toPrint}.toString)"
      val arg2 = q"$numLines"
      val args: List[c.universe.Tree] = List(arg1, arg2)
      val toReturn =
        q"""
        _root_.scala.trace.Debug.err(..$args);
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
      val toTraceString: String = (toPrint.tree).toString + " -> "
      val arg1 = q"$toTraceString + ({$toPrint}.toString)"
      val args: List[c.universe.Tree] = List(arg1)
      val toReturn =
        q"""
        _root_.scala.trace.Debug.errStack(..$args);
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
      val toTraceString: String = (toPrint.tree).toString + " -> "
      val arg1 = q"$toTraceString + ({$toPrint}.toString)"
      val args: List[c.universe.Tree] = List(arg1)
      val toReturn =
        q"""
        _root_.scala.trace.Debug.out(..$args);
    """
      c.Expr[String](toReturn)
    }

    def traceLinesStdOutExpressionImpl[T](c: Compat.Context)(toPrint: c.Expr[T], numLines: c.Expr[Int]): c.Expr[String] = {
      import c.universe._
      val toTraceString: String = (toPrint.tree).toString + " -> "
      val arg1 = q"$toTraceString + ({$toPrint}.toString)"
      val arg2 = q"$numLines"
      val args: List[c.universe.Tree] = List(arg1, arg2)
      val toReturn =
        q"""
        _root_.scala.trace.Debug.out(..$args);
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
      val toTraceString: String = (toPrint.tree).toString + " -> "
      val arg1 = q"$toTraceString + ({$toPrint}.toString)"
      val args: List[c.universe.Tree] = List(arg1)
      val toReturn =
        q"""
        _root_.scala.trace.Debug.outStack(..$args);
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
      val assertionString: String = (assertion.tree).toString + " -> "
      //val arg1 = q"$assertion"
      val arg2 = q"$assertionString + ({$assertion}.toString)"
      val arg3 = q"Int.MaxValue"
      val args: List[c.universe.Tree] = List(arg2, arg3)
      val toReturn =
        q"""
        val assertBoolean = $assertion;
        _root_.scala.trace.Debug.assert(assertBoolean, ..$args);
    """
      c.Expr[String](toReturn)
    }

    def apply(assertion: Boolean, numLines: Int): String = macro assertLinesExpressionImpl

    def assertLinesExpressionImpl(c: Compat.Context)(assertion: c.Expr[Boolean], numLines: c.Expr[Int]): c.Expr[String] = {
      import c.universe._
      val assertionString: String = (assertion.tree).toString + " -> "
      //val arg1 = q"$assertion"
      val arg2 = q"$assertionString + ({$assertion}.toString)"
      val arg3 = q"$numLines"
      val args: List[c.universe.Tree] = List(arg2, arg3)
      val toReturn =
        q"""
        val assertBoolean = $assertion;
        _root_.scala.trace.Debug.assert(assertBoolean, ..$args);
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
      val args: List[c.universe.Tree] = List(arg2, arg3)
      val toReturn =
        q"""
        val assertBoolean = $assertion;
        _root_.scala.trace.Debug.assert(assertBoolean, ..$args);
    """
      c.Expr[String](toReturn)
    }

    def apply(assertion: Boolean, numLines: Int): String = macro assertLinesCodeImpl

    def assertLinesCodeImpl(c: Compat.Context)(assertion: c.Expr[Boolean], numLines: c.Expr[Int]): c.Expr[String] = {
      import c.universe._
      val sourceCode: c.Tree = (new MacroHelperMethod[c.type](c)).getSourceCode(assertion.tree)
      val arg2 = q""" "(" + $sourceCode + ") -> " + ({$assertion}.toString) """
      val arg3 = q"$numLines"
      val args: List[c.universe.Tree] = List(arg2, arg3)
      val toReturn =
        q"""
        val assertBoolean = $assertion;
        _root_.scala.trace.Debug.assert(assertBoolean, ..$args);
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
      val assertionString: String = (assertion.tree).toString + " -> "
      //val arg1 = q"$assertion"
      val arg2 = q"$assertionString + ({$assertion}.toString)"
      val arg3 = q"Int.MaxValue"
      val args: List[c.universe.Tree] = List(arg2, arg3)
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
      val assertionString: String = (assertion.tree).toString + " -> "
      //val arg1 = q"$assertion"
      val arg2 = q"$assertionString + ({$assertion}.toString)"
      val arg3 = q"$numLines"
      val args: List[c.universe.Tree] = List(arg2, arg3)
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
      val args: List[c.universe.Tree] = List(arg2, arg3)
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
      val args: List[c.universe.Tree] = List(arg2, arg3)
      val toReturn =
        q"""
        val assertBoolean = $assertion;
        _root_.scala.trace.Debug.check(assertBoolean, ..$args);
    """
      c.Expr[String](toReturn)
    }
  }
}
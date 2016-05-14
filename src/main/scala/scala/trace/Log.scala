package scala.trace

import scala.trace.internal.Printer
import scala.trace.internal.Helpers.MacroHelperMethod
import scala.language.experimental.macros
import scala.language.existentials
/**
  * Created by johnreed on 4/17/16. A place for log methods
  */
object Log {

 /**
  * Adds a position to your print statements.
  */
  object pos {

     /**
      * Returns a string that helps you locate this print statement. Just adds a position.
      */
    def apply(toPrint: Any): String = macro posImpl

    def posImpl(c: Compat.Context)(toPrint: c.Expr[Any]): c.Expr[String] = {
      import c.universe._
      if (Printer.isDebugDisabled) {
        return c.Expr[String](q"""  ""  """) // return empty string expression
      }
      val lineNum = c.enclosingPosition.line
      val fileName = c.enclosingPosition.source.path // This needs to be trimmed down
      val trimmedFileName = processFileName(fileName)
      val fullName = Compat.enclosingOwner(c).fullName.trim
      val toReturn =
        q"""
       ({$toPrint}.toString) + " - " + $fullName + "(" + $trimmedFileName + ":" + $lineNum + ")"
     """
      c.Expr[String](toReturn)
    }
  }

  def processFileName(fileName: String): String = {
    if(fileName.contains("/")) {
      fileName.split("/").last
    } else {
      fileName.split("\\").last
    }
  }

  /**
   * Makes it easy to find your print statements.
   */
  object find {

    /**
      * Returns a string that helps you locate this print statement
      */
    def apply(toPrint: Any): String = macro findImpl

    def findImpl(c: Compat.Context)(toPrint: c.Expr[Any]): c.Expr[String] = {
      import c.universe._
      if (Printer.isDebugDisabled) {
        return c.Expr[String](q"""  ""  """) // return empty string expression
      }
      val lineNum = c.enclosingPosition.line
      val fileName = c.enclosingPosition.source.path // This needs to be trimmed down
      val trimmedFileName = processFileName(fileName)
      val fullName = Compat.enclosingOwner(c).fullName.trim
      import scala.language.existentials
      val sourceCode: c.Tree = (new MacroHelperMethod[c.type](c)).getSourceCode(toPrint.tree)
      val toReturn =
        q"""
       "(" + $sourceCode + ") -> " + ({$toPrint}.toString) + " - " + $fullName + "(" + $trimmedFileName +
       ":" + $lineNum + ")"
     """
      c.Expr[String](toReturn)
    }

    /**
      * Returns a string that helps you locate this container. Prints all of this container's elements.
      */
    def apply(container: collection.GenTraversableOnce[Any]): String = macro findContainerImpl

    /**
      * Returns a string that helps you locate this container. numElements many elements.
      * @param numElements the number of elements to print
      */
    def apply(container: collection.GenTraversableOnce[Any], numElements: Int): String = macro findContainerImplFromStart

    /**
      * Returns a string that helps you locate this container. Prints numElements from start.
      * @param start the index of the first element to print
      * @param numElements the number of elements to print
      */
    def apply(container: collection.GenTraversableOnce[Any], start: Int, numElements: Int): String = macro findContainerImplFromStartWNumElements

    def findContainerImpl(c: Compat.Context)(container: c.Expr[collection.GenTraversableOnce[Any]]): c.Expr[String] = {
      import c.universe._
      if (Printer.isDebugDisabled) {
        return c.Expr[String](q"""  ""  """) // return empty string expression
      }
      val lineNum = c.enclosingPosition.line
      val fileName = c.enclosingPosition.source.path // This needs to be trimmed down
      val trimmedFileName = processFileName(fileName)
      val fullName = Compat.enclosingOwner(c).fullName.trim
      val sourceCode = (new MacroHelperMethod[c.type](c)).getSourceCode(container.tree)
      val arg3 = q"Int.MaxValue"
      val toReturn =
        q"""
       "(" + $sourceCode + ") ->" + _root_.scala.trace.SDebug.getCollectionAsString($container, 0, $arg3) +
       "\tat " + $fullName + "(" + $trimmedFileName + ":" + $lineNum + ")"
     """
      c.Expr[String](toReturn)
    }
    def findContainerImplFromStart(c: Compat.Context)(container: c.Expr[collection.GenTraversableOnce[Any]], numElements: c.Expr[Int]): c.Expr[String] = {
      import c.universe._
      if (Printer.isDebugDisabled) {
        return c.Expr[String](q"""  ""  """) // return empty string expression
      }
      val lineNum = c.enclosingPosition.line
      val fileName = c.enclosingPosition.source.path // This needs to be trimmed down
      val trimmedFileName = processFileName(fileName)
      val fullName = Compat.enclosingOwner(c).fullName.trim
      val sourceCode = (new MacroHelperMethod[c.type](c)).getSourceCode(container.tree)
      val toReturn =
        q"""
       "(" + $sourceCode + ") ->" + _root_.scala.trace.SDebug.getCollectionAsString($container, 0, $numElements) +
       "\tat " + $fullName + "(" + $trimmedFileName + ":" + $lineNum + ")"
     """
      c.Expr[String](toReturn)
    }
    def findContainerImplFromStartWNumElements(c: Compat.Context)(container: c.Expr[collection.GenTraversableOnce[Any]],
    start: c.Expr[Int], numElements: c.Expr[Int]): c.Expr[String] = {
      import c.universe._
      if (Printer.isDebugDisabled) {
        return c.Expr[String](q"""  ""  """) // return empty string expression
      }
      val lineNum = c.enclosingPosition.line
      val fileName = c.enclosingPosition.source.path // This needs to be trimmed down
      val trimmedFileName = processFileName(fileName)
      val fullName = Compat.enclosingOwner(c).fullName.trim
      val sourceCode = (new MacroHelperMethod[c.type](c)).getSourceCode(container.tree)
      val toReturn =
        q"""
       "(" + $sourceCode + ") ->" + _root_.scala.trace.SDebug.getCollectionAsString($container, $start, $numElements) +
       "\tat " + $fullName + "(" + $trimmedFileName + ":" + $lineNum + ")"
     """
      c.Expr[String](toReturn)
    }

    /* Special case for arrays */

    /**
      * Returns a string that helps you locate this array. Prints all of this container's elements.
      */
    def apply[T](container: Array[T]): String = macro arrayFindContainerImpl[T]

    /**
      * Returns a string that helps you locate this array. numElements many elements.
      * @param numElements the number of elements to print
      */
    def apply[T](container: Array[T], numElements: Int): String = macro arrayFindContainerImplFromStart[T]

    /**
      * Returns a string that helps you locate this array. Prints numElements from start.
      * @param start the index of the first element to print
      * @param numElements the number of elements to print
      */
    def apply[T](container: Array[T], start: Int, numElements: Int): String = macro arrayFindContainerImplFromStartWNumElements[T]

    def arrayFindContainerImpl[T](c: Compat.Context)(container: c.Expr[Array[T]]): c.Expr[String] = {
      import c.universe._
      if (Printer.isDebugDisabled) {
        return c.Expr[String](q"""  ""  """) // return empty string expression
      }
      val lineNum = c.enclosingPosition.line
      val fileName = c.enclosingPosition.source.path // This needs to be trimmed down
      val trimmedFileName = processFileName(fileName)
      val fullName = Compat.enclosingOwner(c).fullName.trim
      val sourceCode = (new MacroHelperMethod[c.type](c)).getSourceCode(container.tree)
      val arg3 = q"Int.MaxValue"
      val toReturn =
        q"""
       "(" + $sourceCode + ") ->" + _root_.scala.trace.SDebug.getCollectionAsString($container, 0, $arg3) +
       "\tat " + $fullName + "(" + $trimmedFileName + ":" + $lineNum + ")"
     """
      c.Expr[String](toReturn)
    }
    def arrayFindContainerImplFromStart[T](c: Compat.Context)(container: c.Expr[Array[T]], numElements: c.Expr[Int]): c.Expr[String] = {
      import c.universe._
      if (Printer.isDebugDisabled) {
        return c.Expr[String](q"""  ""  """) // return empty string expression
      }
      val lineNum = c.enclosingPosition.line
      val fileName = c.enclosingPosition.source.path // This needs to be trimmed down
      val trimmedFileName = processFileName(fileName)
      val fullName = Compat.enclosingOwner(c).fullName.trim
      val sourceCode = (new MacroHelperMethod[c.type](c)).getSourceCode(container.tree)
      val toReturn =
        q"""
       "(" + $sourceCode + ") ->" + _root_.scala.trace.SDebug.getCollectionAsString($container, 0, $numElements) +
       "\tat " + $fullName + "(" + $trimmedFileName + ":" + $lineNum + ")"
     """
      c.Expr[String](toReturn)
    }
    def arrayFindContainerImplFromStartWNumElements[T](c: Compat.Context)(container: c.Expr[Array[T]], start: c.Expr[Int], numElements: c.Expr[Int]): c.Expr[String] = {
      import c.universe._
      if (Printer.isDebugDisabled) {
        return c.Expr[String](q"""  ""  """) // return empty string expression
      }
      val lineNum = c.enclosingPosition.line
      val fileName = c.enclosingPosition.source.path // This needs to be trimmed down
      val trimmedFileName = processFileName(fileName)
      val fullName = Compat.enclosingOwner(c).fullName.trim
      val sourceCode = (new MacroHelperMethod[c.type](c)).getSourceCode(container.tree)
      val toReturn =
        q"""
       "(" + $sourceCode + ") ->" + _root_.scala.trace.SDebug.getCollectionAsString($container, $start, $numElements) +
       "\tat " + $fullName + "(" + $trimmedFileName + ":" + $lineNum + ")"
     """
      c.Expr[String](toReturn)
    }
  }

}

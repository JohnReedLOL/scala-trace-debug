package info.collaboration_station.debug

import info.collaboration_station.debug.Helpers.MacroHelperMethod
import info.collaboration_station.debug.internal.Printer

import scala.collection.TraversableLike
import scala.language.experimental.macros
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
      if (Printer.debugDisabled_?) {
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

    def findImpl(c: Compat.Context)(toPrint: c.Expr[Any]): c.Expr[String] = {
      import c.universe._
      if (Printer.debugDisabled_?) {
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
       "(" + $sourceCode + ")->" + ({$toPrint}.toString) + " - " + $fullName + "(" + $trimmedFileName + ":" + $lineNum + ")"
     """
      c.Expr[String](toReturn)
    }

    def findContainerImpl(c: Compat.Context)(container: c.Expr[collection.GenTraversableOnce[Any]]): c.Expr[String] = {
      import c.universe._
      if (Printer.debugDisabled_?) {
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
       "(" + $sourceCode + ")->" + _root_.info.collaboration_station.debug.Debug.getCollectionAsString($container, 0, $arg3) + " - " + $fullName + "(" + $trimmedFileName + ":" + $lineNum + ")"
     """
      c.Expr[String](toReturn)
    }
    def findContainerImplFromStart(c: Compat.Context)(container: c.Expr[collection.GenTraversableOnce[Any]], numElements: c.Expr[Int]): c.Expr[String] = {
      import c.universe._
      if (Printer.debugDisabled_?) {
        return c.Expr[String](q"""  ""  """) // return empty string expression
      }
      val lineNum = c.enclosingPosition.line
      val fileName = c.enclosingPosition.source.path // This needs to be trimmed down
      val trimmedFileName = processFileName(fileName)
      val fullName = Compat.enclosingOwner(c).fullName.trim
      val sourceCode = (new MacroHelperMethod[c.type](c)).getSourceCode(container.tree)
      val toReturn =
        q"""
       "(" + $sourceCode + ")->" + _root_.info.collaboration_station.debug.Debug.getCollectionAsString($container, 0, $numElements) + " - " + $fullName + "(" + $trimmedFileName + ":" + $lineNum + ")"
     """
      c.Expr[String](toReturn)
    }
    def findContainerImplFromStartWNumElements(c: Compat.Context)(container: c.Expr[collection.GenTraversableOnce[Any]], start: c.Expr[Int], numElements: c.Expr[Int]): c.Expr[String] = {
      import c.universe._
      if (Printer.debugDisabled_?) {
        return c.Expr[String](q"""  ""  """) // return empty string expression
      }
      val lineNum = c.enclosingPosition.line
      val fileName = c.enclosingPosition.source.path // This needs to be trimmed down
      val trimmedFileName = processFileName(fileName)
      val fullName = Compat.enclosingOwner(c).fullName.trim
      val sourceCode = (new MacroHelperMethod[c.type](c)).getSourceCode(container.tree)
      val toReturn =
        q"""
       "(" + $sourceCode + ")->" + _root_.info.collaboration_station.debug.Debug.getCollectionAsString($container, $start, $numElements) + " - " + $fullName + "(" + $trimmedFileName + ":" + $lineNum + ")"
     """
      c.Expr[String](toReturn)
    }
  }

}

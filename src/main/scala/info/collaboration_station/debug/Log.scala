package info.collaboration_station.debug

import info.collaboration_station.debug.Helpers.MacroHelperMethod
import info.collaboration_station.debug.internal.Printer

import scala.language.experimental.macros
import scala.collection.TraversableLike


/**
  * Created by johnreed on 4/17/16.
  */
object Log {

  object find {

    def apply(toPrint: Any): String = macro findImpl

    //def apply(toPrint: TraversableLike[Any, Any]): String = macro findContainerImpl

    def findImpl(c: Compat.Context)(toPrint: c.Expr[Any]): c.Expr[String] = {
      import c.universe._
      if (Printer.debugDisabled_?) {
        return c.Expr[String](q"""  ""  """) // return empty string expression
      }
      val lineNum = c.enclosingPosition.line
      val fileName = c.enclosingPosition.source.path // This needs to be trimmed down
      val trimmedFileName = fileName.split("/").last // Only trims on linux - windows has full file name
      val fullName = Compat.enclosingOwner(c).fullName.trim

      /* Now we need the source code... */
      val sourceCode: c.Tree = (new MacroHelperMethod[c.type](c)).getSourceCode(toPrint.tree)
      //val sourceCode: c.Tree = q""" "" """
      val toReturn =
        q"""
       "(" + $sourceCode + ") -> " + ({$toPrint}.toString) + " at " + $fullName + "(" + $trimmedFileName + ":" + $lineNum + ")"
     """
      c.Expr[String](toReturn)
    }
  }

}

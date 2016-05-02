package scala.trace

import scala.trace.internal.Printer
import scala.language.experimental.macros

/**
  * Provides a file/line position as a string to append to the end of a print or log statement.
  * @example logger.warn("something has occured" + Pos()) // "something has occured - path.to.MyClass.func(MyClass.scala:33)"
  * @author John-Michael Reed (https://github.com/JohnReedLOL/scala-trace-debug)
  */
object Pos {
  /**
    * Provides a file/line position as a string to append to the end of a print or log statement.
    * @example logger.warn("something has occured" + Pos()) // "something has occured - path.to.MyClass.func(MyClass.scala:33)"
    */
  def apply(): String = macro posImpl

  def posImpl(c: Compat.Context)(): c.Expr[String] = {
    import c.universe._
    if (Printer.debugDisabled_?) {
      return c.Expr[String](q"""  ""  """) // return empty string expression
    }
    val lineNum = c.enclosingPosition.line
    val fileName = c.enclosingPosition.source.path // This needs to be trimmed down
    val trimmedFileName = Log.processFileName(fileName)
    val fullName = Compat.enclosingOwner(c).fullName.trim
    val toReturn =
      q"""
       " - " + $fullName + "(" + $trimmedFileName + ":" + $lineNum + ")"
     """
    c.Expr[String](toReturn)
  }
}

package scala.trace

import scala.trace.internal.Printer
import scala.language.experimental.macros

/**
  * Provides position based printing.
  *
  * @author John-Michael Reed (https://github.com/JohnReedLOL/scala-trace-debug)
  */
object Pos {

  /**
    * Prints the value along with a clickable hyperlink to the location in the source code to std out
    *
    * @return the toString of the value
    * @example Pos.prt("Hello World")
    */
  def out(toPrint: Any): String = macro outImpl

  /**
    * Macro implementation.
    */
  def outImpl(c: Compat.Context)(toPrint: c.Expr[Any]): c.Expr[String] = {
    import c.universe._
    if (Printer.debugDisabled_?) {
      c.Expr[String](q"""  ""  """) // return empty string expression
    } else {
      val lineNum: Int = c.enclosingPosition.line
      val fileName: String = c.enclosingPosition.source.path // This needs to be trimmed down
      val trimmedFileName: String = processFileName(fileName)
      val path: String = Compat.enclosingOwner(c).fullName.trim
      val myStringTree = toPrint.tree
      val myString = q"""{if($myStringTree == null) {"null"} else {$myStringTree.toString()}}"""
      val toReturn = q"""
        _root_.scala.Console.println($myString + " - " + $path + "(" + $trimmedFileName + ":" + $lineNum + ")");
        $myString
      """
      c.Expr[String](toReturn)
    }
  }

  /**
    * Prints the value along with a clickable hyperlink to the location in the source code to std out
    *
    * @return the toString of the value
    * @example Pos.prt("Hello World")
    */
  def err(toPrint: Any): String = macro errImpl

  /**
    * Macro implementation.
    */
  def errImpl(c: Compat.Context)(toPrint: c.Expr[Any]): c.Expr[String] = {
    import c.universe._
    if (Printer.debugDisabled_?) {
      c.Expr[String](q"""  ""  """) // return empty string expression
    } else {
      val lineNum: Int = c.enclosingPosition.line
      val fileName: String = c.enclosingPosition.source.path // This needs to be trimmed down
      val trimmedFileName: String = processFileName(fileName)
      val path: String = Compat.enclosingOwner(c).fullName.trim
      val myStringTree = toPrint.tree
      val myString = q"""{if($myStringTree == null) {"null"} else {$myStringTree.toString()}}"""
      val toReturn = q"""
        _root_.java.lang.System.err.println($myString + " - " + $path + "(" + $trimmedFileName + ":" + $lineNum + ")");
        $myString
      """
      c.Expr[String](toReturn)
    }
  }

  /**
    * Provides a file/line position as a string to append to the end of a print or log statement.
    *
    * @example logger.warn("something has occured" + Pos()) // "something has occured - path.to.MyClass.func(MyClass.scala:33)"
    */
  def apply(): String = macro posImpl

  def posImpl(c: Compat.Context)(): c.Expr[String] = {
    import c.universe._
    if (Printer.debugDisabled_?) {
      c.Expr[String](q"""  ""  """) // return empty string expression
    } else {
      val lineNum: Int = c.enclosingPosition.line
      val fileName: String = c.enclosingPosition.source.path // This needs to be trimmed down
      val trimmedFileName: String = processFileName(fileName)
      val path: String = Compat.enclosingOwner(c).fullName.trim
      val toReturn = q"""
       " - " + $path + "(" + $trimmedFileName + ":" + $lineNum + ")"
      """
      c.Expr[String](toReturn)
    }
  }

  protected[trace] def processFileName(fileName: String): String = {
    if (fileName.contains("/")) {
      fileName.split("/").last
    } else {
      fileName.split("\\").last
    }
  }
}

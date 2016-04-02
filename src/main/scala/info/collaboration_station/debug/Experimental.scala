package info.collaboration_station.debug

import scala.language.implicitConversions
import scala.language.experimental.macros
import scala.reflect.macros.blackbox.Context

/**
  * Created by johnreed on 4/1/16. Everything in this object is experimental.
  */
object Experimental {

  case class Text[T](value: T, source: String)
  object Text{
    import scala.language.implicitConversions
    implicit def generate[T](toPrint: T): Text[T] = macro MacroImpl.text[T]
    def apply[T](toPrint: T): Text[T] = macro MacroImpl.text[T]
  }
  object MacroImpl {
    def text[T: c.WeakTypeTag](c: Context)(toPrint: c.Expr[T]): c.Expr[Text[T]] = {
      import c.universe._
      val toPrintTree = toPrint.tree
      val toPrintSourceChars: Array[Char] = toPrintTree.pos.source.content
      val toPrintSource = new String(toPrintSourceChars)
      // apply case tree => tree.pos.start to each subtree on which the function is defined and collect the results.
      val listOfTreePositions: List[Int] = toPrintTree.collect { case tree => tree.pos.start }
      val start: Int = listOfTreePositions.min
      import scala.language.existentials
      val globalContext = c.asInstanceOf[reflect.macros.runtime.Context].global // inferred existential
      val codeParser = globalContext.newUnitParser(code = toPrintSource.drop(start))
      codeParser.expr()
      val end = codeParser.in.lastOffset
      val text = toPrintSource.slice(start, start + end)
      val tree = q"""${c.prefix}(${toPrintTree}, $text)""" // text is raw text from source code
      c.Expr[Text[T]](tree)
    }
  }

}

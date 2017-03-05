package com.github.johnreedlol.internal

import com.github.johnreedlol.Compat

/**
  * Created by johnreed on 4/17/16. A place for helper methods.
  */
protected[johnreedlol] object Helpers {

  final class MacroHelperMethod[C <: Compat.Context](val c: C) {

    def getSourceCode(toPrint: c.Tree): c.Tree = {
      import c.universe._
      val fileContent: String = new String(toPrint.pos.source.content)
      // apply case tree => tree.pos.startOrPoint to each subtree on which the function is defined and collect the results.
      val listOfTreePositions: List[Int] = toPrint.collect {
        case treeVal => treeVal.pos match {
          case NoPosition ⇒ Int.MaxValue
          case p ⇒ p.startOrPoint
        }
      }
      val start: Int = listOfTreePositions.min
      import scala.language.existentials
      val globalContext = c.asInstanceOf[reflect.macros.runtime.Context].global // inferred existential
      val codeParser: globalContext.syntaxAnalyzer.UnitParser
        = globalContext.newUnitParser(code = fileContent.drop(start))
      codeParser.expr()
      val end: globalContext.syntaxAnalyzer.Offset = codeParser.in.lastOffset
      val text: String = fileContent.slice(start, start + end)
      val sourceCode = q""" $text """
      sourceCode
    }
  }

}

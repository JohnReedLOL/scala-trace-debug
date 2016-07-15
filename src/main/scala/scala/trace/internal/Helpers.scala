package scala.trace.internal

import scala.trace.Compat

/**
  * Created by johnreed on 4/17/16. A place for helper methods.
  */
protected[trace] object Helpers {

  final class MacroHelperMethod[C <: Compat.Context](val c: C) {

    def getSourceCode(toPrint: c.Tree): c.Tree = {
      import c.universe._
      val blockTree = toPrint
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
      val sourceCode = q""" $blockString """
      sourceCode
    }
  }
}

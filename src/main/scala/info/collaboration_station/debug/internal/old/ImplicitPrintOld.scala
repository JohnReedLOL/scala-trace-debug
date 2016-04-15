package info.collaboration_station.debug.internal.old

import scala.language.experimental.macros
import scala.language.implicitConversions
import scala.reflect.macros.blackbox.Context

/**
  * Created by johnreed on 4/12/16.
  */
final class ImplicitPrintOld[MyType](val me: MyType) {

  /**
    * Same as System.out.print(this), but with the function name after the object
    *
    * @return the thing that was just printed
    * @example if(foo.print) { /* do something with foo */ }
    */
  object print {
    def apply(): MyType = macro PrintObject.macroPrint[MyType]
  }

  /**
    * Same as System.out.println(this), but with the function name after the object
    *
    * @return the thing that was just printed
    * @example if(foo.println) { /* do something with foo */ }
    */
  object println {
    def apply(): MyType = macro PrintlnObject.macroPrint[MyType]
  }

  /**
    * Same as System.err.print(this), but with the function name after the object
    *
    * @return the thing that was just printed
    * @example if(foo.printStdErr) { /* do something with foo */ }
    */
  object printStdErr {
    def apply(): MyType = macro PrintStdErrObject.macroPrint[MyType]
  }

  /**
    * Same as System.err.println(this), but with the function name after the object
    *
    * @return the thing that was just printed
    * @example if(foo.printlnStdErr) { /* do something with foo */ }
    */
  object printlnStdErr {
    def apply(): MyType = macro PrintlnStdErrObject.macroPrint[MyType]
  }
}

object ImplicitPrintOld {

}

object AssertionMacros {
  def assertEquals[MyType](c: Context)(other: c.Tree): c.Tree = {
    import c.universe._
    val self = q"${c.prefix}.self"
    q"_root_.scala.Predef.assert($self == $other)"
  }
}

object PrintObject {
  def macroPrint[MyType](c: Context)(): c.Expr[MyType] = {
    import c.universe._
    val self = q"${c.prefix}.self"
    // import info.collaboration_station.debug.internal.Printer.traceInternal
    val toReturn = q"System.out.print($self); self;"
    c.Expr[MyType](toReturn)
  }
}

object PrintlnObject {
  def macroPrint[MyType](c: Context)(): c.Expr[MyType] = {
    import c.universe._
    val self = q"${c.prefix}.self"
    // import info.collaboration_station.debug.internal.Printer.traceInternal
    val toReturn = q"System.out.println($self); self;"
    c.Expr[MyType](toReturn)
  }
}

object PrintStdErrObject {
  def macroPrint[MyType](c: Context)(): c.Expr[MyType] = {
    import c.universe._
    val self = q"${c.prefix}.self"
    // import info.collaboration_station.debug.internal.Printer.traceInternal
    val toReturn = q"System.err.print($self); self;"
    c.Expr[MyType](toReturn)
  }
}

object PrintlnStdErrObject {
  def macroPrint[MyType](c: Context)(): c.Expr[MyType] = {
    import c.universe._
    val self = q"${c.prefix}.self"
    // import info.collaboration_station.debug.internal.Printer.traceInternal
    val toReturn = q"System.err.println($self); self;"
    c.Expr[MyType](toReturn)
  }
}

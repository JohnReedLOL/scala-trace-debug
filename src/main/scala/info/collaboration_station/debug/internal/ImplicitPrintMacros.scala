package info.collaboration_station.debug.internal

import scala.language.implicitConversions
import scala.language.experimental.macros
import info.collaboration_station.debug.Compat

/**
  * Created by johnreed on 4/14/16.
  */
object ImplicitPrintMacros {
  def print[MyType](c: Compat.Context)(): c.Expr[MyType] = {
    import c.universe._
    val me = q"${c.prefix}.me" // this "me" has to be the same "me" in ImplicitPrint.
    val toReturn =
      q"""
           _root_.java.lang.System.out.print($me);
           $me;
          """
    c.Expr[MyType](toReturn)
    //toReturn
  }
  def println[MyType](c: Compat.Context)(): c.Expr[MyType] = {
    import c.universe._
    val me = q"${c.prefix}.me"
    val toReturn =
      q"""
           _root_.java.lang.System.out.println($me);
           $me;
          """
    c.Expr[MyType](toReturn)
    //toReturn
  }
  def printStdErr[MyType](c: Compat.Context)(): c.Expr[MyType] = {
    import c.universe._
    val me = q"${c.prefix}.me"
    val toReturn =
      q"""
           _root_.java.lang.System.err.print($me);
           $me;
          """
    c.Expr[MyType](toReturn)
    //toReturn
  }
  def printlnStdErr[MyType](c: Compat.Context)(): c.Expr[MyType] = {
    import c.universe._
    val me = q"${c.prefix}.me"
    val toReturn =
      q"""
           _root_.java.lang.System.err.println($me);
           $me;
          """
    c.Expr[MyType](toReturn)
    // toReturn
  }
}

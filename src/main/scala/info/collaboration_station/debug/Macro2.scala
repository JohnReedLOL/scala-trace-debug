package info.collaboration_station.debug

import scala.language.experimental.macros
import scala.reflect.macros.blackbox.Context

/**
  * Created by johnreed on 3/23/16.
  */
object Macro2 {

  def foo(s: String): Unit = macro fooImpl

  def fooImpl(c: Context)(s: c.Expr[String]): c.Expr[Unit] = {
    import c.universe._
    val unitExpr: c.universe.Tree = q"System.out.println(foo);"
    return c.Expr[Unit](unitExpr)
  }
}

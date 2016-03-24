package info.collaboration_station.debug

import scala.language.experimental.macros
import scala.reflect.macros.blackbox.Context

/**
  * Created by johnreed on 3/23/16.
  */
class Bundle(val c: Context) {
  import c.universe._

  final def foo(s: String): Unit = macro fooImpl

  final def fooImpl(s: c.Expr[String]): c.Expr[Unit] = {

    val unitExpr: c.universe.Tree = q"System.out.println(foo);"
    return c.Expr[Unit](unitExpr)
  }

}

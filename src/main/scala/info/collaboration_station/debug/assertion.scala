package info.collaboration_station.debug

import scala.language.experimental.macros
import scala.reflect.macros.blackbox.Context


object assertions {
  implicit class AssertEquals[T](val self: T) extends AnyVal {
    def assertEquals(other: T): Unit = macro AssertionMacros.assertEquals[T]
  }

  object AssertionMacros {
    def assertEquals[T](c: Context)(other: c.Tree): c.Tree = {
      import c.universe._
      val self = q"${c.prefix}.self"
      q"_root_.scala.Predef.assert($self == $other)"
    }
  }
}

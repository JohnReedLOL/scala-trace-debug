//package so
package so

import scala.language.experimental.macros // There will be macros
import scala.reflect.macros.blackbox._

trait AssertEquals[T, V] {
  def assertEquals(t: T, v: V): Boolean
}

object AssertEquals {
  implicit def assertEquals[T, V]: AssertEquals[T, V] = macro impl[T, V]


  implicit class WithAssertEquals[T](t: T) {
    def assertEquals[V](v: V)(implicit assertEquals: AssertEquals[T, V]): Boolean = assertEquals.assertEquals(t, v)
  }

  def impl[T: c.WeakTypeTag, V: c.WeakTypeTag](c: Context) = {
    import c.universe._
    val _t = c.weakTypeOf[T]
    val _v = c.weakTypeOf[V]
    val _import = reify {
      //need write full name :package.className
      import so.AssertEquals
    }
    q"""
      {
      ${_import}
      new ${typeOf[AssertEquals[_, _]].typeSymbol.name.toTypeName}[${_t},${_v}]{
        def assertEquals(t: ${_t}, v: ${_v}): Boolean = t == v
      }
      }
      """
  }
}

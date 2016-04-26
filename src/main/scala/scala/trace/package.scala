package scala

import scala.trace.internal.ImplicitPrint
import scala.trace.internal.{ImplicitAssert, ImplicitPrint, ImplicitTrace}

/**
  * Created by johnreed on 3/12/16. Makes Scala Trace Debug functions available via implicit conversions. https://github.com/JohnReedLOL/scala-trace-debug
  */
package object trace {

  import scala.language.implicitConversions

  // Warning: implicit conversions language feature

  /**
    * Import this to add print functionality to the current scope
    */
  implicit def implicitlyPrintable[MyType](me: MyType): ImplicitPrint[MyType] = {
    new ImplicitPrint(me)
  }

  /**
    * Import this to add trace functionality to the current scope
    */
  implicit def implicitlyTraceable[MyType](me: MyType): ImplicitTrace[MyType] = {
    new ImplicitTrace(me)
  }

  /**
    * Import this to add assert functionality to the current scope
    */
  implicit def implicitlyAssertable[MyType](me: MyType): ImplicitAssert[MyType] = {
    new ImplicitAssert(me)
  }

}

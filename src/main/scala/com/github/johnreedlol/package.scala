package com.github

import com.github.johnreedlol.conversions.{ImplicitAssert, ImplicitPrint, ImplicitTrace}

/**
  * Makes Scala Trace Debug functions available via implicit conversions.
  * https://github.com/JohnReedLOL/scala-trace-debug
  */
package object johnreedlol {

  import scala.language.implicitConversions

  /**
    * Assertions fail with exit code 7
    */
  protected[johnreedlol] val exitFail = 7

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

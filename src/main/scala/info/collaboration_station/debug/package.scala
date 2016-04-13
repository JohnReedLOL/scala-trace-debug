package info.collaboration_station

import info.collaboration_station.debug.internal.{ImplicitAssert, ImplicitPrint, ImplicitTrace}

/**
  * Created by johnreed on 3/12/16. Contains implicit debug functions. Import with "import info.collaboration_station.debug._"
  */
package object debug {

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

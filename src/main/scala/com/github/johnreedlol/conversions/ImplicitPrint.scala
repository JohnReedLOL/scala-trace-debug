package com.github.johnreedlol.conversions

import scala.language.implicitConversions

/**
  * Wrapper class for implicit conversion .print methods
  */
final class ImplicitPrint[MyType](val me: MyType) extends AnyVal {

  /**
    * Same as System.out.print(this), but with the function name after the object
    *
    * @return the thing that was just printed
    * @example if(foo.print) { /* do something with foo */ }
    */
  final def print(): MyType = {
    System.out.print(me);
    me
  }

  /**
    * Same as System.out.println(this), but with the function name after the object
    *
    * @return the thing that was just printed
    * @example if(foo.println) { /* do something with foo */ }
    */
  final def println(): MyType = {
    System.out.println(me);
    me
  }

  /**
    * Same as System.err.print(this), but with the function name after the object
    *
    * @return the thing that was just printed
    * @example if(foo.printStdErr) { /* do something with foo */ }
    */
  final def printErr(): MyType = {
    System.err.print(me);
    me
  }

  /**
    * Same as System.err.println(this), but with the function name after the object
    *
    * @return the thing that was just printed
    * @example if(foo.printlnStdErr) { /* do something with foo */ }
    */
  final def printlnErr(): MyType = {
    System.err.println(me);
    me
  }
}

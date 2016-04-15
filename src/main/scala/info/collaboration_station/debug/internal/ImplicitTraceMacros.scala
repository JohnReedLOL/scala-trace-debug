package info.collaboration_station.debug.internal

import info.collaboration_station.debug.Compat

/**
  * Created by johnreed on 4/14/16.
  */
object ImplicitTraceMacros {
  def trace[MyType](c: Compat.Context)(): c.Expr[MyType] = {
    import c.universe._
    val me = q"${c.prefix}.me" // this "me" has to be the same "me" in ImplicitTrace.
    val toReturn =
      q"""
           _root_.info.collaboration_station.debug.internal.Printer.traceInternal($me, 1, false);
           $me;
          """
    c.Expr[MyType](toReturn)
  }
  def traceLines[MyType](c: Compat.Context)(numLines: c.Expr[Int]): c.Expr[MyType] = {
    import c.universe._
    val me = q"${c.prefix}.me" // this "me" has to be the same "me" in ImplicitTrace.
    val toReturn =
      q"""
           _root_.info.collaboration_station.debug.internal.Printer.traceInternal($me, $numLines, false);
           $me;
          """
    c.Expr[MyType](toReturn)
  }
  def traceStack[MyType](c: Compat.Context)(): c.Expr[MyType] = {
    import c.universe._
    val me = q"${c.prefix}.me" // this "me" has to be the same "me" in ImplicitTrace.
    val maxLines = q"Int.MaxValue"
    val toReturn =
      q"""
           _root_.info.collaboration_station.debug.internal.Printer.traceInternal($me, $maxLines, false);
           $me;
          """
    c.Expr[MyType](toReturn)
  }
  def traceStdOut[MyType](c: Compat.Context)(): c.Expr[MyType] = {
    import c.universe._
    val me = q"${c.prefix}.me" // this "me" has to be the same "me" in ImplicitTrace.
    val toReturn =
      q"""
           _root_.info.collaboration_station.debug.internal.Printer.traceInternal($me, 1, true);
           $me;
          """
    c.Expr[MyType](toReturn)
  }
  def traceLinesStdOut[MyType](c: Compat.Context)(numLines: c.Expr[Int]): c.Expr[MyType] = {
    import c.universe._
    val me = q"${c.prefix}.me" // this "me" has to be the same "me" in ImplicitTrace.
    val toReturn =
      q"""
           _root_.info.collaboration_station.debug.internal.Printer.traceInternal($me, $numLines, true);
           $me;
          """
    c.Expr[MyType](toReturn)
  }
  def traceStackStdOut[MyType](c: Compat.Context)(): c.Expr[MyType] = {
    import c.universe._
    val me = q"${c.prefix}.me" // this "me" has to be the same "me" in ImplicitTrace.
    val maxLines = q"Int.MaxValue"
    val toReturn =
      q"""
           _root_.info.collaboration_station.debug.internal.Printer.traceInternal($me, $maxLines, true);
           $me;
          """
    c.Expr[MyType](toReturn)
  }
}
package my.pkg

/**
  * Created by johnreed on 3/23/16. Run with sbt test:run
  */
object Main {

  def main(args: Array[String]) {

    def sleep() = Thread.sleep(60)

    import scala.trace.Pos
    println("an error message" + Pos()) /*position*/ ; sleep()

    import scala.trace.Debug

    // debug traces are clickable

    Debug.err("trace to standard error")
    Debug.err("trace to standard error", numLines = 2)
    Debug.arrayErr(Array(1,2,3))
    Debug.arrayErr(Array(1,2,3), numElements = 2,  numLines = 2) ; sleep()

    Debug.out("trace to standard out")
    Debug.out("trace to standard out", numLines = 2)
    Debug.arrayOut(Array(1,2,3))
    Debug.arrayOut(Array(1,2,3), start = 1, numLines = 2) ; sleep()

    // assertions are totally fatal (full stop)
    Debug.assert(1 == 1, "one must equal one")
    Debug.assertOut(1 == 1, "one must equal one") ; sleep()

    // turn them off like this
    Debug.fatalAssertOff()
    Debug.assert(1 == 2, "one must equal two")   // nothing happens

    // checks are non-fatal (no thread death)
    Debug.check("one" == 2, "one must equal one") ; sleep()
    // output is colored bright red for visibility
    Debug.checkOut("one" == 2, "one must equal one", numLines = 1) ; sleep()

    import scala.trace.Macro

    // Macro methods use advanced Scala features to print code and types

    Macro.contentsOut(List(1, 2, 3))
    Macro.contentsOut(List(1, 2, 3), numLines = 2) ; sleep()

    Macro.contentsErr(List(1, 2, 3))
    Macro.contentsErr(List(1, 2, 3), numLines = 2) ; sleep()

    Macro.checkCode("one" == 2)
    Macro.assertCode("one" == "one") ; sleep()

    val (one, two, three) = (1, 2, 3)

    // desugaring includes all the parenthesis

    Macro.desugarOut(one + two / three)
    Macro.codeOut(one + two / three) ; sleep()

    Macro.desugarErr(one + two / three)
    Macro.codeErr(one + two / three) ; sleep()

    import scala.trace.implicitlyTraceable

    // you can easily remove calls to ".out" and ".err" from the source by pressing Ctr-R (find-replace)

    "foo bar baz".out
    "foo bar baz".err ; sleep()

    println("")

    import scala.trace.implicitlyAssertable

    "foo bar".assertEq("foo bar", "foo bar must equal foo bar")
    2.check(_ + 3 == 5, "two plus three is five")

    import scala.trace.Format

    // Formatting makes the string more readable
    println(Format.text("This is a really really really really really really" +
      "really really really really really really really really really really" +
      "really really really really really really really really" +
      "long string that needs to be formatted because it is longer than 100 chars default \n"))

    import scala.trace.implicitlyFormatable

    // Instead of using Format you can use the .wrap method
    println(("This is a really,really,really,really,really,really,really,really,really,really,really," +
      ",really,really,really,really,really," +
      ",really,really,really,really,really,," +
      "long,string,that,needs,to,be,formatted,because,it,is,longer,than," + Format.getLineLength
      + ",chars").wrap(delimiter = ","))

    // output

    /*
    an error message - my.pkg.Main.main(Main.scala:16)

    "trace to standard error" in thread main:
      at my.pkg.Main$.main(Main.scala:22)

    "trace to standard error" in thread main:
      at my.pkg.Main$.main(Main.scala:23)
      at my.pkg.Main.main(Main.scala)

    " 1, 2, 3
    " in thread main:
      at my.pkg.Main$.main(Main.scala:24)

    " 1, 2
    " in thread main:
      at my.pkg.Main$.main(Main.scala:25)
      at my.pkg.Main.main(Main.scala)

    "trace to standard out" in thread main:
      at my.pkg.Main$.main(Main.scala:27)

    "trace to standard out" in thread main:
      at my.pkg.Main$.main(Main.scala:28)
      at my.pkg.Main.main(Main.scala)

    " 1, 2, 3
    " in thread main:
      at my.pkg.Main$.main(Main.scala:29)

    " 2, 3
    " in thread main:
      at my.pkg.Main$.main(Main.scala:30)
      at my.pkg.Main.main(Main.scala)

    "one must equal one" in thread main:
      at my.pkg.Main$.main(Main.scala:41)
      at my.pkg.Main.main(Main.scala)
    ^ The above stack trace leads to an assertion failure. ^

    "one must equal one" in thread main:
      at my.pkg.Main$.main(Main.scala:43)
    ^ The above stack trace leads to an assertion failure. ^

    "Contains: Int 1, 2, 3
    " in thread main:
      at my.pkg.Main$.main(Main.scala:49)

    "Contains: Int 1, 2, 3
    " in thread main:
      at my.pkg.Main$.main(Main.scala:50)
      at my.pkg.Main.main(Main.scala)

    "Contains: Int 1, 2, 3
    " in thread main:
      at my.pkg.Main$.main(Main.scala:52)

    "Contains: Int 1, 2, 3
    " in thread main:
      at my.pkg.Main$.main(Main.scala:53)
      at my.pkg.Main.main(Main.scala)

    "("one" == 2) -> false" in thread main:
      at my.pkg.Main$.main(Main.scala:55)
      at my.pkg.Main.main(Main.scala)
    ^ The above stack trace leads to an assertion failure. ^

    "(one + two / three) -> 1" in thread main:
      at my.pkg.Main$.main(Main.scala:63)

    "one.+(two./(three)) -> 1" in thread main:
      at my.pkg.Main$.main(Main.scala:62)

    "one.+(two./(three)) -> 1" in thread main:
      at my.pkg.Main$.main(Main.scala:65)

    "(one + two / three) -> 1" in thread main:
      at my.pkg.Main$.main(Main.scala:66)

    "foo bar baz" in thread main:
      at my.pkg.Main$.main(Main.scala:72)

    "foo bar baz" in thread main:
      at my.pkg.Main$.main(Main.scala:73)

    This is a really really really really really reallyreally really really really really really really really
    really reallyreally really really really really really really reallylong string that needs to be formatted
    because it is longer than 100 chars default

    This is a really,really,really,really,really,really,really,really,really,really,really,,really,really,
    really,really,really,,really,really,really,really,really,,long,string,that,needs,to,be,formatted,because,
    it,is,longer,than,100,chars,
    */
  }
}

package my.pkg

/**
  * Created by johnreed on 3/23/16. Run with sbt test:run
  */
object Main {

  def main(args: Array[String]) {

    def sleep() = Thread.sleep(60) // to prevent output mangling
    import com.github.johnreedlol.Pos
    Pos.err(null)
    Pos.err("Standard error") ; sleep()
    Pos.out("Hello World")
    Pos.out(null)
    println("an error message" + Pos()) /*position*/ ; sleep()

    import com.github.johnreedlol.Debug

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

    import com.github.johnreedlol.Macro

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
    Macro.codeOut(one + two / three, 2) ; sleep()

    Macro.desugarErr(one + two / three)
    Macro.codeErr(one + two / three) ; sleep()

    import com.github.johnreedlol.implicitlyTraceable

    // you can easily remove calls to ".out" and ".err" from the source by pressing Ctr-R (find-replace)

    "foo bar baz".out
    "foo bar baz".err ; sleep()

    println("")

    import com.github.johnreedlol.implicitlyAssertable

    "foo bar".assertEq("foo bar", "foo bar must equal foo bar")
    2.check(_ + 3 == 5, "two plus three is five")

    import com.github.johnreedlol.Format

    // Formatting makes the string more readable
    println(Format.text("This is a really really really really really really" +
      "really really really really really really really really really really" +
      "really really really really really really really really" +
      "long string that needs to be formatted because it is longer than 100 chars default \n"))

    import com.github.johnreedlol.implicitlyFormatable

    // Instead of using Format you can use the .wrap method
    println(("This is a really,really,really,really,really,really,really,really,really,really,really," +
      ",really,really,really,really,really," +
      ",really,really,really,really,really,," +
      "long,string,that,needs,to,be,formatted,because,it,is,longer,than," + Format.getLineLength
      + ",chars").wrap(delimiter = ","))

    // output

    /*
    Standard error - my.pkg.Main.main(Main.scala:12)
    Hello World - my.pkg.Main.main(Main.scala:13)
    an error message - my.pkg.Main.main(Main.scala:14)

    "trace to standard error" in thread main:
      at my.pkg.Main$.main(Main.scala:20)

    "trace to standard error" in thread main:
      at my.pkg.Main$.main(Main.scala:21)
      at my.pkg.Main.main(Main.scala)

    " 1, 2, 3
    " in thread main:
      at my.pkg.Main$.main(Main.scala:22)

    " 1, 2
    " in thread main:
      at my.pkg.Main$.main(Main.scala:23)
      at my.pkg.Main.main(Main.scala)

    "trace to standard out" in thread main:
      at my.pkg.Main$.main(Main.scala:25)

    "trace to standard out" in thread main:
      at my.pkg.Main$.main(Main.scala:26)
      at my.pkg.Main.main(Main.scala)

    " 1, 2, 3
    " in thread main:
      at my.pkg.Main$.main(Main.scala:27)

    " 2, 3
    " in thread main:
      at my.pkg.Main$.main(Main.scala:28)
      at my.pkg.Main.main(Main.scala)

    "one must equal one" in thread main:
      at my.pkg.Main$.main(Main.scala:39)
      at my.pkg.Main.main(Main.scala)
    ^ The above stack trace leads to an assertion failure. ^

    "one must equal one" in thread main:
      at my.pkg.Main$.main(Main.scala:41)
    ^ The above stack trace leads to an assertion failure. ^

    "Contains: Int 1, 2, 3
    " in thread main:
      at my.pkg.Main$.main(Main.scala:47)

    "Contains: Int 1, 2, 3
    " in thread main:
      at my.pkg.Main$.main(Main.scala:48)
      at my.pkg.Main.main(Main.scala)

    "Contains: Int 1, 2, 3
    " in thread main:
      at my.pkg.Main$.main(Main.scala:50)

    "Contains: Int 1, 2, 3
    " in thread main:
      at my.pkg.Main$.main(Main.scala:51)
      at my.pkg.Main.main(Main.scala)

    "("one" == 2) -> false" in thread main:
      at my.pkg.Main$.main(Main.scala:53)
      at my.pkg.Main.main(Main.scala)
    ^ The above stack trace leads to an assertion failure. ^

    "one.+(two./(three)) -> 1" in thread main:
      at my.pkg.Main$.main(Main.scala:60)

    "(one + two / three) -> 1" in thread main:
      at my.pkg.Main$.main(Main.scala:61)

    "one.+(two./(three)) -> 1" in thread main:
      at my.pkg.Main$.main(Main.scala:63)

    "(one + two / three) -> 1" in thread main:
      at my.pkg.Main$.main(Main.scala:64)

    "foo bar baz" in thread main:
      at my.pkg.Main$.main(Main.scala:71)

    "foo bar baz" in thread main:
      at my.pkg.Main$.main(Main.scala:70)

    Disconnected from the target VM, address: '127.0.0.1:46176', transport: 'socket'
    This is a really really really really really reallyreally really really really really really really really
    really reallyreally really really really really really really reallylong string that needs to be formatted
    because it is longer than 100 chars default

    This is a really,really,really,really,really,really,really,really,really,really,really,,really,really,
    really,really,really,,really,really,really,really,really,,long,string,that,needs,to,be,formatted,because,
    it,is,longer,than,100,chars,
    */
  }
}

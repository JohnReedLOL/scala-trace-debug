package my.pkg

import org.slf4j._

import scala.trace.{Debug, _}


/**
  * Created by johnreed on 3/23/16. Run with sbt test:run
  */
object Main {

  def main(args: Array[String]) {

    import scala.trace.implicitlyFormatable
    println(("This is a really really really really really really really really really really really " +
      "really really really really really really really really really really really really really " +
      "really really really really really really really really" +
      "long string that needs to be formatted because it is longer than " + Format.getLineWrap
      + " chars").wrap)
    println(("This is a really,really,really,really,really,really,really,really,really,really,really," +
      ",really,really,really,really,really," +
      ",really,really,really,really,really,," +
      "long,string,that,needs,to,be,formatted,because,it,is,longer,than," + Format.getLineWrap
      + ",chars").wrap(","))

    println("an error message" + Pos())
    Macro.codeErr("foo" + 2 + "bar")
    Macro.desugarErr("foo" + 2 + "bar")
    Macro.contentsErr(List(1, 2, 3))
  }
}

package info.collaboration_station.debug.testing

//import info.collaboration_station.debug.implicitlyPrintable
import info.collaboration_station.debug.ImplicitPrint
import org.scalatest._

/**
  * Created by johnreed on 4/13/16.
  */
class PartialImportTest extends FlatSpec {
  "printing with partial import" should "work" in {

    _root_.java.lang.System.out.println("LOOOOOOOOOOOOOOOOOOOOO");
    _root_.java.lang.System.err.println("LAAAAAAAAAAAAAAAAAAAAA");

    val v1 = (ImplicitPrint("fooqqqq")).println();
    val v2 = (ImplicitPrint("foowwwww")).printlnStdErr();
    val v3 = (("foowwwww2222")).printlnStdErr();
    val v4 = ("foowwwww22223333").printlnStdErr();


    val v5 = ("fooeeeeeee").println();

    _root_.java.lang.System.err.println( "Ready?\n" +
      v1 + "\n" +
      v2 + "\n" +
      v3 + "\n" +
      v4 + "\n" +
      v5 + "\n");

    //("This was printed with a partial import").printlnStdErr()
    assertResult(2) {
      1 + 1
    }
  }
}

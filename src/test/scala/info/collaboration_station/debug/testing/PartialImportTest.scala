package info.collaboration_station.debug.testing

import info.collaboration_station.debug.implicitlyPrintable
import org.scalatest._

/**
  * Created by johnreed on 4/13/16.
  */
class PartialImportTest extends FlatSpec {
  "printing with partial import" should "work" in {
    ("This was printed with a partial import").printlnStdErr()
    assertResult(2) {
      1 + 1
    }
  }
}

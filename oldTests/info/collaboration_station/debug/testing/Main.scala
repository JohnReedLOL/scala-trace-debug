package info.collaboration_station.debug.testing
import info.collaboration_station.debug._
/**
  * In IntelliJ Scala console, hit Ctrl+Enter instead of enter to execute
  */
object Main {
  /**
    * Run this by doing "sbt test:run"
    */
  def main(args: Array[String]) {
    "Hello World 1".trace()
    // "Hello World 2".traceStdOut
    Debug.trace("Hello World 3")
    "foo".assertNonFatalEquals("bar", "message", 2)
    "foo".assertEquals("foo", "message2")
    "foo".assertEquals("bar", "message3", 3) // exits with code 7
  }
}

package info.collaboration_station.debug.testing

import java.io.{BufferedReader, IOException}

import scala.util.control.Breaks._
import info.collaboration_station.debug._

/**
  * Created by johnreed on 3/14/16.
  */
object TestingUtils {
  /**
    * Gets the assert (or trace or other) message from either standard out or standard error using the given BufferedReader
    *
    * @param bfReader the BufferedReader used to extract the message. Null bfReader will throw IllegalArgumentException
    * @return the message. Each line of the message is a row in the array. And empty array means either no message or exception.
    */
  def getMessage(bfReader: BufferedReader): Array[String] = {
    require(bfReader != null)
    var messageRows: Array[String] = Array[String]()
    try {
      breakable {
        while (true) {
          val nextLine = bfReader.readLine()
          // nextLine.trace
          //System.err.println("bf " + bfReader1.toString + " reading: " + nextLine)
          if (nextLine == null) {
            break // break out of loop if the end of the stream has been reached
          }
          else {
            messageRows = messageRows :+ nextLine // append the next line to the end
          }
        }
      }
    } catch {
      case ioe: IOException => // Don't worry about it.
    }
    messageRows
  }
}

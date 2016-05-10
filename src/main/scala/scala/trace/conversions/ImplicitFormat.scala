package scala.trace.conversions

import scala.trace.{Debug, Format}

/**
  * Created by johnreed on 5/10/16.
  * Provides formatting functionality to strings.
  */
class ImplicitFormat(val me: String) {

  /** Formats a string by inserting line breaks on the " " delimiter
    */
  def wrap: String = {
    return wrap(" ")
  }

  /** Formats a string by inserting line breaks on the delimiter
    *
    * @param delimiter to format on
    */
  def wrap(delimiter: String): String = {
    val splitText: Array[String] = me.split(delimiter)
    var lineLength: Int = 0
    var formattedText: String = ""
    for (word <- splitText) {
      formattedText += word + delimiter
      lineLength += word.length + delimiter.length
      if (lineLength > Format.getLineWrap) {
        formattedText += "\n"
        lineLength = 0
      }
    }
    return formattedText
  }
}


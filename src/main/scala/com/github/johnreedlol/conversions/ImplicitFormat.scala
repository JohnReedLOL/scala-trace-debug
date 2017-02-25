package com.github.johnreedlol.conversions

import scala.trace.{Debug, Format}

/**
  * Provides formatting functionality to strings.
  */
class ImplicitFormat(val me: String) extends AnyVal {

  /** Formats a string by inserting line breaks on the " " delimiter
    */
  def wrap: String = {
    wrap(" ")
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
      if (lineLength > Format.getLineLength) {
        formattedText += "\n"
        lineLength = 0
      }
    }
    formattedText
  }
}


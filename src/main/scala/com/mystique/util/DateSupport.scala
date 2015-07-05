package com.mystique.util

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat


class DateSupport {
  val ISO_8601 = "yyyy-MM-dd"

  def parse(d: String) = {
    DateTime.parse(d, DateTimeFormat.forPattern(ISO_8601))
  }

  def isValidDate(d: String) = {
    parse(d) match {
      case e: DateTime => true
      case _ => false
    }
  }
}


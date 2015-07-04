package com.mystique.util

import java.text.SimpleDateFormat
import java.util.Date

object DateSupport {
  val ISO_8601 = "yyyy-MM-dd"

  val fmt = new SimpleDateFormat(ISO_8601)

  def isValidDate(d: String) = {
    fmt.parse(d) match {
      case _: Date => true
      case _ => false
    }
  }
}


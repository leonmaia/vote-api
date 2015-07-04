package com.mystique.server

import com.twitter.logging.Logger

trait Log {
  val log = Logger.get(getClass)
}

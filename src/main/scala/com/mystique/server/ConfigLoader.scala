package com.mystique.server

import com.twitter.server.TwitterServer

trait ConfigLoader {
  self: TwitterServer =>

  val env = flag("env", "dev", "Running Environment")

  premain {
    ApiConfig.load(env.get.getOrElse("dev"))
  }
}

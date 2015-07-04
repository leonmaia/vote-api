package com.mystique.server

import com.twitter.server.TwitterServer
import com.typesafe.config.{Config, ConfigFactory}

trait ConfigLoader extends TwitterServer {

  val env = flag("env", "dev", "Running environment")

  var config: Config = null

  premain {
    config = ConfigFactory.load("application.conf").getConfig(env.get.getOrElse("dev"))
    log.info(
      s"""
         |
         |************************************************************
         |
         |    Mystique API is running in ${env.get.getOrElse("dev").toUpperCase} mode
         |
         |************************************************************
         |
     """.stripMargin)
  }
}

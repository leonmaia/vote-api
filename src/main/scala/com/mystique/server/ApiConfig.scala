package com.mystique.server

import com.typesafe.config.{Config, ConfigFactory}

object ApiConfig  extends Log {

  var config: Config = _
  var env: String = _

  def load(env: String) = {
    config = ConfigFactory.load("application.conf").getConfig(env)
    this.env = env
    log.info(
      s"""
         |
         |************************************************************
         |
         |    Mystique API is running in ${env.toUpperCase} mode
         |    Tracing is $tracingIs for ${env.toUpperCase} mode
         |
         |************************************************************
         |
     """.stripMargin)
  }

  def tracingIs = if(isDev) "enabled" else "disabled"
  def isNotDev = env != "dev"
  def isDev = env == "dev"
}


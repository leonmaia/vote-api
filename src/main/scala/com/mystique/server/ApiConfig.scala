package com.mystique.server

import com.typesafe.config.{Config, ConfigFactory}

object ApiConfig  extends Log{

  var config: Config = null
  var env: String = null

  def load(env: String) = {
    config = ConfigFactory.load("application.conf").getConfig(env)
    this.env = env
    log.info(
      s"""
         |
         |************************************************************
         |
         |    Mystique API is running in ${env.toUpperCase} mode
         |
         |************************************************************
         |
     """.stripMargin)
  }

  def isNotDev = env != "dev"
  def isDev = env == "dev"
}


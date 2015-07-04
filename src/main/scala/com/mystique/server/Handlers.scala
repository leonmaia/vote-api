package com.mystique.server

import com.mystique.core.healthcheck.HealthCheckHandler
import com.twitter.server.TwitterServer

trait Handlers extends Services {
  self: TwitterServer =>

  lazy val heathCheckHandler = new HealthCheckHandler
}


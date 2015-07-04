package com.mystique.server

import com.mystique.core.healthcheck.HealthCheckHandler

trait Handlers extends Services {
  lazy val heathCheckHandler = new HealthCheckHandler
}


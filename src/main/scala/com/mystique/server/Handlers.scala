package com.mystique.server

import com.mystique.core.contests.ContestCreatorHandler
import com.mystique.core.healthcheck.HealthCheckHandler
import com.twitter.server.TwitterServer

trait Handlers extends Services {
  self: TwitterServer =>

  lazy val contestCreatorHandler = new ContestCreatorHandler(redisClient, ApiConfig.config)
  lazy val heathCheckHandler = new HealthCheckHandler
}


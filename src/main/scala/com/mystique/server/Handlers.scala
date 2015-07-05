package com.mystique.server

import com.mystique.core.candidates.CandidateHandler
import com.mystique.core.contests.{ContestHandler, ContestCreatorHandler}
import com.mystique.core.healthcheck.HealthCheckHandler
import com.mystique.core.votes.VoteHandler
import com.twitter.server.TwitterServer

trait Handlers extends Services {
  self: TwitterServer =>

  lazy val contestCreatorHandler = new ContestCreatorHandler(redisClient, ApiConfig.config)
  lazy val contestHandler = new ContestHandler(redisClient)
  lazy val candidateHandler = new CandidateHandler(redisClient, ApiConfig.config)
  lazy val voteHandler= new VoteHandler(redisClient)
  lazy val heathCheckHandler = new HealthCheckHandler
}


package com.mystique.server

import com.mystique.core.candidates.CandidateHandler
import com.mystique.core.contests.{ContestCreatorHandler, ContestHandler}
import com.mystique.core.healthcheck.HealthCheckHandler
import com.mystique.core.votes.VoteHandler
import com.mystique.server.ApiConfig._
import com.twitter.server.TwitterServer

trait Handlers extends ConfigLoader {
  self: TwitterServer =>

  lazy val contestCreatorHandler = new ContestCreatorHandler(config)
  lazy val contestHandler = new ContestHandler
  lazy val candidateHandler = new CandidateHandler(config)
  lazy val voteHandler= new VoteHandler
  lazy val heathCheckHandler = new HealthCheckHandler
}


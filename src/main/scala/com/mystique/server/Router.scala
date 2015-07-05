package com.mystique.server

import com.mystique.server.http.HttpRouter
import com.twitter.finagle.http.path._
import com.twitter.finagle.http.Method
import com.twitter.server.TwitterServer

trait Router extends Handlers {
  self: TwitterServer =>

  lazy val router = HttpRouter.forRoutes({
    case Method.Get ->  Root / "healthcheck" => heathCheckHandler
    case Method.Post -> Root / "contests"    => contestCreatorHandler
    case Method.Get ->  Root / "contests"    => contestHandler
    case Method.Post->  Root / "contest" / contestSlug / "candidates" => candidateHandler.create(contestSlug)
    case Method.Get ->  Root / "contest" / contestSlug / "candidates" => candidateHandler.list(contestSlug)

    case Method.Post -> Root / "votes"   / contestSlug / idCandidate  => voteHandler.vote(contestSlug, idCandidate)
    case Method.Get  -> Root / "votes"   / contestSlug / idCandidate  => voteHandler.get(contestSlug, idCandidate)
  })
}

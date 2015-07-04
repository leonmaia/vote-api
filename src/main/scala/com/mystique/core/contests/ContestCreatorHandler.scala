package com.mystique.core.contests

import com.mystique.models.Contest
import com.mystique.server.http.Responses._
import com.mystique.service.tracing.Tracing
import com.mystique.util.RedisUtils._
import com.twitter.finagle.Service
import com.twitter.finagle.http.{Request, Response}
import com.twitter.finagle.redis.Client
import com.twitter.util.Future
import com.typesafe.config.Config
import org.jboss.netty.handler.codec.http.HttpResponseStatus

import scala.util.{Failure, Success, Try}


class ContestCreatorHandler (val redis: Client, config: Config) extends Service[Request, Response] with Tracing {

  def apply(request: Request): Future[Response] = {
    withTrace("ContestCreatorHandler - #apply", "ContestCreatorHandler") {
      Try(Contest(request)) match {
        case Success(c) => {
          val location = s"${config.getString("host")}/contests/${c.slug}"
          redis.simpleHMSet(s"contest:${c.slug}", c.toMap)
          Future(respond("", HttpResponseStatus.CREATED, locationHeader = location))
        }
        case Failure(f) =>
          Future(respond("", HttpResponseStatus.BAD_REQUEST))
      }
    }
  }
}

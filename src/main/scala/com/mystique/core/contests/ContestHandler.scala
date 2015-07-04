package com.mystique.core.contests

import com.mystique.models.Contest
import com.mystique.server.http.Responses._
import com.mystique.service.tracing.Tracing
import com.twitter.finagle.Service
import com.twitter.finagle.http.{Request, Response}
import com.twitter.finagle.redis.Client
import com.twitter.util.{Await, Future}
import org.jboss.netty.handler.codec.http.HttpResponseStatus
import com.mystique.util.RedisUtils._

class ContestHandler(val redis: Client) extends Service[Request, Response] with Tracing {
  def apply(request: Request): Future[Response] = {
    withTrace("ContestHandler - #apply", "ContestHandler") {
      val keys = Await.result(redis.simpleGetKeys("contest:*"))
      redis.simpleHMGetAll(keys) map {
        case Some(r) => respond(toJson(r map Contest.fromMap), HttpResponseStatus.OK)
        case _ => respond(List.empty, HttpResponseStatus.OK)
      }
    }
  }
}


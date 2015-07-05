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

import scala.collection.mutable.ListBuffer
import scala.util.{Failure, Success}

class ContestHandler(val redis: Client) extends Service[Request, Response] with Tracing {
  def apply(request: Request): Future[Response] = {
    withTrace("ContestHandler - #apply", "ContestHandler") {
      redis.simpleGetKeys(s"contest:*") map {
        case r: List[String] => {
          respond(toJson(r map Contest.fromKey), HttpResponseStatus.OK)
        }
        case _ => respond(List.empty, HttpResponseStatus.OK)
      }
    }
  }
}


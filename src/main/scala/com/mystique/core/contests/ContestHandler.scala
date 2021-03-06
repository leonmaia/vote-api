package com.mystique.core.contests

import com.mystique.models.Contest
import com.mystique.server.RedisStore
import com.mystique.server.http.Responses._
import com.mystique.service.tracing.Tracing
import com.twitter.finagle.Service
import com.twitter.finagle.http.{Request, Response}
import com.twitter.finagle.redis.Client
import com.twitter.util.{Await, Future}
import org.jboss.netty.handler.codec.http.HttpResponseStatus

import scala.collection.mutable.ListBuffer
import scala.util.{Failure, Success}

class ContestHandler extends Service[Request, Response] with Tracing with RedisStore{
  def apply(request: Request): Future[Response] = {
    withTrace("ContestHandler - #apply", "ContestHandler") {
      getKeys(s"contest::*") map {
        case r: List[String] => {
          respond(toJson(r map Contest.fromKey), HttpResponseStatus.OK)
        }
        case _ => respond(List.empty, HttpResponseStatus.OK)
      }
    }
  }
}


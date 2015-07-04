package com.mystique.core.candidates

import com.mystique.models.Candidate
import com.mystique.server.http.Responses._
import com.mystique.service.tracing.Tracing
import com.mystique.util.RedisUtils._
import com.twitter.finagle.Service
import com.twitter.finagle.http.{Request, Response}
import com.twitter.finagle.redis.Client
import com.twitter.util.{Await, Future}
import com.typesafe.config.Config
import org.jboss.netty.handler.codec.http.HttpResponseStatus

import scala.util.{Failure, Success, Try}

class CandidateHandler(val redis: Client, config: Config) extends Tracing {

  def create(contestSlug: String) = new Service[Request, Response] {
    def apply(request: Request): Future[Response] = {
      withTrace("CandidateHandler- #create", "CandidateHandler") {
        Try(Candidate(request)) match {
          case Success(c) => {
            val location = s"${config.getString("host")}/contest/$contestSlug/candidate"
            redis.simpleHMSet(s"candidate:contest:$contestSlug:id:${c.id}", c.toMap)
            Future(respond("", HttpResponseStatus.CREATED, locationHeader = location))
          }
          case Failure(f) =>
            Future(respond(s"Errors!", HttpResponseStatus.BAD_REQUEST))
        }
      }
    }
  }

  def list(contestSlug: String) = new Service[Request, Response] {
    def apply(request: Request): Future[Response] = {
      withTrace("CandidateHandler- # list", "CandidateHandler") {
        val keys = Await.result(redis.simpleGetKeys(s"candidate:contest:$contestSlug:*"))
        redis.simpleHMGetAll(keys) map {
          case Some(r) => respond(toJson(r map Candidate.fromMap), HttpResponseStatus.OK)
          case _ => respond(List.empty, HttpResponseStatus.OK)
        }
      }
    }
  }
}

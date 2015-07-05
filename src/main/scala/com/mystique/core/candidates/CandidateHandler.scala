package com.mystique.core.candidates

import com.mystique.models.Candidate
import com.mystique.server.RedisStore
import com.mystique.server.http.Responses._
import com.mystique.service.tracing.Tracing
import com.twitter.finagle.Service
import com.twitter.finagle.http.{HeaderMap, Request, Response}
import com.twitter.finagle.redis.Client
import com.twitter.util.{Await, Future}
import com.typesafe.config.Config
import org.jboss.netty.handler.codec.http.HttpResponseStatus

import scala.util.{Failure, Success, Try}

class CandidateHandler(config: Config) extends Tracing with RedisStore {

  def create(contestSlug: String) = new Service[Request, Response] {
    def createKey(c: Candidate): String = s"candidate:contest=$contestSlug:id=${c.id}:name=${c.name}:avatar=${c.avatar.getOrElse("")}"
    def apply(request: Request): Future[Response] = {
      withTrace("CandidateHandler- #create", "CandidateHandler") {
        Try(Candidate(request)) match {
          case Success(c) => {
            val location = s"${config.getString("host")}/contest/$contestSlug/candidate"
            hmSet(createKey(c), c.toMap)
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
        getKeys(s"candidate:contest=$contestSlug:*") map {
          case r: List[String] => {
            respond(toJson(r map Candidate.fromKey), HttpResponseStatus.OK)
          }
          case _ => respond(List.empty, HttpResponseStatus.OK)
        }
      }
    }
  }
}

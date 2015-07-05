package com.mystique.core.votes

import com.mystique.models.Vote
import com.mystique.server.RedisStore
import com.mystique.server.http.Responses._
import com.mystique.service.tracing.Tracing
import com.twitter.finagle.Service
import com.twitter.finagle.http.{Request, Response}
import com.twitter.util.Future
import org.jboss.netty.handler.codec.http.HttpResponseStatus

import scala.util.{Failure, Success, Try}

class VoteHandler extends Tracing with RedisStore{
  val TTL = 600
  val MAXIMUM_VOTES_WHEN_KEY_EXISTS = 5

  def respondWith(key: String, idCandidate: String)(f: ()=> Future[Long])  = {
    f()
    incr(key)
    incr(s"$key:candidate:$idCandidate")
    respond("", HttpResponseStatus.OK)
  }

  def withResult(key: String): Future[Response] = {
    get(key) map {
      case Some(v) => Try(v.toLong) match {
        case Success(i) => respond(toJson(Vote(i)), HttpResponseStatus.OK)
        case Failure(f) => respond(List.empty, HttpResponseStatus.BAD_REQUEST)
      }
      case _ => respond(List.empty, HttpResponseStatus.NOT_FOUND)
    }
  }

  def vote(contestSlug: String, idCandidate: String) = new Service[Request, Response] {
    def apply(request: Request): Future[Response] = withTrace("VoteHandler- #vote", "VoteHandler") {
      val key = s"votes:contest:$contestSlug"
      Option(request.headers().get("user-token")) match {
        case Some(h) =>
          get(s"user-token:$h") map {
            case Some(s) => Try(s.toInt) match {
              case Success(v) =>
                if (v > MAXIMUM_VOTES_WHEN_KEY_EXISTS)
                  respond("Wait a few minutes before voting again!", HttpResponseStatus.FORBIDDEN)
                else respondWith(key, idCandidate){ ()=> incr(s"user-token:$h")}
              case Failure(f) => respond(List.empty, HttpResponseStatus.BAD_REQUEST)
            }
            case _ => respondWith(key, idCandidate)(()=> incrWithExpire(s"user-token:$h", "1", TTL))
          }
        case _ => Future(respond("No user-token on header!", HttpResponseStatus.UNAUTHORIZED))
      }
    }
  }


  def get(contestSlug: String, idCandidate: String) = new Service[Request, Response] {
    def apply(request: Request): Future[Response] = {
      withTrace("VoteHandler- #get", "VoteHandler") {
        withResult(s"votes:contest:$contestSlug:candidate:$idCandidate")
      }
    }
  }

  def result(contestSlug: String) = new Service[Request, Response] {
    def apply(request: Request): Future[Response] = {
      withTrace("VoteHandler- #result", "VoteHandler") {
        withResult(s"votes:contest:$contestSlug")
      }
    }
  }
}


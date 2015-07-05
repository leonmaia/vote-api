package com.mystique.core.votes

import com.mystique.models.Vote
import com.mystique.server.http.Responses._
import com.mystique.service.tracing.Tracing
import com.mystique.util.RedisUtils._
import com.twitter.finagle.Service
import com.twitter.finagle.http.{Request, Response}
import com.twitter.finagle.redis.Client
import com.twitter.util.Future
import org.jboss.netty.handler.codec.http.HttpResponseStatus

import scala.util.{Try, Failure, Success}

class VoteHandler(client: Client) extends Tracing {
  def vote(contestSlug: String, idCandidate: String) = new Service[Request, Response] {
    def apply(request: Request): Future[Response] = {
      withTrace("VoteHandler- #vote", "VoteHandler") {
        val key = s"votes:contest:$contestSlug"
        client.simpleINCR(key)
        client.simpleINCR(s"$key:candidate:$idCandidate") map {
          r => respond(toJson(Vote(r)), HttpResponseStatus.OK)
        }
      }
    }
  }

  def get(contestSlug: String, idCandidate: String) = new Service[Request, Response] {
    def apply(request: Request): Future[Response] = {
      client.simpleGet(s"votes:contest:$contestSlug:candidate:$idCandidate") map {
        case Some(v) => Try(v.toLong) match {
          case Success(i) => respond(toJson(Vote(i)), HttpResponseStatus.OK)
          case Failure(f) => respond(List.empty, HttpResponseStatus.OK)
        }
        case _ => respond(List.empty, HttpResponseStatus.OK)
      }
    }
  }
}


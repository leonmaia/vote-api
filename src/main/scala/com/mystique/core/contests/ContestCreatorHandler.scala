package com.mystique.core.contests

import com.mystique.models.Contest
import com.mystique.server.RedisStore
import com.mystique.server.http.Responses._
import com.mystique.service.tracing.Tracing
import com.twitter.finagle.Service
import com.twitter.finagle.http.{Request, Response}
import com.twitter.util.Future
import com.typesafe.config.Config
import org.jboss.netty.handler.codec.http.HttpResponseStatus

import scala.util.{Failure, Success, Try}


class ContestCreatorHandler (config: Config) extends Service[Request, Response] with Tracing with RedisStore {
  def apply(request: Request): Future[Response] = {
    withTrace("ContestCreatorHandler - #apply", "ContestCreatorHandler") {
      Try(Contest(request)) match {
        case Success(c) => {
          getKeys(s"contest::slug=${c.slug}::name=*::*") map {
            case x if x.isEmpty =>
              val location = s"${config.getString("host")}/contests/${c.slug}"
              hmSet(s"contest::slug=${c.slug}::name=${c.name}::start_date=${c.startDate}::end_date=${c.endDate}::description=${c.description.getOrElse("")}", c.toMap)
              respond("", HttpResponseStatus.CREATED, locationHeader = location)
            case _ => respond("", HttpResponseStatus.CONFLICT)
          }
        }
        case Failure(f) =>
          Future(respond("Errors!", HttpResponseStatus.BAD_REQUEST))
      }
    }
  }
}

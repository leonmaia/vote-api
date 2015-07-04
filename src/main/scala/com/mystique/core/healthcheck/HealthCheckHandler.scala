package com.mystique.core.healthcheck

import com.mystique.server.http.Responses._
import com.twitter.finagle.Service
import com.twitter.finagle.http.{Request, Response}
import com.twitter.util.Future
import org.jboss.netty.handler.codec.http.HttpResponseStatus

class HealthCheckHandler() extends Service[Request, Response] {
  def apply(request: Request): Future[Response] =  {
    Future(respond("It Works! Yay!", HttpResponseStatus.OK))
  }
}

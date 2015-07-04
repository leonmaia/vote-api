package com.mystique.server.http

import com.twitter.finagle.Service
import com.twitter.finagle.http.path.Path
import com.twitter.finagle.http.service.RoutingService
import com.twitter.finagle.http.{Request, Response}
import com.twitter.util.Future
import org.jboss.netty.handler.codec.http.HttpMethod

object HttpRouter {
  def forRoutes(routes: PartialFunction[(HttpMethod, Path), Service[Request,Response]]) =
    new RoutingService(
      new PartialFunction[Request, Service[Request, Response]] {
        def apply(request: Request) = new Service[Request, Response] {
          override def apply(request: Request): Future[Response] = routes(request.method -> Path(request.path))(request)
        }

        def isDefinedAt(request: Request) = routes.isDefinedAt(request.method -> Path(request.path))
      }
    )
}


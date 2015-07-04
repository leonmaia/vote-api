package com.mystique.server

import com.mystique.server.http.HttpRouter
import com.twitter.finagle.http.path._
import com.twitter.finagle.http.Method

trait Router extends Handlers {
  lazy val router = HttpRouter.forRoutes({
    case Method.Get ->  Root / "healthcheck" => heathCheckHandler
  })
}


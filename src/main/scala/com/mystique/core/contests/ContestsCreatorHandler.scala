package com.mystique.core.contests

import com.mystique.util.RedisCommands
import com.twitter.finagle.Service
import com.twitter.finagle.http.{Request, Response}
import com.twitter.finagle.redis.Client
import com.twitter.util.Future


class ContestCreatorHandler (val redis: Client) extends Service[Request, Response] with RedisCommands {

  def apply(request: Request): Future[Response] = ???
}


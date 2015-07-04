package com.mystique.server

import com.twitter.finagle.Redis

import ApiConfig.config
import com.twitter.server.TwitterServer

trait Services extends ConfigLoader {
  self: TwitterServer =>
 lazy val redisClient = Redis.newRichClient(config.getString("redis.host"))
}


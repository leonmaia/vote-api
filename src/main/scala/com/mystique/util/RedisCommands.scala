package com.mystique.util

import com.twitter.finagle.redis.Client
import com.twitter.finagle.redis.util.{CBToString, StringToChannelBuffer}
import com.twitter.util.Future

trait RedisCommands {
  def simpleGet(client: Client, key: String) = {
    val k = StringToChannelBuffer(key)
    client.get(k) flatMap {
      case Some(v) => Future(Some(CBToString(v)))
      case _ => Future.value(None)
    }
  }

  def simpleSet(client: Client, key: String, value: String) = {
    val k = StringToChannelBuffer(key)
    val v = StringToChannelBuffer(value)
    client.set(k, v)
  }

  def simpleINCR(redisClient: Client, key: String) = {
    val k = StringToChannelBuffer(key)
    redisClient.incr(k)
  }

  def simpleHMSet(redisClient: Client, key: String, hm: Map[String, String]) = {
    val k = StringToChannelBuffer(key)
    val v = hm.map(r => StringToChannelBuffer(r._1) -> StringToChannelBuffer(r._2))
    redisClient.hMSet(k, v)
  }

  def simpleHMGet(redisClient: Client, key: String) = {
    val k = StringToChannelBuffer(key)
    redisClient.hGetAll(k) map (
      b => b map(cb => CBToString(cb._1) -> CBToString(cb._2))) map (r => r.toMap)
  }

  def simpleGetKeys(redisClient: Client, pattern: String): Future[Seq[String]] = {
    val k = StringToChannelBuffer(pattern)
    redisClient.keys(k) map (b => b map (CBToString(_)))
  }
}

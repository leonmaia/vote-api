package com.mystique.server

import com.twitter.finagle.redis.util.{CBToString, StringToChannelBuffer}
import com.twitter.util.Future

trait RedisStore extends DataStore {

  def get(key: String): Future[Option[String]] = {
    val k = StringToChannelBuffer(key)
    redisClient.get(k) flatMap {
      case Some(v) => Future(Some(CBToString(v)))
      case _ => Future.value(None)
    }
  }

  def incrWithExpire(key: String, value: String, ttl: Long ) = {
    incr(key) onSuccess { r =>
      redisClient.expire(StringToChannelBuffer(key), ttl)
    }
  }

  def incr(key: String) = {
    val k = StringToChannelBuffer(key)
    redisClient.incr(k).map(_.toLong)
  }

  def hmSet(key: String, hm: Map[String, String]): Future[Unit] = {
    val k = StringToChannelBuffer(key)
    val v = hm.map(r => StringToChannelBuffer(r._1) -> StringToChannelBuffer(r._2))
    redisClient.hMSet(k, v)
  }

  def getKeys(pattern: String): Future[Seq[String]] = {
    val k = StringToChannelBuffer(pattern)
    redisClient.keys(k) map (b => b map (CBToString(_)))
  }
}

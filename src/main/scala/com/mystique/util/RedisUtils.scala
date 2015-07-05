package com.mystique.util

import com.twitter.finagle.redis.Client
import com.twitter.finagle.redis.util.{CBToString, StringToChannelBuffer}
import com.twitter.util.Future

object RedisUtils {
 implicit class SimpleRedis(val client: Client) {
   def simpleGet(key: String): Future[Option[String]] = {
     val k = StringToChannelBuffer(key)
     client.get(k) flatMap {
       case Some(v) => Future(Some(CBToString(v)))
       case _ => Future.value(None)
     }
   }

   def simpleSet(key: String, value: String): Future[Unit] = {
     val k = StringToChannelBuffer(key)
     val v = StringToChannelBuffer(value)
     client.set(k, v)
   }

   def simpleINCR(key: String) = {
     val k = StringToChannelBuffer(key)
     client.incr(k)
   }

   def simpleHMSet(key: String, hm: Map[String, String]): Future[Unit] = {
     val k = StringToChannelBuffer(key)
     val v = hm.map(r => StringToChannelBuffer(r._1) -> StringToChannelBuffer(r._2))
     client.hMSet(k, v)
   }

   def simpleGetKeys(pattern: String): Future[Seq[String]] = {
     val k = StringToChannelBuffer(pattern)
     client.keys(k) map (b => b map (CBToString(_)))
   }
 }
}

package com.mystique.util

import com.twitter.finagle.redis.Client
import com.twitter.finagle.redis.util.{CBToString, StringToChannelBuffer}
import com.twitter.util.{Await, Future}

import scala.collection.mutable.ListBuffer

object RedisUtils {
 implicit class SimpleRedis(val client: Client) {
   def simpleGet(key: String) = {
     val k = StringToChannelBuffer(key)
     client.get(k) flatMap {
       case Some(v) => Future(Some(CBToString(v)))
       case _ => Future.value(None)
     }
   }

   def simpleSet(key: String, value: String) = {
     val k = StringToChannelBuffer(key)
     val v = StringToChannelBuffer(value)
     client.set(k, v)
   }

   def simpleINCR(key: String) = {
     val k = StringToChannelBuffer(key)
     client.incr(k)
   }

   def simpleHMSet(key: String, hm: Map[String, String]) = {
     val k = StringToChannelBuffer(key)
     val v = hm.map(r => StringToChannelBuffer(r._1) -> StringToChannelBuffer(r._2))
     client.hMSet(k, v)
   }

   def simpleHMGet(key: String) = {
     val k = StringToChannelBuffer(key)
     client.hGetAll(k) map (
       b => b map(cb => CBToString(cb._1) -> CBToString(cb._2))) map (r => r.toMap)
   }

   def simpleHMGetAll(keys: Seq[String]) = {
     val arr = new ListBuffer[Map[String, String]]
     keys.map(r => Await.result(simpleHMGet(r))) map(arr += _)
     Future(Option(arr))
   }
   def simpleGetKeys(pattern: String): Future[Seq[String]] = {
     val k = StringToChannelBuffer(pattern)
     client.keys(k) map (b => b map (CBToString(_)))
   }
 }
}

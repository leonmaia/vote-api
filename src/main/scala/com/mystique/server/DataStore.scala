package com.mystique.server

import com.twitter.finagle.Redis


trait DataStore {
   var redisClient = Redis.newRichClient("localhost:6379")
}

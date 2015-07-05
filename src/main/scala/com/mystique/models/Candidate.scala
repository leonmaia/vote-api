package com.mystique.models

import com.fasterxml.jackson.databind.JsonMappingException
import com.mystique.util.JsonSupport
import com.twitter.finagle.http.Request


object Candidate extends JsonSupport {
  def apply(request: Request): Candidate={
    try {
      fromJson[Candidate](request.getContentString())
    }  catch {
      case e: JsonMappingException => {
        throw e.getCause
      }
    }
  }

  def fromKey(key: String): Candidate = {
    var map = scala.collection.mutable.Map[String, String]()
    key.split("::").toList map(x => map += x.split("=").head -> x.split("=").last)

    Candidate(map.getOrElse("id", "").toInt, map.getOrElse("name", ""), map.get("avatar"))
  }
}

case class Candidate(id: Int,
                     name: String,
                     avatar: Option[String] = None) {

  require(name.nonEmpty, "name is required")
  require(name.length > 2 && name.length < 11, "name is maximum 10 and minimum 3")

  require(avatar.nonEmpty, "name is required")
  require(avatar.getOrElse("").length < 256, "avatar should be maximum 255")

  def toMap = {
    Map(
      "id" -> id.toString,
      "name" -> name,
      "avatar" -> avatar.getOrElse("")
    )
  }
}


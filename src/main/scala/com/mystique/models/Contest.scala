package com.mystique.models

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.JsonMappingException
import com.mystique.util.DateSupport._
import com.mystique.util.JsonSupport
import com.twitter.finagle.http.Request

object Contest extends JsonSupport{
  def apply(request: Request): Contest ={
    try {
      fromJson[Contest](request.getContentString())
    }  catch {
      case e: JsonMappingException => {
        throw e.getCause
      }
    }
  }

  def fromMap(map: Map[String, String]): Contest = {
    Contest(
      map.getOrElse("slug", ""),
      map.getOrElse("name", ""),
      map.get("description"),
      map.getOrElse("start_date", ""),
      map.getOrElse("end_date", ""))

  }
}

case class Contest(slug: String,
                   name: String,
                   description: Option[String],
                   @JsonProperty("start_date")startDate: String,
                   @JsonProperty("end_date")endDate: String ) {

  require(slug.nonEmpty, "slug is required")
  require(slug.length > 2 && slug.length < 21, "slug is maximum 20 and minimum 3")

  require(name.nonEmpty, "name is required")
  require(name.length > 2 && name.length < 11, "name is maximum 10 and minimum 3")

  require(description.getOrElse("").length < 256, "description should be maximum 255")

  require(startDate.nonEmpty, "start_date is required")
  require(isValidDate(startDate), "start_date should be a date")

  require(endDate.nonEmpty, "end_date is required")
  require(isValidDate(endDate), "end_date should be a date")

  require(fmt.parse(startDate).before(fmt.parse(endDate)), "start_date should be before end_date")

  def toMap = {
    Map(
      "slug" -> slug,
      "name" -> name,
      "description" -> description.getOrElse(""),
      "start_date" -> startDate,
      "end_date" -> endDate
    )
  }
}

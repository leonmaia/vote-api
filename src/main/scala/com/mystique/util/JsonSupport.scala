package com.mystique.util

import com.fasterxml.jackson.annotation.JsonInclude.Include
import com.fasterxml.jackson.databind.{DeserializationFeature, ObjectMapper}
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper

trait JsonSupport {
  private val mapper = new ObjectMapper with ScalaObjectMapper
  mapper.disable(DeserializationFeature.WRAP_EXCEPTIONS)
  mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
  mapper.setSerializationInclusion(Include.NON_NULL)
  mapper.registerModule(DefaultScalaModule)

  def toBytes(obj: Any) = mapper.writeValueAsBytes(obj)

  def toJson(obj: Any) = mapper.writeValueAsString(obj)

  def fromJson[T](json: String)(implicit m: Manifest[T]): T = {
    mapper.readValue[T](json)
  }
}


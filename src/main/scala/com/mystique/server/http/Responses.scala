package com.mystique.server.http

import com.mystique.util.JsonSupport
import com.twitter.finagle.http.Response
import org.jboss.netty.buffer.ChannelBuffers.{wrappedBuffer => toBuffer}
import org.jboss.netty.handler.codec.http.HttpResponseStatus

object Responses extends JsonSupport {

  def respond(obj: Any, status: HttpResponseStatus = HttpResponseStatus.OK, contentType: String = "application/json"): Response = {
    val res = Response()
    res.setStatusCode(status.getCode)
    res.setContentType(contentType)
    res.setContent(serialize(obj))

    res.headers().set("Access-Control-Allow-Origin", "*")
    res
  }

  private def serialize(obj: Any) = {
    obj match {
      case s:String => toBuffer(s.getBytes)
      case a:Array[Byte] => toBuffer(a)
      case _ => toBuffer(toBytes(obj))
    }
  }
}


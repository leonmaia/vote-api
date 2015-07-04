package unit.com.mystique.server.http

import com.mystique.server.http.Responses

import org.jboss.netty.handler.codec.http.HttpResponseStatus
import org.scalatest.{FlatSpec, Matchers}

class ResponsesSpec extends FlatSpec with Matchers{

  it should "create bad request response" in {
    val bad = Responses.respond("", HttpResponseStatus.BAD_REQUEST)
    bad.getStatusCode() shouldBe 400
  }

  it should "create response with string response" in {
    val content = "yeah yeah"
    val contentType = "text/plain"

    val resp = Responses.respond(content, HttpResponseStatus.CONFLICT, contentType )
    resp.getStatusCode shouldBe 409
    resp.getContentString shouldBe content
    resp.contentType.get shouldBe contentType + ";charset=utf-8"
  }

   it should "create response with json response" in {
    val content = Map("name" -> "john", "age" -> 34)

    val resp = Responses.respond(content, HttpResponseStatus.OK )
    resp.getStatusCode shouldBe 200
    resp.getContentString shouldBe """{"name":"john","age":34}"""
    resp.contentType.get shouldBe "application/json;charset=utf-8"
  }
}

package unit.com.mystique.core.candidate

import com.mystique.core.candidates.CandidateHandler
import com.mystique.models.Candidate
import com.mystique.util.JsonSupport
import com.twitter.finagle.http.Request
import com.twitter.finagle.redis.Client
import com.twitter.util.Await
import com.typesafe.config.Config
import org.jboss.netty.handler.codec.http.HttpMethod
import org.mockito.Matchers.anyString
import org.mockito.Mockito._
import org.scalatest.mock.MockitoSugar
import org.scalatest.{BeforeAndAfter, FlatSpec, Matchers}

class CandidateHandlerSpec extends FlatSpec with Matchers with MockitoSugar with JsonSupport with BeforeAndAfter {
  var redis = mock[Client]
  var config = mock[Config]
  val handler = new CandidateHandler(redis, config)

  def buildRequest(content: String, method: HttpMethod = HttpMethod.GET) = {
    val req = Request()
    req.setContentString(content)
    req.setContentTypeJson()
    req.setMethod(method)

    req
  }

  before {
    when(config.getString(anyString)).thenReturn("localhost:8088")
  }

  behavior of "#create"

  it should "return status code 201" in {
    val req = Candidate(1, "nome", Option("avatar"))
    val response = Await.result(handler.create("vote1")(buildRequest(toJson(req), HttpMethod.POST)))

    response.statusCode should be(201)
    response.location should be(Option("localhost:8088/contest/vote1/candidate"))
  }
}

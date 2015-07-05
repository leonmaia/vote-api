package unit.com.mystique.core.candidate

import com.mystique.core.candidates.CandidateHandler
import com.mystique.models.Candidate
import com.mystique.server.DataStore
import com.mystique.util.JsonSupport
import com.twitter.finagle.http.Request
import com.twitter.finagle.redis.Client
import com.twitter.util.Await
import com.typesafe.config.Config
import org.jboss.netty.handler.codec.http.HttpMethod
import org.mockito.Matchers.anyString
import org.mockito.Mockito._
import org.scalatest.mock.MockitoSugar
import org.scalatest.{BeforeAndAfterEach, BeforeAndAfter, FlatSpec, Matchers}

class CandidateHandlerSpec extends FlatSpec with Matchers with MockitoSugar with JsonSupport with BeforeAndAfter with BeforeAndAfterEach{
  var config = mock[Config]
  var request: Request = _
  var handler: CandidateHandler = _
  implicit var redis: Client = _

  before {
    request = mock[Request]
    redis = mock[Client]
    handler = new CandidateHandler(config) with TestRedisStore
    when(config.getString(anyString)).thenReturn("localhost:8088")
  }

  def buildRequest(content: String, method: HttpMethod = HttpMethod.GET) = {
    val req = Request()
    req.setContentString(content)
    req.setContentTypeJson()
    req.setMethod(method)

    req
  }


  behavior of "#create"

  it should "return status code 201" in {
    val req = Candidate(1, "nome", Option("avatar"))
    val response = Await.result(handler.create("vote1")(buildRequest(toJson(req), HttpMethod.POST)))

    response.statusCode should be(201)
    response.location should be(Option("localhost:8088/contest/vote1/candidate"))
  }

  trait TestRedisStore extends DataStore {
    redisClient = redis
  }
}

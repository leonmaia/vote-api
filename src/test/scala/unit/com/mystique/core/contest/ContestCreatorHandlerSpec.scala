package unit.com.mystique.core.contest

import com.mystique.core.contests.ContestCreatorHandler
import com.mystique.models.Contest
import com.mystique.server.DataStore
import org.mockito.Matchers.any
import com.mystique.util.JsonSupport
import com.twitter.finagle.http.Request
import com.twitter.finagle.redis.Client
import com.twitter.util.{Future, Await}
import com.typesafe.config.Config
import org.jboss.netty.buffer.ChannelBuffer
import org.mockito.Mockito._
import org.scalatest.mock.MockitoSugar
import org.scalatest.{BeforeAndAfter, FlatSpec, Matchers}
import org.mockito.Matchers.anyString

class ContestCreatorHandlerSpec extends FlatSpec with Matchers with MockitoSugar with JsonSupport with BeforeAndAfter {

  var config = mock[Config]
  var request: Request = _
  var handler: ContestCreatorHandler = _
  implicit var redis: Client = _

  before {
    request = mock[Request]
    redis = mock[Client]
    handler = new ContestCreatorHandler(config) with TestRedisStore
    when(config.getString(anyString)).thenReturn("localhost:8088")
    when(redis.keys(any[ChannelBuffer])).thenReturn(Future(Seq.empty))
  }

  def buildRequest(content: String) = {
    val req = Request()
    req.setContentString(content)
    req.setContentTypeJson()

    req
  }

  behavior of "#apply"

  it should "return status code 201" in {
    val req = Contest("teste", "nome",Option("descricao"), "2015-01-10","2015-01-15")
    val response = Await.result(handler.apply(buildRequest(toJson(req))))

    response.statusCode should be(201)
    response.location should be(Option("localhost:8088/contests/teste"))
  }

  it should "return status code 400 if error" in {
    val response = Await.result(handler.apply(buildRequest(toJson(""))))

    response.statusCode should be(400)
  }

  trait TestRedisStore extends DataStore {
    redisClient = redis
  }
}

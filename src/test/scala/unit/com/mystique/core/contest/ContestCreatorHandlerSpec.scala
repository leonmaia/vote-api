package unit.com.mystique.core.contest

import com.mystique.core.contests.ContestCreatorHandler
import com.mystique.models.Contest
import com.mystique.util.JsonSupport
import com.twitter.finagle.http.Request
import com.twitter.finagle.redis.Client
import com.twitter.util.Await
import com.typesafe.config.Config
import org.mockito.Mockito._
import org.scalatest.mock.MockitoSugar
import org.scalatest.{BeforeAndAfter, FlatSpec, Matchers}
import org.mockito.Matchers.anyString

class ContestCreatorHandlerSpec extends FlatSpec with Matchers with MockitoSugar with JsonSupport with BeforeAndAfter {

  var redis = mock[Client]
  var config = mock[Config]
  val handler = new ContestCreatorHandler(redis, config)

  def buildRequest(content: String) = {
    val req = Request()
    req.setContentString(content)
    req.setContentTypeJson()

    req
  }

  before {
    when(config.getString(anyString)).thenReturn("localhost:8088")
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
}

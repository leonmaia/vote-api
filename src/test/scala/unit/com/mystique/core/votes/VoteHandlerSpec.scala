package unit.com.mystique.core.votes

import com.mystique.core.votes.VoteHandler
import com.mystique.server.DataStore
import com.mystique.util.JsonSupport
import com.twitter.finagle.http.Request
import com.twitter.finagle.redis.Client
import com.twitter.finagle.redis.util.StringToChannelBuffer
import com.twitter.util.{Await, Future}
import org.jboss.netty.handler.codec.http.HttpMethod
import org.mockito.Matchers.any
import org.mockito.Mockito._
import org.scalatest.mock.MockitoSugar
import org.scalatest.{BeforeAndAfter, BeforeAndAfterEach, FlatSpec, Matchers}

class VoteHandlerSpec extends FlatSpec with Matchers with MockitoSugar with JsonSupport with BeforeAndAfter with BeforeAndAfterEach{
  var request: Request = _
  var handler: VoteHandler = _
  implicit var redis: Client = _

  before {
    request = mock[Request]
    redis = mock[Client]
    handler = new VoteHandler with TestRedisStore
    import java.lang.Long
    when(redis.incr(any())).thenReturn(Future(new Long("0")))
  }

  def buildRequest(content: String = "", method: HttpMethod = HttpMethod.GET, token: (String,String)= "user-token" -> "" ) = {
    val req = Request()
    req.setContentString(content)
    req.headers().set(token._1, token._2)
    req.setContentTypeJson()
    req.setMethod(method)

    req
  }

  behavior of "#get"

  it should "return status code 200" in {
    when(redis.get(any())).thenReturn(Future(Option(StringToChannelBuffer("1"))))
    val response = Await.result(handler.get("","")(Request()))
    response.statusCode should be(200)
  }

  it should "return status code 400" in {
    when(redis.get(any())).thenReturn(Future(Option(StringToChannelBuffer("Not A Long"))))
    val response = Await.result(handler.get("","")(Request()))
    response.statusCode should be(400)
  }

  it should "return status code 404" in {
    when(redis.get(any())).thenReturn(Future(Option.empty))
    val response = Await.result(handler.get("","")(Request()))
    response.statusCode should be(404)
  }

  behavior of "#result"

  it should "return status code 200" in {
    when(redis.get(any())).thenReturn(Future(Option(StringToChannelBuffer("1"))))
    val response = Await.result(handler.result("")(Request()))
    response.statusCode should be(200)
  }

  it should "return status code 400" in {
    when(redis.get(any())).thenReturn(Future(Option(StringToChannelBuffer("Not A Long"))))
    val response = Await.result(handler.result("")(Request()))
    response.statusCode should be(400)
  }

  it should "return status code 404" in {
    when(redis.get(any())).thenReturn(Future(Option.empty))
    val response = Await.result(handler.result("")(Request()))
    response.statusCode should be(404)
  }

  behavior of "#vote"

  it should "return status code 401" in {
    when(redis.get(any())).thenReturn(Future(Option(StringToChannelBuffer("1"))))
    val response = Await.result(handler.vote("","")(buildRequest(token = "outro-header" -> "", method = HttpMethod.POST)))
    response.statusCode should be(401)
  }

  it should "return status code 403" in {
    when(redis.get(any())).thenReturn(Future(Option(StringToChannelBuffer("6"))))
    val response = Await.result(handler.vote("","")(buildRequest(token = "user-token" -> "lala", method = HttpMethod.POST)))
    response.statusCode should be(403)
  }

  it should "return status code 404" in {
    when(redis.get(any())).thenReturn(Future(Option(StringToChannelBuffer("1"))))
    val response = Await.result(handler.vote("","")(buildRequest(token = "user-token" -> "lala", method = HttpMethod.POST)))
    response.statusCode should be(200)
  }

  trait TestRedisStore extends DataStore {
    redisClient = redis
  }
}

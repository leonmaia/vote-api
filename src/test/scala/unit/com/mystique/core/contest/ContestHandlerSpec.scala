package unit.com.mystique.core.contest

import com.mystique.core.contests.ContestHandler
import com.mystique.server.DataStore
import com.mystique.util.JsonSupport
import com.twitter.finagle.http.Request
import com.twitter.finagle.redis.Client
import com.twitter.util.{Await, Future}
import com.typesafe.config.Config
import org.jboss.netty.buffer.ChannelBuffer
import org.mockito.Matchers.any
import org.mockito.Mockito._
import org.scalatest.mock.MockitoSugar
import org.scalatest.{BeforeAndAfter, FlatSpec, Matchers}

class ContestHandlerSpec extends FlatSpec with Matchers with MockitoSugar with JsonSupport with BeforeAndAfter {

  var request: Request = _
  var handler: ContestHandler= _
  implicit var redis: Client = _

  before {
    request = mock[Request]
    redis = mock[Client]
    handler = new ContestHandler with TestRedisStore
    when(redis.keys(any[ChannelBuffer])).thenReturn(Future(Seq.empty))
  }

  behavior of "#apply"

  it should "return status code 200" in {
    val response = Await.result(handler.apply(Request()))

    response.statusCode should be(200)
  }

  trait TestRedisStore extends DataStore {
    redisClient = redis
  }
}

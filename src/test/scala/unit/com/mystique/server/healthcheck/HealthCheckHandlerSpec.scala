package unit.com.mystique.server.healthcheck

import com.mystique.core.healthcheck.HealthCheckHandler
import com.twitter.finagle.http.Request
import com.twitter.util.Await
import org.scalatest.{FlatSpec, Matchers}

class HealthCheckHandlerSpec extends FlatSpec with Matchers {

  val handler = new HealthCheckHandler()

  behavior of "#apply"

  it should "return status code 200" in {
    val response = Await.result(handler.apply(Request()))

    response.getContentString should include("It Works! Yay!")
    response.statusCode should be(200)
  }
}

package unit.com.mystique.server.http

import com.mystique.server.http.HttpRouter
import com.twitter.finagle.Service
import com.twitter.finagle.http.path.{Root, _}
import com.twitter.finagle.http.{Method, Request, Response}
import com.twitter.util.{Await, Future}
import org.scalatest.{FlatSpec, Matchers}

class HttpRouterSpec extends FlatSpec with Matchers {

  val router = HttpRouter.forRoutes({
    case Method.Get -> Root / "test" => new Service[Request, Response] {
      override def apply(request: Request): Future[Response] = Future(Response())
    }
  })

  it should "route to correct service" in {
    val resp = Await.result(router(Request("/test")))
    resp.getStatusCode() shouldBe 200
  }

  it should "return 404 if route does not exist" in {
    val resp = Await.result(router(Request("/notfound")))
    resp.getStatusCode() shouldBe 404
  }
}


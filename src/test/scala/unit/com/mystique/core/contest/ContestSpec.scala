package unit.com.mystique.core.contest

import com.mystique.core.candidates.CandidateHandler
import com.mystique.models.{Contest, Candidate}
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

class ContestSpec extends FlatSpec with Matchers with MockitoSugar with JsonSupport with BeforeAndAfter {

  behavior of "#fromKey"

  it should "parse from redis key" in {
    val possibleKey =
      "contest::slug=thevoice::name=Globo::start_date=2015-06-20::end_date=2016-01-01::description=the voice brazil 2015"

    val contest = Contest.fromKey(possibleKey)

    contest.name should be("Globo")
    contest.description should be(Some("the voice brazil 2015"))
    contest.startDate should be("2015-06-20")
    contest.endDate should be("2016-01-01")
  }
}

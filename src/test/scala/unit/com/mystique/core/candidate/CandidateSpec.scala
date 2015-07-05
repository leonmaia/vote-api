package unit.com.mystique.core.candidate

import com.mystique.models.Candidate
import com.mystique.util.JsonSupport
import org.scalatest.mock.MockitoSugar
import org.scalatest.{BeforeAndAfter, FlatSpec, Matchers}

class CandidateSpec extends FlatSpec with Matchers with MockitoSugar with JsonSupport with BeforeAndAfter {

  behavior of "#fromKey"

  it should "parse from redis key" in {
    val possibleKey = "candidate:contest=thevoice:id=1:name=Garoto:avatar=avatar.jpg"
    val candidate = Candidate.fromKey(possibleKey)

    candidate.name should be("Garoto")
    candidate.avatar should be(Some("avatar.jpg"))
    candidate.id should be(1)
  }
}

import org.scalatestplus.play.PlaySpec
import models.{MonitoringTeam, MonitoringTeamRepository}
import services.MonitoringTeamService
import java.util.Date
import play.api.libs.json._
import org.mockito.Mock
import org.mockito.Mockito._
import org.scalatest.mock.MockitoSugar

class MonitoringTeamSpec extends PlaySpec with MockitoSugar {

  "MonitoringTeamService#findAll" should {
    "find no JsonValue" in {
      val events = List()
      val repo = mock[MonitoringTeamRepository]
      when(repo.findlAll()) thenReturn events

      val service = new MonitoringTeamService(repo)
      val actual = service.findAll()
      actual mustBe None
    }

    "find JsonValue including all data" in {
      val events = List(new MonitoringTeam(id = Some(1), name = "team 1"),
      									new MonitoringTeam(id = Some(2), name = "team 2"))
      val repo = mock[MonitoringTeamRepository]
      when(repo.findlAll()) thenReturn events

      val service = new MonitoringTeamService(repo)
      val actual = service.findAll()
      actual must not be empty
      actual.get.as[JsArray].value.size mustBe events.size
      var idx = 0
      for (event <- events) {
        (actual.get(idx) \ "id").as[Int] mustBe (event.id.get)
        (actual.get(idx) \ "name").as[String] mustBe (event.name)
        idx = idx + 1
    	}
  	}
  }

}

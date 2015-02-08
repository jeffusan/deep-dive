import org.scalatestplus.play.PlaySpec
import models.{SurveyEvent, SurveyEventRepository}
import services.SurveyEventService
import java.util.Date
import play.api.libs.json._
import org.mockito.Mock
import org.mockito.Mockito._
import org.scalatest.mock.MockitoSugar

class SurveyEventSpec extends PlaySpec with MockitoSugar {

  "SurveyEventService#findBySiteId" should {
    "find None using" in {
      val events = List()
      val repo = mock[SurveyEventRepository]
      when(repo.findBySiteId(0)) thenReturn events

      val service = new SurveyEventService(repo)
      val actual = service.findBySiteId(0)
      actual.size mustBe 0
    }

    "find list with one object" in {
      val events = List(new SurveyEvent(Some(1),  1, new Date ,"p1", "a1", 1, 1, Json.parse("""{"test1": "value1"}""")))
      val repo = mock[SurveyEventRepository]
      when(repo.findBySiteId(1)) thenReturn events

      val service = new SurveyEventService(repo)
      val actual = service.findBySiteId(1)
      actual must not be empty
      actual.size mustBe events.size

      val surveyEvent = actual(0)

      surveyEvent.id.get mustBe 1
      surveyEvent.siteId mustBe 1

    }

    "find list with two objects" in {
      val events = List(new SurveyEvent(Some(1), 2, new Date, "p1", "a1", 1, 1, Json.parse("""{"test2":[{"test21": "value21"},{"test22": "value22"}]}""")),
                        new SurveyEvent(Some(2), 2, new Date, "p2", "a2", 2, 2, Json.parse("""{"test2":[{"test21": "value21"},{"test22": "value22"}]}""")))
      val repo = mock[SurveyEventRepository]
      when(repo.findBySiteId(2)) thenReturn events

      val service = new SurveyEventService(repo)
      val actual = service.findBySiteId(2)
      actual must not be empty
      actual.size mustBe events.size

      val sv = actual(0)
      sv.id.get mustBe 1
    }
  }

  "SurveyEventService#findByEventDate" should {
    "find empty list using" in {
      val eventDate = new Date
      val events = List()
      val repo = mock[SurveyEventRepository]
      when(repo.findByEventDate(eventDate)) thenReturn events

      val service = new SurveyEventService(repo)
      val actual = service.findByEventDate(eventDate)
      actual.size mustBe 0
    }

    "find Json Value included one object" in {
      val eventDate = new Date
      val events = List(new SurveyEvent(Some(1), 1, eventDate, "p1", "a1", 1, 1, Json.parse("""{"test1": "value1"}""")))
      val repo = mock[SurveyEventRepository]
      when(repo.findByEventDate(eventDate)) thenReturn events

      val service = new SurveyEventService(repo)
      val actual = service.findByEventDate(eventDate)
      actual must not be empty
      actual.size mustBe events.size
    }

    "find Json Value included two objects" in {
      val eventDate = new Date
      val events = List(new SurveyEvent(Some(1), 2, eventDate, "p1", "a1", 1, 1, Json.parse("""{"test2":[{"test21": "value21"},{"test22": "value22"}]}""")),
                        new SurveyEvent(Some(2), 2, eventDate, "p2", "a2", 2, 2, Json.parse("""{"test2":[{"test21": "value21"},{"test22": "value22"}]}""")))
      val repo = mock[SurveyEventRepository]
      when(repo.findByEventDate(eventDate)) thenReturn events

      val service = new SurveyEventService(repo)
      val actual = service.findByEventDate(eventDate)
      actual must not be empty
      actual.size mustBe events.size
    }
  }

  "SurveyEventService#findByMonitoringTeamId" should {
    "find None using" in {
      val events = List()
      val repo = mock[SurveyEventRepository]
      when(repo.findByMonitoringTeamId(0)) thenReturn events

      val service = new SurveyEventService(repo)
      val actual = service.findByMonitoringTeamId(0)
      actual.size mustBe 0
    }

    "find Json Value included one object" in {
      val events = List(new SurveyEvent(Some(1), 1, new Date, "p1", "a1", 1, 1, Json.parse("""{"test1": "value1"}""")))
      val repo = mock[SurveyEventRepository]
      when(repo.findByMonitoringTeamId(1)) thenReturn events

      val service = new SurveyEventService(repo)
      val actual = service.findByMonitoringTeamId(1)
      actual must not be empty
      actual.size mustBe events.size
    }

    "find Json Value included two objects" in {
      val events = List(new SurveyEvent(Some(1), 2, new Date, "p1", "a1", 1, 1, Json.parse("""{"test2":[{"test21": "value21"},{"test22": "value22"}]}""")),
                        new SurveyEvent(Some(2), 2, new Date, "p2", "a2", 2, 2, Json.parse("""{"test2":[{"test21": "value21"},{"test22": "value22"}]}""")))
      val repo = mock[SurveyEventRepository]
      when(repo.findByMonitoringTeamId(2)) thenReturn events

      val service = new SurveyEventService(repo)
      val actual = service.findByMonitoringTeamId(2)
      actual must not be empty
      actual.size mustBe events.size
    }
  }

}

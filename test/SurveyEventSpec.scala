import org.scalatestplus.play.PlaySpec
import models.{SurveyEvent, SurveyEventRepository}
import services.SurveyEventService
import java.util.Date
import play.api.libs.json._
import org.mockito.Mock
import org.mockito.Mockito._
import org.scalatest.mock.MockitoSugar

class SurveyEventSpec extends PlaySpec with MockitoSugar {

  "SurveyEventService#findAllBySiteId" should {
    "find None using" in {
      val events = List()
      val repo = mock[SurveyEventRepository]
      when(repo.findAllBySiteId(0)) thenReturn events

      val service = new SurveyEventService(repo)
      val actual = service.findAllBySiteId(0)
      actual mustBe None
    }

    "find JsonValue included one object" in {
      val events = List(new SurveyEvent(Some(1),  1, new Date ,"p1", "a1", 1, 1, Json.parse("""{"test1": "value1"}""")))
      val repo = mock[SurveyEventRepository]
      when(repo.findAllBySiteId(1)) thenReturn events

      val service = new SurveyEventService(repo)
      val actual = service.findAllBySiteId(1)
      actual must not be empty
      actual.get.as[JsArray].value.size mustBe events.size
      var idx = 0
      for (event <- events) {
        (actual.get(idx) \ "id").as[Int] mustBe (event.id.get)
        (actual.get(idx) \ "siteId").as[Int] mustBe (event.siteId)
        (actual.get(idx) \ "eventDate").as[Date] mustBe (event.eventDate)
        (actual.get(idx) \ "photographer").as[String] mustBe (event.photographer)
        (actual.get(idx) \ "analyzer").as[String] mustBe (event.analyzer)
        (actual.get(idx) \ "transectLength").as[Short] mustBe (event.transectLength)
        (actual.get(idx) \ "transectLength").as[Short] mustBe (event.transectLength)
        (actual.get(idx) \ "data") mustBe (event.data)
        idx = idx + 1
      }
    }

    "find JsonValue included two objects" in {
      val events = List(new SurveyEvent(Some(1), 2, new Date, "p1", "a1", 1, 1, Json.parse("""{"test2":[{"test21": "value21"},{"test22": "value22"}]}""")),
                        new SurveyEvent(Some(2), 2, new Date, "p2", "a2", 2, 2, Json.parse("""{"test2":[{"test21": "value21"},{"test22": "value22"}]}""")))
      val repo = mock[SurveyEventRepository]
      when(repo.findAllBySiteId(2)) thenReturn events

      val service = new SurveyEventService(repo)
      val actual = service.findAllBySiteId(2)
      actual must not be empty
      actual.get.as[JsArray].value.size mustBe events.size
      var idx = 0
      for (event <- events) {
        (actual.get(idx) \ "id").as[Int] mustBe (event.id.get)
        (actual.get(idx) \ "siteId").as[Int] mustBe (event.siteId)
        (actual.get(idx) \ "eventDate").as[Date] mustBe (event.eventDate)
        (actual.get(idx) \ "photographer").as[String] mustBe (event.photographer)
        (actual.get(idx) \ "analyzer").as[String] mustBe (event.analyzer)
        (actual.get(idx) \ "transectLength").as[Short] mustBe (event.transectLength)
        (actual.get(idx) \ "transectLength").as[Short] mustBe (event.transectLength)
        (actual.get(idx) \ "data") mustBe (event.data)
        idx = idx + 1
      }
    }
  }

  "SurveyEventService#findAllByEventDate" should {
    "find None using" in {
      val eventDate = new Date
      val events = List()
      val repo = mock[SurveyEventRepository]
      when(repo.findAllByEventDate(eventDate)) thenReturn events

      val service = new SurveyEventService(repo)
      val actual = service.findAllByEventDate(eventDate)
      actual mustBe None
    }

    "find Json Value included one object" in {
      val eventDate = new Date
      val events = List(new SurveyEvent(Some(1), 1, eventDate, "p1", "a1", 1, 1, Json.parse("""{"test1": "value1"}""")))
      val repo = mock[SurveyEventRepository]
      when(repo.findAllByEventDate(eventDate)) thenReturn events

      val service = new SurveyEventService(repo)
      val actual = service.findAllByEventDate(eventDate)
      actual must not be empty
      actual.get.as[JsArray].value.size mustBe events.size
      var idx = 0
      for (event <- events) {
        (actual.get(idx) \ "id").as[Int] mustBe (event.id.get)
        (actual.get(idx) \ "siteId").as[Int] mustBe (event.siteId)
        (actual.get(idx) \ "eventDate").as[Date] mustBe (event.eventDate)
        (actual.get(idx) \ "photographer").as[String] mustBe (event.photographer)
        (actual.get(idx) \ "analyzer").as[String] mustBe (event.analyzer)
        (actual.get(idx) \ "transectLength").as[Short] mustBe (event.transectLength)
        (actual.get(idx) \ "transectLength").as[Short] mustBe (event.transectLength)
        (actual.get(idx) \ "data") mustBe (event.data)
        idx = idx + 1
      }
    }

    "find Json Value included two objects" in {
      val eventDate = new Date
      val events = List(new SurveyEvent(Some(1), 2, eventDate, "p1", "a1", 1, 1, Json.parse("""{"test2":[{"test21": "value21"},{"test22": "value22"}]}""")),
                        new SurveyEvent(Some(2), 2, eventDate, "p2", "a2", 2, 2, Json.parse("""{"test2":[{"test21": "value21"},{"test22": "value22"}]}""")))
      val repo = mock[SurveyEventRepository]
      when(repo.findAllByEventDate(eventDate)) thenReturn events

      val service = new SurveyEventService(repo)
      val actual = service.findAllByEventDate(eventDate)
      actual must not be empty
      actual.get.as[JsArray].value.size mustBe events.size
      var idx = 0
      for (event <- events) {
        (actual.get(idx) \ "id").as[Int] mustBe (event.id.get)
        (actual.get(idx) \ "siteId").as[Int] mustBe (event.siteId)
        (actual.get(idx) \ "eventDate").as[Date] mustBe (event.eventDate)
        (actual.get(idx) \ "photographer").as[String] mustBe (event.photographer)
        (actual.get(idx) \ "analyzer").as[String] mustBe (event.analyzer)
        (actual.get(idx) \ "transectLength").as[Short] mustBe (event.transectLength)
        (actual.get(idx) \ "transectLength").as[Short] mustBe (event.transectLength)
        (actual.get(idx) \ "data") mustBe (event.data)
        idx = idx + 1
      }
    }
  }

  "SurveyEventService#findAllByMonitoringTeamId" should {
    "find None using" in {
      val events = List()
      val repo = mock[SurveyEventRepository]
      when(repo.findAllByMonitoringTeamId(0)) thenReturn events

      val service = new SurveyEventService(repo)
      val actual = service.findAllByMonitoringTeamId(0)
      actual mustBe None
    }

    "find Json Value included one object" in {
      val events = List(new SurveyEvent(Some(1), 1, new Date, "p1", "a1", 1, 1, Json.parse("""{"test1": "value1"}""")))
      val repo = mock[SurveyEventRepository]
      when(repo.findAllByMonitoringTeamId(1)) thenReturn events

      val service = new SurveyEventService(repo)
      val actual = service.findAllByMonitoringTeamId(1)
      actual must not be empty
      actual.get.as[JsArray].value.size mustBe events.size
      var idx = 0
      for (event <- events) {
        (actual.get(idx) \ "id").as[Int] mustBe (event.id.get)
        (actual.get(idx) \ "siteId").as[Int] mustBe (event.siteId)
        (actual.get(idx) \ "eventDate").as[Date] mustBe (event.eventDate)
        (actual.get(idx) \ "photographer").as[String] mustBe (event.photographer)
        (actual.get(idx) \ "analyzer").as[String] mustBe (event.analyzer)
        (actual.get(idx) \ "transectLength").as[Short] mustBe (event.transectLength)
        (actual.get(idx) \ "transectLength").as[Short] mustBe (event.transectLength)
        (actual.get(idx) \ "data") mustBe (event.data)
        idx = idx + 1
      }
    }

    "find Json Value included two objects" in {
      val events = List(new SurveyEvent(Some(1), 2, new Date, "p1", "a1", 1, 1, Json.parse("""{"test2":[{"test21": "value21"},{"test22": "value22"}]}""")),
                        new SurveyEvent(Some(2), 2, new Date, "p2", "a2", 2, 2, Json.parse("""{"test2":[{"test21": "value21"},{"test22": "value22"}]}""")))
      val repo = mock[SurveyEventRepository]
      when(repo.findAllByMonitoringTeamId(2)) thenReturn events

      val service = new SurveyEventService(repo)
      val actual = service.findAllByMonitoringTeamId(2)
      actual must not be empty
      actual.get.as[JsArray].value.size mustBe events.size
      var idx = 0
      for (event <- events) {
        (actual.get(idx) \ "id").as[Int] mustBe (event.id.get)
        (actual.get(idx) \ "siteId").as[Int] mustBe (event.siteId)
        (actual.get(idx) \ "eventDate").as[Date] mustBe (event.eventDate)
        (actual.get(idx) \ "photographer").as[String] mustBe (event.photographer)
        (actual.get(idx) \ "analyzer").as[String] mustBe (event.analyzer)
        (actual.get(idx) \ "transectLength").as[Short] mustBe (event.transectLength)
        (actual.get(idx) \ "transectLength").as[Short] mustBe (event.transectLength)
        (actual.get(idx) \ "data") mustBe (event.data)
        idx = idx + 1
      }
    }
  }

}

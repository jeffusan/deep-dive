import org.scalatestplus.play.PlaySpec
import org.mockito.Mock
import org.mockito.Mockito._
import org.scalatest.mock.MockitoSugar
import play.api.test._
import play.api.test.Helpers._

import play.api.libs.json._
import play.api.Logger

import java.util.Date

import test.controllers._

class SurveyEventsServiceSpec extends PlaySpec with MockitoSugar with ControllerUnitTest {

  import models.SurveyEvent
  import services.SurveyEventService
  import controllers.SurveyEvents

  "SurveyEvents API" should {
    "can return None" in {
      withFakeApplication {
        val siteId: Long = 0
        val events: Seq[SurveyEvent] = List()
    
        val mockService = mock[SurveyEventService]
        when(mockService.findBySiteId(siteId)) thenReturn events

        val controller = new SurveyEvents {
          override lazy val service = mockService
        }

        val response = controller.findBySiteId(siteId)

        response.header.status mustBe 200

        response.header.headers.contains("Content-Type") mustBe true
        val contentTypeValue = response.header.headers.get("Content-Type")
        contentTypeValue.get mustBe "application/json; charset=utf-8"

        val body = Utils.extractBody(response.body)
        val json = Json.parse(body)
        json mustBe Json.toJson(events)
      }
    }

    "can return List which contains a SurveyEvent object" in {
      withFakeApplication {
        val siteId: Long = 1
        val events = List(new SurveyEvent(Some(1),  1, new Date ,"p1", "a1", 1, 1, Json.parse("""{"test1": "value1"}""")))
    
        val mockService = mock[SurveyEventService]
        when(mockService.findBySiteId(siteId)) thenReturn events

        val controller = new SurveyEvents {
          override lazy val service = mockService
        }

        val response = controller.findBySiteId(siteId)

        response.header.status mustBe 200

        response.header.headers.contains("Content-Type") mustBe true
        val contentTypeValue = response.header.headers.get("Content-Type")
        contentTypeValue.get mustBe "application/json; charset=utf-8"

        val body = Utils.extractBody(response.body)
        val json = Json.parse(body)
        json mustBe Json.toJson(events)
      }
    }

    "can return List which contains some SurveyEvent objects" in {
      withFakeApplication {
        val siteId: Long = 2
        val events = List(new SurveyEvent(Some(1), 2, new Date, "p1", "a1", 1, 1, Json.parse("""{"test2":[{"test21": "value21"},{"test22": "value22"}]}""")),
                          new SurveyEvent(Some(2), 2, new Date, "p2", "a2", 2, 2, Json.parse("""{"test2":[{"test21": "value21"},{"test22": "value22"}]}""")))

        val mockService = mock[SurveyEventService]
        when(mockService.findBySiteId(siteId)) thenReturn events

        val controller = new SurveyEvents {
          override lazy val service = mockService
        }

        val response = controller.findBySiteId(siteId)

        response.header.status mustBe 200

        response.header.headers.contains("Content-Type") mustBe true
        val contentTypeValue = response.header.headers.get("Content-Type")
        contentTypeValue.get mustBe "application/json; charset=utf-8"

        val body = Utils.extractBody(response.body)
        val json = Json.parse(body)
        json mustBe Json.toJson(events)
      }
    }
  }
}






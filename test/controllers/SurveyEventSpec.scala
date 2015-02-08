
import org.scalatest._

import play.api.libs.json._
import play.api.test._
import play.api.test.Helpers._
import play.api.Logger
import test.controllers.Authentication

class SurveyEventsServiceSpec extends FunSpec with GivenWhenThen with Matchers with Authentication {

  import models.SurveyEvent

  describe("SurveyEvents API") {

    describe("GET /eventsBySiteId") {
      it("should return all survey events by site id") {
        withAuthentication { authenticatedHeaders =>
          val siteId = 1
          Given(s"site id is ${siteId}.")

          val request = FakeRequest(GET, s"/eventsBySiteId/${siteId}").withHeaders(authenticatedHeaders: _*)

        }
      }
    }

  }
}

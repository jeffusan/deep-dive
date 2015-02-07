
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

					val request = FakeRequest(GET, s"/eventsBySiteId/${siteId}").withHeaders(authenticatedHeaders:_*)

					val response = route(request).get
					Then("StatusCode is 200")
					status(response) shouldBe OK

					val resultBody = contentAsJson(response)
					val mtMap = resultBody.as[Seq[SurveyEvent]] map {e => e.id -> e} toMap

					And("There are one survey event")
					mtMap.size shouldBe 1

					And("Attribute data is expected")
					val value = mtMap.get(Some(1)).get
					value.id shouldBe Some(1)
					value.siteId shouldBe 1
					value.eventDate.toString shouldBe "Wed May 30 00:00:00 JST 2012"
					value.photographer shouldBe "photag"
					value.analyzer shouldBe "analyzer" 
					value.transectDepth shouldBe 50
					value.transectLength shouldBe 50
					value.data shouldBe Json.parse("""{"id":"id1","name":"name1"}""")
				}
			}
		}

	}
}
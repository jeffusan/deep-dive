
import org.scalatest._

import play.api.libs.json._
import play.api.test._
import play.api.test.Helpers._
import play.api.Logger
import test.controllers.Authentication

class MonitoringTeamServiceSpec extends FunSpec with GivenWhenThen with Matchers with Authentication {

	import models.MonitoringTeam

	describe("MonitoringTeams API") {

		describe("GET /monitoringTeams") {
			it("should return all MonitoringTeams") {
				withAuthentication { authenticatedHeaders =>
					Given("no parameter.")
					val request = FakeRequest(GET, "/monitoringTeams").withHeaders(authenticatedHeaders:_*)

					val response = route(request).get
					Then("StatusCode is 200")
					status(response) shouldBe OK

					val resultBody = contentAsJson(response)
					val mtMap = resultBody.as[Seq[MonitoringTeam]] map {e => e.id -> e} toMap

					And("There are more than 10 of the monitoring team")
					mtMap.size should be >= 10

					for( i <- 0 to 9) {
						val id = i + 1
						mtMap should contain key(Some(id))
						And(s"The name is Monitoring ${id}")
						mtMap should contain value(MonitoringTeam(Some(id), s"Monitoring ${id}"))
					}
				}
			}
		}

		val id = 11
		val name = "New Monitoring Team"

		describe(	"POST /monitoringTeams") {
			it("should create a MonitoringTeam") {
				withAuthentication { authenticatedHeaders =>
					Given(s"name is ${name}.")
					val requestBody = Json.parse(s""" {"name" : "${name}"} """)

					val request = FakeRequest(POST, "/monitoringTeams").withHeaders(authenticatedHeaders:_*).withBody(requestBody)

					val response = route(request).get

					Then("StatusCode is 200")
					status(response) shouldBe OK
					And("Message contains OK")
					contentAsJson(response) shouldBe controllers.Application.messageOk
				}
			}
		}

		describe("PUT /monitoringTeams") {
			it("should modify a MonitoringTeam") {
				withAuthentication { authenticatedHeaders =>
					Given(s"id is ${id} and name is ${name}.")
		 			val requestBody = Json.parse(s""" {"id" : ${id}, "name" : "${name}"} """)

					val request = FakeRequest(PUT, "/monitoringTeams").withHeaders(authenticatedHeaders:_*).withBody(requestBody)

					val response = route(request).get

					Then("StatusCode is 200")
					status(response) shouldBe OK
					And("Message contains OK")
					contentAsJson(response) shouldBe controllers.Application.messageOk
				}
			}
		}

	}
}





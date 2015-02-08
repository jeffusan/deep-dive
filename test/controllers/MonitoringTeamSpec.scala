
import org.scalatest._
import play.api.Logger
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
          val request = FakeRequest(GET, "/monitoringTeams").withHeaders(authenticatedHeaders: _*)
        }
      }
    }

    val id = 11
    val name = "New Monitoring Team"

    describe("POST /monitoringTeams") {
      it("should create a MonitoringTeam") {
        withAuthentication { authenticatedHeaders =>
          Given(s"name is ${name}.")
          val requestBody = Json.parse(s""" {"name" : "${name}"} """)

          val request = FakeRequest(POST, "/monitoringTeams").withHeaders(authenticatedHeaders: _*).withBody(requestBody)

        }
      }
    }

    describe("PUT /monitoringTeams") {
      it("should modify a MonitoringTeam") {
        withAuthentication { authenticatedHeaders =>
          Given(s"id is ${id} and name is ${name}.")
          val requestBody = Json.parse(s""" {"id" : ${id}, "name" : "${name}"} """)

          val request = FakeRequest(PUT, "/monitoringTeams").withHeaders(authenticatedHeaders: _*).withBody(requestBody)

        }
      }
    }

  }
}


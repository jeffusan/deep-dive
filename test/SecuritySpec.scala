import collection.mutable.Stack
import org.scalatest._
import org.scalatestplus.play._
import org.mockito.Mockito._
import org.mockito.Matchers._
import org.scalatest.mock.MockitoSugar
import models.{UserRepository, User}
import services.UserService
import models.Role.NormalUser
import play.api.libs.json._



/**
  example test
  */
class UserServiceSpec extends PlaySpec with MockitoSugar {

  "User#fromJson" should {

    "match emails when converted from json" in {
      val userJson: JsValue = Json.parse("""
      {
       "id" : 1,
       "email" : "tester@json.com",
       "name" : "Tester",
       "role" : "Administrator"
      }
      """)
      val user: User = (userJson).as[User]

      user.email mustBe "tester@json.com"
    }

    "error when cannot be parsed" in {
      val badUserJson: JsValue = Json.parse("""
      {
       "id" : 1,
       "email" : "tester@json.com",
       "name" : "Tester"
      }
      """)

      try {
        val user: User = (badUserJson).as[User]

        fail()
      } catch {
        case jsr: JsResultException =>
        case _: Exception => fail()
      }

    }
  }


  "User#toJson" should {
    "match emails when converted to json" in {
      val user: User = new User(Some(1), "tester@json.com", null, "tester", "Administrator")

      val userJson: JsValue = Json.toJson(user)

      val email: String = (userJson \ "email").as[String]

      email mustBe "tester@json.com"
    }

  }
}

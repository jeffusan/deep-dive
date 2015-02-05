import models.{User, Role}
import org.scalatestplus.play._
import play.api.libs.json._



/**
  example test
  */
class UserSpec extends PlaySpec {

  "User#fromJson" should {

    "match emails when json is converted to a user" in {
      val userJson: JsValue = Json.parse("""
      {
       "id" : 1,
       "email" : "tester@json.com",
       "name" : "Tester",
       "roles" : ["Administrator"]
      }
      """)
      val user: User = (userJson).as[User]

      user.id mustBe Some(1)
      user.email mustBe "tester@json.com"
      user.name mustBe "Tester"
      user.roles must contain ("Administrator")
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
      val user: User = new User(Some(1), "tester@json.com", null, "tester", List("Administrator"))

      val userJson: JsValue = Json.toJson(user)

      val email: String = (userJson \ "email").as[String]

      email mustBe "tester@json.com"
    }

  }

}

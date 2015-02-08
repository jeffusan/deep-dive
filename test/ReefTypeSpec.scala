import models.{ReefType, ReefTypeRepository}
import org.scalatest.mock.MockitoSugar
import org.mockito.Mockito._
import org.scalatestplus.play.PlaySpec
import play.api.libs.json.{JsArray, JsValue, Json}
import services.ReefTypeService

/**
 * Tests for ReefType
 */

class ReefTypeSpec extends PlaySpec with MockitoSugar {

  "ReefType to json conversion " should {
    "corresponding values should match when reefType is converted to json" in {

      val reefType: ReefType = new ReefType(Some(1), "Inner", "3-5m")

      val jsonReefType: JsValue = Json.toJson(reefType)

      reefType.id mustBe (jsonReefType \ "id").as[Option[Long]]
      reefType.name mustBe (jsonReefType \ "name").as[String]
      reefType.depth mustBe (jsonReefType \ "depth").as[String]

    }
  }


  "ReefType from json conversion " should {
    "corresponding values should match when json is converted to a reefType " in {

      val jsonReefType: JsValue = Json.parse( """
          {
          "id" : 1,
          "name" : "Inner",
          "depth" : "3-5m"
          }
                                              """)

      val reefType: ReefType = new ReefType(Some(1), "Inner", "3-5m")

      jsonReefType.as[ReefType] mustBe reefType
    }
  }


  "Retrieve all available reef types" should {

    "return List with no object in it" in {
      val reefs = List()
      val repo = mock[ReefTypeRepository]
      when(repo.findAllReefTypes) thenReturn reefs

      val service = new ReefTypeService(repo)
      val actual = service.findAllReefTypes

      actual.size mustBe 0
    }

    "return List with one object in it" in {
      val reefs = List(
        new ReefType(Some(1), "Inner", "3-5m")
      )
      val repo = mock[ReefTypeRepository]
      when(repo.findAllReefTypes) thenReturn reefs

      val service = new ReefTypeService(repo)
      val actual = service.findAllReefTypes

      actual must not be empty
      actual.size mustBe reefs.size

      val reef1 = actual(0)
      reef1.id.get mustBe 1
      reef1.name mustBe "Inner"
    }

    "return JsonValue with two objects in it" in {
      val reefs = List(
        new ReefType(Some(1), "Inner", "3-5m"),
        new ReefType(Some(2), "Channel", "7-9m")
      )
      val repo = mock[ReefTypeRepository]
      when(repo.findAllReefTypes) thenReturn reefs

      val service = new ReefTypeService(repo)
      val actual = service.findAllReefTypes

      actual must not be empty
      actual.size mustBe reefs.size

      val reef1 = actual(0)
      val reef2 = actual(1)

      reef1.id.get mustBe 1
      reef1.name mustBe "Inner"

      reef2.id.get mustBe 2
      reef2.name mustBe "Channel"
    }

  }

  "Retrieve reef by given reef id" should {

    "should not accept reef id equal to Zero" in {
      val repo = mock[ReefTypeRepository]
      val service = new ReefTypeService(repo)

      try {
        service.findReefById(0)
        // if no exception, test should fail
        fail()
      }
      catch {
        // this is expected
        case il: IllegalArgumentException =>
        // in other cases, test should fail
        case _ => fail()
      }
    }

    "should not accept negative reef id" in {
      val repo = mock[ReefTypeRepository]
      val service = new ReefTypeService(repo)

      try {
        service.findReefById(-1)
        // if no exception, test should fail
        fail()
      }
      catch {
        // this is expected
        case il: IllegalArgumentException =>
        // in other cases, test should fail
        case _ => fail()
      }
    }
  }

}

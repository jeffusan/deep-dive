import models.ReefType
import org.scalatestplus.play.PlaySpec
import play.api.libs.json.{Json, JsValue}

/**
 * Tests for ReefType
 */

class ReefTypeSpec extends PlaySpec {

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

}

import models.Region
import org.scalatestplus.play.PlaySpec
import play.api.libs.json.{Json, JsValue}

/**
 * Tests for Region
 */

class RegionSpec extends PlaySpec {

  "Region to json conversion " should {
    "corresponding values should match when region is converted to json " in {

      val region: Region = new Region(Some(1), "Tokyo Prefecture")

      val regionJson: JsValue = Json.toJson(region)

      region.id mustBe (regionJson \ "id").as[Option[Long]]
      region.name mustBe (regionJson \ "name").as[String]

    }
  }


  "Region from json conversion " should {
    "corresponding values should match when json is converted to a region" in {
      val regionJson: JsValue = Json.parse( """
          {
          "id" : 1,
          "name": "Tokyo Prefecture"
          }
                                            """)

      val region: Region = new Region(Some(1), "Tokyo Prefecture")

      regionJson.as[Region] mustBe region
    }
  }

}


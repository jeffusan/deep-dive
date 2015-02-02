import models.SubRegion
import org.scalatestplus.play.PlaySpec
import play.api.libs.json.{Json, JsValue}


/**
 * Tests for SubRegion
 */

class SubRegionSpec extends PlaySpec {

  "SubRegion to json conversion " should {
    "corresponding values and type should match when subRegion is converted to json" in {

      val subRegion = new SubRegion(Some(1), "Izu-Oshima", 1, "OSH")

      val jsonSubRegion: JsValue = Json.toJson(subRegion)

      subRegion.id mustBe (jsonSubRegion \ "id").as[Option[Long]]
      subRegion.name mustBe (jsonSubRegion \ "name").as[String]
      subRegion.regionId mustBe (jsonSubRegion \ "regionId").as[Long]
      subRegion.code mustBe (jsonSubRegion \ "code").as[String]

    }
  }


  "SubRegion from json conversion " should {
    "corresponding values should match when json converted to a subRegion " in {
      val jsonSubRegion: JsValue = Json.parse( """
          {
          "id" : 1,
          "name" : "Izu-Oshima",
          "regionId" : 1,
          "code" : "OSH"
          }
                                               """)

      val subRegion: SubRegion = new SubRegion(Some(1), "Izu-Oshima", 1, "OSH")

      jsonSubRegion.as[SubRegion] mustBe (subRegion)
    }
  }
}

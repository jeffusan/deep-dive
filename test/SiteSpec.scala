import models.Site
import org.scalatestplus.play.PlaySpec
import play.api.libs.json.{Json, JsValue}

/**
 * Tests for Site
 */

class SiteSpec extends PlaySpec {

  "Site to Json conversion " should {
    "corresponding values should match when site is converted to json " in {

      val site: Site = new Site(Some(1), 1, 1, "OSH-1", 34.7937520000, 139.3575700000, "WGS 1984")

      val jsonSite: JsValue = Json.toJson(site)

      site.id mustBe (jsonSite \ "id").as[Option[Long]]
      site.subRegionId mustBe (jsonSite \ "subRegionId").as[Long]
      site.reefTypeId mustBe (jsonSite \ "reefTypeId").as[Long]
      site.name mustBe (jsonSite \ "name").as[String]
      site.latitude mustBe (jsonSite \ "latitude").as[Double]
      site.longitude mustBe (jsonSite \ "longitude").as[Double]
      site.mapDatum mustBe (jsonSite \ "mapDatum").as[String]

    }
  }

  "Site from json conversion " should {
    "corresponding values should match when json is converted to a site " in {

      val jsonSite: JsValue = Json.parse("""
          {
          "id" : 1,
          "subRegionId" : 1,
          "reefTypeId" : 1,
          "name" : "OSH-1",
          "latitude" : 34.7937520000,
          "longitude" : 139.3575700000,
          "mapDatum" : "WGS 1984"
          }
                                         """)

      val site: Site = new Site(Some(1), 1, 1, "OSH-1", 34.7937520000, 139.3575700000, "WGS 1984")

      jsonSite.as[Site] mustBe site
    }
  }

}

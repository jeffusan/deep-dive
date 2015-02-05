import models.{Site, SiteRepository}
import org.mockito.Mockito._
import org.scalatest.mock.MockitoSugar
import org.scalatestplus.play.PlaySpec
import play.api.libs.json.{JsArray, JsValue, Json}
import services.SiteService

/**
 * Tests for Site
 */

class SiteSpec extends PlaySpec with MockitoSugar {

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

      val jsonSite: JsValue = Json.parse( """
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


  "Retrieve all sites based on given sub-region id" should {
    "should not accept sub-region id equal to Zero" in {
      val repo = mock[SiteRepository]
      val service = new SiteService(repo)

      try {
        service.findAllBySubRegionId(0)
        // if no exception, test should fails
        fail()
      }
      catch {
        // expect to receive this exception
        case il: IllegalArgumentException =>
        // in other cases, test fails
        case _ => fail()
      }
    }

    "should not accept negative sub-region id" in {
      val repo = mock[SiteRepository]
      val service = new SiteService(repo)

      try {
        service.findAllBySubRegionId(-1)
        // if no exception, test should fails
        fail()
      }
      catch {
        // expect to receive this exception
        case il: IllegalArgumentException =>
        // in other cases, test should fails
        case _ => fail()
      }
    }

    "can return JsonValue with no object in it" in {
      val subRegionId = 1
      val sites = List()
      val repo = mock[SiteRepository]
      when(repo.findAllBySubRegionId(subRegionId)) thenReturn sites

      val service = new SiteService(repo)
      val actual = service.findAllBySubRegionId(subRegionId)

      actual mustBe None
    }

    "can return JsonValue with one object in it" in {
      val subRegionId = 1
      val sites = List(new Site(Some(1), 1, 1, "OSH-1", 34.7937520000, 139.3575700000, "WGS 1984"))
      val repo = mock[SiteRepository]
      when(repo.findAllBySubRegionId(subRegionId)) thenReturn sites

      val service = new SiteService(repo)
      val actual = service.findAllBySubRegionId(subRegionId)

      actual must not be empty
      actual.get.as[JsArray].value.size mustBe sites.size

      var index = 0
      sites.foreach(f = e => {
        (actual.get(index) \ "id").as[Long] mustBe e.id.get
        (actual.get(index) \ "subRegionId").as[Long] mustBe e.subRegionId
        (actual.get(index) \ "reefTypeId").as[Long] mustBe e.reefTypeId
        (actual.get(index) \ "name").as[String] mustBe e.name
        (actual.get(index) \ "latitude").as[Double] mustBe e.latitude
        (actual.get(index) \ "longitude").as[Double] mustBe e.longitude
        (actual.get(index) \ "mapDatum").as[String] mustBe e.mapDatum
        index += 1
      })
    }

    "can return JsonValue with two objects in it" in {
      val subRegionId = 1
      val sites = List(
        new Site(Some(1), 1, 1, "OSH-1", 34.7937520000, 139.3575700000, "WGS 1984"),
        new Site(Some(2), 1, 2, "OSH-2", 34.7937520000, 139.3575700000, "WGS 1984")
      )
      val repo = mock[SiteRepository]
      when(repo.findAllBySubRegionId(subRegionId)) thenReturn sites

      val service = new SiteService(repo)
      val actual = service.findAllBySubRegionId(subRegionId)

      actual must not be empty
      actual.get.as[JsArray].value.size mustBe sites.size

      var index = 0
      sites.foreach(f = e => {
        (actual.get(index) \ "id").as[Long] mustBe e.id.get
        (actual.get(index) \ "subRegionId").as[Long] mustBe e.subRegionId
        (actual.get(index) \ "reefTypeId").as[Long] mustBe e.reefTypeId
        (actual.get(index) \ "name").as[String] mustBe e.name
        (actual.get(index) \ "latitude").as[Double] mustBe e.latitude
        (actual.get(index) \ "longitude").as[Double] mustBe e.longitude
        (actual.get(index) \ "mapDatum").as[String] mustBe e.mapDatum
        index += 1
      })
    }

  }

  "Retrieve site based on given site id" should {

    "should not accept sub-region id equal to Zero" in {
      val repo = mock[SiteRepository]
      val service = new SiteService(repo)

      try {
        service.findOneById(0)
        // if no exception, test should fails
        fail()
      }
      catch {
        // expect to receive this exception
        case il: IllegalArgumentException =>
        // in other cases, test fails
        case _ => fail()
      }
    }


    "should not accept negative sub-region id" in {
      val repo = mock[SiteRepository]
      val service = new SiteService(repo)

      try {
        service.findOneById(-1)
        // if no exception, test should fails
        fail()
      }
      catch {
        // expect to receive this exception
        case il: IllegalArgumentException =>
        // in other cases, test fails
        case _ => fail()
      }
    }
  }

}

// End of SiteSpec

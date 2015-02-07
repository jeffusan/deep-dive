import models.{RegionRepository, Region}
import org.scalatest.mock.MockitoSugar
import org.mockito.Mockito._
import org.scalatestplus.play.PlaySpec
import play.api.libs.json.{JsArray, Json, JsValue}
import services.RegionService

/**
 * Tests for Region
 */

class RegionSpec extends PlaySpec with MockitoSugar {

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


  "Retrieve all available regions " should {

    "can return JsonValue with no object in it" in {
      val regions = List()
      val repo = mock[RegionRepository]
      when(repo.findAllRegion) thenReturn regions

      val service = new RegionService(repo)
      val actual = service.findAllRegions

      actual mustBe None

    }

    "can return JsonValue with one object in it" in {

      val regions = List(new Region(Some(1), "Tokyo Prefecture"))
      val repo = mock[RegionRepository]
      when(repo.findAllRegion) thenReturn regions

      val service = new RegionService(repo)
      val actual = service.findAllRegions
      actual must not be empty
      actual.get.as[JsArray].value.size mustBe regions.size


      var index = 0
      regions.foreach(f = e => {
        (actual.get(index) \ "id").as[Long] mustBe e.id.get
        (actual.get(index) \ "name").as[String] mustBe e.name
        index += 1
      })


    }

    "can return JsonValue with two objects in it" in {

      val regions = List(
        new Region(Some(1), "Tokyo Prefecture"),
        new Region(Some(2), "Kagoshima Prefecture")
      )

      val repo = mock[RegionRepository]
      when(repo.findAllRegion) thenReturn regions

      val service = new RegionService(repo)
      val actual = service.findAllRegions
      actual must not be empty
      actual.get.as[JsArray].value.size mustBe regions.size


      var index = 0
      regions.foreach(f = e => {
        (actual.get(index) \ "id").as[Long] mustBe e.id.get
        (actual.get(index) \ "name").as[String] mustBe e.name
        index += 1
      })


    }
  }


  "Retrieve region based on given region id" should {

    "should not accept region id equal to Zero" in {
      val repo = mock[RegionRepository]
      val service = new RegionService(repo)

      try {
        service.findOneById(0)
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

    "should not accept negative region id " in {

      val repo = mock[RegionRepository]
      val service = new RegionService(repo)

      try {
        service.findOneById(-1)
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

// End of RegionSpec


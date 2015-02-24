import models.{Region, SubRegion, SubRegionRepository}
import org.mockito.Mockito._
import org.scalatest.mock.MockitoSugar
import org.scalatestplus.play.PlaySpec
import play.api.libs.json.{JsArray, JsValue, Json}
import services.SubRegionService


/**
 * Tests for SubRegion
 */

class SubRegionSpec extends PlaySpec with MockitoSugar {

  "Sub-region to json conversion " should {
    "corresponding values should match when sub-region is converted to json" in {

      val subRegion = new SubRegion(Some(1), "Izu-Oshima", new Region(Option(1), "TEST"), "OSH")

      val jsonSubRegion: JsValue = Json.toJson(subRegion)

      subRegion.id mustBe (jsonSubRegion \ "id").as[Option[Int]]
      subRegion.name mustBe (jsonSubRegion \ "name").as[String]
      subRegion.region.id mustBe (jsonSubRegion \ "region" \ "id").as[Option[Int]]
      subRegion.code mustBe (jsonSubRegion \ "code").as[String]

    }
  }


  "Sub-region from json conversion " should {
    "corresponding values should match when json converted to a sub-region " in {
      val jsonSubRegion: JsValue = Json.parse( """
          {
          "id" : 1,
          "name" : "Izu-Oshima",
          "region" : {
            "id": 1,
            "name": "test"
          },
          "code" : "OSH"
          }
      """)

      val subRegion: SubRegion = new SubRegion(Some(1), "Izu-Oshima", new Region(Option(1), "test"), "OSH")

      jsonSubRegion.as[SubRegion] mustBe subRegion
    }
  }


  "Retrieve sub-region by id" should {

    "should not accept sub-region id equal to Zero" in {
      val repo = mock[SubRegionRepository]
      val service = new SubRegionService(repo)

      try {
        service.findOneById(0)
        fail()
      }
      catch {
        case il: IllegalArgumentException =>
        case _ : Throwable => fail()
      }
    }

    "should not accept negative sub-region id" in {
      val repo = mock[SubRegionRepository]
      val service = new SubRegionService(repo)

      try {
        service.findOneById(-1)
        fail()
      }
      catch {
        case il: IllegalArgumentException =>
        case _ : Throwable => fail()
      }
    }

  }


  "Retrieve all sub-regions specified by region id" should {

    "should not accept region id equal to Zero" in {
      val repo = mock[SubRegionRepository]
      val service = new SubRegionService(repo)

      try {
        service.findAllByRegionId(0)
        fail()
      }
      catch {
        case il: IllegalArgumentException =>
        case _ : Throwable => fail()
      }
    }

    "should not accept negative region id" in {
      val repo = mock[SubRegionRepository]
      val service = new SubRegionService(repo)

      try {
        service.findAllByRegionId(-1)
        fail()
      }
      catch {
        case il: IllegalArgumentException =>
        case _ : Throwable => fail()
      }

    }

    "can return object with no object in it" in {
      val regionId = 1
      val regions = List()
      val repo = mock[SubRegionRepository]
      when(repo.findAllByRegionId(regionId)) thenReturn regions
      val service = new SubRegionService(repo)
      val actual = service.findAllByRegionId(regionId)

      actual.size mustBe 0
    }

    "can return object with one object in it" in {
      val regionId = 1
      val regions = List(new SubRegion(Some(1), "Izu-Oshima", new Region(Option(1), "test"), "OSH"))
      val repo = mock[SubRegionRepository]
      when(repo.findAllByRegionId(regionId)) thenReturn regions

      val service = new SubRegionService(repo)
      val actual = service.findAllByRegionId(regionId)

      actual must not be empty
      actual.size mustBe 1
    }

    "can return object with two objects in it" in {
      val regionId = 1
      val regions = List(
        new SubRegion(Some(1), "Izu-Oshima", new Region(Option(1), "test"), "OSH"),
        new SubRegion(Some(2), "Nii-Jima", new Region(Option(1), "test"), "NII")
      )
      val repo = mock[SubRegionRepository]
      when(repo.findAllByRegionId(regionId)) thenReturn regions

      val service = new SubRegionService(repo)
      val actual = service.findAllByRegionId(regionId)

      actual must not be empty
      actual.size mustBe 2

    }
  }



  "Retrieve all available sub-regions" should {

    "can return JsonValue with no object in it" in {
      val regions = List()
      val repo = mock[SubRegionRepository]
      when(repo.findAll) thenReturn regions

      val service = new SubRegionService(repo)
      val actual = service.findAll

      actual.size mustBe 0
    }

    "can return an object with one object in it" in {
      val regions = List(new SubRegion(Some(1), "Izu-Oshima", new Region(Option(1), "test"), "OSH"))
      val repo = mock[SubRegionRepository]
      when(repo.findAll) thenReturn regions

      val service = new SubRegionService(repo)
      val actual = service.findAll

      actual must not be empty

    }

    "can return object with two object in it" in {
      val regions = List(
        new SubRegion(Some(1), "Izu-Oshima", new Region(Option(1), "test"), "OSH"),
        new SubRegion(Some(2), "Nii-Jima", new Region(Option(1), "test"), "NII")
      )
      val repo = mock[SubRegionRepository]
      when(repo.findAll) thenReturn regions

      val service = new SubRegionService(repo)
      val actual = service.findAll

      actual must not be empty
    }
  }
}

// End of SubRegionSpec

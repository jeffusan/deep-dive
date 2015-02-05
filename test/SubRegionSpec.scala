import models.{SubRegion, SubRegionRepository}
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

      val subRegion = new SubRegion(Some(1), "Izu-Oshima", 1, "OSH")

      val jsonSubRegion: JsValue = Json.toJson(subRegion)

      subRegion.id mustBe (jsonSubRegion \ "id").as[Option[Long]]
      subRegion.name mustBe (jsonSubRegion \ "name").as[String]
      subRegion.regionId mustBe (jsonSubRegion \ "regionId").as[Long]
      subRegion.code mustBe (jsonSubRegion \ "code").as[String]

    }
  }


  "Sub-region from json conversion " should {
    "corresponding values should match when json converted to a sub-region " in {
      val jsonSubRegion: JsValue = Json.parse( """
          {
          "id" : 1,
          "name" : "Izu-Oshima",
          "regionId" : 1,
          "code" : "OSH"
          }
                                               """)

      val subRegion: SubRegion = new SubRegion(Some(1), "Izu-Oshima", 1, "OSH")

      jsonSubRegion.as[SubRegion] mustBe subRegion
    }
  }


  "Retrieve sub-region by id" should {

    "should not accept sub-region id equal to Zero" in {
      val repo = mock[SubRegionRepository]
      val service = new SubRegionService(repo)

      try {
        service.findSubRegionById(0)
        fail()
      }
      catch {
        case il: IllegalArgumentException =>
        case _ => fail()
      }
    }

    "should not accept negative sub-region id" in {
      val repo = mock[SubRegionRepository]
      val service = new SubRegionService(repo)

      try {
        service.findSubRegionById(-1)
        fail()
      }
      catch {
        case il: IllegalArgumentException =>
        case _ => fail()
      }
    }

  }


  "Retrieve all sub-regions specified by region id" should {

    "should not accept region id equal to Zero" in {
      val repo = mock[SubRegionRepository]
      val service = new SubRegionService(repo)

      try {
        service.findAllSubRegionByRegionId(0)
        fail()
      }
      catch {
        case il: IllegalArgumentException =>
        case _ => fail()
      }
    }

    "should not accept negative region id" in {
      val repo = mock[SubRegionRepository]
      val service = new SubRegionService(repo)

      try {
        service.findAllSubRegionByRegionId(-1)
        fail()
      }
      catch {
        case il: IllegalArgumentException =>
        case _ => fail()
      }

    }

    "can return JsonValue with no object in it" in {
      val regionId = 1
      val regions = List()
      val repo = mock[SubRegionRepository]
      when(repo.findAllSubRegionByRegionId(regionId)) thenReturn regions
      val service = new SubRegionService(repo)
      val actual = service.findAllSubRegionByRegionId(regionId)

      actual mustBe None
    }

    "can return JsonValue with one object in it" in {
      val regionId = 1
      val regions = List(new SubRegion(Some(1), "Izu-Oshima", 1, "OSH"))
      val repo = mock[SubRegionRepository]
      when(repo.findAllSubRegionByRegionId(regionId)) thenReturn regions

      val service = new SubRegionService(repo)
      val actual = service.findAllSubRegionByRegionId(regionId)

      actual must not be empty
      actual.get.as[JsArray].value.size mustBe regions.size

      var index = 0
      regions.foreach(f = e => {
        (actual.get(index) \ "id").as[Long] mustBe e.id.get
        (actual.get(index) \ "name").as[String] mustBe e.name
        (actual.get(index) \ "regionId").as[Long] mustBe e.regionId
        (actual.get(index) \ "code").as[String] mustBe e.code
        index += 1
      })
    }

    "can return JsonValue with two objects in it" in {
      val regionId = 1
      val regions = List(
        new SubRegion(Some(1), "Izu-Oshima", 1, "OSH"),
        new SubRegion(Some(2), "Nii-Jima", 1, "NII")
      )
      val repo = mock[SubRegionRepository]
      when(repo.findAllSubRegionByRegionId(regionId)) thenReturn regions

      val service = new SubRegionService(repo)
      val actual = service.findAllSubRegionByRegionId(regionId)

      actual must not be empty
      actual.get.as[JsArray].value.size mustBe regions.size

      var index = 0
      regions.foreach(f = e => {
        (actual.get(index) \ "id").as[Long] mustBe e.id.get
        (actual.get(index) \ "name").as[String] mustBe e.name
        (actual.get(index) \ "regionId").as[Long] mustBe e.regionId
        (actual.get(index) \ "code").as[String] mustBe e.code
        index += 1
      })

    }
  }



  "Retrieve all available sub-regions" should {

    "can return JsonValue with no object in it" in {
      val regions = List()
      val repo = mock[SubRegionRepository]
      when(repo.findAllSubRegion) thenReturn regions

      val service = new SubRegionService(repo)
      val actual = service.findAllSubRegion

      actual mustBe None
    }

    "can return JsonValue with one object in it" in {
      val regions = List(new SubRegion(Some(1), "Izu-Oshima", 1, "OSH"))
      val repo = mock[SubRegionRepository]
      when(repo.findAllSubRegion) thenReturn regions

      val service = new SubRegionService(repo)
      val actual = service.findAllSubRegion

      actual must not be empty
      actual.get.as[JsArray].value.size mustBe regions.size

      var index = 0
      regions.foreach(f = e => {
        (actual.get(index) \ "id").as[Long] mustBe e.id.get
        (actual.get(index) \ "name").as[String] mustBe e.name
        (actual.get(index) \ "regionId").as[Long] mustBe e.regionId
        (actual.get(index) \ "code").as[String] mustBe e.code
        index += 1
      })

    }

    "can return JsonValue with two object in it" in {
      val regions = List(
        new SubRegion(Some(1), "Izu-Oshima", 1, "OSH"),
        new SubRegion(Some(2), "Nii-Jima", 1, "NII")
      )
      val repo = mock[SubRegionRepository]
      when(repo.findAllSubRegion) thenReturn regions

      val service = new SubRegionService(repo)
      val actual = service.findAllSubRegion

      actual must not be empty
      actual.get.as[JsArray].value.size mustBe regions.size

      var index = 0
      regions.foreach(f = e => {
        (actual.get(index) \ "id").as[Long] mustBe e.id.get
        (actual.get(index) \ "name").as[String] mustBe e.name
        (actual.get(index) \ "regionId").as[Long] mustBe e.regionId
        (actual.get(index) \ "code").as[String] mustBe e.code
        index += 1
      })
    }

  }

}

// End of SubRegionSpec

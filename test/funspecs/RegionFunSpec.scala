package funspec

import play.api.libs.json._
import org.scalatest._
import play.api.test._
import play.api.test.Helpers._
import org.scalatestplus.play._
import models.{AnormRegionRepository, Region}

class RegionFunSpec extends PlaySpec with OneAppPerSuite {

  "The Region Model" should {

    "Create Regions" in {
      val region = AnormRegionRepository.add("My Region")

      (region \ "name").as[String] mustBe "My Region"
    }

    "Update Regions" in {
      val region: JsValue = AnormRegionRepository.add("My Region")
      val regionId = region \ "id"
      val updated: JsValue = AnormRegionRepository.update(regionId.as[Int], "My New Region")

      (updated \ "name").as[String] mustBe "My New Region"

    }

    "Remove Regions" in {
      val region: JsValue = AnormRegionRepository.add("My Region")
      (region \ "name").as[String] mustBe "My Region"

    }

  }
}

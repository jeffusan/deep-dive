package funspec

import play.api.libs.json._
import org.scalatest._
import play.api.test._
import play.api.test.Helpers._
import org.scalatestplus.play._
import models.{AnormSubRegionRepository, SubRegion, Region}

class SubRegionFunSpec extends PlaySpec with OneAppPerSuite {

  "The SubRegion Model" should {

    "Create SubRegions" in {
      val subr = AnormSubRegionRepository.add("hot stuff", 2, "HS")

      (subr \ "name").as[String] mustBe "hot stuff"
    }

    "Update SubRegions" in {
      val sbur: JsValue = AnormSubRegionRepository.add("bit time", 2, "BT")
      val id = (sbur \ "id").as[Int]
      val updated: JsValue = AnormSubRegionRepository.update(id, "bit time", "BT2")

      (updated \ "code").as[String] mustBe "BT2"

    }

    "Remove SubRegions" in {
      val sb: JsValue = AnormSubRegionRepository.add("My SbuRegion", 2, "BT3")
      (sb \ "name").as[String] mustBe "My SbuRegion"
      AnormSubRegionRepository.remove((sb \ "id").as[Int])

    }

  }
}

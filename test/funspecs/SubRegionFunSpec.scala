package funspec

import org.scalatest._
import play.api.test._
import play.api.test.Helpers._
import org.scalatestplus.play._
import models.{AnormSubRegionRepository, AnormRegionRepository, Region, SubRegion}

class SubRegionFunSpec extends PlaySpec with MustMatchers with OneAppPerSuite {

  "The SubRegion Model" should {

    "Create SubRegions" in {
      val regions = AnormRegionRepository.findAll
      regions.length must be > 0

      val region = regions(0)

      val maybeSubId = AnormSubRegionRepository.add("test subregion", region.id.get, "TEST")
      val subId = maybeSubId getOrElse fail()

    }
  }
}

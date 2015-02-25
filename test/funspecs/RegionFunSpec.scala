package funspec

import org.scalatest._
import play.api.test._
import play.api.test.Helpers._
import org.scalatestplus.play._
import models.{AnormRegionRepository, Region}

class RegionFunSpec extends PlaySpec with OneAppPerSuite {

  "The Region Model" should {

    "Create Regions" in {
      val maybeRegion = AnormRegionRepository.add("My Region")

      val region = maybeRegion getOrElse fail()
      region.name mustBe "My Region"
    }

    "Update Regions" in {
      val maybeRegion = AnormRegionRepository.add("My Region")
      val region = maybeRegion getOrElse fail()
      val regionId = region.id getOrElse fail()
      val maybeUpdated = AnormRegionRepository.update(regionId, "My New Region")

      val updated = maybeUpdated getOrElse fail()

      updated.name mustBe "My New Region"

    }

    "Remove Regions" in {
      val maybeRegion = AnormRegionRepository.add("My Region")
      val region = maybeRegion getOrElse(fail())
      val regionId = region.id getOrElse(fail())

      val regionJs = AnormRegionRepository.findOneById(regionId)

    }
  }
}

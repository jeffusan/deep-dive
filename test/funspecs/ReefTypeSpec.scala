package funspec

import play.api.libs.json._
import org.scalatest._
import play.api.test._
import play.api.test.Helpers._
import org.scalatestplus.play._
import models.{AnormReefTypeRepository}

class ReefTypeFunSpec extends PlaySpec with OneAppPerSuite {

  "The Reef Type Model" should {

    "Create Reef Types" in {
      val reefType = AnormReefTypeRepository.add("Reef 1", "1000 leagues")

      (reefType \ "name").as[String] mustBe "Reef 1"
    }

    "Return a list of all reef types" in {

      val reefTypes = AnormReefTypeRepository.findAll

    }
  }

}

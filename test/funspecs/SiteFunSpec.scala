package funspec


import play.api.libs.json._
import org.scalatest._
import play.api.test._
import play.api.test.Helpers._
import org.scalatestplus.play._
import models.{AnormSiteRepository}

class SiteFunSpec extends PlaySpec with OneAppPerSuite {

  "The Site Model" should {

    "Get All Sites" in {
      val sites = AnormSiteRepository.findAll

    }
  }
}

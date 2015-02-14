package controllers

import play.api.libs.json.Json
import play.api.mvc._
import play.api.Logger
import services.RegionService
import models.{AnormRegionRepository, Region}

trait Regions extends Controller with Security {

  lazy val service = new RegionService(AnormRegionRepository)

  /** Find all the regions */
  def show() = HasAdminToken() { token => userId => implicit request =>
    service.findAll.fold {
      BadRequest(Json.obj("status" -> "KO", "message" -> "Something terrible has happened"))
    } { regionData =>
      Ok(Json.toJson(regionData))
    }
  }
}

object Regions extends Regions

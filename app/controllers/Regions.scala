package controllers

import models.AnormRegionRepository
import play.api.Logger
import play.api.data._
import play.api.data.Forms._
import play.api.libs.json.Json
import play.api.mvc._
import services.RegionService

case class RegionData(name: String)
case class UpdateRegionData(pk: Int, name: String, value: String)

trait Regions extends Controller with Security {

  lazy val service = new RegionService(AnormRegionRepository)

  /** Delete a region */
  def remove(id: Int) =HasAdminToken() { token => userId => implicit request =>

    try {
      service.remove(id)
      Ok(Json.obj("status" -> "Ok", "message" -> "Great Success!"))
    } catch {
      case e: Exception => BadRequest(Json.obj("status" -> "KO", "message" -> "Something terrible this way comes"))
    }

  }

  val updateForm = Form(
    mapping(
      "pk" -> number,
      "name" -> nonEmptyText,
      "value" -> nonEmptyText
    )(UpdateRegionData.apply)(UpdateRegionData.unapply)
  )

  def update() = HasAdminToken(parse.json) { token => userId => implicit request =>
    Logger.info("Controller update!")
    updateForm.bindFromRequest.fold(
      formWithErrors => BadRequest(Json.obj("msg" -> "Bad Credentials", "status" -> "error")),
      regionData => {
        service.update(regionData.pk, regionData.value).fold {
          BadRequest(Json.obj("status" -> "KO", "message" -> "Yeah, about that region name..."))
        } { region =>
          Ok(Json.toJson(region))
        }
      }
    )
  }

  /** Find all the regions */
  def show() = HasAdminToken() { token => userId => implicit request =>
    Ok(Json.toJson(service.findAll))
  }

  val createForm = Form(
    mapping(
      "name" -> nonEmptyText
    )(RegionData.apply)(RegionData.unapply)
  )

  /** Create a new region */
  def create() = HasAdminToken(parse.json) { token => userId => implicit request =>
    createForm.bindFromRequest.fold(
      formWithErrors => BadRequest(Json.obj("err" -> "Bad Credentials")),
      regionData => {
        service.add(regionData.name).fold {
          BadRequest(Json.obj("status" -> "KO", "message" -> "Yeah, about that region name..."))
        } { region =>
          Ok(Json.toJson(region))
        }
      }
    )
  }
}

object Regions extends Regions

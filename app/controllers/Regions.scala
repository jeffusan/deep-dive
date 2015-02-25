package controllers

import models.AnormRegionRepository
import play.api.Logger
import play.api.data._
import play.api.data.Forms._
import play.api.libs.json.Json
import play.api.mvc._

case class RegionData(name: String)
case class UpdateRegionData(pk: Int, name: String, value: String)

trait Regions extends Controller with Security {

  /** Delete a region */
  def remove(id: Int) =HasAdminToken() { token => userId => implicit request =>

    try {
      AnormRegionRepository.remove(id)
      Ok(
        Json.obj("status" -> "Ok", "message" -> "Great Success!")
      )
    } catch {
      case e: Exception =>
        BadRequest(
          Json.obj("status" -> "KO", "message" -> "Something terrible this way comes")
        )
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
        Ok(AnormRegionRepository.update(regionData.pk, regionData.value))
      }
    )
  }

  /** Find all the regions */
  def show() = HasAdminToken() { token => userId => implicit request =>
    Ok(AnormRegionRepository.findAll)
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
        Ok(AnormRegionRepository.add(regionData.name))
      }
    )
  }
}

object Regions extends Regions

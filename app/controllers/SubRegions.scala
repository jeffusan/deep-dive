package controllers

import play.api.libs.json.Json
import play.api.mvc._
import play.api.Logger
import play.api.data._
import play.api.data.Forms._
import models.AnormSubRegionRepository

case class SubRegionData(name: String, regionId: Int, code: String)
case class UpdateSubRegionData(pk: Int, name: String, value: String)

trait SubRegions extends Controller with Security {

  /** Delete a subregion */
  def remove(id: Int) =HasAdminToken() { token => userId => implicit request =>

    try {
      AnormSubRegionRepository.remove(id)
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
    )(UpdateSubRegionData.apply)(UpdateSubRegionData.unapply)
  )

  def update() = HasAdminToken(parse.json) { token => userId => implicit request =>
    updateForm.bindFromRequest.fold(
      formWithErrors => BadRequest(Json.obj("msg" -> "Bad Credentials", "status" -> "error")),
      subRegionData => {
        Ok(AnormSubRegionRepository.update(subRegionData.pk, subRegionData.value))
      }
    )
  }

  /** Find all the regions */
  def show() = HasAdminToken() { token => userId => implicit request =>
    Ok(Json.toJson(AnormSubRegionRepository.findAll))
  }

  val createForm = Form(
    mapping(
      "name" -> nonEmptyText,
      "regionId" -> number,
      "code" -> nonEmptyText
    )(SubRegionData.apply)(SubRegionData.unapply)
  )

  /** Create a new region */
  def create() = HasAdminToken(parse.json) { token => userId => implicit request =>
    createForm.bindFromRequest.fold(
      formWithErrors => BadRequest(Json.obj("err" -> "You're Either Sending Bad Credentials or Bad Data")),
      subRegionData => {
        Ok(AnormSubRegionRepository.add(subRegionData.name, subRegionData.regionId, subRegionData.code))
      }
    )
  }
}

object SubRegions extends SubRegions

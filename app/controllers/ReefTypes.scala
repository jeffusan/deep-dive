package controllers

import play.api.libs.json.Json
import play.api.mvc._
import play.api.Logger
import play.api.data._
import play.api.data.Forms._
import models.AnormReefTypeRepository

case class ReefTypeData(name: String, depth: String)
case class ReefTypeUpdateData(pl: Int, name: String, value: String)

trait ReefTypes extends Controller with Security {


  /** Delete a subregion */
  def remove(id: Int) =HasAdminToken() { token => userId => implicit request =>

    try {
      AnormReefTypeRepository.remove(id)
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
    )(ReefTypeUpdateData.apply)(ReefTypeUpdateData.unapply)
  )

  def updateName() = HasAdminToken(parse.json) { token => userId => implicit request =>
    updateForm.bindFromRequest.fold(
      formWithErrors => BadRequest(Json.obj("msg" -> "Bad Credentials", "status" -> "error")),
      subRegionData => {
        Ok(AnormReefTypeRepository.updateName(subRegionData.pl, subRegionData.value))
      }
    )
  }

  def updateDepth() = HasAdminToken(parse.json) { token => userId => implicit request =>
    updateForm.bindFromRequest.fold(
      formWithErrors => BadRequest(Json.obj("msg" -> "Bad Credentials", "status" -> "error")),
      subRegionData => {
        Ok(AnormReefTypeRepository.updateDepth(subRegionData.pl, subRegionData.value))
      }
    )
  }


  /** Find all the regions */
  def show() = HasAdminToken() { token => userId => implicit request =>
    Ok(Json.toJson(AnormReefTypeRepository.findAll))
  }

  val createForm = Form(
    mapping(
      "name" -> nonEmptyText,
      "depth" -> nonEmptyText
    )(ReefTypeData.apply)(ReefTypeData.unapply)
  )

  /** Create a new region */
  def create() = HasAdminToken(parse.json) { token => userId => implicit request =>
    createForm.bindFromRequest.fold(
      formWithErrors => BadRequest(Json.obj("err" -> "You're Either Sending Bad Credentials or Bad Data")),
      subRegionData => {
        Ok(AnormReefTypeRepository.add(subRegionData.name, subRegionData.depth))
      }
    )
  }

}

object ReefTypes extends ReefTypes

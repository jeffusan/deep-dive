package controllers

import play.api.libs.json.Json
import play.api.mvc._
import play.api.Logger
import play.api.data._
import play.api.data.Forms._
import models.{AnormSiteRepository}

case class SiteData(localName: String, latitude: String, longitude: String, mapDatum: String, subregion: Int, reefType: Int)

trait Sites extends Controller with Security {

  /** Find all the regions */
  def show() = HasAdminToken() { token => userId => implicit request =>
    Ok(Json.toJson(AnormSiteRepository.findAll))
  }

  /** Delete a reeftype */
  def remove(id: Int) =HasAdminToken() { token => userId => implicit request =>

    try {
      AnormSiteRepository.remove(id)
      Ok(Json.obj("status" -> "Ok", "message" -> "Great Success!"))
    } catch {
      case e: Exception => BadRequest(Json.obj("status" -> "KO", "message" -> "Something terrible this way comes"))
    }

  }

  val createForm = Form(
    mapping(
      "localName" -> nonEmptyText,
      "latitude" -> nonEmptyText,
      "longitude" -> nonEmptyText,
      "mapDatum" -> nonEmptyText,
      "subregion" -> number,
      "reeftype" -> number
    )(SiteData.apply)(SiteData.unapply)
  )

  /** Create a new region */
  def create() = HasAdminToken(parse.json) { token => userId => implicit request =>
    createForm.bindFromRequest.fold(
      formWithErrors => BadRequest(Json.obj("err" -> "You're Either Sending Bad Credentials or Bad Data")),
      siteData => {
        Ok(AnormSiteRepository.add(siteData))
      }
    )
  }

}

object Sites extends Sites

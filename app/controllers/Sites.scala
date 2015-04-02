package controllers

import play.api.libs.json.Json
import play.api.mvc._
import play.api.Logger
import play.api.data._
import play.api.data.Forms._
import models.{AnormSiteRepository}

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

}

object Sites extends Sites

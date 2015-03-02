package controllers

import play.api.libs.json.Json
import play.api.mvc._
import play.api.Logger
import models.AnormSurveyEventRepository

trait SurveyEvents extends Controller with Security {


  /** Find all survey events by site id. */
  def showBySiteId(siteId: Int) = HasToken() { token => userId => implicit request =>
    findBySiteId(siteId)
  }
  def findBySiteId(siteId: Int) = {
    Ok(AnormSurveyEventRepository.findBySiteId(siteId))
  }

  def show() = HasAdminToken() { token => userId => implicit request =>
    Ok(Json.toJson(AnormSurveyEventRepository.findAll))
  }
}

object SurveyEvents extends SurveyEvents

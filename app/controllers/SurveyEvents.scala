package controllers

import play.api.libs.json.Json
import play.api.mvc._
import play.api.Logger
import services.SurveyEventService
import models.AnormSurveyEventRepository

trait SurveyEvents extends Controller with Security {

  lazy val service = new SurveyEventService(AnormSurveyEventRepository)

  /** Find all survey events by site id. */
  def showBySiteId(siteId: Long) = HasToken() { token => userId => implicit request =>
    val surveyEvents = service.findBySiteId(siteId)
    Ok(Json.toJson(surveyEvents))
  }

}

object SurveyEvents extends SurveyEvents

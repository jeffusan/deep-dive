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
    service.findAllBySiteId(siteId) match {
    	case Some(json) => Ok(json)
    	case None => BadRequest(messageError("faild to create"))
    }
  }

}

object SurveyEvents extends SurveyEvents

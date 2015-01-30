package services

import java.util.Date
import play.api.libs.json._
import models.{SurveyEvent, SurveyEventRepository}

class SurveyEventService(repository: SurveyEventRepository) {
	import play.api.libs.json._

	def findAllBySiteId(siteId: Long): Option[JsValue] = {
		repository.findAllBySiteId(siteId) match {
			case List() => None
			case lists => Some(Json.toJson(lists)) 
		}
	}

	def findAllByEventDate(eventDate: Date): Option[JsValue] = {
		repository.findAllByEventDate(eventDate) match {
			case List() => None
			case lists => Some(Json.toJson(lists))
		}
	}

	def findAllByMonitoringTeamId(monitoringTeamId: Long): Option[JsValue] = {
		val result = repository.findAllByMonitoringTeamId(monitoringTeamId)
		result match {
			case List() => None
			case _ => Some(Json.toJson(result))
		}
	}

	def save(surveyEvents: Seq[SurveyEvent]) = {
		repository.save(surveyEvents)
	}

}

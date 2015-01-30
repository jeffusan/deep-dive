package services
/** This is the ScalaDoc for SurveyEvent Service of services package. **/

import java.util.Date
import play.api.libs.json._
import models.{SurveyEvent, SurveyEventRepository}

/** A SurveyEvent including the details of survey result.
	*
 	* @contructor create a new [[services.SurveyEventService]] with a instance extendes SurveyEventRepogitory.
	* @param id the team id only use in application.
	* @param name the monitoring team's name.
	*/
class SurveyEventService(repository: SurveyEventRepository) {
	import play.api.libs.json._

  /** 
  	* Find all survey events by site id.
  	* 
    * @param siteId the search criteria.
    * @return a option instance has JsValue Type.
    */
	def findAllBySiteId(siteId: Long): Option[JsValue] = {
		repository.findAllBySiteId(siteId) match {
			case List() => None
			case lists => Some(Json.toJson(lists)) 
		}
	}

  /** 
  	* Find all survey events by event date.
  	* 
    * @param eventDate the search criteria.
    * @return a option instance has JsValue Type.
    */
	def findAllByEventDate(eventDate: Date): Option[JsValue] = {
		repository.findAllByEventDate(eventDate) match {
			case List() => None
			case lists => Some(Json.toJson(lists))
		}
	}

  /** 
  	* Find all survey events by event date.
  	* 
    * @param eventDate the search criteria.
    * @return a option instance has JsValue Type.
    */
	def findAllByMonitoringTeamId(monitoringTeamId: Long): Option[JsValue] = {
		val result = repository.findAllByMonitoringTeamId(monitoringTeamId)
		result match {
			case List() => None
			case _ => Some(Json.toJson(result))
		}
	}

  /** 
  	* Save the survey events.
    *
   	* @param a sequence instance including 0 or more [[models.SurveyEvent]] instances.
   	* @return a array instance including 0 or more [[Int]] instances.Int is a result for each query.
    */
	def save(surveyEvents: Seq[SurveyEvent]): Array[Int] = {
		repository.save(surveyEvents)
	}

}

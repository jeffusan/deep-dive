package services
/** This is the ScalaDoc for SurveyEvent Service of services package. **/

import java.util.Date
import play.api.libs.json._
import models.{ SurveyEvent, SurveyEventRepository }

/**
 * A SurveyEvent including the details of survey result.
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
  def findBySiteId(siteId: Long): Seq[SurveyEvent] = {
    repository.findBySiteId(siteId) 
  }

  /**
   * Find all survey events by event date.
   *
   * @param eventDate the search criteria.
   * @return a option instance has JsValue Type.
   */
  def findByEventDate(eventDate: Date): Seq[SurveyEvent] = {
    repository.findByEventDate(eventDate)
  }

  /**
   * Find all survey events by event date.
   *
   * @param eventDate the search criteria.
   * @return a option instance has JsValue Type.
   */
  def findByMonitoringTeamId(monitoringTeamId: Long): Seq[SurveyEvent] = {
    repository.findByMonitoringTeamId(monitoringTeamId)
  }

  /**
   * Save the survey events.
   *
   * @param a sequence instance including 0 or more [[models.SurveyEvent]] instances.
   * @return status true is success, otherwise failure
   */
  def save(surveyEvents: Seq[SurveyEvent]): Boolean = {
    surveyEvents.size == repository.save(surveyEvents).foldLeft(0)(_ + _)
  }

}

package models

/** This is the ScalaDoc for SurveyEvent Model of models package. **/

import java.util.Date
import play.api.libs.json._
import anorm._
import play.api.db.DB
import play.api.Play.current
import org.postgresql.util.PGobject
import anorm.SqlParser.{ long, int, str, date, get }
import scala.language.postfixOps


/** A trait for data access. */
trait SurveyEventRepository {
  def findBySiteId(siteId: Int): JsArray
  def findByEventDate(eventDate: Date): JsArray
  def findAll: JsArray
}

/** A concrete class that extends [[models.SurveyEventRepository]].	*/
object AnormSurveyEventRepository extends SurveyEventRepository with JSONParsers {

  def findAll(): JsArray = {
    DB.withConnection{ implicit c =>

      SQL(
        """
        select array_to_json(array_agg(survey_event)) from survey_event;
        """
      ).as(array.single)
    }
  }

  /**
   * Find all survey events by site id.
   *
   * @param siteId the search criteria.
   * @return a sequence instance including 0 or more [[models.SurveyEvent]] instances.
   */
  def findBySiteId(siteId: Int): JsArray = {
    DB.withConnection { implicit c =>
      SQL(
        """
        with data as (
          select s.id as id,
          s.event_date as event_date,
     s.photographer as photographer,
     s.analyzer as analyzer,
     s.transect_depth as transect_depth
     from survey_event s
     where s.site_id={id}
     select row_to_json(data) from data;
	"""
      )
        .on('id -> siteId)
        .as(array.single)
    }
  }

  /**
   * Find all survey events by event date.
   *
   * @param eventDate the search criteria.
   * @return a sequence instance including 0 or more [[models.SurveyEvent]] instances.
   */
  def findByEventDate(eventDate: Date): JsArray = {
    DB.withConnection { implicit c =>
      SQL(
        """
	select
	ID,
	SITE_ID,
	EVENT_DATE,
	PHOTOGRAPHER,
	ANALYZER,
	TRANSECT_DEPTH,
	TRANSECT_LENGTH,
	DATA
	from
	SURVEY_EVENT
	where
	EVENT_DATE = {eventDate}
	"""
      )
        .on('eventDate -> eventDate)
        .as(array.single)
    }
  }
}

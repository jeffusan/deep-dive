package models

/** This is the ScalaDoc for SurveyEvent Model of models package. **/

import java.util.Date
import play.api.libs.json._

/**
 * A SurveyEvent including the details of survey result.
 *
 * @contructor create a new [[models.SurveyEvent]] with 8 parameters.
 * @param id the survey event id only use in application.
 * @param siteId the id related to Site only use in application.
 * @param eventDate the survey event date only use in application.
 * @param photographer the name.
 * @param analyzer the name.
 * @param transectDepth the depth.
 * @param transectLength the length.
 * @param data the details of the survey results.
 */
case class SurveyEvent(
  id: Option[Long],
  siteId: Long,
  eventDate: Date,
  photographer: String,
  analyzer: String,
  transectDepth: Int,
  transectLength: Int,
  data: JsValue
)

/** Factory for [[models.SurveyEvent]] instances. */
object SurveyEvent {

  import play.api.libs.functional.syntax._

  /**
   * Implicit serializer for converting Json into instance.
   *
   * @return a reading serializer that specializes in [[models.SurveyEvent]].
   */
  implicit val SurveyEventFromJson: Reads[SurveyEvent] = (
    (__ \ "id").readNullable[Long] ~
    (__ \ "siteId").read[Long] ~
    (__ \ "eventDate").read[Date] ~
    (__ \ "photographer").read[String] ~
    (__ \ "analyzer").read[String] ~
    (__ \ "transectDepth").read[Int] ~
    (__ \ "transectLength").read[Int] ~
    (__ \ "data").read[JsValue]
  )(SurveyEvent.apply _)

  /**
   * Implicit serializer for converting instance into Json.
   *
   * @return a writing serializer that specializes in [[models.SurveyEvent]].
   */
  implicit val SurveyEventToJson: Writes[SurveyEvent] = (
    (__ \ "id").writeNullable[Long] ~
    (__ \ "siteId").write[Long] ~
    (__ \ "eventDate").write[Date] ~
    (__ \ "photographer").write[String] ~
    (__ \ "analyzer").write[String] ~
    (__ \ "transectDepth").write[Int] ~
    (__ \ "transectLength").write[Int] ~
    (__ \ "data").write[JsValue]
  )((se: SurveyEvent) => (
      se.id,
      se.siteId,
      se.eventDate,
      se.photographer,
      se.analyzer,
      se.transectDepth,
      se.transectLength,
      se.data
    ))
}

/** A trait for data access. */
trait SurveyEventRepository {
  def findAllBySiteId(siteId: Long): Seq[SurveyEvent]
  def findAllByEventDate(eventDate: Date): Seq[SurveyEvent]
  def findAllByMonitoringTeamId(monitoringTeamId: Long): Seq[SurveyEvent]
  def save(surveyEvents: Seq[SurveyEvent]): Array[Int]
}

/** A concrete class that extends [[models.SurveyEventRepository]].	*/
object AnormSurveyEventRepository extends SurveyEventRepository {

  import anorm._
  import play.api.db.DB
  import play.api.Play.current
  import org.postgresql.util.PGobject
  import anorm.SqlParser.{ long, int, str, date, get }
  import scala.language.postfixOps

  /**
   * Extractor for generating the [[models.SurveyEvent]] instance to retrieve data from resultset.
   */
  val extractor = {
    long("ID") ~
      long("SITE_ID") ~
      date("EVENT_DATE") ~
      str("PHOTOGRAPHER") ~
      str("ANALYZER") ~
      int("TRANSECT_DEPTH") ~
      int("TRANSECT_LENGTH") ~
      get[JsValue]("DATA") map {
        case i ~ si ~ ed ~ p ~ a ~ td ~ tl ~ d => SurveyEvent(Some(i), si, ed, p, a, td, tl, d)
      }
  }

  /**
   * Implicit converter Json to JsValue instance.
   *
   * @return a sequence instance including 0 or more [[models.MonitoringTeam]] instances.
   */
  implicit def rowToJsValue: Column[JsValue] = Column.nonNull { (value, meta) =>
    val MetaDataItem(qualified, nullable, clazz) = meta
    value match {
      case pgo: org.postgresql.util.PGobject => Right(Json.parse(pgo.getValue))
      case _ => Left(TypeDoesNotMatch("Cannot convert " + value + ":" +
        value.asInstanceOf[AnyRef].getClass + " to JsValue for column " + qualified))
    }
  }
  /**
   * Find all survey events by site id.
   *
   * @param siteId the search criteria.
   * @return a sequence instance including 0 or more [[models.SurveyEvent]] instances.
   */
  def findAllBySiteId(siteId: Long): Seq[SurveyEvent] = {
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
						SITE_ID = {siteId}
				"""
      )
        .on('siteId -> siteId)
        .as(extractor *)
    }
  }

  /**
   * Find all survey events by event date.
   *
   * @param eventDate the search criteria.
   * @return a sequence instance including 0 or more [[models.SurveyEvent]] instances.
   */
  def findAllByEventDate(eventDate: Date): Seq[SurveyEvent] = {
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
        .as(extractor *)
    }
  }

  /**
   * Find all survey events by monitoring team id.
   *
   * @param monitoringTeamId the search criteria.
   * @return a sequence instance including 0 or more [[models.SurveyEvent]] instances.
   */
  def findAllByMonitoringTeamId(monitoringTeamId: Long): Seq[SurveyEvent] = {
    DB.withConnection { implicit c =>
      SQL(
        """
					select
						se.ID,
						se.SITE_ID,
						se.EVENT_DATE,
						se.PHOTOGRAPHER,
						se.ANALYZER,
						se.TRANSECT_DEPTH,
						se.TRANSECT_LENGTH,
						se.DATA
					from
						SURVEY_EVENT se,
						SURVEYEVENT_MONITORING_TEAM smt
					where
						se.ID = smt.SURVEYEVENT_ID
					and
						smt.MONITORINGTEAM_ID = {monitoringTeamId}
				"""
      )
        .on('monitoringTeamId -> monitoringTeamId)
        .as(extractor *)
    }
  }

  /**
   * Save the survey events.
   *
   * @param a sequence instance including 0 or more [[models.SurveyEvent]] instances.
   * @return a array instance including 0 or more [[Int]] instances.Int is a result for each query.
   */
  def save(surveyEvents: Seq[SurveyEvent]): Array[Int] = {
    DB.withTransaction { implicit c =>
      val insertQuery = SQL(
        """
					insert into SURVEY_EVENT (
						SITE_ID,
						EVENT_DATE,
						PHOTOGRAPHER,
						ANALYZER,
						TRANSECT_DEPTH,
						TRANSECT_LENGTH,
						DATA)
					values (
						{siteId}, 
						{photographer}, 
						{analyzer}, 
						{eventDate}, 
						{transectDepth}, 
						{transectLength},
						{data})
				"""
      )

      val batchInsert = (insertQuery.asBatch /: surveyEvents)(
        (s, v) => s.addBatchParams(
          v.siteId,
          v.eventDate,
          v.photographer,
          v.analyzer,
          v.transectDepth,
          v.transectLength,
          {
            val pgObject = new PGobject()
            pgObject.setType("jsonb")
            pgObject.setValue(Some(v.data.toString).getOrElse("{}"))
            anorm.Object(pgObject)
          }
        )
      )
      batchInsert.execute()
    }
  }
}

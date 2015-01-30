package models

import java.util.Date
import play.api.libs.json._
import play.api.libs.functional.syntax._

case class SurveyEvent (
  id: Option[Long],
  siteId: Long,
  eventDate: Date,
  photographer: String,
  analyzer: String,
  transectDepth: Int,
  transectLength: Int,
  data: JsValue
)

object SurveyEvent { 

	import anorm._
	import play.api.db.DB
	import anorm.SqlParser.{long, int, str, date, get}
	import play.api.Play.current
	import org.postgresql.util.PGobject

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

  val extractor = {
		long("ID") ~
		long("SITE_ID") ~
		date("EVENT_DATE") ~
		str("PHOTOGRAPHER") ~
		str("ANALYZER") ~
		int("TRANSECT_DEPTH") ~
		int("TRANSECT_LENGTH") ~
		get[JsValue]("DATA") map {
	    case i~si~ed~p~a~td~tl~d => SurveyEvent(Some(i), si, ed, p, a, td, tl, d)
	  }
	}

	implicit def rowToJsValue: Column[JsValue] = Column.nonNull { (value, meta) =>
	    val MetaDataItem(qualified, nullable, clazz) = meta
	    value match {
	      case pgo: org.postgresql.util.PGobject => Right(Json.parse(pgo.getValue))
	      case _ => Left(TypeDoesNotMatch("Cannot convert " + value + ":" +
	          value.asInstanceOf[AnyRef].getClass + " to JsValue for column " + qualified))
	  }
	}

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
			.as (extractor *)
		}
 	}

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
			.as (extractor *)
		}
 	}

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
			.as (extractor *)
		}
 	}

 	def save(surveyEvents: Seq[SurveyEvent]) = {
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
				""")

	    val batchInsert = (insertQuery.asBatch /: surveyEvents)(
	      (s, v) => s.addBatchParams(
	      	v.siteId, 
	      	v.eventDate, 
	      	v.photographer, 
	      	v.analyzer, 
	      	v.transectDepth, 
	      	v.transectLength, 
	      	{val pgObject = new PGobject()
						pgObject.setType("jsonb")
						pgObject.setValue(Some(v.data.toString).getOrElse("{}"))
		      	anorm.Object(pgObject)
					})
	    )
	    batchInsert.execute()
	  }
 	}
}

trait SurveyEventRepository {
	def findAllBySiteId(siteId: Long): Seq[SurveyEvent]
	def findAllByEventDate(eventDate: Date): Seq[SurveyEvent]
	def findAllByMonitoringTeamId(monitoringTeamId: Long): Seq[SurveyEvent]
	def save(surveyEvents: Seq[SurveyEvent])
}

class AnormSurveyEventRepository extends SurveyEventRepository {

	def findAllBySiteId(siteId: Long): Seq[SurveyEvent] = {
		SurveyEvent.findAllBySiteId(siteId)
	}

	def findAllByEventDate(eventDate: Date): Seq[SurveyEvent] = {
		SurveyEvent.findAllByEventDate(eventDate)
	}

	def findAllByMonitoringTeamId(monitoringTeamId: Long): Seq[SurveyEvent] = {
		SurveyEvent.findAllByMonitoringTeamId(monitoringTeamId)
	}

	def save(surveyEvents: Seq[SurveyEvent]) = {
		SurveyEvent.save(surveyEvents)
	}
}

package models

import java.util.Date
import play.api.libs.json._
import play.api.libs.functional.syntax._

case class SurveyEvent (
  id: Option[Long],
  siteId: Long,
  photographer: String,
  analyzer: String,
  transectDepth: Int,
  transectLength: Int,
  data: JsValue,
  eventDate: Date = new Date
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
    (__ \ "photographer").read[String] ~
    (__ \ "analyzer").read[String] ~
    (__ \ "transectDepth").read[Int] ~
    (__ \ "transectLength").read[Int] ~
    (__ \ "data").read[JsValue] ~
    (__ \ "eventDate").read[Date]
  )(SurveyEvent.apply _)

  implicit val SurveyEventToJson: Writes[SurveyEvent] = (
    (__ \ "id").writeNullable[Long] ~
    (__ \ "siteId").write[Long] ~
    (__ \ "photographer").write[String] ~
    (__ \ "analyzer").write[String] ~
    (__ \ "transectDepth").write[Int] ~
    (__ \ "transectLength").write[Int] ~
    (__ \ "data").write[JsValue] ~
    (__ \ "eventDate").write[Date]
  )((se: SurveyEvent) => (
    se.id,
    se.siteId,
    se.photographer,
    se.analyzer,
    se.transectDepth,
    se.transectLength,
    se.data,
    se.eventDate
  ))

  val extractor = {
		long("ID")~
		long("SITE_ID")~
		str("PHOTOGRAPHER")~
		str("ANALYZER")~
		int("TRANSECT_DEPTH")~
		int("TRANSECT_LENGTH")~
		get[JsValue]("DATA")~
		date("EVENT_DATE") map {
	    case i~si~p~a~td~tl~d~ed => SurveyEvent(Some(i), si, p, a, td, tl, d, ed)
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

	def findAllBySiteId(siteId: Long): List[SurveyEvent] = {
		DB.withConnection { implicit c => 
			SQL(
				"""
					select
						ID,
						SITE_ID,
						PHOTOGRAPHER,
						ANALYZER,
						TRANSECT_DEPTH,
						TRANSECT_LENGTH,
						DATA,
						EVENT_DATE
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

 	def findAllByEventDate(eventDate: Date): List[SurveyEvent] = {
		DB.withConnection { implicit c => 
			SQL(
				"""
					select
						ID,
						SITE_ID,
						PHOTOGRAPHER,
						ANALYZER,
						TRANSECT_DEPTH,
						TRANSECT_LENGTH,
						DATA,
						EVENT_DATE
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

 	def findAllByMonitoringTeamId(monitoringTeamId: Long): List[SurveyEvent] = {
		DB.withConnection { implicit c => 
			SQL(
				"""
					select
						se.ID,
						se.SITE_ID,
						se.PHOTOGRAPHER,
						se.ANALYZER,
						se.TRANSECT_DEPTH,
						se.TRANSECT_LENGTH,
						se.DATA,
						se.EVENT_DATE
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

 	def save(surveyEvents: List[SurveyEvent]) = {
		DB.withTransaction { implicit c => 
  	    val insertQuery = SQL(
				"""
					insert into SURVEY_EVENT (
						SITE_ID,
						PHOTOGRAPHER,
						ANALYZER,
						TRANSECT_DEPTH,
						TRANSECT_LENGTH,
						DATA,
						EVENT_DATE)
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
	      	v.photographer, 
	      	v.analyzer, 
	      	v.eventDate, 
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
	def findAllBySiteId(siteId: Long): List[SurveyEvent]
	def findAllByEventDate(eventDate: Date): List[SurveyEvent]
	def findAllByMonitoringTeamId(monitoringTeamId: Long): List[SurveyEvent]
	def save(surveyEvents: List[SurveyEvent])
}

class AnormSurveyEventRepository extends SurveyEventRepository {

	def findAllBySiteId(siteId: Long): List[SurveyEvent] = {
		SurveyEvent.findAllBySiteId(siteId)
	}

	def findAllByEventDate(eventDate: Date): List[SurveyEvent] = {
		SurveyEvent.findAllByEventDate(eventDate)
	}

	def findAllByMonitoringTeamId(monitoringTeamId: Long): List[SurveyEvent] = {
		SurveyEvent.findAllByMonitoringTeamId(monitoringTeamId)
	}

	def save(surveyEvents: List[SurveyEvent]) = {
		SurveyEvent.save(surveyEvents)
	}
}

trait Service {
	// def convertToJson(input: List[Any]) = Option[JsValue] {
	// 	input match {
	// 		case Nil => None
	// 		case _ => Some(Json.toJson(input)) 
	// 	}
	// }
}

class SurveyEventService(repository: SurveyEventRepository) extends Service {
	import play.api.libs.json._
	//import models.SurveyEvent

	def getJsonBySiteId(siteId: Long): Option[JsValue] = {
		val result = repository.findAllBySiteId(siteId)
		result match {
			case Nil => None
			case _ => Some(Json.toJson(result)) 
		}
		// result match {
		// 	case List() => None
		// 	case _ => Some(Json.toJson(result)) 
		// }
	}

	def getJsonByEventDate(eventDate: Date): Option[JsValue] = {
		val result = repository.findAllByEventDate(eventDate)
		result match {
			case List() => None
			case _ => Some(Json.toJson(result))
		}
	}

	def getJsonByMonitoringTeamId(monitoringTeamId: Long): Option[JsValue] = {
		val result = repository.findAllByMonitoringTeamId(monitoringTeamId)
		result match {
			case List() => None
			case _ => Some(Json.toJson(result))
		}
	}

	def save(surveyEvents: List[SurveyEvent]) = {
		repository.save(surveyEvents)
	}

}

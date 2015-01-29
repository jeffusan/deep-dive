package models

case class MonitoringTeam (
	id: Option[Long],
	name: String
)

object MonitoringTeam {

	import play.api.libs.json._
  import play.api.libs.functional.syntax._

  implicit val MonitoringTeamFromJson: Reads[MonitoringTeam] = (
    (__ \ "id").readNullable[Long] ~
    (__ \ "name").read[String]
  )(MonitoringTeam.apply _)

  implicit val MonitoringTeamToJson: Writes[MonitoringTeam] = (
    (__ \ "id").writeNullable[Long] ~
    (__ \ "name").write[String]
  )((mt: MonitoringTeam) => (
    mt.id,
    mt.name
  ))

	import anorm._
	import play.api.db.DB
	import anorm.SqlParser.{scalar, long, str, flatten}
	import play.api.Play.current

  val extractor = {
		long("ID")~
		str("NAME") map {
			case id~name => MonitoringTeam(Some(id), name)
		}
	}

  def findOneById(id: Long): Option[MonitoringTeam] = {
		DB.withConnection{ implicit c => 
			SQL(
				"""
					select
						ID,
						NAME
					from
						MONITORING_TEAM
					where
						id = {id}
				"""
				)
				.on("id" -> id)
				.as(extractor singleOpt)
		}
	}

	def findlAll(): List[MonitoringTeam] = {
		DB.withConnection{ implicit c => 
			SQL(
				"""
					select
						ID,
						NAME
					from
						MONITORING_TEAM
				"""
				)
				.as(extractor *)
		}
	}

	def save(monitoringTeams: Seq[MonitoringTeam]) = {
		DB.withTransaction{ implicit c =>
	    val insertQuery = SQL(
				"""
					insert into MONITORING_TEAM (
						NAME)
					values (
						{name})
				""")

	    val batchInsert = (insertQuery.asBatch /: monitoringTeams)(
	      (s, v) => s.addBatchParams(
	      	v.name)
	    )
	    batchInsert.execute()
		}
	}
}

trait MonitoringTeamRepository {
	def findOneById(id: Long): Option[MonitoringTeam]
	def findlAll(): List[MonitoringTeam]
	def save(monitoringTeams: List[MonitoringTeam])
}

class AnormMonitoringTeamRepository extends MonitoringTeamRepository {

	def findOneById(id: Long): Option[MonitoringTeam] = {
		MonitoringTeam.findOneById(id)
	}

	def findlAll(): List[MonitoringTeam] = {
		MonitoringTeam.findlAll()
	}

	def save(monitoringTeams: List[MonitoringTeam]) = {
		MonitoringTeam.save(monitoringTeams)
	}
}

class MonitoringTeamService(repository: MonitoringTeamRepository) {
	import play.api.libs.json._
	//import models.MonitoringTeam

	def getJsonById(id: Long): Option[JsValue] = {
		val result = repository.findOneById(id)
		result match {
			case None => None
			case _ => Some(Json.toJson(result)) 
		}
	}

	def getJsonByEventDate(): Option[JsValue] = {
		val result = repository.findlAll()
		result match {
			case Nil => None
			case _ => Some(Json.toJson(result))
		}
	}

	def save(monitoringTeams: List[MonitoringTeam]) = {
		repository.save(monitoringTeams)
	}
}
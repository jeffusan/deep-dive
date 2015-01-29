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

	def findlAll(): Seq[MonitoringTeam] = {
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
	def findlAll(): Seq[MonitoringTeam]
	def save(monitoringTeams: Seq[MonitoringTeam])
}

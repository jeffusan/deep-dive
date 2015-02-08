package models

/** This is the ScalaDoc for MonitoringTeam Model of models package. **/

import play.api.libs.json._

/**
 * A MonitoringTeam that is doing the monitoring.
 *
 * @contructor create a new [[models.MonitoringTeam]] with id and name.
 * @param id the team id only use in application.
 * @param name the monitoring team's name.
 */
case class MonitoringTeam (
  id: Option[Long],
  name: String
)

/** Factory for [[models.MonitoringTeam]] instances. */
object MonitoringTeam {

  import play.api.libs.functional.syntax._

  /**
   * Implicit serializer for converting Json into instance.
   *
   * @return a reading serializer that specializes in [[models.MonitoringTeam]].
   */
  implicit val MonitoringTeamFromJson: Reads[MonitoringTeam] = (
    (__ \ "id").readNullable[Long] ~
    (__ \ "name").read[String]
  )(MonitoringTeam.apply _)

  /**
   * Implicit serializer for converting instance into Json.
   *
   * @return a writing serializer that specializes in [[models.MonitoringTeam]].
   */
  implicit val MonitoringTeamToJson: Writes[MonitoringTeam] = (
    (__ \ "id").writeNullable[Long] ~
    (__ \ "name").write[String]
  )((mt: MonitoringTeam) => (
      mt.id,
      mt.name
    ))
}

/** A trait for data access. */
trait MonitoringTeamRepository {

  def findAll(): Seq[MonitoringTeam]

  def save(monitoringTeams: Seq[MonitoringTeam]): Array[Int]

}

/** A concrete class that extends [[models.MonitoringTeamRepository]].	*/
object AnormMonitoringTeamRepository extends MonitoringTeamRepository {

  import anorm._
  import play.api.db.DB
  import anorm.SqlParser.{ scalar, long, str, flatten }
  import play.api.Play.current


  /**
   * Extractor for generating the [[models.MonitoringTeam]] instance to retrieve data from resultset.
   */
  val extractor: RowParser[MonitoringTeam] = {
    long("ID") ~
    str("NAME") map {
      case id ~ name => MonitoringTeam(id=Some(id), name=name)
    }
  }

  /**
   * Find all monitoring teams.
   *
   * @return a sequence instance including 0 or more [[models.MonitoringTeam]] instances.
   */
  def findAll(): Seq[MonitoringTeam] = {
    DB.withConnection { implicit c =>
      SQL(
        """
	select
        ID,
	NAME
	from
	MONITORING_TEAM
	"""
      ).as(extractor *)
    }
  }

  /**
   * Save the monitoring teams.
   *
   * @param a sequence instance including 0 or more [[models.MonitoringTeam]] instances.
   * @return a array instance including 0 or more [[Int]] instances.Int is a result for each query.
   */
  def save(monitoringTeams: Seq[MonitoringTeam]): Array[Int] = {
    DB.withTransaction { implicit c =>
      val insertQuery = SQL(
        """
	insert into MONITORING_TEAM (NAME) values ({name})
	"""
      )

      val batchInsert = (insertQuery.asBatch /: monitoringTeams)(
        (s, v) => s.addBatchParams(
          v.name
        )
      )
      batchInsert.execute()
    }
  }
}

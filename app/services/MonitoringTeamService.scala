package services
/** This is the ScalaDoc for MonitoringTeam Service of services package. **/

import models.{MonitoringTeam, MonitoringTeamRepository}

/** A MonitoringTeam that is doing the monitoring.
  *
  * @contructor create a new [[services.MonitoringTeamService]] with a instance extendes MonitoringTeamRepogitory.
  * @param id the team id only use in application.
  * @param name the monitoring team's name.
  */
class MonitoringTeamService(repository: MonitoringTeamRepository) {

  import java.util.Date
  import play.api.libs.json._

  /** 
    * Find all monitoring teams.
    *
    * @return a option instance has JsValue Type.
    */
  def findAll(): Seq[MonitoringTeam] = {
    repository.findAll()
  }

  /**
    * Save the monitoring teams.
    *
    * @param a sequence instance including 0 or more [[models.MonitoringTeam]] instances.
    * @return a array instance including 0 or more [[Int]] instances.Int is a result for each query.
    */
  def save(monitoringTeams: Seq[MonitoringTeam]): Array[Int] = {
    repository.save(monitoringTeams)
  }

}

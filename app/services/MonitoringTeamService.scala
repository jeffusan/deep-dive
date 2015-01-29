package services

import java.util.Date
import play.api.libs.json._
import models.{MonitoringTeam, MonitoringTeamRepository}

class AnormMonitoringTeamRepository extends MonitoringTeamRepository {

	def findlAll(): Seq[MonitoringTeam] = {
		MonitoringTeam.findlAll()
	}

	def save(monitoringTeams: Seq[MonitoringTeam]) = {
		MonitoringTeam.save(monitoringTeams)
	}
}

class MonitoringTeamService(repository: MonitoringTeamRepository) {
	import play.api.libs.json._

	def findAll(): Option[JsValue] = {
		val result = repository.findlAll()
		result match {
			case Nil => None
			case _ => Some(Json.toJson(result))
		}
	}

	def save(monitoringTeams: Seq[MonitoringTeam]) = {
		repository.save(monitoringTeams)
	}
	
}
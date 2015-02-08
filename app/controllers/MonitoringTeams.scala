package controllers

import play.api.libs.json.Json
import play.api.mvc._
import play.api.Logger
import services.MonitoringTeamService
import models.{AnormMonitoringTeamRepository, MonitoringTeam}

trait MonitoringTeams extends Controller with Security {

  lazy val service = new MonitoringTeamService(AnormMonitoringTeamRepository)

  /** Find all monitoring teams. */
  def show() = HasToken() { token => userId => implicit request =>
    Ok(Json.toJson(service.findAll()))
  }

  /** Create a monitoring team. */
  def create() = HasToken() { token => userId => implicit request =>
    val body = request.body.asJson.get
    val name = (body \ "name").as[String]
    service.save(List(new MonitoringTeam(null, name)))
    Ok(Json.obj("ok" -> "positive response"))
  }

  /** Modify name of a monitoring team. */
  def update() = HasToken() { token => userId => implicit request =>
    val body = request.body.asJson.get
    val id = (body \ "id").as[Long]
    val name = (body \ "name").as[String]
    service.save(List(new MonitoringTeam(Some(id), name)))
    Ok(Json.obj("ok" -> "positive response"))
  }

}

object MonitoringTeams extends MonitoringTeams

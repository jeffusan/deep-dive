package controllers

import play.api.libs.json.Json
import play.api.mvc._
import play.api.Logger
import services.MonitoringTeamService
import models.AnormMonitoringTeamRepository

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
    if (service.insert(name)) {
      Ok(messageOk)
    } else {
      BadRequest(messageError("faild to create"))
    }
  }

  /** Modify name of a monitoring team. */
  def update() = HasToken() { token => userId => implicit request =>
    val body = request.body.asJson.get
    val id = (body \ "id").as[Long]
    val name = (body \ "name").as[String]
    if (service.update(id, name)) {
      Ok(messageOk)
    } else {
      BadRequest(messageError("faild to create"))
    }
  }

}

object MonitoringTeams extends MonitoringTeams

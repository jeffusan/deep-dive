package controllers

import play.api.libs.json._
import play.api.cache._
import play.api.data.Forms._
import play.api.data._
import play.api.mvc._
import play.api.Logger
import java.io.FileInputStream
import models.AnormSurveyEventRepository
import akka.util.Timeout
import scala.concurrent.duration._
import akka.pattern.ask
import scala.concurrent.ExecutionContext.Implicits.global
import play.api.libs.concurrent.Akka
import akka.actor.Props
import actors.{BenthicInputFileHandler, BenthicFileMessage}

trait SurveyEvents extends Controller with Security {


  /** Find all survey events by site id. */
  def showBySiteId(siteId: Int) = HasToken() { token => userId => implicit request =>
    findBySiteId(siteId)
  }
  def findBySiteId(siteId: Int) = {
    Ok(AnormSurveyEventRepository.findBySiteId(siteId))
  }

  def show() = HasAdminToken() { token => userId => implicit request =>
    Ok(Json.toJson(AnormSurveyEventRepository.findAll))
  }

  def benthicUploadHandler = Action(parse.multipartFormData) { implicit request =>

    Logger.warn("Request body: " + request.body)
    val dataParts = request.body.dataParts
    val photographer = dataParts("photographer")(0)
    val depth = dataParts("depth")(0)
    val analyzer = dataParts("analyzer")(0)
    val eventDate = dataParts("eventDate")(0)

    if(request.body.files.isEmpty) BadRequest("Invalid file!")
    else if (request.body.asFormUrlEncoded.isEmpty) BadRequest("Invalid Data!")
    else {
      val filepart = request.body.files(0)
      Logger.warn("Filepart: " + filepart.toString())
      Logger.warn("Ref: " + filepart.ref.toString())
      val fileIn = new FileInputStream(filepart.ref.file)
      val inputHandler = Akka.system.actorOf(Props(new BenthicInputFileHandler()))
      implicit val timeout = Timeout(25 seconds)
      val future = inputHandler ? BenthicFileMessage(photographer, depth, analyzer, eventDate, fileIn)
      future.map { result =>
        Logger.warn("Total number of words " + result)
      }
      Ok("Everything is okay!")
    }

  }

}

object SurveyEvents extends SurveyEvents

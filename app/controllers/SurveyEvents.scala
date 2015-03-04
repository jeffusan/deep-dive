package controllers

import play.api.libs.json._
import play.api.cache._
import play.api.data.Forms._
import play.api.data._
import play.api.mvc._
import play.api.Logger
import play.api.libs.iteratee.Enumerator
import models.AnormSurveyEventRepository

case class UploadBenthicData(depth: String, photographer: String, analyzer: String, eventDate : String, data: String)

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

  val uploadForm = Form(
    mapping(
      "transectDepth" -> nonEmptyText,
      "photographer" -> nonEmptyText,
      "analyzer" -> nonEmptyText,
      "eventDate" -> nonEmptyText,
      "fileData" ->  nonEmptyText
    )(UploadBenthicData.apply)(UploadBenthicData.unapply)
  )

  def benthicUpload() = HasAdminToken(parse.json) { token => userId => implicit request =>

    uploadForm.bindFromRequest.fold(
      formWithErrors => BadRequest(Json.obj("msg" -> "this isn't working for me")),
      up => {
        Logger.warn(up.toString())
        request.body
        Ok(Json.obj("msg" -> "nice work"))
      }
    )

  }

  def benthicUploadHandler = Action(parse.multipartFormData) { implicit request =>

    Logger.warn("Request body: " + request.body)
    val dataParts = request.body.dataParts
    val photographer = dataParts("photographer")(0)
    val depth = dataParts("depth")(0)
    val analyzer = dataParts("analyzer")(0)
    val eventDate = dataParts("eventDate")(0)
    Logger.warn(s"Photographer: ${photographer}")
    Logger.warn(s"TransectDepth: ${depth}")
    Logger.warn(s"Analyzer: ${analyzer}")
    Logger.warn(s"EventDate: ${eventDate}")

    if(request.body.files.isEmpty) BadRequest("Invalid file!")
    else if (request.body.asFormUrlEncoded.isEmpty) BadRequest("Invalid Data!")
    else {
      val dataContent = Enumerator.fromStream(request.body.file("inputFile"))
    }
    Ok("Everything is okay!")
  }


}

object SurveyEvents extends SurveyEvents

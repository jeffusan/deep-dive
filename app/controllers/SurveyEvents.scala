package controllers

import play.api.libs.json._
import play.api.cache._
import play.api.data.Forms._
import play.api.data._
import play.api.mvc._
import play.api.Logger
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

    Logger.warn("Accepted")
      request.body.file("picture").map { picture =>
    import java.io.File
    val filename = picture.filename
    val contentType = picture.contentType
    picture.ref.moveTo(new File(s"/tmp/picture/$filename"))
        Ok(Json.obj("message" -> "File uploaded"))
      }.getOrElse {
        Redirect(routes.Application.index).flashing("error" -> "Missing file")
      }

  }

}

object SurveyEvents extends SurveyEvents

package funspec

import java.io.{ByteArrayOutputStream, File}
import java.nio.charset.Charset
import java.nio.file.{Files, StandardCopyOption, StandardOpenOption}

import org.apache.http.entity.ContentType
import org.apache.http.entity.mime.MultipartEntityBuilder
//import org.specs2.mutable.Specification
import play.api.http.{ContentTypeOf, Writeable}
import play.api.libs.Files.TemporaryFile
import play.api.mvc.MultipartFormData.FilePart
import play.api.mvc.{MultipartFormData, Result}
import play.api.test.Helpers._
import play.api.test.{FakeHeaders, FakeRequest, WithApplication}
import org.scalatestplus.play._
import scala.concurrent.Future
import play.api.libs.json.Json
import play.api.libs.json._
import play.api.Logger

class SurveyEventUploadSpec extends PlaySpec with OneAppPerSuite {

   def writeableOf_multipartFormData(request: FakeRequest[MultipartFormData[TemporaryFile]]) = {
     val builder = MultipartEntityBuilder.create()
     request.body.dataParts.foreach { case (k, vs) => builder.addTextBody(k, vs.mkString)}

     // ContentType part is necessary here because it gets parsed as a DataPart otherwise.
     request.body.files.foreach {
       case f =>
         builder.addBinaryBody(
         f.filename,
         f.ref.file,
         ContentType.create(f.contentType.get, null: Charset),
         f.filename)}

     val entity = builder.build()

     implicit val contentTypeOf_MultipartFormData: ContentTypeOf[MultipartFormData[TemporaryFile]] = ContentTypeOf[MultipartFormData[TemporaryFile]](Some(entity.getContentType.getValue))

     Writeable[MultipartFormData[TemporaryFile]] {
       (mfd: MultipartFormData[TemporaryFile]) =>
       val outputStream = new ByteArrayOutputStream()

       entity.writeTo(outputStream)

       outputStream.toByteArray
     }
   }

  def sendUploadRequest(url: String, file: File, mimeType: String, parameters: Map[String, String]): Future[Result] = {
    // Your original file will be deleted after the controller executes if you don't do the copy part below
    val tempFile = TemporaryFile("inputFile")
    Files.copy(file.toPath, tempFile.file.toPath, StandardCopyOption.REPLACE_EXISTING)

    val part = FilePart[TemporaryFile](
      key = tempFile.file.getName,
      filename = tempFile.file.getName,
      contentType = Some(mimeType),
      ref = tempFile)

    val formData = MultipartFormData(
      dataParts = parameters.map {
        case (k, v) => k -> Seq(v)}, files = Seq(part), badParts = Nil, missingFileParts = Nil)

    val request = FakeRequest("POST", url, FakeHeaders(), formData)

    implicit val writeable = writeableOf_multipartFormData(request)

    route(request)(writeable).get
  }

  "Upload" should {
    "pass" in new WithApplication {

      val tempFile = TemporaryFile("inputFile")
      val fileRef = tempFile.file

      Files.write(fileRef.toPath, """{"hello":"world"}""".getBytes, StandardOpenOption.WRITE)

      val parameters = Map(
        "photographer" -> "AAA",
        "depth" -> "25m",
        "analyzer" -> "BBB",
        "eventDate" -> "2015-01-01"
      )
      val json = Json.obj("message" -> "Everything is okay!")

      val future = sendUploadRequest(
        controllers.routes.SurveyEvents.benthicUploadHandler().url,
        fileRef, "multipart/form-data", parameters
      )

      status(future) mustBe OK
      val jsonResult = contentAsJson(future)
      jsonResult mustBe json
    }
  }
}

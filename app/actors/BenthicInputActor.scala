package actors

import akka.actor._
import play.api.Logger
import java.io.FileInputStream
import akka.dispatch.ExecutionContexts._
import play.api.libs.json._
import scala.concurrent.{Future, Await}
import akka.pattern.ask
import akka.util.Timeout
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Success, Failure}
import actors.utils.{Sheet}
import models.AnormSurveyEventRepository
import java.util.Date

case class BenthicFileInputMessage(photographer: String, monitoring: String, depth: Int, length: Int, analyzer: String, eventDate: Date, input: FileInputStream)

case class SheetResult(sheet: Sheet)

trait BenthicInputResponse
case class ValidBenthicInputResponse(json: JsValue) extends BenthicInputResponse
case class ErrorBenthicInputResponse(message: String) extends BenthicInputResponse

/**
  * Supervisor actor for extracting data from benthic input spreadsheets
  */
class BenthicInputActor extends Actor {

  implicit val timeout = Timeout(15 seconds)

  val workbookActor = context.actorOf(Props[WorkbookActor])

  /**
    * Actor receive function
    */
  def receive = {
    case message: BenthicFileInputMessage => processMessage(sender, message)
  }

  /**
    * Actual message processing takes place here
    */
  def processMessage(requestor: ActorRef, msg: BenthicFileInputMessage) {

    Logger.debug("Processing benthic spreadsheet")

    val future = workbookActor ? new WorkbookMessage(msg.input)
    val result = Await.result(future, Duration("14 seconds")).asInstanceOf[WorkbookResponse]

    result match {
      case a: ValidWorkbookResponse => {
        val response = AnormSurveyEventRepository.add(
          msg.photographer,
          msg.analyzer,
          msg.monitoring,
          msg.depth,
          msg.length,
          msg.eventDate,
          a.json)

        requestor ! new ValidBenthicInputResponse(response)
      }
      case b: ErrorWorkbookResponse => requestor ! new ErrorBenthicInputResponse(b.message)
      case _ => requestor ! new ErrorBenthicInputResponse("Terrible, frightening error")
    }

  }

}

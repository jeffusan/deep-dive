package actors

import akka.actor._
import play.api.Logger
import java.io.FileInputStream
import akka.dispatch.ExecutionContexts._
import info.folone.scala.poi._
import play.api.libs.json._
import scala.concurrent.{Future, Await}
import akka.pattern.ask
import akka.pattern.pipe
import akka.util.Timeout
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Success, Failure}

case class BenthicFileInputMessage(photographer: String, depth: String, analyzer: String, eventDate: String, input: FileInputStream)

case class SheetResult(sheet: Sheet)

case class FutureSheetException(message:String) extends Exception(message)

class BenthicInputActor extends Actor {

  implicit val timeout = Timeout(5 seconds)

  val workbookActor = context.actorOf(Props[WorkbookActor])
  val sheetActor = context.actorOf(Props[SheetActor])
  val transectActor = context.actorOf(Props[TransectActor])
  val surveyResultAggregator = context.actorOf(Props[SurveyResultAggregator])


  def receive = {

    case message: BenthicFileInputMessage => processMessage(sender, message)

  }

  // set to 2 seconds to respond faster than the TestProbe
  def processMessage(requestor: ActorRef, msg: BenthicFileInputMessage) {

    val future = workbookActor ? new WorkbookMessage(msg.input)

    val result = Await.result(future, Duration("4 seconds")).asInstanceOf[WorkbookResponse]

    result match {
      case a: ValidWorkbookResponse => {
        val sheetFuture = sheetActor ? new SheetMessage(a.workbook)
        val sheetResult = Await.result(sheetFuture, Duration("4 seconds")).asInstanceOf[Sheet]
        requestor ! result
      }
      case b: ErrorWorkbookResponse => requestor ! result
      case _ => requestor ! new ErrorWorkbookResponse("Unknown error!")
    }

  }

}

package actors

import info.folone.scala.poi._
import akka.actor._
import play.api.Logger
import akka.dispatch.ExecutionContexts._
import akka.util.Timeout
import akka.pattern.ask
import scala.concurrent.{Future, Await}
import scala.concurrent.duration._



case class SheetMessage(workbook: Workbook)
trait SheetResponse
case class ErrorSheetResponse(message: String) extends SheetResponse
case class ValidSheetResponse(sheet: Sheet) extends SheetResponse

class SheetActor extends Actor {

  implicit val timeout = Timeout(5 seconds)
  val transectActor = context.actorOf(Props[TransectActor])

  def receive = {

    case message: SheetMessage => processMessage(sender, message)
  }

  def processMessage(requestor: ActorRef, msg: SheetMessage) {
    Logger.debug("Finding the sheet")
    val response: SheetResponse = findSheet(msg.workbook)

    response match {
      case a: ValidSheetResponse => {
        val transectFuture = transectActor ? new TransectMessage(a.sheet)
        val transectResult = Await.result(transectFuture, Duration("4 seconds")).asInstanceOf[TransectResponse]
        transectResult match {
          case a: ValidTransectResponse => requestor ! response
          case _ => requestor ! response
        }
      }
      case _ =>
        requestor ! response
    }

  }

  def findSheet(workbook: Workbook): SheetResponse = {
      val maybeSheet = (workbook).sheets.find((s: Sheet) => s.name == "Data Summary")
      val sheet = maybeSheet.getOrElse {
        new ErrorSheetResponse("Exception- unable to find Data Summary sheet")
      }
      new ValidSheetResponse(sheet.asInstanceOf[Sheet])
  }
}

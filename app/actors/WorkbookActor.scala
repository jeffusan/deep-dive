package actors

import akka.actor._
import play.api.Logger
import java.io.{InputStream, FileInputStream}
import akka.dispatch.ExecutionContexts._
import akka.util.Timeout
import akka.pattern.ask
import scala.concurrent.{Future, Await}
import scala.concurrent.duration._
import org.apache.poi.ss.usermodel.FormulaEvaluator
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import play.api.libs.json._

case class WorkbookMessage(input: FileInputStream)
trait WorkbookResponse
case class ErrorWorkbookResponse(message: String) extends WorkbookResponse
case class ValidWorkbookResponse(json: JsValue) extends WorkbookResponse

/**
  * A Workbook actor loads a workbook, extracts the input sheet and returns it
  */
class WorkbookActor extends Actor {

  implicit val timeout = Timeout(5 seconds)
  val sheetActor = context.actorOf(Props[SheetActor])

  def safely[T](handler: PartialFunction[Throwable, T]): PartialFunction[Throwable, T] = {
    //case ex: java.io.FileNotFoundException => return None
    // case ex: OutOfMemoryError (Assorted other nasty exceptions you don't want to catch)
    //If it's an exception they handle, pass it on
    case ex: Throwable if handler.isDefinedAt(ex) => handler(ex)
    // If they didn't handle it, rethrow. This line isn't necessary, just for clarity
    case ex: Throwable => throw ex
  }

  def receive = {
    case message: WorkbookMessage => loadWorkbook(sender, message.input)
  }

  def loadWorkbook(requestor: ActorRef, inputStream: FileInputStream) {

    Logger.debug("Loading the workbook")

    try {
      val workBook = new XSSFWorkbook(inputStream)
      Logger.debug("loaded valid workbook")

      val sheetFuture = sheetActor ? new SheetMessage(workBook)
      val sheetResult = Await.result(sheetFuture, Duration("10 seconds")).asInstanceOf[SheetResponse]

      sheetResult match {
        case a: ValidSheetResponse => {
          Logger.info("You're a hero")
          requestor ! new ValidWorkbookResponse(a.json)
        }
        case b: ErrorSheetResponse => {
          val message = "Something is wrong with the sheet"
          Logger.error(message)
          requestor ! new ErrorWorkbookResponse(message)
        }
        case _ => {
          val message = "Unfathomable! Without fathom!"
          Logger.error(message)
          requestor ! new ErrorWorkbookResponse(message)
        }
      }

    } catch safely {
      case nse: java.io.FileNotFoundException => {
        val errorMessage = "Input file was not found"
        Logger.error(errorMessage)
        requestor ! new ErrorWorkbookResponse(errorMessage)
      }

      case ile: java.lang.IllegalArgumentException => {
        val errorMessage = "Illegal file type"
        Logger.error(errorMessage)
        requestor !new ErrorWorkbookResponse(errorMessage)
      }

      case _ => {
        requestor ! new ErrorWorkbookResponse("Illegal file type")
      }

    }
  }

}

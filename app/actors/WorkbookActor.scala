package actors

import akka.actor._
import play.api.Logger
import java.io.{InputStream, FileInputStream}
import akka.dispatch.ExecutionContexts._
import info.folone.scala.poi._
import akka.util.Timeout
import akka.pattern.ask
import scala.concurrent.{Future, Await}
import scala.concurrent.duration._

case class WorkbookMessage(input: FileInputStream)
trait WorkbookResponse
case class ErrorWorkbookResponse(message: String) extends WorkbookResponse
case class ValidWorkbookResponse(workbook: Workbook) extends WorkbookResponse

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

    case message: WorkbookMessage =>
      Logger.debug("Loading the workbook")
      val response: WorkbookResponse = loadWorkbook(message.input)
      sender ! response
  }

  def loadWorkbook(inputStream: FileInputStream): WorkbookResponse = {

    try {
      val workBook = load(inputStream)
      val sheetFuture = sheetActor ? new SheetMessage(workBook)
      val sheetResult = Await.result(sheetFuture, Duration("4 seconds")).asInstanceOf[SheetResponse]

      sheetResult match {
        case a: ValidSheetResponse => {
          Logger.info("You're a hero")
        }
        case b: ErrorSheetResponse => {
          Logger.error("Something is wrong with the sheet")
        }
        case _ => {
          Logger.error("Unfathomable! Without fathom!")
        }
      }
      new ValidWorkbookResponse(workBook)
    } catch safely {
      case nse: java.io.FileNotFoundException =>
        val errorMessage = "Input file was not found"
        Logger.error(errorMessage)
        new ErrorWorkbookResponse(errorMessage)
      case ile: java.lang.IllegalArgumentException =>
        val errorMessage = "Illegal file type"
        Logger.error(errorMessage)
        new ErrorWorkbookResponse(errorMessage)
    }
  }

}

object load {
  def apply(is: InputStream): Workbook =
    Workbook(is).fold(ex ⇒ throw ex, identity).unsafePerformIO
}

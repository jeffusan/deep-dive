package actors

import akka.actor._
import play.api.Logger
import java.io.FileInputStream
import akka.dispatch.ExecutionContexts._
import info.folone.scala.poi._
import impure._

case class BenthicFileInputMessage(photographer: String, depth: String, analyzer: String, eventDate: String, inputFile: FileInputStream)

class BenthicInputActor extends Actor {

  def safely[T](handler: PartialFunction[Throwable, T]): PartialFunction[Throwable, T] = {
    //case ex: java.io.FileNotFoundException => return None
    // case ex: OutOfMemoryError (Assorted other nasty exceptions you don't want to catch)
    //If it's an exception they handle, pass it on
    case ex: Throwable if handler.isDefinedAt(ex) => handler(ex)
    // If they didn't handle it, rethrow. This line isn't necessary, just for clarity
    case ex: Throwable => throw ex
  }

  def receive = {

    case message: BenthicFileInputMessage =>
      Logger.warn("Hello!")
      val file = loadWorkbook(message.inputFile)

      val validSpreadsheet = file.getOrElse {
        sender ! "not a valid file"
      }

      sender ! "this is a test"
  }

  def loadWorkbook(inputStream: FileInputStream): Option[Workbook] = {

    try {
      val workBook = load(inputStream)
      Some(workBook)
    } catch safely {
        case nse: java.io.FileNotFoundException =>
          None
        case ile: java.lang.IllegalArgumentException =>
          None
    }
  }

  object load {
    def apply(inputStream: FileInputStream): Workbook =
      Workbook(inputStream).fold(ex ⇒ throw ex, identity).unsafePerformIO
  }

}

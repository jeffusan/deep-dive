package actors

import info.folone.scala.poi._
import akka.actor._
import play.api.Logger


case class SheetMessage(workbook: Workbook)
trait SheetResponse
case class ErrorSheetResponse(message: String) extends SheetResponse
case class ValidSheetResponse(sheet: Sheet) extends SheetResponse

class SheetActor extends Actor {

  def receive = {

    case message: SheetMessage =>
      Logger.debug("Finding the sheet")
      val response: SheetResponse = findSheet(message.workbook)
      sender ! response
  }

  def findSheet(workbook: Workbook): SheetResponse = {
      val maybeSheet = (workbook).sheets.find((s: Sheet) => s.name == "Data Summary")
      val sheet = maybeSheet.getOrElse {
        new ErrorSheetResponse("Exception- unable to find Data Summary sheet")
      }
      new ValidSheetResponse(sheet.asInstanceOf[Sheet])
  }
}

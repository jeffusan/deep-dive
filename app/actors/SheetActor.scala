package actors

import info.folone.scala.poi._
import akka.actor._
import play.api.Logger

case class SheetMessage(workbook: Workbook)
case class SheetException(smth:String) extends Exception(smth)

class SheetActor extends Actor {

  def receive = {

    case message: SheetMessage =>
      Logger.debug("Loading the sheet")
      val maybeSheet = (message.workbook).sheets.find((s: Sheet) => s.name == "Data Summary")
      val sheet = maybeSheet.getOrElse {
        throw new SheetException("Unable to find the data input sheet")
      }
      sender ! sheet
  }
}

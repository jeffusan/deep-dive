package actors

import info.folone.scala.poi._
import akka.actor._
import play.api.Logger

case class TransectMessage(sheet: Sheet)
case class TransectException(smth:String) extends Exception(smth)

class TransectActor extends Actor {

  def receive = {

    case message: TransectMessage =>
      Logger.debug("Extracting transects")
      val maybeRow = maybeFindTransectRow(message.sheet)
      val row = maybeRow.getOrElse {
        throw new TransectException("Unable to find a transect row")
      }
      val transects = extractTransectValues(row)
      sender ! transects
  }

  def maybeFindTransectRow(sheet: Sheet): Option[Row] = {

    def hasCellWithTransectNameMatch(row: Row): Boolean = {

      val maybeMatch = row.cells.find((c: Cell) => {

        c match {
          case StringCell(_, "TRANSECT NAME") => true
          case _ => false
        }
      })

      !maybeMatch.isEmpty
    }

    sheet.rows.find((r: Row) => hasCellWithTransectNameMatch(r))

  }

  def extractTransectValues(row: Row): Seq[String] = {
    val transectCells = row.cells
      .filter((c: Cell) => c.asInstanceOf[StringCell].data != "TRANSECT NAME")
      .map((c: Cell) => c.asInstanceOf[StringCell].data)

    (for (x <- transectCells) yield x)(collection.breakOut)

  }

}

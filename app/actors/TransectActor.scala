package actors

import info.folone.scala.poi._
import akka.actor._
import play.api.Logger

trait TransectResponse
case class ErrorTransectResponse(message: String) extends TransectResponse
case class ValidTransectResponse(transects: Seq[String]) extends TransectResponse
case class TransectMessage(sheet: Sheet)


class TransectActor extends Actor {

  def receive = {

    case message: TransectMessage => processMessage(sender, message)

  }

  def processMessage(requestor: ActorRef, msg: TransectMessage) {
    Logger.debug("Checking transects")

    val response: TransectResponse = findTransects(msg.sheet)

    requestor ! response
  }

  def findTransects(sheet: Sheet): TransectResponse = {

    def hasCellWithTransectNameMatch(row: Row): Boolean = {

      val maybeMatch = row.cells.find((c: Cell) => {

        c match {
          case StringCell(_, "TRANSECT NAME") => true
          case _ => false
        }
      })

      !maybeMatch.isEmpty
    }

    val maybeRow = sheet.rows.find((r: Row) => hasCellWithTransectNameMatch(r))
    Logger.debug("Has maybeRow: " + maybeRow.isEmpty)
    maybeRow match {
      case None => new ErrorTransectResponse("Could not find a transect row")
      case Some(row) => {
        val transects = extractTransectValues(row)
        new ValidTransectResponse(transects)
      }
    }
  }

  def extractTransectValues(row: Row): Seq[String] = {
    val transectCells = row.cells
      .filter((c: Cell) => c.asInstanceOf[StringCell].data != "TRANSECT NAME")
      .map((c: Cell) => c.asInstanceOf[StringCell].data)

    (for (x <- transectCells) yield x)(collection.breakOut)

  }

}

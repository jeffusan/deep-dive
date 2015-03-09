package actors

import akka.actor._
import akka.dispatch.ExecutionContexts._
import info.folone.scala.poi._

case class BenthicTransectMessage(workBook: Workbook)

class BenthicTransectActor extends Actor {

  def receive = {

    case message: BenthicTransectMessage =>

      val transects = extractTransects(message.workBook)

      val validTransects = transects.getOrElse {
        sender ! "no valid transects"
      }
  }

  def extractTransects(workbook: Workbook): Option[Seq[String]] = {
    None
  }
}

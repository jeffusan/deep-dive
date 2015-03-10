package actors

import akka.actor._
import play.api.Logger
import java.io.FileInputStream
import akka.dispatch.ExecutionContexts._
import info.folone.scala.poi._
import play.api.libs.json._

case class SurveyResult(transects: Seq[String])

class SurveyResultAggregator extends Actor {

  def receive = {
    case message: SurveyResult =>
      Logger.warn("Received Survey Results")
      sender ! Json.obj("message" -> "hurrah")
  }
}

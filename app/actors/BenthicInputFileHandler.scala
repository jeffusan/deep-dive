package actors

import akka.actor._
import play.api.Logger
import java.io.FileInputStream
import akka.dispatch.ExecutionContexts._

case class BenthicFileMessage(photographer: String, depth: String, analyzer: String, eventDate: String, inputFile: FileInputStream)

class BenthicInputFileHandler extends Actor {

  import akka.actor._

  def receive = {

    case message: BenthicFileMessage =>
      Logger.warn("Hello!")
  }
}

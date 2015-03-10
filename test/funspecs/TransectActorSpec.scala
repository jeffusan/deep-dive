package funspec

import actors._
import akka.actor.{Props, ActorSystem}
import akka.testkit.{TestProbe, TestKit}
import play.api.test.WithApplication
import org.specs2.specification.Scope
import org.specs2.mutable.Specification
import scala.concurrent.duration._
import java.io.FileInputStream
//import actors.{T}
import info.folone.scala.poi._
import play.api.Logger

class TransectActorSpec extends Specification {

  class Actors extends TestKit(ActorSystem("SheetActorSpec")) with Scope

  "When given a valid workbook sheet" should {
    val workbook = load(new FileInputStream("test/resources/workbook.xlsx"))
    val maybeSheet:Option[Sheet] = (workbook).sheets.find((s: Sheet) => s.name == "Data Summary")

    "return a list of transects" in new Actors {
      new WithApplication {
        Logger.debug("maybeSheet: " + maybeSheet.isEmpty)
        maybeSheet match {

          case Some(sheet) => {
            val message = new TransectMessage(sheet)
            val transectActor = system.actorOf(Props[TransectActor])
            val probe = TestProbe()

            transectActor.tell(message, probe.ref)

            val response = probe.receiveOne(Duration("10 seconds"))
            response must not be null
            response must beAnInstanceOf[ValidTransectResponse]
            Logger.debug((response.asInstanceOf[ValidTransectResponse]).transects.toString())
          }
          case None => failure
        }

      }
    }
  }
}

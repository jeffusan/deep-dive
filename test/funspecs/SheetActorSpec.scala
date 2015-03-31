package funspec

import actors._
import akka.actor.{Props, ActorSystem}
import akka.testkit.{TestProbe, TestKit}
import org.apache.poi.xssf.usermodel.XSSFWorkbook

import play.api.test.WithApplication
import org.specs2.specification.Scope
import org.specs2.mutable.Specification
import scala.concurrent.duration._
import java.io.FileInputStream
import play.api.libs.json._


class SheetActorSpec extends Specification {

  class Actors extends TestKit(ActorSystem("SheetActorSpec")) with Scope

  "When given a valid workbook a SheetActor" should {
    val workbook = new XSSFWorkbook(new FileInputStream("test/resources/wkb2.xlsx"))
    "return a sheet" in new Actors {
      new WithApplication {
        val message = new SheetMessage(workbook)
        val sheetActor = system.actorOf(Props[SheetActor])
        val probe = TestProbe()

        sheetActor.tell(message, probe.ref)

        val response = probe.receiveOne(Duration("10 seconds"))
        response must not be null
        response must beAnInstanceOf[ValidSheetResponse]

      }
    }
  }
}

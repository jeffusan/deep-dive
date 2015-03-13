package funspec

import actors.{BenthicInputActor, BenthicFileInputMessage, ValidBenthicInputResponse}
import akka.actor.{ActorSystem, Props}
import akka.testkit.{TestProbe, ImplicitSender, TestKit}
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.{Matchers, FunSuiteLike, BeforeAndAfterAll}
import org.specs2.mutable.Specification
import org.specs2.specification.Scope
import play.api.test.WithApplication
import java.io.FileInputStream
import play.api.libs.json._
import scala.concurrent.duration._
import play.api.Logger
import java.util.Date
import java.text.SimpleDateFormat

class BenthicInputActorSpec extends Specification {

  class Actors extends TestKit(ActorSystem("BenthicInputSpec")) with Scope

  "When given input text file a BenthiFileInputActor" should {
    "return a message stating this is not a spreadsheet" in new Actors {
      new WithApplication {
        val date = new SimpleDateFormat("yyyy-MM-dd").parse("2015-01-01")
        val message = new BenthicFileInputMessage("aaa", "bbb", 10, 10, "ccc", date, new FileInputStream("test/resources/wkb2.xlsx"))
        val myActor = system.actorOf(Props[BenthicInputActor])
        val probe = TestProbe()
        myActor.tell(message, probe.ref)

        val response = probe.receiveOne(Duration("25 seconds"))
        response must not be null
        response must beAnInstanceOf[ValidBenthicInputResponse]

      }
    }
  }
}

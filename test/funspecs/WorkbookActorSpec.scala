package funspec

import actors._
import akka.actor.{Props, ActorSystem}
import akka.testkit.{TestProbe, TestKit}
import play.api.test.WithApplication
import org.specs2.specification.Scope
import org.specs2.mutable.Specification
import java.io.FileInputStream
import scala.concurrent.duration._

class WorkbookActorSpec extends Specification {

  class Actors extends TestKit(ActorSystem("WorkbookActorSpec")) with Scope

  "When given a text file a WorkbookActor" should {
    "return an error message" in new Actors {
      new WithApplication {
        val message = new WorkbookMessage(new FileInputStream("test/resources/wrongfile.txt"))
        val workbookActor = system.actorOf(Props[WorkbookActor])
        val probe = TestProbe()
        workbookActor.tell(message, probe.ref)

        val response = probe.receiveOne(Duration("10 seconds"))
        response must not be null
        val expectedResponse = new ErrorWorkbookResponse("Illegal file type")
        response must beLike { case expectedResponse => ok }
      }
    }
  }

  "When given an invalid file path to a WorkbookActor" should {
    "return an error message" in new Actors {
      new WithApplication {
        val message = new WorkbookMessage(new FileInputStream("test/resources/wrongfile.txt"))
        val workbookActor = system.actorOf(Props[WorkbookActor])
        val probe = TestProbe()
        workbookActor.tell(message, probe.ref)

        val response = probe.receiveOne(Duration("10 seconds"))
        response must not be null
        val expectedResponse = new ErrorWorkbookResponse("Input file was not found")
        response must beLike { case expectedResponse => ok }
      }
    }
  }

  "When given an Excel file a WorkbookActor" should {
    "returns a ValidWorkbookMessage" in new Actors {
      new WithApplication {
        val message = new WorkbookMessage(new FileInputStream("test/resources/wkb2.xlsx"))
        val workbookActor = system.actorOf(Props[WorkbookActor])
        val probe = TestProbe()
        workbookActor.tell(message, probe.ref)

        val response = probe.receiveOne(Duration("10 seconds"))
        response must not be null
        response must beAnInstanceOf[ValidWorkbookResponse]
      }
    }
  }
}

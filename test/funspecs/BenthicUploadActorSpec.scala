package funspec

import actors.{BenthicInputActor, BenthicFileInputMessage}
import akka.actor.{ActorSystem, Props}
import akka.testkit.{TestProbe, ImplicitSender, TestKit}
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.{Matchers, FunSuiteLike, BeforeAndAfterAll}
import org.specs2.mutable.Specification
import org.specs2.specification.Scope
import play.api.test.WithApplication
import java.io.FileInputStream

class BenthicInputActorSpec extends Specification {

  class Actors extends TestKit(ActorSystem("BenthicInputSpec")) with Scope

  "When given input text file a BenthiFileInputActor" should {
    "return a message stating this is not a spreadsheet" in new Actors {
      new WithApplication {
        val message = new BenthicFileInputMessage("aaa", "bbb", "ccc", "ddd", new FileInputStream("test/resources/wrongfile.txt"))
        val myActor = system.actorOf(Props[BenthicInputActor])
        val probe = TestProbe()
        myActor.tell(message, probe.ref)

        probe.expectMsg("not a valid file")
      }
    }
  }
}

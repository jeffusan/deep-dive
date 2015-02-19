package test.controllers

import play.api.test._
import play.api.test.Helpers._

/** Test Utils **/
object Utils {

  import play.api.libs.iteratee._

  def extractBody(inputBody: Enumerator[Array[Byte]]) = {
    import scala.concurrent.ExecutionContext.Implicits.global

    var outputBody = ""
    val future = inputBody |>>> Iteratee.foreach { s => 
        outputBody += new String(s)
    }

    import scala.concurrent.Await
    import scala.concurrent.duration.Duration
    Await.ready(future, Duration.Inf)

    outputBody
  }
}

/** Authentication Trait. **/
trait ControllerUnitTest {
  import play.core.DevSettings
//  import play.api.mvc.Results._

  /**
    * Pre-process of the controller unit test.
    *  
    * @param spec the test code block.
   **/
  def withFakeApplication(spec: => Unit) {

    val fake = new FakeApplication with DevSettings {
            def devSettings: Map[String, String] = Map("config.file" -> "conf/application-test.conf")}
    running(fake) {
      // test code
      spec
    }
  }

}
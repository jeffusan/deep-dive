package test.controllers

import play.api.libs.json._
import play.api.mvc.Request
import play.core.DevSettings
import play.api.test._
import play.api.test.Helpers._
import org.scalatestplus.play._
import play.api.Logger

/** Authentication Trait. **/
trait Authentication {

  /** default credential **/
  lazy implicit val credentials = Json.parse("""{ "email" : "jeffusan@atware.jp", "password":"secret"}""")

  /** ehCachePlugin do not use if you test **/
  lazy val additionalConf = Map("ehcacheplugin" -> "disabled")

  /**
   *  Pre-process of the controller test.
   *
   *  @param spec the test code block.
   *  @param credentials the JSON format credential.
   */
  def withAuthentication(spec: ((String, String)*) => Unit)(implicit credentials: JsValue) {

    running(FakeApplication(additionalConfiguration = additionalConf)) {

      val method = POST
      val uri = controllers.routes.Application.login().url
      val request = FakeRequest(method, uri)
        .withHeaders(("Content-Type", "application/json"))
        .withJsonBody(credentials)
      val response = route(request).get
      val result = status(response)
      if (result == 200) {
        val responseBody = contentAsJson(response)
        val token = (responseBody \ "authToken").as[String]
        if (Logger.isDebugEnabled) Logger.debug("Authenticaion successed.")
        // test code with authenticated token
        spec("Content-Type" -> "application/json", "X-XSRF-TOKEN" -> token)
      } else {
        if (Logger.isDebugEnabled) Logger.error("Authenticaion failed.")
      }
    }
  }

}

import play.api.Application
import play.api.cache.{ CachePlugin, CacheAPI }
import net.sf.ehcache.{ Element, CacheManager }

/**
 * Fixed EhCachePlugin Instead of Default EhCachePlugin.
 * Default EhCachePlugin shut down when application stop.
 * Work around for multiple test implementation.
 */
class FixedEhCachePlugin(app: Application) extends CachePlugin {

  lazy val manager = { CacheManager.getInstance() }

  lazy val cache = {
    manager.addCacheIfAbsent("play")
    manager.getCache("play")
  }

  override def onStart() {
    cache
    ()
  }

  override def onStop() {
    if (play.api.Play.isTest(app)) {
      cache.flush()
    } else {
      // shutdown of the cache manager in dev/prod mode
      manager.shutdown()
    }
  }

  lazy val api = new CacheAPI {

    def set(key: String, value: Any, expiration: Int) {
      val element = new Element(key, value)
      if (expiration == 0) element.setEternal(true)
      element.setTimeToLive(expiration)
      cache.put(element)
    }

    def get(key: String): Option[Any] = {
      Option(cache.get(key)).map(_.getObjectValue)
    }

    def remove(key: String) {
      val b = cache.remove(key)
    }
  }

}

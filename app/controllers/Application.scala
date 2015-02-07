package controllers

import models.{AnormUserRepository, User}
import play.api.Logger
import play.api.cache._
import play.api.data.Forms._
import play.api.data._
import play.api.libs.json._
import play.api.mvc._
import services.UserService

/**
 * Security actions that should be used by all controllers that need to protect their actions.
 * Can be composed to fine-tune access control.
 */
trait Security { self: Controller =>

  implicit val app: play.api.Application = play.api.Play.current

  val AuthTokenHeader = "X-XSRF-TOKEN"
  val AuthTokenCookieKey = "XSRF-TOKEN"
  val AuthTokenUrlKey = "auth"

  /**
   * Retrieves all routes via reflection.
   * http://stackoverflow.com/questions/12012703/less-verbose-way-of-generating-play-2s-javascript-router
   * @todo If you have controllers in multiple packages, you need to add each package here.
   */
  val routeCache = {
    val jsRoutesClass = classOf[routes.javascript]
    val controllers = jsRoutesClass.getFields.map(_.get(null))
    controllers.flatMap { controller =>
      controller.getClass.getDeclaredMethods.map { action =>
        action.invoke(controller).asInstanceOf[play.core.Router.JavascriptReverseRoute]
      }
    }
  }

  def HasAdminRole(roles: List[String]): Boolean = {
    var result = false
    for( role <- roles) {
      role match {
        case "administrator" => result = true
      }
    }
    result
  }


  /** Checks token and admin role */
  def HasAdminToken[A](p: BodyParser[A] = parse.anyContent)(f: String => User => Request[A] => Result): Action[A] =
    Action(p) { implicit request =>
      val maybeToken = request.headers.get(AuthTokenHeader).orElse(request.getQueryString(AuthTokenUrlKey))
      maybeToken flatMap { token =>
        Cache.getAs[User](token) map { user =>
          if(HasAdminRole(user.roles)) {
          f(token)(user)(request)
          } else {
            Unauthorized(Json.obj("err" -> "No Token"))
          }
        }
      } getOrElse Unauthorized(Json.obj("err" -> "No Token"))
    }

  /** Checks that a token is either in the header or in the query string */
  def HasToken[A](p: BodyParser[A] = parse.anyContent)(f: String => User => Request[A] => Result): Action[A] =
    Action(p) { implicit request =>
      val maybeToken = request.headers.get(AuthTokenHeader).orElse(request.getQueryString(AuthTokenUrlKey))
      maybeToken flatMap { token =>
        Cache.getAs[User](token) map { user =>
          f(token)(user)(request)
        }
      } getOrElse Unauthorized(Json.obj("err" -> "No Token"))
    }

  def CanEditUser[A](userId: Long, p: BodyParser[A] = parse.anyContent)(f: User => Request[A] => Result) =
    HasToken(p) { _ => user => request =>

      // either the user is editing herself or is an administrator
      if(userId == user.id.get || HasAdminRole(user.roles)) {

          f(user)(request)

      } else {
        Forbidden (Json.obj("err" -> "You don't have sufficient privileges"))
      }
    }

}

/** General Application actions, mainly session management */
trait Application extends Controller with Security {

  lazy val CacheExpiration =
    app.configuration.getInt("cache.expiration").getOrElse(60 /*seconds*/ * 2 /* minutes */)

  lazy val userService = new UserService(AnormUserRepository)

  /** Returns the index page */
  def index = Action {
    Ok(views.html.index())
  }

  case class Login(email: String, password: String)

  val loginForm = Form(
    mapping(
      "email" -> email,
      "password" -> nonEmptyText
    )(Login.apply)(Login.unapply)
  )

  implicit class ResultWithToken(result: Result) {
    def withToken(token: (String, User)): Result = {
      Cache.set(token._1, token._2, CacheExpiration)
      result.withCookies(Cookie(AuthTokenCookieKey, token._1, None, httpOnly = false))
    }

    def discardingToken(token: String): Result = {
      Cache.remove(token)
      result.discardingCookies(DiscardingCookie(name = AuthTokenCookieKey))
    }
  }

  /** Check credentials, generate token and serve it back as auth token in a Cookie */
  def login = Action(parse.json) { implicit request =>
    loginForm.bind(request.body).fold( // Bind JSON body to form values
      formErrors => BadRequest(Json.obj("err" -> formErrors.errorsAsJson)),
      loginData => {
        Logger.debug("Attempting risky calculation")
        userService.authenticate(loginData.email, loginData.password) map { user =>
          val token = java.util.UUID.randomUUID().toString
          Ok(Json.obj(
            "authToken" -> token,
            "user" -> user
          )).withToken(token -> user)
        } getOrElse Unauthorized(Json.obj("err" -> "User Not Found or Password Invalid"))
      }
    )
  }

  def comments = Action {
    Ok(Json.parse("""
    [
     {"author" : "Peter Hunt",
      "text" : "This is one comment"},
     {"author": "Jordon Walke", "text": "This is another comment"}]
    """))
  }

  /** Invalidate the token in the Cache and discard the cookie */
  def logout = Action { implicit request =>
    request.headers.get(AuthTokenHeader) map { token =>
      Ok(Json.obj(
        "message" -> "ok"
      )).discardingToken(token)
    } getOrElse BadRequest(Json.obj("err" -> "No Token"))
  }

  /**
   * Returns the current user's ID if a valid token is transmitted.
   * Also sets the cookie (useful in some edge cases).
   * This action can be used by the route service.
   */
  def ping() = HasToken() { token => user => implicit request =>
      Ok(Json.obj("userId" -> user.id.get)).withToken(token -> user)
  }

  /** Example for token protected access */
  def myAccountInfo() = HasToken() { _ => user => implicit request =>
      Ok(Json.toJson(user))
  }

  def user(id: Long) = CanEditUser(id) { user => _ =>
    Ok(Json.toJson(user))
  }

  /** Creates a user from the given JSON */
  def createUser() = HasToken(parse.json) { token => userId => implicit request =>
    // TODO Implement User creation, typically via request.body.validate[User]
    NotImplemented
  }

  /** Updates the user for the given id from the JSON body */
  def updateUser(id: Long) = HasToken(parse.json) { token => userId => implicit request =>
    // TODO Implement User creation, typically via request.body.validate[User]
    NotImplemented
  }

  /** Deletes a user for the given id */
  def deleteUser(id: Long) = HasToken(parse.empty) { token => userId => implicit request =>
    // TODO Implement User creation, typically via request.body.validate[User]
    NotImplemented
  }


}

object Application extends Application

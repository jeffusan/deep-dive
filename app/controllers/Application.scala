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

case class Login(email: String, password: String)

/** General Application actions, mainly session management */
trait Application extends Controller with Security {

  lazy val CacheExpiration =
    app.configuration.getInt("cache.expiration").getOrElse(60 /*seconds*/ * 2 /* minutes */)

  lazy val userService = new UserService(AnormUserRepository)

  val loginForm = Form(
    mapping(
      "email" -> email,
      "password" -> nonEmptyText
    )(Login.apply)(Login.unapply)
  )

  /** Returns the index page */
  def index = Action {
    Ok(views.html.index())
  }

  def login = Action(parse.json) { implicit request =>

    loginForm.bindFromRequest.fold(
      formWithErrors => BadRequest(Json.obj("err" -> "Bad Credentials")),
      userData => {
        userService.authenticate(userData.email, userData.password).fold {
          BadRequest(Json.obj("status" -> "KO", "message" -> "User not registered"))
        } { userData =>
          /* TODO:
           * > The token must be unique for each user and must be verifiable by the server (to
           * > prevent the JavaScript from making up its own tokens). We recommend that the token is
           * > a digest of your site's authentication cookie with a salt) for added security.
           *
           */
          val token = java.util.UUID.randomUUID.toString
          Cache.set(token, userData)
          Ok(Json.obj("token" -> token, "user" -> userData))
        }
      }
    )
  }

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
    Ok(Json.obj("token" -> token, "user" -> user)).withToken(token -> user)
  }

}

object Application extends Application

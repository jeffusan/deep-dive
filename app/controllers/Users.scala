package controllers

import play.api.libs.json.Json
import play.api.mvc._
import services.UserService
import models.{AnormUserRepository, User, Role}



trait Users extends Controller with Security {

  lazy val userService = new UserService(AnormUserRepository)

  /** Example for token protected access */
  def myAccountInfo() = HasToken() { _ => user => implicit request =>
      Ok(Json.toJson(user))
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

object Users extends Users

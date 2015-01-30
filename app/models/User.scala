package models

import play.api.libs.json._
import play.api.libs.functional.syntax._

sealed trait Role

object Role {

  case object Administrator extends Role
  case object NormalUser extends Role

  def valueOf(value: String): Role = value match {
    case "Administrator" => Administrator
    case "NormalUser"    => NormalUser
    case _ => throw new IllegalArgumentException()
  }

}

case class User(
  id: Option[Long],
  email: String,
  password: Option[String],
  name: String,
  role: String
)

object User {

  implicit val UserFromJson: Reads[User] = (
    (__ \ "id").readNullable[Long] ~
      (__ \ "email").read(Reads.email) ~
      (__ \ "password").readNullable[String] ~
      (__ \ "name").read[String] ~
      (__ \ "role").read[String]
  )(User.apply _)

  implicit val UserToJson: Writes[User] = (
    (__ \ "id").writeNullable[Long] ~
      (__ \ "email").write[String] ~
      (__ \ "password").writeNullable[String] ~
      (__ \ "name").write[String] ~
      (__ \ "role").write[String]
  )((user: User) => (
    user.id,
    user.email,
    None,
    user.name,
    user.role
  ))
}

trait UserRepository {

  def findOneByEmailAndPassword(email: String, password: String): Option[User]
  def findOneById(id: Long): Option[User]
}

object AnormUserRepository extends UserRepository {

  import anorm._
  import anorm.SqlParser._
  import play.api.db.DB
  import play.api.Play.current

  val userParser: RowParser[User] = {

    long("id") ~ str("email") ~ str("name") map {
      case i~e~n => User(id=Some(i),email=e,password=null,name=n, Role.NormalUser.toString)
    }
  }

  def findOneByEmailAndPassword(email: String, password: String): Option[User] = {
    DB.withConnection{ implicit c =>
      val maybeUser: Option[User] = SQL(
        """
        select u.id as id, u.email as email, u.name as name, r.name as role
        from dd_user u
        join dd_user_role ur
        on ur.user_id = u.id
        join dd_role r
        on r.id = ur.role_id
        where u.email = {email} and password={password}
        """
      ).on(
        'email -> email,
        'password -> password
      ).as(userParser.singleOpt)

      maybeUser
    }
  }

  def findOneById(id: Long): Option[User] = {
    DB.withConnection { implicit c =>
      val maybeUser: Option[User] = SQL(
        """
        select u.id as id, u.email as email, u.name as name, r.name as role
        from dd_user u
        join dd_user_role ur
        on ur.user_id = u.id
        join dd_role r
        on r.id = ur.role_id
        where u.id={id};
        """
      ).on(
        'id -> id
      ).as(userParser.singleOpt)
      maybeUser
    }
  }

}

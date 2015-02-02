package models

import play.api.libs.json._
import play.api.libs.functional.syntax._

case class Role(
  id: Option[Long],
  name: String
)

object Role {

  implicit val RoleFromJson: Reads[Role] = (
    (__ \ "id").readNullable[Long] ~
      (__ \ "name").read[String]
  )(Role.apply _)

  implicit val RoleToJson: Writes[Role] = (
    (__ \ "id").writeNullable[Long] ~
      (__ \ "name").write[String]
  )((role: Role) => (
    role.id,
    role.name
  ))
}

trait RoleRepository {

  def findOneByName(name: String): Option[Role]
  def findAll(): List[Role]
}

trait AnormRoleRepository extends RoleRepository {

  import anorm._
  import anorm.SqlParser._
  import play.api.db.DB
  import play.api.Play.current

  val roleParser: RowParser[Role] = {

    long("id") ~ str("name") map {
      case i~n => Role(id=Some(i), name=n)
    }
  }

  def findOneByName(name:String): Option[Role] = {
    DB.withConnection{ implicit c =>
      val maybeRole: Option[Role] = SQL (
        """
        select id, name from role where name = {name} limit 1;
        """
      ).on( 'name -> name).as(roleParser.singleOpt)

      maybeRole
    }
  }

  def findAll(): List[Role] = {
    DB.withConnection { implicit c =>
      val results: List[Role] = SQL (
        """
        select * from role;
        """
      ).as(roleParser *)
      results
    }
  }
}

case class User(
  id: Option[Long],
  email: String,
  password: Option[String],
  name: String,
  roles: List[Role]
)

object User {

  implicit val UserFromJson: Reads[User] = (
    (__ \ "id").readNullable[Long] ~
      (__ \ "email").read(Reads.email) ~
      (__ \ "password").readNullable[String] ~
      (__ \ "name").read[String] ~
      (__ \ "roles").read[List[Role]]
  )(User.apply _)

  implicit val UserToJson: Writes[User] = (
    (__ \ "id").writeNullable[Long] ~
      (__ \ "email").write[String] ~
      (__ \ "password").writeNullable[String] ~
      (__ \ "name").write[String] ~
      (__ \ "role").write[List[Role]]
  )((user: User) => (
    user.id,
    user.email,
    None,
    user.name,
    user.roles
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

    long("id") ~ str("email") ~ str("name") ~ str("role") map {
      case i~e~n~r => User(id=Some(i),email=e,password=null,name=n, roles=List(new Role(null, r)))
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

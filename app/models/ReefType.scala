package models

import play.api.libs.json._
import play.api.libs.functional.syntax._
import anorm._
import anorm.SqlParser._
import play.api.db.DB
import play.api.Play.current
import play.api.Logger

/**
 * [[ReefTypeRepository]] trait defines the functionalities supported by [[ReefType]] object
 */
trait ReefTypeRepository {

  def findAll: JsArray

  def findOneById(reefTypeId: Int): JsValue

  def add(name: String, depth: String): JsValue

  def remove(id: Int)

  def update(id: Int, name: String, depth: String): JsValue

}

/**
 * Anorm specific database implementations of [[ReefTypeRepository]] trait
 */
object AnormReefTypeRepository extends ReefTypeRepository with JSONParsers {

  override def update(id: Int, name: String, depth: String): JsValue = {
    DB.withConnection { implicit c =>
      SQL(
        """
        with data(id, name, depth) as (
          update reef_type set name={name}, depth={depth} where id={id} returning id, name, depth
        )
        select row_to_json(data) from data;
        """
      ).on('id -> id, 'name -> name, 'depth -> depth).as(simple.single)
    }
  }

  override def remove(id: Int) {
    DB.withConnection { implicit c =>
      SQL(
        """
        delete from reef_type where id={id}
        """
      ).on('id -> id).execute
    }
  }

  override def add(name: String, depth: String): JsValue = {
    try {
      DB.withConnection { implicit c =>
        SQL(
          """
          with data(id, name, depth) as (
            insert into reef_type(id, name, depth) values (DEFAULT, {name}, {depth}) returning id, name, depth
          ) select row_to_json(data) from data;
          """
        ).on('name -> name, 'depth -> depth).as(simple.single)
      }
    } catch {
      case nse: NoSuchElementException =>
        Json.parse("")
    }
  }

  /**
   * Retrieves reef type specified by ID
   * @param reefTypeId reef type id
   * @return JsValue
   */
  override def findOneById(reefTypeId: Int): JsValue = {
    DB.withConnection { implicit c =>
      try {
        SQL(
        """
        select row_to_json(row) from (
          select id, name, depth from reef_type where id={id}) row;
        """).on('id -> reefTypeId).as(simple.single)
      } catch {
        case nse: Throwable =>
          Logger.warn("Found a problem" + nse.toString())
          Json.parse("")
      }
    }
  }

  /**
   * Retrieves all available reef type info
   * @return JsArray
   */
  override def findAll: JsArray = {
    DB.withConnection { implicit c =>
      try {
      SQL(
        """
          select array_to_json(array_agg(reef_type)) from reef_type;
        """).as(array.single)
      } catch {
        case unf: Throwable =>
          Logger.warn("Found a problem: " + unf.toString())
          Json.toJson(Seq("")).asInstanceOf[JsArray]
      }
    }
  }
}

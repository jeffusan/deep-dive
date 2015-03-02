package models

import anorm._
import anorm.SqlParser._
import play.api.db.DB
import play.api.Play.current
import play.api.Logger
import play.api.libs.functional.syntax._
import play.api.libs.json._

/**
 * [[RegionRepository]] trait defines functionalities supported by [[Region]] object
 */
trait RegionRepository {
  /**
   * Finds all regions available
   * @return list of regions or null
   */
  def findAll: JsArray

  /**
   * Finds a region based on given region id
   * @param regionId region id
   * @return JsValue
   */
  def findOneById(regionId: Int): JsValue

  /**
    * Add a region
    */
  def add(name: String): JsValue

  /**
    * Delete a region and return nothing
    */
  def remove(id: Int)

  /** Update a region and return it */
  def update(id: Int, name: String): JsValue
}


/**
 * Anorm specific database implementations of [[RegionRepository]] trait
 */
object AnormRegionRepository extends RegionRepository with JSONParsers {

  /**
    * Removes a region based on id
    */
  def remove(id: Int) {

    DB.withConnection { implicit c =>
      SQL("""
      delete from region where id={id}
      """
      ).on('id -> id).execute
    }
  }

  /**
    *  Adds a region based on name and returns it with it's new id
    */
  def add(name: String): JsValue = {
    DB.withConnection { implicit c =>
      SQL(
        """
        with data(id, name) as (
          insert into region (id, name) values (DEFAULT, {name}) returning id, name
        )
        select row_to_json(data) from data;
        """
      ).on('name -> name).as(simple.single)

    }
  }

  /**
    * Updates a region and returns it
    */
  def update(id: Int, name: String): JsValue = {
    DB.withConnection { implicit c =>
      SQL(
        """
        with data(id, name) as (
          update region set name={name} where id={id} returning id, name
        )
        select row_to_json(data) from data;
        """
      ).on('name -> name, 'id -> id).as(simple.single)
    }
  }


  /**
   * Retrieves region info based on given id
   * @param id region id
   * @return JsValue
   */
  def findOneById(id: Int): JsValue = {
    DB.withConnection { implicit c =>
      try {
        SQL(
        """
          SELECT ROW_TO_JSON(row)
          FROM(
            SELECT id, name
            FROM region
            WHERE id={id}) row;
        """).on('id -> id).as(simple.single)
      } catch {
        case nse: NoSuchElementException =>
          Json.parse("{}")
      }

    }
  }

  /**
   * Retrieves all available region info
   * @return list of Region JSON
   */
  def findAll: JsArray = {
    DB.withConnection { implicit c =>
      try {
        SQL(
          """
          select array_to_json(array_agg(region)) from region;
          """
        ).as(array.single)
      } catch {
        case nse: NoSuchElementException =>
          Json.toJson(Seq("")).asInstanceOf[JsArray]
      }
    }
  }
}

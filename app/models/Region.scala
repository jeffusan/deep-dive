package models

import play.api.libs.json._
import play.api.libs.functional.syntax._
import play.api.Logger

/**
 * This represents a region object
 */

/**
 * Case class for Region
 * @param id region id
 * @param name region name
 */
case class Region(
  id: Option[Int],
  name: String
)

/**
 * Companion object of case class
 */
object Region {

  /**
   * Converts Region object from & to Json
   */
  // Json to Object
  implicit val RegionFromJson: Reads[Region] = (
    (__ \ "id").readNullable[Int] ~
      (__ \ "name").read[String]
    )(Region.apply _)

  // Object to Json
  implicit val RegionToJson: Writes[Region] = (
    (__ \ "id").writeNullable[Int] ~
      (__ \ "name").write[String]
    )((region: Region) => (
    region.id,
    region.name
    )
    )
}


/**
 * [[RegionRepository]] trait defines functionalities supported by [[Region]] object
 */
trait RegionRepository {
  /**
   * Finds all regions available
   * @return list of regions or null
   */
  def findAll: List[Region]

  /**
   * Finds a region based on given region id
   * @param regionId region id
   * @return region or null
   */
  def findOneById(regionId: Int): Option[Region]

  /**
    * Add a region
    */
  def add(name: String): Option[Region]

  /**
    * Delete a region and return nothing
    */
  def remove(id: Int)

  /** Update a region and return it */
  def update(id: Int, name: String): Option[Region]
}


/**
 * Anorm specific database implementations of [[RegionRepository]] trait
 */
object AnormRegionRepository extends RegionRepository {

  // Database related dependencies

  import anorm._
  import anorm.SqlParser._
  import play.api.db.DB
  import play.api.Play.current

  /**
   * SQL parser
   */
  val regionParser: RowParser[Region] = {
    int("id") ~
      str("name") map {
      case i ~ n => Region(
        id = Some(i),
        name = n)
    }
  }

  /**
    * Removes a region based on id
    */
  def remove(id: Int) {

    DB.withConnection { implicit c =>
      SQL("""
      delete from region where id={id}
      """
      ).on('id -> id).execute()
    }
  }

  /**
    *  Adds a region based on name and returns it with it's new id
    */
  def add(name: String): Option[Region] = {
    DB.withConnection { implicit c =>
      val maybeInsert: Option[Region] = SQL(
        """
        insert into region (id, name) values (DEFAULT, {name}) returning id, name;
        """
      ).on('name -> name).as(regionParser.singleOpt)
      maybeInsert
    }
  }

  /**
    * Updates a region and returns it
    */
  def update(id: Int, name: String): Option[Region] = {
    DB.withConnection { implicit c =>
      val maybeRegion: Option[Region] = SQL(
        """
        update region set name={name} where id={id} returning id, name;
        """
      ).on('name -> name, 'id -> id).as(regionParser.singleOpt)
      Logger.info("The returned region: " + maybeRegion.get)
      maybeRegion
    }
  }


  /**
   * Retrieves region info based on given id
   * @param id region id
   * @return region or null
   */
  def findOneById(id: Int): Option[Region] = {

    DB.withConnection { implicit c =>
      val mybeRegion: Option[Region] = SQL(
        """
          SELECT
          id,
          name
          FROM region
          WHERE id={id}
        """).on('id -> id).as(regionParser.singleOpt)
      mybeRegion
    }
  }

  /**
   * Retrieves all available region info
   * @return list of [[Region]] or null
   */
  def findAll: List[Region] = {
    DB.withConnection { implicit c =>
      val regionList: List[Region] = SQL(
        """
          SELECT
          id,
          name
          FROM region;
        """).as(regionParser.*)

      regionList
    }
  }
}

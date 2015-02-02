package models

import play.api.libs.json._
import play.api.libs.functional.syntax._

/**
 * This represents a region object
 */

/**
 * Case class for Region
 * @param id region id
 * @param name region name
 */
case class Region(
                   id: Option[Long],
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
    (__ \ "id").readNullable[Long] ~
      (__ \ "name").read[String]
    )(Region.apply _)

  // Object to Json
  implicit val RegionToJson: Writes[Region] = (
    (__ \ "id").writeNullable[Long] ~
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
  def findAllRegion: List[Region]

  /**
   * Finds a region based on given region id
   * @param regionId region id
   * @return region or null
   */
  def findOneById(regionId: Long): Option[Region]
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
    long("id") ~
      str("name") map {
      case i ~ n => Region(
        id = Some(i),
        name = n)
    }
  }


  /**
   * Retrieves region info based on given id
   * @param id region id
   * @return region or null
   */
  def findOneById(id: Long): Option[Region] = {
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
  override def findAllRegion: List[Region] = {
    DB.withConnection { implicit c =>
      val regionList: List[Region] = SQL(
        """
          SELECT
          id,
          name
          FROM region
        """).as(regionParser.*)

      regionList
    }
  }
}
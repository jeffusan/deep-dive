package models

import play.api.libs.json._
import play.api.libs.functional.syntax._

/**
 * This represents a sub-region object
 */

/**
 * Case class for SubRegion
 * @param id sub-region id
 * @param name sub-region name
 * @param regionId related region id
 * @param code sub-region code
 */
case class SubRegion(
                      id: Option[Int],
                      name: String,
                      regionId: Int,
                      code: String
                      )

/**
 * Companion object of case class
 */
object SubRegion {
  /**
   * Converts SubRegion object from & to Json
   */
  // Json to Object
  implicit val SubRegionFromJson: Reads[SubRegion] = (
    (__ \ "id").readNullable[Int] ~
      (__ \ "name").read[String] ~
      (__ \ "regionId").read[Int] ~
      (__ \ "code").read[String]
    )(SubRegion.apply _)

  // Object to Json
  implicit val SubRegionToJson: Writes[SubRegion] = (
    (__ \ "id").writeNullable[Int] ~
      (__ \ "name").write[String] ~
      (__ \ "regionId").write[Int] ~
      (__ \ "code").write[String]
    )((subRegion: SubRegion) => (
    subRegion.id,
    subRegion.name,
    subRegion.regionId,
    subRegion.code
    )
    )
}


/**
 * [[SubRegionRepository]] trait defines the functionalities supported by [[SubRegion]] object
 */
trait SubRegionRepository {

  def update(id: Int, name: String): Option[SubRegion] = {
    None
  }

  def remove(id: Int) = {

  }

  def add(name: String, regionId: Int, code: String): Option[SubRegion]

  /**
   * Finds list of sub-regions based on given region id
   * @param regionId region id
   * @return list of sub-regions
   */
  def findAllByRegionId(regionId: Int): List[SubRegion]

  /**
   * Finds a sub-region based on given sub-region id
   * @param subRegionId sub-region id
   * @return sub-region or null
   */
  def findOneById(subRegionId: Int): Option[SubRegion]

  /**
   * Retrieves all available sub-regions
   * @return list of sub-regions
   */
  def findAll: List[SubRegion]
}


/**
 * Anorm specific database implementations of [[SubRegionRepository]] trait
 */
object AnormSubRegionRepository extends SubRegionRepository {

  // Database related dependencies

  import anorm._
  import anorm.SqlParser._
  import play.api.db.DB
  import play.api.Play.current

  /**
   * SQL parser
   */
  val subRegionParser: RowParser[SubRegion] = {
    int("id") ~
      str("name") ~
      int("region_id") ~
      str("code") map {
      case i ~ n ~ r ~ c => SubRegion(
        id = Some(i),
        name = n,
        regionId = r,
        code = c
      )
    }
  }

  def add(name: String, regionId: Int, code: String): Option[SubRegion] = {
    DB.withConnection { implicit c =>
      val maybe: Option[SubRegion] = SQL(
        """
        insert into subregion(id, name, region_id, code)
        values (DEFAULT, {name}, {regionId}, {code})
        returning id, name, region_id, code;
        """
      ).on(
        'name -> name,
        'regionId -> regionId,
        'code -> code
      ).as(subRegionParser.singleOpt)
      maybe
    }
  }

  /**
   * Retrieves sub-region specified by given ID
   * @param id sub-region ID
   * @return sub-region or null
   */
  override def findOneById(id: Int): Option[SubRegion] = {
    DB.withConnection { implicit c =>
      val maybe: Option[SubRegion] = SQL(
        """
          SELECT
          id,
          name,
          region_id,
          code
          FROM subregion
          WHERE id={id}
        """).on('id -> id).as(subRegionParser.singleOpt)

      maybe
    }
  }

  /**
   * Finds list of sub-regions based on given region id
   * @param regionId region id
   * @return list of sub-regions or null
   */
  override def findAllByRegionId(regionId: Int): List[SubRegion] = {
    DB.withConnection { implicit c =>
      val maybe: List[SubRegion] = SQL(
        """
          SELECT
          id,
          name,
          region_id,
          code
          FROM subregion
          WHERE region_id = {id}
        """).on(
          'id -> regionId
        ).as(subRegionParser.*)

      maybe
    }
  }

  /**
   * Retrieves all available sub-regions
   * @return list of sub-regions
   */
  override def findAll: List[SubRegion] = {
    DB.withConnection { implicit c =>
    val subRegionList: List[SubRegion] = SQL(
      """
        SELECT
        id,
        name,
        region_id,
        code
        FROM subregion
      """).as(subRegionParser.*)

      subRegionList
   }
  }
}

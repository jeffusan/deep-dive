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
                      id: Option[Long],
                      name: String,
                      regionId: Long,
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
    (__ \ "id").readNullable[Long] ~
      (__ \ "name").read[String] ~
      (__ \ "regionId").read[Long] ~
      (__ \ "code").read[String]
    )(SubRegion.apply _)

  // Object to Json
  implicit val SubRegionToJson: Writes[SubRegion] = (
    (__ \ "id").writeNullable[Long] ~
      (__ \ "name").write[String] ~
      (__ \ "regionId").write[Long] ~
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

  /**
   * Finds list of sub-regions based on given region id
   * @param regionId region id
   * @return list of sub-regions
   */
  def findAllSubRegionByRegionId(regionId: Long): List[SubRegion]

  /**
   * Finds a sub-region based on given sub-region id
   * @param subRegionId sub-region id
   * @return sub-region or null
   */
  def findOneById(subRegionId: Long): Option[SubRegion]

  /**
   * Retrieves all available sub-regions
   * @return list of sub-regions
   */
  def findAllSubRegion: List[SubRegion]
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
    long("id") ~
      str("name") ~
      long("region_id") ~
      str("code") map {
      case i ~ n ~ r ~ c => SubRegion(
        id = Some(i),
        name = n,
        regionId = r,
        code = c
      )
    }
  }


  /**
   * Retrieves sub-region specified by given ID
   * @param id sub-region ID
   * @return sub-region or null
   */
  override def findOneById(id: Long): Option[SubRegion] = {
    DB.withConnection { implicit c =>
      val mybeSubRegion: Option[SubRegion] = SQL(
        """
          SELECT
          id,
          name,
          region_id,
          code
          FROM subregion
          WHERE id={id}
        """).on('id -> id).as(subRegionParser.singleOpt)

      mybeSubRegion
    }
  }

  /**
   * Finds list of sub-regions based on given region id
   * @param regionId region id
   * @return list of sub-regions or null
   */
  override def findAllSubRegionByRegionId(regionId: Long): List[SubRegion] = {
    DB.withConnection { implicit c =>
      val mybeSubRegionList: List[SubRegion] = SQL(
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

      mybeSubRegionList
    }
  }

  /**
   * Retrieves all available sub-regions
   * @return list of sub-regions
   */
  override def findAllSubRegion: List[SubRegion] = {
    DB.withConnection { implicit c =>
    val subRegionList: List[SubRegion] = SQL(
      """
        SELECT
        id,
        name,
        region_id,
        code
        FROM subregion
        WHERE region_id = {id}
      """).as(subRegionParser.*)

      subRegionList
   }

  }

}

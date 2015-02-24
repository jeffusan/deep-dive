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
case class SubRegion(id: Option[Int], name: String, region: Region, code: String)

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
      (__ \ "region").read[Region] ~
      (__ \ "code").read[String]
    )(SubRegion.apply _)

  // Object to Json
  implicit val SubRegionToJson: Writes[SubRegion] = (
    (__ \ "id").writeNullable[Int] ~
      (__ \ "name").write[String] ~
      (__ \ "region").write[Region] ~
      (__ \ "code").write[String]
    )((subRegion: SubRegion) => (
    subRegion.id,
    subRegion.name,
    subRegion.region,
    subRegion.code
    )
    )
}


/**
 * [[SubRegionRepository]] trait defines the functionalities supported by [[SubRegion]] object
 */
trait SubRegionRepository {

  /**
    * This will allow updating a subregions name or code
    */
  def update(id: Int, name: String, code: String): Option[SubRegion]

  /**
    * Remove a SubRegion
    */
  def remove(id: Int)

  /**
    * Add a subregion providing a name, a code, and a reference to the Region
    */
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
    str("code") ~
    int("region_id") ~
    str("region_name") map {
      case i ~ n ~ c ~ ri ~ rn => SubRegion(Some(i), n, Region(Some(ri), rn), c)
    }
  }

  def update(id: Int, name: String, code: String): Option[SubRegion] = {
    DB.withConnection{ implicit c =>
      val maybe: Option[SubRegion] = SQL(
        """
        with updated as (update subregion set name={name} where id={id} returning id)
        select
          subregion.id,
          subregion.name as name,
          subregion.code,
          subregion.region_id,
          region.name as region_name
        from subregion, region where subregion.id in (select id from updated);
        """
      ).on(
        'id -> id,
        'name -> name,
        'code -> code
      ).as(subRegionParser.singleOpt)
      maybe
    }
  }

  def add(name: String, regionId: Int, code: String): Option[SubRegion] = {
    DB.withConnection { implicit c =>
      val maybe: Option[Long] = SQL(
        """
        insert into subregion (id, name, region_id, code)
        values (DEFAULT, {name}, {regionId}, {code}) returning id;
        """
      ).on(
        'name -> name,
        'regionId -> regionId,
        'code -> code
      ).executeInsert()
      maybe match {
        case Some(maybe) =>
          val maybe2: Option[SubRegion] = SQL (
            """
            select
              subregion.id,
              subregion.name as name,
              subregion.code,
              subregion.region_id,
              region.name as region_name
            from subregion, region where subregion.id={id}
            """
          ).on(
            'id -> maybe.toInt
          ).as(subRegionParser.singleOpt)
          maybe2
        case None => None

      }
    }
  }

  def remove(id: Int) {
    DB.withConnection { implicit c =>
      SQL("""
      delete from subregion where id={id};
      """).on('id -> id).execute

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
          s.id,
          s.name,
          s.code
          s.region_id,
          r.name as region_name
          FROM subregion s, region r
          WHERE s.id={id}
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
          subregion.id,
          subregion.name,
          subregion.code,
          subregion.region_id,
          region.name as region_name
          FROM subregion, region
          WHERE subregion.region_id = {id}
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
        s.id,
        s.name,
        s.code,
        s.region_id,
        r.name as region_name
        FROM subregion s, region r
        WHERE s.region_id = r.id
        ORDER BY s.name;
      """).as(subRegionParser.*)

      subRegionList
   }
  }
}

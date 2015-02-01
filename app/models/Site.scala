package models

import play.api.libs.json._
import play.api.libs.functional.syntax._

/**
 * This represents a site object
 */

/**
 * Case class for Site
 * @param id site id
 * @param name site name
 * @param subRegionId sub-region id
 * @param reefTypeId reef type id for site
 * @param latitude latitude of the site
 * @param longitude longitude of the site
 * @param mapDatum map datum used by site
 */
case class Site(
                 id: Option[Long],
                 subRegionId: Long,
                 reefTypeId: Long,
                 name: String,
                 latitude: Double,
                 longitude: Double,
                 mapDatum: String
                 )


/**
 * Companion object of case class
 */
object Site {

  /**
   * Converts Site object from Json
   */
  implicit val SiteFromJson: Reads[Site] = (
    (__ \ "id").readNullable[Long] ~
      (__ \ "subRegionId").read[Long] ~
      (__ \ "reefTypeId").read[Long] ~
      (__ \ "name").read[String] ~
      (__ \ "latitude").read[Double] ~
      (__ \ "longitude").read[Double] ~
      (__ \ "mapDatum").read[String]
    )(Site.apply _)

  /**
   * Converts Site object to Json
   */
  implicit val SiteToJson: Writes[Site] = (
    (__ \ "id").writeNullable[Long] ~
      (__ \ "subRegionId").write[Long] ~
      (__ \ "reefTypeId").write[Long] ~
      (__ \ "name").write[String] ~
      (__ \ "latitude").write[Double] ~
      (__ \ "longitude").write[Double] ~
      (__ \ "mapDatum").write[String]
    )((site: Site) => (
    site.id,
    site.subRegionId,
    site.reefTypeId,
    site.name,
    site.latitude,
    site.longitude,
    site.mapDatum
    )
    )
}


/**
 * This trait defines functionalities supported by Site object
 */
trait SiteRepository {
  /**
   * Finds list of sites based on given sub-region id
   * @param subRegionId sub-region id
   * @return list of sites or null
   */
  def findAllBySubRegionId(subRegionId: Long): List[Site]

  /**
   * Finds a site based on given site id
   * @param siteId site id
   * @return site or null
   */
  def findOneById(siteId: Long): Option[Site]
}


/**
 * This object implements functions defined in SiteRepository trait
 */
object AnormSiteRepository extends SiteRepository {

  import anorm._
  import anorm.SqlParser._
  import play.api.db.DB
  import play.api.Play.current


  /**
   * SQL parser for single result
   */
  val siteParser: RowParser[Site] = {
    long("id") ~
      long("subregion_id") ~
      long("reef_type_id") ~
      str("name") ~
      double("latitude") ~
      double("longitude") ~
      str("map_datum") map {
      case i ~ s ~ r ~ n ~ la ~ lo ~ m => Site(
        id = Some(i),
        subRegionId = s,
        reefTypeId = r,
        name = n,
        latitude = la,
        longitude = lo,
        mapDatum = m
      )
    }
  }




  /**
   * Finds list of sites based on given sub-region id
   * @param subRegionId sub-region id
   * @return list of sites or null
   */
  override def findAllBySubRegionId(subRegionId: Long): List[Site] = {
    DB.withConnection { implicit c =>
      val mybeSiteList: List[Site] = SQL(
        """
          |SELECT
          |id,
          |subregion_id,
          |reef_type_id,
          |name,
          |latitude,
          |longitude,
          |map_datum
          |FROM site
          |WHERE subregion_id = {id}
        """).on(
          'id -> subRegionId
        ).as(siteParser.*)

      mybeSiteList
    }
  }


  /**
   * Finds a site based on given site id
   * @param id site id
   * @return site or null
   */
  override def findOneById(id: Long): Option[Site] = {
    DB.withConnection { implicit c =>
      val mybeSite: Option[Site] = SQL(
        """
          |SELECT
          |id,
          |subregion_id,
          |reef_type_id,
          |name,
          |latitude,
          |longitude,
          |map_datum
          |FROM site
          |WHERE id = {id}
        """).on('id -> id).as(siteParser.singleOpt)

      mybeSite
    }
  }
}
package models

import play.api.libs.json._
import play.api.libs.functional.syntax._



/**
 * [[SiteRepository]] trait defines functionalities supported
 */
trait SiteRepository {

  /**
    * Find all Sites
    *  @return JsArray
    */
  def findAll(): JsArray

  /**
   * Finds list of sites based on given sub-region id
   * @param subRegionId sub-region id
   * @return JsArray
   */
  def findAllBySubRegionId(subRegionId: Int): JsArray

  /**
   * Finds a site based on given site id
   * @param siteId site id
   * @return site or null
   */
  def findOneById(siteId: Int): JsValue
}


/**
 * Anorm specific database implementations of [[SiteRepository]] trait
 */
object AnormSiteRepository extends SiteRepository with JSONParsers {

  // Database related dependencies
  import anorm._
  import anorm.SqlParser._
  import play.api.db.DB
  import play.api.Play.current

  /**
    * Find all sites
    * @return JsArray
    */
  override def findAll(): JsArray = {
    DB.withConnection { implicit c =>
      SQL(
        """
        with data as (
          select
            s.id,
            s.name,
            s.latitude,
            s.longitude,
            s.map_datum,
            json_build_object('id', rt.id, 'name', rt.name, 'depth', rt.depth) as reef_type,
            json_build_object('id', sr.id, 'name', sr.name, 'code', sr.code, 'region', json_build_object('id', r.id, 'name', r.name)) as subregion
          from site s, reef_type rt, subregion sr, region r
          where s.reef_type_id = rt.id
          and s.subregion_id = sr.id
          and sr.region_id = r.id
          order by name
        )
        select array_to_json(array_agg(data), true) from data;
        """
      ).as(array.single)
    }
  }

  /**
   * Finds list of sites based on given sub-region id
   * @param subRegionId sub-region id
   * @return list of sites or null
   */
  override def findAllBySubRegionId(subRegionId: Int): JsArray = {
    DB.withConnection { implicit c =>
      SQL(
        """
        with data as (
          select
            s.id,
            s.name,
            s.latitude,
            s.longitude,
            s.map_datum,
            json_build_object('id', rt.id, 'name', rt.name, 'depth', rt.depth) as reef_type,
            json_build_object('id', sr.id, 'name', sr.name, 'code', sr.code, 'region', json_build_object('id', r.id, 'name', r.name)) as subregion
          from site s, reef_type rt, subregion sr, region r
          where s.subregion_id = {id}
          and s.reef_type_id = rt.id
          and s.subregion_id = sr.id
          and sr.region_id = r.id
          order by name
        )
        select array_to_json(array_agg(data), true) from data;
        """).on(
          'id -> subRegionId
        ).as(array.single)
    }
  }


  /**
   * Finds a site based on given site id
   * @param id site id
   * @return site or null
   */
  override def findOneById(id: Int): JsValue = {
    DB.withConnection { implicit c =>
      SQL(
        """
         with data as (
          select
            s.id,
            s.name,
            s.latitude,
            s.longitude,
            s.map_datum,
            json_build_object('id', rt.id, 'name', rt.name, 'depth', rt.depth) as reef_type,
            json_build_object('id', sr.id, 'name', sr.name, 'code', sr.code, 'region', json_build_object('id', r.id, 'name', r.name)) as subregion
          from site s, reef_type rt, subregion sr, region r
          where s.id = 1
          and s.reef_type_id = rt.id
          and s.subregion_id = sr.id
          and sr.region_id = r.id
          order by name
        )
        select to_json(data) from data;
        """).on('id -> id).as(simple.single)
    }
  }
}

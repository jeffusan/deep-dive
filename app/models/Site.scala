package models

import java.util.NoSuchElementException

import controllers.SiteData
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

  def remove(id: Int)

  def add(data: SiteData): JsValue
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
            s.site_id,
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
            s.site_id,
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
            s.site_id,
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

  override def remove(id: Int) {
    DB.withConnection { implicit c =>
      SQL(
        """
        delete from site where id={id}
        """
      ).on('id -> id).execute
    }
  }

  override def add(data: SiteData): JsValue = {
    try {
      DB.withConnection { implicit c =>
        SQL(
          """
          with data(id, subregion_id, reef_type_id, name, latitude, longitude, map_datum, site_id) as (
            insert into site(id, subregion_id, reef_type_id, name, latitude, longitude, map_datum, site_id) values
            (DEFAULT, {subregion}, {reef_type}, {local_name}, {latitude}, {longitude}, {map_datum},
            (select code from subregion where id={subregion})
            returning id, subregion_id, reef_type_id, name, latitude, longitude, map_datum, site_id
          ) select row_to_json(data) from data;
          """
        ).on('local_name -> data.localName,
            'subregion -> data.subregion,
            'reef_type -> data.reefType,
            'latitude -> data.latitude,
            'longitude -> data.longitude,
            'map_datum -> data.mapDatum).as(simple.single)
      }
    } catch {
      case nse: NoSuchElementException =>
        Json.parse("")
    }
  }
}

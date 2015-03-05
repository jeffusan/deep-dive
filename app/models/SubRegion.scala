package models

import anorm._
import anorm.SqlParser._
import play.api.db.DB
import play.api.Play.current
import play.api.libs.json._
import play.api.libs.functional.syntax._
import play.api.Logger
import org.postgresql.util.PGobject

/**
 * [[SubRegionRepository]] trait defines the functionalities supported by [[SubRegion]] object
 */
trait SubRegionRepository {

  /**
    * This will allow updating a subregions name or code
    */
  def update(id: Int, name: String): JsValue

  /**
    * Remove a SubRegion
    */
  def remove(id: Int)

  /**
    * Add a subregion providing a name, a code, and a reference to the Region
    */
  def add(name: String, regionId: Int, code: String): JsValue

  /**
   * Finds list of sub-regions based on given region id
   * @param regionId region id
   * @return list of sub-regions
   */
  def findAllByRegionId(regionId: Int): JsArray

  /**
   * Finds a sub-region based on given sub-region id
   * @param subRegionId sub-region id
   * @return sub-region or null
   */
  def findOneById(subRegionId: Int): JsValue

  /**
   * Retrieves all available sub-regions
   * @return list of sub-regions
   */
  def findAll: JsArray
}

/**
 * Anorm specific database implementations of [[SubRegionRepository]] trait
 */
object AnormSubRegionRepository extends SubRegionRepository with JSONParsers {

  def update(id: Int, name: String): JsValue = {
    DB.withConnection{ implicit c =>
      SQL(
        """
        with updated as
          (update subregion set name={name} where id={id} returning id, name)
        select
          json_build_object('id', (select id from updated),
          'name', (select name from updated),
          'code', s.code,
          'region', json_build_object(
            'id', s.region_id,
            'name', r.name))
        from subregion s, region r
        where s.id in (select id from updated)
        and s.region_id = r.id;
        """
      ).on(
        'id -> id,
        'name -> name
      ).as(simple_build.single)
    }
  }

  def add(name: String, regionId: Int, code: String): JsValue = {
    DB.withConnection { implicit c =>
      try {
      SQL(
        """
        with data( name, code, subregion_id, region_id) as (
          insert into subregion (id, name, code, region_id)
          values (DEFAULT, {name}, {code}, {region_id})
          returning name, code, id, region_id)
            select json_build_object(
              'id', data.subregion_id,
              'name', data.name,
              'code', data.code,
              'region', json_build_object('id', data.region_id, 'name', region.name))
            from data, region
          where region.id = data.region_id;
         """
      ).on('name -> name, 'region_id -> regionId, 'code -> code).as(simple_build.single)
      } catch {
        case nse: NoSuchElementException =>
          Json.toJson("")
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
   * @return sub-region JsValue
   */
  override def findOneById(id: Int): JsValue = {
    DB.withConnection { implicit c =>
      try {
      SQL(
        """
        with data as (
          select s.id as id,
          s.name as name,
          s.code as code,
          json_build_object('id', r.id, 'name', r.name) as region
          from subregion s, region r
          where r.id = s.region_id
          and s.id={id}
          order by name)
        select to_json(data) from data;
        """).on('id -> id).as(simple.single)

      } catch {
        case nse: NoSuchElementException =>
          Json.toJson("")
      }
    }
  }

  /**
   * Finds list of sub-regions based on given region id
   * @param regionId region id
   * @return list of sub-regions or null
   */
  override def findAllByRegionId(regionId: Int): JsArray = {
    DB.withConnection { implicit c =>
      try {
        SQL(
          """
        with data as (
          select s.id as id,
          s.name as name,
          s.code as code,
          json_build_object('id', r.id, 'name', r.name) as region
          from subregion s, region r
          where r.id = s.region_id
          and r.id=2
          order by name)
        select array_to_json(array_agg(data), true) from data;
          """
        ).on('id -> regionId).as(array.single)
      } catch {
        case nse: NoSuchElementException =>
          Json.toJson(Seq("")).asInstanceOf[JsArray]
      }
    }
  }

  /**
   * Retrieves all available sub-regions
   * @return list of sub-regions in JSON
   */
  override def findAll: JsArray = {
    DB.withConnection { implicit c =>
      try {
        SQL("""
        with data as (
          select
            s.id as id,
            s.name as name,
            s.code as code,
            json_build_object('id', r.id, 'name', r.name) as region
          from subregion s, region r
          where r.id = s.region_id
          order by name
        )
        select array_to_json(array_agg(data), true) from data;
      """).as(array.single)
      } catch {
        case nse: NoSuchElementException =>
          Json.toJson(Seq("")).asInstanceOf[JsArray]
      }
   }
  }
}

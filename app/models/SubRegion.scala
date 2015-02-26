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
 * [[SubRegionRepository]] trait defines the functionalities supported by [[SubRegion]] object
 */
trait SubRegionRepository {

  /**
    * This will allow updating a subregions name or code
    */
  def update(id: Int, name: String, code: String): JsValue

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
object AnormSubRegionRepository extends SubRegionRepository {


  /**
   * SQL parsers
   */
  implicit def rowToJsValue: Column[JsValue] = Column.nonNull { (value, meta) =>
    val MetaDataItem(qualified, nullable, clazz) = meta
    value match {
      case pgo: PGobject => Right({
        pgo.getType match {
          case "json" => Json.parse(pgo.getValue)
          case _ => JsNull
        }
      })
      case _ => Left(TypeDoesNotMatch("Cannot convert " + value + ":" +
      value.asInstanceOf[AnyRef].getClass + " to JsValue for column " + qualified))
    }
  }

  implicit def rowToJsArray: Column[JsArray] = Column.nonNull { (value, meta) =>
    val MetaDataItem(qualified, nullable, clazz) = meta
    value match {
      case pgo: PGobject => Right({
        pgo.getType match {
          case "json" => Json.parse(pgo.getValue).asInstanceOf[JsArray]
          case _ => new JsArray()
        }
      })
      case _ => Left(TypeDoesNotMatch("Cannot convert " + value + ":" +
      value.asInstanceOf[AnyRef].getClass + " to JsValue for column " + qualified))
    }
  }

  val simple = {
    get[JsValue]("row_to_json") map {
      case row_to_json =>
        row_to_json.asInstanceOf[JsValue]
    }
  }

  val simple_build = {
    get[JsValue]("json_build_object") map {
      case json_build_object =>
        json_build_object.asInstanceOf[JsValue]
    }
  }

  val array = {
    get[JsArray]("array_to_json") map {
      case array_to_json =>
        array_to_json.asInstanceOf[JsArray]
    }
  }


  def update(id: Int, name: String, code: String): JsValue = {
    DB.withConnection{ implicit c =>
      SQL(
        """
        with updated as
          (update subregion set name={name}, code={code} where id={id} returning id, name, code)
        select
          json_build_object('id', (select id from updated),
          'name', (select name from updated),
          'code', (select code from updated),
          'region', json_build_object(
            'id', s.region_id,
            'name', r.name))
        from subregion s, region r
        where s.id in (select id from updated)
        and s.region_id = r.id;
        """
      ).on(
        'id -> id,
        'name -> name,
        'code -> code
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
   * @return sub-region or null
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
   * @return list of sub-regions
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

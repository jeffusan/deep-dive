package models

import play.api.libs.json._
import play.api.libs.functional.syntax._

/**
 * This represents a reef-type object
 */

/**
 * Case class for ReefType
 * @param id reef-type id
 * @param name reef name
 * @param depth depth of reef location
 */
case class ReefType(
                     id: Option[Long],
                     name: String,
                     depth: String
                     )


/**
 * Companion object of case class
 */
object ReefType {

  /**
   * Converts Reef-type object from & to Json
   */
  // Json to Object
  implicit val ReefTypeFromJson: Reads[ReefType] = (
    (__ \ "id").readNullable[Long] ~
      (__ \ "name").read[String] ~
      (__ \ "depth").read[String]
    )(ReefType.apply _)

  // Object to Json
  implicit val ReefTypeToJson: Writes[ReefType] = (
    (__ \ "id").writeNullable[Long] ~
      (__ \ "name").write[String] ~
      (__ \ "depth").write[String]
    )((reefType: ReefType) => (
    reefType.id,
    reefType.name,
    reefType.depth
    )
    )
}

/**
 * [[ReefTypeRepository]] trait defines the functionalities supported by [[ReefType]] object
 */
trait ReefTypeRepository {

  def findAllReefTypes: List[ReefType]

  def findReefTypeById(reefTypeId: Long): Option[ReefType]

}

/**
 * Anorm specific database implementations of [[ReefTypeRepository]] trait
 */
object AnormReefTypeRepository extends ReefTypeRepository {

  // Database related dependencies

  import anorm._
  import anorm.SqlParser._
  import play.api.db.DB
  import play.api.Play.current

  /**
   * SQL parser
   */
  val reefTypeParser: RowParser[ReefType] = {
    long("id") ~
      str("name") ~
      str("depth") map {
      case i ~ n ~ d => ReefType(
        id = Some(i),
        name = n,
        depth = d
      )
    }
  }


  /**
   * Retrieves reef type specified by ID
   * @param reefTypeId reef type id
   * @return reef-type or null
   */
  override def findReefTypeById(reefTypeId: Long): Option[ReefType] = {
    DB.withConnection { implicit c =>
      val mybeReefType: Option[ReefType] = SQL(
        """
          SELECT
          id,
          name,
          depth
          FROM reef_type
          WHERE id = {id}
        """).on('id -> reefTypeId).as(reefTypeParser.singleOpt)

      mybeReefType
    }
  }

  /**
   * Retrieves all available reef type info
   * @return list of [[ReefType]] or null
   */
  override def findAllReefTypes: List[ReefType] = {
    DB.withConnection { implicit c =>
      val reefTypeList: List[ReefType] = SQL(
        """
          SELECT
          id,
          name,
          depth
          FROM reef_type
        """).as(reefTypeParser.*)

      reefTypeList
    }
  }

}

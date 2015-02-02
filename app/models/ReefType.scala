package models


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

}


trait ReefTypeRepository {

  def findAllReefTypes: Option[List[ReefType]]

  def findOneById(reefTypeid: Long): Option[ReefType]

}



object AnormReefTypeRepository extends ReefTypeRepository {


  override def findAllReefTypes: Option[List[ReefType]] = ???

  override def findOneById(reefTypeid: Long): Option[ReefType] = ???
}

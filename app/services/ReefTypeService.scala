package services

import models.ReefTypeRepository
import play.api.libs.json.{Json, JsValue}

/**
 * Services for [[models.ReefType]] object
 * @param repository repository of supported functions by [[models.ReefType]]
 */
class ReefTypeService(repository: ReefTypeRepository) {

  /**
   * Retrieves all available reef types
   * @return json converted list of reefs or null
   */
  def findAllReefTypes: Option[JsValue] = {
    repository.findAllReefTypes match {
      // in case of empty list, do nothing
      case List() => None
      // returns converted json result
      case lists => Some(Json.toJson(lists))
    }
  }


  /**
   * Retrieves reef info based on specified reef ID
   * @param reefId reef ID
   * @return json converted reef info or null
   */
  def findReefById(reefId: Long): Option[JsValue] = {
    // invalid of input
    require(reefId >= 1, "invalid reef id")

    // returns converted json result
    Some(Json.toJson(repository.findReefTypeById(reefId)))

  }


}

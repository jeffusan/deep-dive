package services

import models.SubRegionRepository
import play.api.libs.json.{Json, JsValue}

/**
 * Service for [[models.SubRegion]] object
 * @param repository repository of supported functions by [[models.SubRegion]]
 */
class SubRegionService(repository: SubRegionRepository) {

  /**
   * Retrieves sub-region info based on specified sub-region ID
   * @param subRegionId sub-region ID
   * @return json converted sub-region info or null
   */
  def findSubRegionById(subRegionId: Long): Option[JsValue] = {
    // returns converted json result
    Some(Json.toJson(repository.findOneById(subRegionId)))

  }

  /**
   * Retrieves list of all related sub-regions based on specified region ID
   * @param regionId region ID
   * @return json converted list of sub-region or null
   */
  def findAllByRegionId(regionId: Long): Option[JsValue] = {
    repository.findAllByRegionId(regionId) match {
      case List() => None
      case lists => Some(Json.toJson(lists))
    }

  }

}

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
   * @return json converted sub-region
   */
  def findSubRegionById(subRegionId: Long): Option[JsValue] = {
    // validation of input
    require(subRegionId >= 1, "incorrect id")

    // returns converted json result
    Some(Json.toJson(repository.findOneById(subRegionId)))

  }

  /**
   * Retrieves list of all related sub-regions based on specified region ID
   * @param regionId region ID
   * @return json converted list of sub-regions
   */
  def findAllSubRegionByRegionId(regionId: Long): Option[JsValue] = {
    // validation of input
    require(regionId >= 1, "invalid region id")

    repository.findAllSubRegionByRegionId(regionId) match {
      // in case of empty list, do nothing
      case List() => None
      // in case of list is return, convert to json
      case lists => Some(Json.toJson(lists))
    }

  }


  /**
   * Retrieves all available sub-regions
   * @return json converted list of sub-regions
   */
  def findAllSubRegion: Option[JsValue] = {
    repository.findAllSubRegion match {
      // in case of empty list, do nothing
      case List() => None
      // in case of list is return, convert to json
      case lists => Some(Json.toJson(lists))
    }
  }

}

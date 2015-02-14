package services

import models.RegionRepository
import play.api.libs.json.{JsValue, Json}


/**
 * Services for [[models.Region]] object
 * @param repository repository of supported functions by [[models.Region]]
 */
class RegionService(repository: RegionRepository) {

  /**
   * Retrieves region based on specified region ID
   * @param regionId region ID
   * @return json converted region info or null
   */
  def findOneById(regionId: Long): Option[JsValue] = {
    // validation of input
    require(regionId >= 1, "invalid region id")

    // returns converted json result
    Some(Json.toJson(repository.findOneById(regionId)))

  }

  /**
   * Retrieves all available regions
   * @return json converted list of regions or null
   */
  def findAll: Option[JsValue] = {
    repository.findAllRegion match {
      // in case of empty list, do nothing
      case List() => None
      // in case of list is return, convert to json
      case lists => Some(Json.toJson(lists))

    }
  }
}


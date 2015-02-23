package services

import models.{SubRegionRepository, SubRegion}
import play.api.libs.json.{Json, JsValue}

/**
 * Service for [[models.SubRegion]] object
 * @param repository repository of supported functions by [[models.SubRegion]]
 */
class SubRegionService(repository: SubRegionRepository) {

  def update(id: Int, name: String): Option[SubRegion] = {
    repository.update(id, name)
  }

  def remove(id: Int) {
    repository.remove(id)
  }

  def add(name: String): Option[SubRegion] = {
    repository.add(name)
  }

  /**
   * Retrieves sub-region info based on specified sub-region ID
   * @param subRegionId sub-region ID
   * @return json converted sub-region
   */
  def findOneById(subRegionId: Int): Option[JsValue] = {
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
  def findAllByRegionId(regionId: Int): Option[JsValue] = {
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
  def findAll: Option[JsValue] = {
    repository.findAllSubRegion match {
      // in case of empty list, do nothing
      case List() => None
      // in case of list is return, convert to json
      case lists => Some(Json.toJson(lists))
    }
  }

}

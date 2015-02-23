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

  def add(name: String, regionId: Int, code: String): Option[SubRegion] = {
    repository.add(name, regionId, code)
  }

  /**
   * Retrieves sub-region info based on specified sub-region ID
   * @param subRegionId sub-region ID
   * @return sub-region option
   */
  def findOneById(subRegionId: Int): Option[SubRegion] = {
    // validation of input
    require(subRegionId >= 1, "incorrect id")

    // returns converted json result
    repository.findOneById(subRegionId)

  }

  /**
   * Retrieves list of all related sub-regions based on specified region ID
   * @param regionId region ID
   * @return list of sub-regions
   */
  def findAllByRegionId(regionId: Int): List[SubRegion] = {
    // validation of input
    require(regionId >= 1, "invalid region id")

    repository.findAllByRegionId(regionId)

  }


  /**
   * Retrieves all available sub-regions
   * @return list of sub-regions
   */
  def findAll: List[SubRegion] = {
    repository.findAll
  }

}

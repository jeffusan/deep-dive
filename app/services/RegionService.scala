package services

import models.{Region, RegionRepository}


/**
 * Services for [[models.Region]] object
 * @param repository repository of supported functions by [[models.Region]]
 */
class RegionService(repository: RegionRepository) {

  def update(id: Int, name: String): Option[Region] = {

    repository.update(id, name)
  }

  /** Remove a Region */
  def remove(id: Int) {

    repository.remove(id)
  }

  /** Create a new Region */
  def add(name: String): Option[Region] = {
    repository.add(name)
  }

  /**
   * Retrieves region based on specified region ID
   * @param regionId region ID
   * @return json converted region info or null
   */
  def findOneById(regionId: Int): Option[Region] = {
    // validation of input
    require(regionId >= 1, "invalid region id")
    repository.findOneById(regionId)
  }

  /**
   * Retrieves all available regions
   * @return json converted list of regions or null
   */
  def findAll: List[Region] = {
    repository.findAll
  }
}


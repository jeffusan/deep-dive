package services

import models.{ReefTypeRepository, ReefType}

/**
 * Services for [[models.ReefType]] object
 * @param repository repository of supported functions by [[models.ReefType]]
 */
class ReefTypeService(repository: ReefTypeRepository) {

  /**
   * Retrieves all available reef types
   * @return json converted list of reefs or null
   */
  def findAllReefTypes: List[ReefType] = {
    repository.findAllReefTypes
  }


  /**
   * Retrieves reef info based on specified reef ID
   * @param reefId reef ID
   * @return json converted reef info or null
   */
  def findReefById(reefId: Long): Option[ReefType] = {
    // invalid of input
    require(reefId >= 1, "invalid reef id")

    repository.findReefTypeById(reefId)
  }


}

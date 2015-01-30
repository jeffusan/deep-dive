package services

import models.Region
import models.RegionRepository

class RegionService (regionRepository: RegionRepository) {

  /**
   * Should finds a Region object based on given id
   * @param id region id
   * @return region object specified by id
   */

  def findOneById(id: Long): Option[Region] = {
    regionRepository.findOneById(id)

    // to JSON
  }

}


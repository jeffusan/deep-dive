package services

import models.SiteRepository
import play.api.libs.json.{Json, JsValue}

/**
 * Services for [[models.Site]] object
 * @param repository repository of supported functions by [[models.Site]]
 */
class SiteService(repository: SiteRepository) {

  /**
   * Retrieves list of available sites based on given sub-region ID
   * @param subRegionId sub-region ID
   * @return json converted list of sites or null
   */
  def findAllBySubRegionId(subRegionId: Long): Option[JsValue] = {
    // validation of input
    require(subRegionId >= 1, "invalid sub-region id")

    repository.findAllBySubRegionId(subRegionId) match {
      // in case of empty list, do nothing
      case List() => None
      // in case of list is return, convert to json
      case lists => Some(Json.toJson(lists))
    }

  }

  /**
   * Retrieves site info based on given site ID
   * @param siteId site ID
   * @return json converted site or null
   */
  def findOneById(siteId: Long): Option[JsValue] = {
    // validation of input
    require(siteId >= 1, "invalid site id")

    // returns converted json result
    Some(Json.toJson(repository.findOneById(siteId)))

  }

}

package services

import models.SiteRepository
import play.api.libs.json.{Json, JsValue}

/**
 * Services for [[models.Site]] object
 */
/**
 * Services for [[models.Site]] object
 * @param repository repository of supported functions by [[models.Site]]
 */
class SiteService (repository: SiteRepository) {

  /**
   * Retrieves list of available sites based on given sub-region ID
   * @param subRegionId sub-region ID
   * @return json converted list of sites or null
   */
  def findAllBySubRegionId(subRegionId: Long): Option[JsValue] = {
    repository.findAllBySubRegionId(subRegionId) match {
      case List() => None
      case lists  => Some(Json.toJson(lists))
    }

  }

  /**
   * Retrieves site info based on given site ID
   * @param siteId site ID
   * @return json converted site or null
   */
  def findOneById(siteId: Long): Option[JsValue] = {

    Some(Json.toJson(repository.findOneById(siteId)))

  }

}

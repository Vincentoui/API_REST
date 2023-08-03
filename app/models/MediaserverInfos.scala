package models

import anorm.RowParser
import anorm.Macro
import play.api.libs.json.Json
import play.api.libs.json.Writes

case class MediaServerInfos (name : String, display_name : String, users : Int, calls : Int);

object MediaServerInfos {
    implicit val mdsWrites : Writes[MediaServerInfos] = Json.writes[MediaServerInfos]
}

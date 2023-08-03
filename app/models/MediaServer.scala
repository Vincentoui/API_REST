package models

import anorm.RowParser
import anorm.Macro
import play.api.libs.json.Json
import play.api.libs.json.Writes

case class MediaServer (name : String, display_name : String, users : Int)

object MediaServer {
    val parser: RowParser[MediaServer] = Macro.namedParser[MediaServer]
    implicit val mdsWrites : Writes[MediaServer] = Json.writes[MediaServer]
}

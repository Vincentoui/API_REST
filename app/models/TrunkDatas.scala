package models

import anorm.RowParser
import anorm.Macro
import play.api.libs.json.Json
import play.api.libs.json.Writes

case class TrunkDatas (name : String, incoming : Int, outgoing : Int, mds : String)

object TrunkDatas {
    val parser: RowParser[TrunkDatas] = Macro.namedParser[TrunkDatas]
    implicit val mdsWrites : Writes[TrunkDatas] = Json.writes[TrunkDatas]
}

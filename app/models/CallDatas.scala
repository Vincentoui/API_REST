package models

import anorm.RowParser
import anorm.Macro
import play.api.libs.json.Writes
import play.api.libs.json.Json

case class CallDatas(caller_mds : String, callee_mds : String, calls : Int)

object CallDatas {
    val parser: RowParser[CallDatas] = Macro.namedParser[CallDatas]
    implicit val CallDatasWrites : Writes[CallDatas] = Json.writes[CallDatas]
}

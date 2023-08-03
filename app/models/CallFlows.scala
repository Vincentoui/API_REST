package models


import anorm.Macro
import play.api.libs.json.Json
import play.api.libs.json.Writes
import models._

case class CallFlows(mdsList : List[MediaServerInfos], callDatasList : List[CallDatas], trunkList : List[TrunkDatas])

object CallFlows{
    implicit val CallFlowsWrites : Writes[CallFlows] = Json.writes[CallFlows]
}

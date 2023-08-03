package managers

import javax.inject._
import anorm._
import anorm.SqlParser._
import play.api.db._
import javax.print.attribute.standard.Media
import scala.concurrent.Future
import play.api.libs.concurrent.CustomExecutionContext
import models._

class MDS_flows_manager {
  def get_Mds_Call_Flows(mdsList : List[MediaServer], callDatasList : List[CallDatas], trunkList : List[TrunkDatas]) : CallFlows = {
    var mdsInfosList = getMdsInfos(mdsList, callDatasList, trunkList);
    return new CallFlows(mdsInfosList, callDatasList, trunkList)
  }


  def getMdsInfos(mdsList : List[MediaServer], callDatasList : List[CallDatas], trunkList : List[TrunkDatas]) : List [MediaServerInfos] = {
    return mdsList.map(mds=> new MediaServerInfos(mds.name, mds.display_name, mds.users, {
      callDatasList.filter(call => (call.caller_mds == mds.name || call.callee_mds == mds.name)).map(x=>x.calls).sum +
      trunkList.filter(_.mds == mds.name).map(trunk=>trunk.incoming+trunk.outgoing).sum
    }))
  }
}

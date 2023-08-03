package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import managers._
import models._
import play.api.libs.json.Json
import play.api.libs.json.JsSuccess
import play.api.libs.json.JsError


/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(val controllerComponents: ControllerComponents, trkMgr : Trunk_Manager, mdsMgr: MDS_manager, mdsCallsMgr: MDS_calls_manager, flowsMgr : MDS_flows_manager) extends BaseController {
  implicit val ec: scala.concurrent.ExecutionContext = scala.concurrent.ExecutionContext.global

  def get_Inter_Mds_Calls() = Action { implicit request: Request[AnyContent] =>
    val interMdsCalls = mdsCallsMgr.getMdsCallsList(new Dates("1970-01-01 00:00:00", "2100-01-01 00:00:00"))
    Ok(Json.toJson(interMdsCalls)).withHeaders(
    "Access-Control-Allow-Origin" -> "*")
  }

  def get_Mds_List() = Action { implicit request: Request[AnyContent] =>
    val mdsInfos =  mdsMgr.getMdsList()
    Ok(Json.toJson(mdsInfos)).withHeaders(
    "Access-Control-Allow-Origin" -> "*")
  }

  def get_Trunk_List() = Action { implicit request: Request[AnyContent] =>
    val trunkInfos =  trkMgr.get_Trunk_List(new Dates("1970-01-01 00:00:00", "2100-01-01 00:00:00"))
    Ok(Json.toJson(trunkInfos)).withHeaders(
    "Access-Control-Allow-Origin" -> "*")
  }

  def get_Mds_Call_Flows() = Action { implicit request: Request[AnyContent] =>
    getDatesIfValid(request) match {
      case Left(errorResult) => errorResult
      case Right(dates) =>
        val CallFlows = flowsMgr.get_Mds_Call_Flows(mdsMgr.getMdsList(), mdsCallsMgr.getMdsCallsList(dates), trkMgr.get_Trunk_List(dates))
        Ok(Json.toJson(CallFlows)).withHeaders(
        "Access-Control-Allow-Origin" -> "*")
    }
  }

  def getDatesIfValid(request: Request[AnyContent]): Either[Result, Dates] = {
    request.body.asJson.toRight(NotFound).flatMap(
      maybeDates => maybeDates.validate[Dates] match {
          case JsSuccess(dates, _) => Right(dates)
          case e: JsError => Left(BadRequest)
    })
  }
}

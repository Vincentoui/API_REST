package models

import play.api.libs.json.Json
import play.api.libs.json.Reads

case class Dates(lowerBound : String, upperBound : String)

object Dates {
  implicit val datesRead: Reads[Dates] = Json.reads[Dates]
}

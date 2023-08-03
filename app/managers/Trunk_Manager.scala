package managers

import javax.inject._
import anorm._
import anorm.SqlParser._
import play.api.db._
import javax.print.attribute.standard.Media
import scala.concurrent.Future
import play.api.libs.concurrent.CustomExecutionContext
import models._
import java.sql.Timestamp


@Singleton
class Trunk_Manager @Inject() (@NamedDatabase("default") db: Database){

    implicit val ec: scala.concurrent.ExecutionContext = scala.concurrent.ExecutionContext.global

    def get_Trunk_List(dates : Dates) : List[TrunkDatas] = {
        db.withConnection{ implicit c =>
            SQL(
            """
            SELECT trunks_outgoing_calls.name, trunks_incoming_calls.incoming, trunks_outgoing_calls.outgoing, ms.name as mds FROM
            (SELECT COUNT(*) as outgoing, us.name
            FROM xc_call_channel cch
            JOIN usersip us
            ON (us.name = SUBSTRING(cch.channel_interface from 7))
            WHERE us.category = 'trunk' AND cch.emitted=true AND start_time >= {lower_bound} AND start_time<={upper_bound}
            GROUP BY us.name) trunks_outgoing_calls

            JOIN

            (SELECT COUNT(*) as incoming, us.name
            FROM xc_call_channel cch
            JOIN usersip us
            ON (us.name = SUBSTRING(cch.channel_interface from 7))
            WHERE us.category = 'trunk' AND cch.emitted=false AND start_time >= {lower_bound} AND start_time<={upper_bound}
            GROUP BY us.name) trunks_incoming_calls

            ON trunks_outgoing_calls.name = trunks_incoming_calls.name

            JOIN usersip us ON us.name = trunks_outgoing_calls.name
            JOIN trunkfeatures tf ON tf.protocolid=us.id
            JOIN mediaserver ms ON ms.id=tf.mediaserverid
            """)
            .on(
            "upper_bound" -> Timestamp.valueOf(dates.upperBound),
            "lower_bound" -> Timestamp.valueOf(dates.lowerBound)
            )
            .as(TrunkDatas.parser.*)
        }
    }
}

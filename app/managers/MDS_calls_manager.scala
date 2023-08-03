package managers

import javax.inject._
import anorm._
import anorm.SqlParser._
import play.api.db._
import javax.print.attribute.standard.Media
import scala.concurrent.Future
import play.api.libs.concurrent.CustomExecutionContext
import models._
import java.util.Date
import java.sql.Timestamp

@Singleton
class MDS_calls_manager @Inject() (@NamedDatabase("default") db: Database){

    implicit val ec: scala.concurrent.ExecutionContext = scala.concurrent.ExecutionContext.global

    def getMdsCallsList(dates : Dates): List[CallDatas] = {
        db.withConnection{ implicit c =>
            SQL(
            """
            SELECT caller_mds, callee_mds, count(caller_mds||callee_mds) as calls FROM
            (SELECT configregistrar as caller_mds, call_thread_id, start_time
            FROM
            (SELECT call_thread_id,configregistrar, emitted, start_time
            FROM xc_call_conversation cco
            JOIN xc_call_channel cch
            ON (cco.caller_call_id=cch.id)
            JOIN linefeatures lf
            ON lf.name=SUBSTRING(cch.channel_interface from 7)) as _
            WHERE emitted) as caller

            JOIN

            (SELECT configregistrar as callee_mds, call_thread_id
            FROM
            (SELECT call_thread_id,configregistrar, emitted
            FROM xc_call_conversation cco
            JOIN xc_call_channel cch
            ON (cco.callee_call_id=cch.id)
            JOIN linefeatures lf
            ON lf.name=SUBSTRING(cch.channel_interface from 7)) as _
            WHERE NOT(emitted)) as callee

            ON caller.call_thread_id=callee.call_thread_id
            WHERE start_time >= {lower_bound} AND start_time <= {upper_bound}

            group by caller_mds, callee_mds

            """)
            .on(
            "upper_bound" -> Timestamp.valueOf(dates.upperBound),
            "lower_bound" -> Timestamp.valueOf(dates.lowerBound)
            )
            .as(CallDatas.parser.*)
        }
    }

}

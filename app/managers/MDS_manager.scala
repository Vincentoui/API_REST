package managers

import javax.inject._
import anorm._
import anorm.SqlParser._
import play.api.db._
import javax.print.attribute.standard.Media
import scala.concurrent.Future
import play.api.libs.concurrent.CustomExecutionContext
import models._


@Singleton
class MDS_manager @Inject() (@NamedDatabase("default") db: Database){

    implicit val ec: scala.concurrent.ExecutionContext = scala.concurrent.ExecutionContext.global

    def getMdsList() : List[MediaServer] = {
        db.withConnection{ implicit c =>
            SQL(
            """
            SELECT mds.name, mds.display_name, users

            FROM
              (SELECT COUNT(lf.configregistrar) as users, lf.configregistrar
              FROM linefeatures lf
              GROUP BY lf.configregistrar) as users_count
            RIGHT JOIN mediaserver as mds
            ON mds.name = users_count.configregistrar
            """)
            .as(MediaServer.parser.*)
        }
    }
}

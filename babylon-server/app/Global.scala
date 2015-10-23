import controllers.manager.{FieldRouterManager, PhotoManager}
import java.text.SimpleDateFormat
import java.util.Date
import jp.utokyo.babylon.db._
import net.liftweb.common.Full
import net.liftweb.common.{Full, Box}
import net.liftweb.db.{ DefaultConnectionIdentifier, StandardDBVendor, ProtoDBVendor}
import net.liftweb.mapper.DB
import net.liftweb.mapper.Schemifier
import play.api.GlobalSettings
import play.libs.Akka
import play.{api, Logger}
import play.api.mvc._
import scala.concurrent.ExecutionContext
import ExecutionContext.Implicits.global

import scala.concurrent.Future

/**
 * Created with IntelliJ IDEA.
 * User: takezoux2
 * Date: 13/06/24
 * Time: 2:38
 * To change this template use File | Settings | File Templates.
 */
object Global extends GlobalSettings {
  lazy val vendor = new StandardDBVendor("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/babylon", Full("babylon"), Full("babylon"))

  def models = List(User,UploadedImage,UserPost,Comment,
    Land,
    Contact,ContactType,PostCategory,PostStatus,PostUpdate,PrivateMessage,
    FieldRouter,FieldData,DisplayFieldData,WaterLevelField,WaterLevel,
    QuestionnaireAnswer
  )


  override def onStart(app: api.Application) {
    println("##Application start")
    Logger.info("Application has started")
    DB.defineConnectionManager(DefaultConnectionIdentifier, vendor)
    Schemifier.schemify(true, Schemifier.infoF _, models :_*)

    val imageDir = app.configuration.getString("image-directory").get
    Logger.info("Image dir = " + imageDir)
    PhotoManager.imageDir = imageDir

    scheduleFieldRouterDataUpdate()


  }
  def scheduleFieldRouterDataUpdate() = {
    import play.api.libs.concurrent.Execution.Implicits._
    import scala.concurrent.duration._

    //FieldRouterManager.updateData()
    /*Akka.system.scheduler.schedule(0 seconds, 4.hours)({
      FieldRouterManager.updateData()
    })*/
    1
  }



}

object LoggingFilter extends Filter {
  def apply(next: (RequestHeader) => Future[Result])(rh: RequestHeader) = {
    val dateFormat = new SimpleDateFormat("yyyy/mm/dd HH:mm:ss")
    val start = System.currentTimeMillis


    next(rh).map( result => {
      val time = dateFormat.format(new Date)
      Logger.info(s"${rh.method} ${rh.uri} took ${time}ms and returned ${result.header.status}")
      result.withHeaders("Request-Time" -> time.toString)
    })
  }
}


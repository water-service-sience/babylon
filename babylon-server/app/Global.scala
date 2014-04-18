import controllers.manager.{FieldRouterManager, PhotoManager}
import jp.utokyo.babylon.db._
import net.liftweb.common.Full
import net.liftweb.common.{Full, Box}
import net.liftweb.db.{ DefaultConnectionIdentifier, StandardDBVendor, ProtoDBVendor}
import net.liftweb.mapper.DB
import net.liftweb.mapper.Schemifier
import play.api.GlobalSettings
import play.libs.Akka
import play.{api, Logger}

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
    FieldRouter,FieldData,DisplayFieldData,
    QuestionnaireAnswer
  )


  override def onStart(app: api.Application) {
    Logger.info("Application has started")
    DB.defineConnectionManager(DefaultConnectionIdentifier, vendor)
    Schemifier.schemify(true, Schemifier.infoF _, models :_*)

    val imageDir = play.Play.application().configuration().getString("image-directory")
    Logger.info("Image dir = " + imageDir)
    PhotoManager.imageDir = imageDir

    scheduleFieldRouterDataUpdate()


  }
  def scheduleFieldRouterDataUpdate() = {
    import play.api.libs.concurrent.Execution.Implicits._
    import scala.concurrent.duration._
    Akka.system.scheduler.schedule(0 seconds, 4.hours)({
      FieldRouterManager.updateData()
    })
  }



}



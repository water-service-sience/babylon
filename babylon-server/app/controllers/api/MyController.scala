package controllers.api

import play.api.mvc._
import scala.util.DynamicVariable
import scala.Some
import net.liftweb.common.Full
import play.api.Logger
import jp.utokyo.babylon.db.User

/**
 * Created with IntelliJ IDEA.
 * User: takezoux2
 * Date: 13/06/25
 * Time: 23:48
 * To change this template use File | Settings | File Templates.
 */
trait MyController extends Controller {

  val AccessKeyHeader = "BBLN-ACCESS-KEY"

  var meVar = new DynamicVariable[User](null)

  def me = meVar.value
  def userId = me.id.is

  def AuthenticatedIMG( func : (Request[RawBuffer]) => Result) : Action[RawBuffer] = Action(parse.raw(10 * 1024 * 1024))(implicit request => {
    val accessKey = request.headers.get(AccessKeyHeader).getOrElse({
      User.findByKey(1).get.accessKey.get
    })
    User.findByAccessKey(accessKey) match{
      case Full(me) => {
        this.meVar.withValue(me){
          func(request)
        }
      }
      case _ => {
        Logger.warn("Wrong access key : " + accessKey)
        Unauthorized("No user")
      }
    }
  })


  def Authenticated( func : (Request[AnyContent]) => Result) : Action[AnyContent] = Action(implicit request => {
    val accessKey = request.headers.get(AccessKeyHeader).getOrElse({
      User.findByKey(1).get.accessKey.get
    })
    User.findByAccessKey(accessKey) match{
      case Full(me) => {
        this.meVar.withValue(me){
          func(request)
        }
      }
      case _ => {
        Logger.warn("Wrong access key : " + accessKey)
        Unauthorized("No user")
      }
    }


  })

}

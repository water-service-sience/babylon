package controllers.api

import play.api.mvc.Action
import controllers.manager.{Jsonize, PostManager, PhotoManager}
import play.api.libs.json._
import play.api.libs.json.Json.JsValueWrapper
import play.api.Logger
import jp.utokyo.babylon.db._
import java.util.Date
import net.liftweb.common.Full
import net.liftweb.mapper.MappedField
import play.api.libs.json.JsObject
import net.liftweb.common.Full
import scala.Some
import org.slf4j.LoggerFactory

/**
 * Created with IntelliJ IDEA.
 * User: takezoux2
 * Date: 13/06/26
 * Time: 0:10
 * To change this template use File | Settings | File Templates.
 */
object QuestionnaireAPI extends MyController{

  val logger = LoggerFactory.getLogger(getClass)

  def answer = Authenticated( implicit req => {
    val json = req.body.asJson.get

    val p = QuestionnaireAnswer.createOrUpdate(userId)
    updateFields(p,json)
    Ok(Jsonize.withMessage("ok"))
  })


  val ignoreFields = Set("id","answerTime","userId")
  val classToReads = Map[Class[_],Reads[_]](
    classOf[Int] -> Reads.IntReads,
    classOf[Long] -> Reads.LongReads,
    classOf[String] -> Reads.StringReads,
    classOf[Date] -> Reads.DefaultDateReads,
    classOf[Boolean] -> Reads.BooleanReads,
    classOf[Double] -> Reads.DoubleReads,
    classOf[Float] -> Reads.FloatReads
  )

  def updateFields( q : QuestionnaireAnswer,json : JsValue) = {

    (json.asInstanceOf[JsObject]).fields.foreach(p => {
      q.fieldByName(p._1) match{
        case Full(field) => {
          if(!ignoreFields.contains(field.name)) {
            val c = field.dbFieldClass
            classToReads.get(c) match{
              case Some(reads) => {
                (field.asInstanceOf[MappedField[Any,UserPost]]).set(
                  p._2.as[Any](reads.asInstanceOf[Reads[Any]]))
              }
              case None => {
                logger.debug(s"Unknown type for field:${field.name} @@ ${c.getName}")
              }
            }
          }
        }
        case _ => {
          logger.info(s"Unknown field for update:${p._1}")
        }
      }
    })
    q.save()
  }

}

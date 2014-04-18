package jp.utokyo.babylon.db
import java.util.Date
import net.liftweb.mapper._
import net.liftweb.common.{Box, Full}

object QuestionnaireAnswer extends QuestionnaireAnswer with LongKeyedMetaMapper[QuestionnaireAnswer]{

  def createOrUpdate(userId : Long,evaluation : Int , note : String) = {
    val q = find(By(QuestionnaireAnswer.userId,userId)) match{
      case Full(q) => {
        q
      }
      case _ => {
        val q = createInstance
        q.userId := userId
        q
      }
    }

    q.evaluation := evaluation
    q.note := note
    q.save()

    q
  }



}
class QuestionnaireAnswer extends LongKeyedMapper[QuestionnaireAnswer] with IdPK{

  def getSingleton = QuestionnaireAnswer

  object userId extends MappedLong(this){
    override def uniqueFieldId: Box[String] = Full("Unique_QuestionnaireAnswer_User")
  }
  object evaluation extends MappedInt(this)
  object note extends MappedText(this)
  object answerTime extends MappedDateTime(this){
    override def defaultValue = new Date()
  }
}
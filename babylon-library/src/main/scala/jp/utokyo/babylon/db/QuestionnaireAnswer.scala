package jp.utokyo.babylon.db
import java.util.Date
import net.liftweb.mapper._
import net.liftweb.common.{Box, Full}

object QuestionnaireAnswer extends QuestionnaireAnswer with LongKeyedMetaMapper[QuestionnaireAnswer]{



  def createOrUpdate(userId : Long) = {
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
    q.save()

    q
  }



}
class QuestionnaireAnswer extends LongKeyedMapper[QuestionnaireAnswer] with IdPK{

  def getSingleton = QuestionnaireAnswer

  object userId extends MappedLong(this){
    override def uniqueFieldId: Box[String] = Full("Unique_QuestionnaireAnswer_User")
  }

  object age extends MappedInt(this){
    override def defaultValue: Int = {
      -1
    }
  }
  object useFrequency extends MappedInt(this){
    override def defaultValue: Int = {
      0
    }
  }
  object easiness extends MappedInt(this){
    override def defaultValue: Int = 1
  }
  object useForImagePost extends MappedInt(this){
    override def defaultValue: Int = 1
  }
  object useForInquiry extends MappedInt(this){
    override def defaultValue: Int = 1
  }
  object useForFieldMonitoring extends MappedInt(this){
    override def defaultValue: Int = 1
  }

  object willUse extends MappedInt(this){
    override def defaultValue: Int = 1
  }

  object costForUse extends MappedInt(this){
    override def defaultValue: Int = 0
  }

  object freeMessage extends MappedText(this)

  object answerTime extends MappedDateTime(this){
    override def defaultValue = new Date()
  }
}
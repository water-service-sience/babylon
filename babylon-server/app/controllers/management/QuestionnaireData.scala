package controllers.management

import jp.utokyo.babylon.db.{User, QuestionnaireAnswer}
import java.text.SimpleDateFormat
import java.util.Date

/**
 * Created by takezoux2 on 2014/08/07.
 */
object QuestionnaireData extends ManagerBase {


  def getCsv = AdminAuth(implicit req => {

    val quests = QuestionnaireAnswer.findAll()


    val builder = new StringBuilder()
    builder.append( List("id","userId","名前","歳","回答日","使用頻度","操作性","利便性:画像投稿サービス","利便性:お問い合わせ","利便性:圃場モニタリング","継続利用","利用料金","意見").mkString(","))
    builder.append("\n")
    quests.foreach(q => {
      val v = List(q.id.get,q.userId.get,User.findByKey(q.userId.get).map(_.nickname).getOrElse(""),
      q.age.get,q.answerTime.get,
        toS(q.useFrequency.get,frequency),
        toS(q.easiness.get,easiness),
        toS(q.useForImagePost.get,usefull),
        toS(q.useForInquiry.get,usefull),
        toS(q.useForFieldMonitoring.get,usefull),
        toS(q.willUse.get,willUse),
        toS(q.costForUse.get,cost),
        "\"" + q.freeMessage.get.replace("\"","\"\"") + "\""
       )
      builder.append(v.mkString(",") + "\n")

    })

    val format = new SimpleDateFormat("yyyyMMdd").format(new Date)
    Ok(builder.toString()).withHeaders(
      "Content-Type" -> "text/csv",
      "Content-Disposition" -> s"""attachment; filename="questionnaire_${format}.csv" """
    )



  })

  def toS(v : Int, list : List[String]) = try{
    list(v)
  }catch{
    case e : Throwable => "未回答"
  }

  val frequency = ("未回答" :: (1 to 6).map(i => s"週に${i}日").toList) :+ "毎日"
  val easiness = List("未回答","よくわかった","だいたいわかった","あまりわからなかった","ほとんどわからなかった")
  val usefull = List("未回答","とても役に立った","役に立った","あまり役に立たなかった","全然役に立たなかった")
  val willUse = List("未回答","是非使いたい","使いたい","使ってもいい","使いたくない")
  val cost = ("未回答" :: "絶対に無料でないと使わない" :: List(500,1000,2000,5000,10000).map(m => {
    s"年額${m}円以下"
  })) :+ "年額10000円以上"


}

package web

import play.api.libs.ws.WS
import scala.concurrent.duration._

/**
 * Created by takezoux2 on 14/03/01.
 */
class MizoLabFieldRouter( id : String) {

  def getCsvFor( year : Int , month : Int, dayOfMonth : Int) = {



  }

  def getCsvNames = {
    WS.url(urlForDataList).get().map(res => {
      (res.xml \ "table" \ "td" \ "a").map( a => {
        a.text
      })
    }).result
  }

  def urlForDataList = {
    s"http://x-ability.jp/cgi-bin/FieldRouter/dirIndex.cgi?dir=/FieldRouter/${id}/data"
  }

  def getCsv( csvName : String) = {
    WS.url(urlForCsv(csvName)).get().map(res => {
      val lines = res.body.lines
      lines.next()
      val labels = lines.next().split(",")
      var lastLine = lines.next()
      while( lines.hasNext){
        lastLine = lines.next()
      }


    }).result()
  }
  def urlForCsv(csvName : String) = {
    s"http://x-ability.jp/FieldRouter/${id}/data/${csvName}"
  }




}

package jp.utokyo.babylon.util

import net.liftweb.mapper.{MappedForeignKey, Mapper}
import org.json4s._
import org.json4s.JsonDSL._
import jp.utokyo.babylon.annotations.JsonFieldLevel

/**
 * Created by takezoux2 on 13/12/09.
 */
object Jsonizer {

  implicit val formats = DefaultFormats

  def listToJson( ms : List[Mapper[_]]) = {
    JArray(ms.map(toJson(_)))
  }

  def toJson( m : Mapper[_],level : Int = 0) : JValue = {
    JObject(
    m.allFields.filter(f => {
      val a = f.getClass.getAnnotation(classOf[JsonFieldLevel])
      if( a == null){
        true
      }else{
        a.value() <= level
      }
    }).map(_ match{
      case foreign : MappedForeignKey[_,_,_] => {

        JField( foreign.name , foreign.obj.map(o => toJson(o.asInstanceOf[Mapper[_]])).getOrElse(JNothing))
      }
      case f => {
        val v : Any = f.get
        JField(f.name , Extraction.decompose(v))
      }
    }).toList)

  }

}

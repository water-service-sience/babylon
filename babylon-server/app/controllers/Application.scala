package controllers

import play.api._
import controllers.manager.FieldRouterManager
import controllers.manager.PhotoManager
import play.api.mvc._
import java.util.Date
import java.text.SimpleDateFormat
import controllers.management.EditPost
import jp.utokyo.babylon.util.FileUtil
import jp.utokyo.babylon.db._
import web.MizoLabFieldRouter
import play.twirl.api.Html

object Application extends Controller {


  def photo(path : String) = Action {
    val photo = PhotoManager.getFileData(path)

    val format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz")
    val expireSecs = 60 * 60 * 24 * 30 //キャッシュはおよそ１ヶ月
    val lastModified = new Date(new Date().getTime - 1000)
    val expireTime = new Date(lastModified.getTime + expireSecs * 1000)


    //header('Last-Modified: Fri Jan 01 2010 00:00:00 GMT');

    withCacheHeader(Ok(photo).as("image/jpeg"))
  }

  def latestThumbnail(categoryId : Int, index : Int) = Action{

    UserPost.findLatest(categoryId , index).map( p => {
      p.image.obj.toOption.map( image => {
        val fileKey = image.fileKey.get

        val photo = PhotoManager.getThumbnailData(fileKey)
        withCacheHeader(Ok(photo).as("image/jpeg"))
      })
    }).flatten.getOrElse({

      Play.resourceAsStream("/public/images/no_image.jpg")(Play.current) match{
        case Some(s) => {
          val d = new Array[Byte](s.available())
          s.read(d)
          s.close()
          withCacheHeader(Ok(d).as("image/jpeg"))
        }
        case None => NotFound
      }

    })

  }


  def withCacheHeader( status : Result) = {

    val format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz")
    val expireSecs = 60 * 60 * 24 * 30 //キャッシュはおよそ１ヶ月
    val lastModified = new Date(new Date().getTime - 1000)
    val expireTime = new Date(lastModified.getTime + expireSecs * 1000)


    status.withHeaders(
      "Last-Modified" -> format.format(lastModified),
      "Expires" -> format.format(expireTime),
      "Cache-Control" -> ("private, max-age=" + expireSecs))
  }

  def csvs = Action{
    FieldRouterManager.updateData
    val data = FieldRouterManager.getLatestData("vbox0094")


    Ok(Html(data.mkString("\n")))

  }

}
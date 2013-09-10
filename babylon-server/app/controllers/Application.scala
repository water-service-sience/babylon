package controllers

import play.api._
import play.api.mvc._
import controllers.manager.PhotoManager
import java.util.Date
import java.text.SimpleDateFormat

object Application extends Controller {
  
  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def photo(path : String) = Action {
    val photo = PhotoManager.getFileData(path)

    val format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz")
    val expireSecs = 60 * 60 * 24 * 30 //キャッシュはおよそ１ヶ月
    val lastModified = new Date(new Date().getTime - 1000)
    val expireTime = new Date(lastModified.getTime + expireSecs * 1000)


    //header('Last-Modified: Fri Jan 01 2010 00:00:00 GMT');

    Ok(photo).as("image/jpeg").withHeaders(
      "Last-Modified" -> format.format(lastModified),
      "Expires" -> format.format(expireTime),
      "Cache-Control" -> ("private, max-age=" + expireSecs))
  }

}
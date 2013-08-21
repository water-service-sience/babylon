package controllers

import play.api._
import play.api.mvc._
import controllers.manager.PhotoManager

object Application extends Controller {
  
  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def photo(path : String) = Action {
    val photo = PhotoManager.getFileData(path)

    Ok(photo).as("image/jpeg")
  }

}
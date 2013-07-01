package controllers.manager

import play.api.mvc.{AnyContent, Request}
import controllers.APIException
import play.api.{Logger, Configuration}
import util.{EncryptUtil, FileUtil}
import models.UploadedImage

/**
 * Created with IntelliJ IDEA.
 * User: takezoux2
 * Date: 13/06/26
 * Time: 0:22
 * To change this template use File | Settings | File Templates.
 */
object PhotoManager {

  var imageDir = "./images/"

  def saveUploadedFile(userId : Long)(implicit req : Request[AnyContent]) = {
    req.body.asRaw match{
      case Some(buffer) => {
        Logger.debug("User:%s uploaded image.Size = %s".format(userId,buffer.size))
        val b = buffer.asBytes().get
        val filename = "u" + userId + "/" + EncryptUtil.sha1Digest(b)
        FileUtil.saveTo(imageDir + filename,b)

        UploadedImage.create(userId,filename)
      }
      case None => {
        throw new APIException("Fail to get photo")
      }
    }

  }


}

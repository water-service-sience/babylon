package controllers

import jp.utokyo.babylon.db.UserPost

/**
 * Created with IntelliJ IDEA.
 * User: takezoux2
 * Date: 2013/09/08
 * Time: 23:58
 * To change this template use File | Settings | File Templates.
 */
object UIParts {

  def labelInForm(id : String,label : String,value : String) = {
    views.html.ui.uneditable(id,label,value)
  }

  def submitButton(label : String) = {
    views.html.ui.submit_button(label)
  }


}

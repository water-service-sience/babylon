package jp.utokyo.babylon.util

import java.util.Date
import java.util.concurrent.TimeUnit

/**
 * Created by takezoux2 on 2014/06/04.
 */
object TimeUtil {

  def beforeXDays(date : Date,days : Int) = {
    new Date(date.getTime - TimeUnit.DAYS.toMillis(days))
  }

  def beforeXHours(date : Date,hours : Int) = {
    new Date(date.getTime - TimeUnit.HOURS.toMillis(hours))
  }

}

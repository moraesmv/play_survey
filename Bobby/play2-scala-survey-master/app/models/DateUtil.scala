package models

import org.joda.time._
import org.joda.time.format._

object DateUtil {
  def d2s(t: DateTime): String = {
    val fmt: DateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")
    fmt.print(t)
  }

}

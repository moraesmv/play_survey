package models

import java.security.SecureRandom
import scala.util.Random

object IdGenerator {

  val secureRandom = new SecureRandom()
  val random = (0 to 3).map{_ => new Random(secureRandom.nextLong())}.toArray

  def getString(length: Int): String = {
    val rngId = (secureRandom.nextBoolean,secureRandom.nextBoolean) match {
      case (true,true) => 3
      case (true,false) => 2
      case (false,true) => 1
      case (false,false) => 0
    }
    val rs = random(rngId).alphanumeric.take(length)
    val sb = new StringBuilder
    rs.foreach(sb.append)
    sb.mkString
  }

  def getId(prefix: String,length: Int): String = {
    val sb = new StringBuilder
    sb.append(prefix)
    sb.append(getString(length))
    sb.mkString
  }

  def newUser: String     = getId("usr_",24)
  def newQuestion: String = getId("qst_",24)
  def newSurvey: String   = getId("srv_",24)
  def newOrg: String      = getId("org_",14)

}

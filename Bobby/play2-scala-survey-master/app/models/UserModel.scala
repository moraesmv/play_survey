package models

import anorm._
import anorm.SqlParser._
import org.mindrot.jbcrypt.BCrypt
import play.api.data.Form
import play.api.data.Forms._
import play.api.db.DB
import play.api.Play.current
import models.IdGenerator
import play.api.mvc.Request

object UserModel {

  case class Hashed(userId: String, hashed: String, u_id: String)

  case class LoginRequest(userId: String, password: String)

  trait AuthResult
  object AuthFailure extends AuthResult
  case class AuthSuccess(u_id: String) extends AuthResult

  val loginForm = Form(
    mapping(
      "userId" -> text
      ,"password" -> text
    )(LoginRequest.apply)(LoginRequest.unapply)
  )

  def authenticate(r: LoginRequest): AuthResult = {
    val hashed = getHashed(r.userId)
    hashed match{
      case Some(x) if BCrypt.checkpw(r.password, x.hashed) => AuthSuccess(x.u_id)
      case _ => AuthFailure
    }
  }

  def registerCredentials(r: LoginRequest): Boolean = {
    if (getHashed(r.userId).nonEmpty) {false}
    else {
      DB.withConnection {implicit connection =>
        SQL("""INSERT INTO SECURITY (U_ID, HASHED, ID) values ({U_ID}, {HASHED}, {ID})""")
          .on("U_ID"   -> r.userId)
          .on("HASHED" -> BCrypt.hashpw(r.password, BCrypt.gensalt()))
          .on("ID"     -> IdGenerator.newUser)
          .executeInsert()
      }
      true
    }
  }
  
  def getHashed(userId: String): Option[Hashed] = {
    val r: List[Hashed] = DB.withConnection{implicit connection =>
      SQL("""SELECT U_ID,HASHED,ID FROM SECURITY WHERE U_ID = {U_ID};""")
        .on("U_ID"   -> userId)
        .as(hashedRowParser *)
    }
    if (r.nonEmpty) Some(r(0))
    else None
  }

  def deleteHash(userId: String): Unit = {
    DB.withConnection {implicit connection =>
      SQL("""DELETE FROM SECURITY WHERE U_ID = {U_ID};""")
        .on("U_ID"   -> userId)
        .execute()
    }
  }

  private val hashedRowParser = {
    get[String]("U_ID") ~
    get[String]("HASHED") ~
    get[String]("ID") map {
      case a~b~c => Hashed(a,b,c)
    }
  }

  /**  For anonymous users, we use their IP as a userId
    *  This function looks up an existing u_id or creates a new one if this is a new IP
    */
  def getUidForIp(a: String): String = {
    getHashed(a) match {
      case Some(x) => x.u_id
      case _ =>
        val u_id = IdGenerator.newUser
        DB.withConnection {implicit connection =>
        SQL("""INSERT INTO SECURITY (U_ID, HASHED, ID) values ({U_ID}, {HASHED}, {ID})""")
          .on("U_ID"   -> u_id)
          .on("HASHED" -> "")
          .on("ID"     -> u_id)
          .executeInsert()
        }
        u_id
    }
  }


}

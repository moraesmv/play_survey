package models

import anorm._
import anorm.SqlParser._
import play.api.data.Form
import play.api.data.Forms._
import play.api.db._
import play.api.Play.current
import play.api.libs.json.{Json, Writes}

object OrgModel {

  case class Org(name: String)
  case class OrgRecord(name: String, org_id: String = IdGenerator.newOrg)

  def insertOrg(org: Org): Option[Long] = {
    DB.withConnection {implicit connection =>
      SQL("INSERT INTO ORGS (O_ID, NAME) values ({o_id}, {name})")
        .on("o_id" -> IdGenerator.newOrg)
        .on("name" -> org.name)
        .executeInsert()
    }
  }

  val orgForm: Form[Org] = Form(
    mapping(
      "name" -> text
    )(Org.apply)(Org.unapply)
  )

  implicit val orgWrites = new Writes[Org] {
    def writes(org: Org) = Json.obj(
      "name" -> org.name
    )
  }

  val orgRowParser = {
    get[String]("name") map {
      case name => Org(name = name)
    }
  }

  def getOrgs = {
    val orgs: List[Org] = DB.withConnection{implicit connection =>
      SQL("""SELECT O_ID, NAME FROM ORGS;""")
        .as(orgRowParser *)
    }
    orgs match {
      case x if x.nonEmpty => Some(x)
      case _ => None
    }
  }
}

package models

import anorm.SqlParser._
import anorm._
import play.api.db.DB
import play.api.libs.functional.syntax._
import play.api.libs.json._
import play.api.libs.json.Reads._
import play.api.Play.current
import org.joda.time._
import models.AnormExtension._
import models.DateUtil.d2s

object ResponseModel {

  case class Response(
     q_id: String
    ,response: Int
  ) {
    require(response >= 0 && response <= 5)
  }

  case class Responses(
     s_id: String
    ,o_id: String
    ,responses: List[Response]
  ) {
    require(responses.nonEmpty)
  }

  case class ResponseRecord(
     s_id: String
    ,q_id: String
    ,u_id: String
    ,o_id: String
    ,response: Int
    ,timestamp: DateTime
  )

  implicit val responseReads: Reads[Response] = (
    (JsPath \ "q_id").read[String] and
    (JsPath \ "response").read[Int](Reads.min(0) keepAnd Reads.max(5))
  )(Response.apply _)

  implicit val responsesReads: Reads[Responses] = (
    (__ \ "s_id").read[String] and
    (__ \ "o_id").read[String] and
    (__ \ "responses").read[List[Response]]
  )(Responses.apply _)

  implicit val responseWrites = new Writes[ResponseRecord] {
    def writes(x: ResponseRecord) = Json.obj(
      "surveyId"    -> x.s_id
      ,"questionId" -> x.q_id
      ,"orgId"      -> x.o_id
      ,"response"   -> x.response
      ,"timestamp"  -> d2s(x.timestamp)
    )
  }

  def saveResponses(r: Responses, u_id: String): Unit = {
    // TODO verify that S_ID and Q_ID actually exist
    // TODO collect User ID
    r.responses.foreach{response =>
      DB.withConnection {implicit connection =>
        SQL("""INSERT INTO RESPONSES (S_ID,Q_ID,U_ID,O_ID,RESPONSE) values ({S_ID},{Q_ID},{U_ID},{O_ID},{RESPONSE})""")
          .on("S_ID"     -> r.s_id)
          .on("Q_ID"     -> response.q_id)
          .on("U_ID"     -> u_id)
          .on("O_ID"     -> r.o_id)
          .on("RESPONSE" -> response.response)
          .executeInsert()
      }
    }
  }

  private val rowParser = {
    get[String]("S_ID") ~
    get[String]("Q_ID") ~
    get[String]("U_ID") ~
    get[String]("O_ID") ~
    get[Int]("RESPONSE") ~
    get[DateTime]("TIMESTAMP") map {
      case a~b~c~d~e~f => ResponseRecord(a,b,c,d,e,f)
    }
  }

  def getResponses: Option[List[ResponseRecord]] = {
    val list: List[ResponseRecord] = DB.withConnection{implicit connection =>
      SQL("""SELECT TOP 5000 S_ID, Q_ID, U_ID, O_ID, RESPONSE, TIMESTAMP FROM RESPONSES;""").as(rowParser *)
    }
    list match {
      case x if x.nonEmpty => Some(x)
      case _ => None
    }
  }

  def getResponsesById(q_id: String) = {
    val list: List[ResponseRecord] = DB.withConnection{implicit connection =>
      SQL("""SELECT TOP 5000 S_ID, Q_ID, U_ID, O_ID, RESPONSE, TIMESTAMP FROM RESPONSES WHERE Q_ID = {q_id};""")
        .on("q_id" -> q_id)
        .as(rowParser *)
    }
    list match {
      case x if x.nonEmpty => Some(x)
      case _ => None
    }
  }

  def getResponsesByOrgId(o_id: String) = {
    val list: List[ResponseRecord] = DB.withConnection{implicit connection =>
      SQL("""SELECT TOP 5000 S_ID, Q_ID, U_ID, O_ID, RESPONSE, TIMESTAMP FROM RESPONSES WHERE O_ID = {o_id};""")
        .on("o_id" -> o_id)
        .as(rowParser *)
    }
    list match {
      case x if x.nonEmpty => Some(x)
      case _ => None
    }
  }
}

package models

import anorm._
import anorm.SqlParser._
import models.QuestionModel.{QuestionRecord, Question}
import play.api.data.Form
import play.api.data.Forms._
import play.api.db._
import play.api.Play.current
import play.api.libs.json.{JsPath, Json, Writes}
import play.api.libs.functional.syntax._

object SurveyModel {

  case class SurveyRecord(s_id: String, name: String)
  case class Survey(name: String)
  case class FullSurvey(id: String, name: String, questions: List[QuestionRecord])

  def insertSurvey(x: Survey): Unit = {
    DB.withConnection {implicit connection =>
      SQL("INSERT INTO SURVEYS (S_ID, NAME) values ({s_id},{name})")
        .on("s_id" -> IdGenerator.newSurvey)
        .on("name" -> x.name)
        .executeInsert()
    }
  }

  def getSurveyName(id: String): Option[String] = {
    val surveyRecords = DB.withConnection{implicit connection =>
      SQL("""SELECT S_ID, NAME FROM SURVEYS WHERE S_ID = {S_ID} LIMIT 1;""")
        .on("S_ID" -> id)
        .as(surveyRecordRowParser *)
    }
    surveyRecords match {
      case x if x.nonEmpty => Some(x(0).name)
      case _ => None
    }
  }

  def getFullSurvey(id: String): Option[FullSurvey] = {
    val name = getSurveyName(id)
    val questions = QuestionModel.getQuestionsBySurveyID(id)
    (name,questions) match {
      case (Some(n),Some(q)) => Some(FullSurvey(id,n,q))
      case _ => None
    }
  }
  
  val surveyForm = Form(
    mapping(
      "name" -> text
    )(Survey.apply)(Survey.unapply)
  )

  implicit val fullSurveyWrites: Writes[FullSurvey] = (
    (JsPath \ "s_id").write[String] and
    (JsPath \ "name").write[String] and
    (JsPath \ "questions").write[List[QuestionRecord]]
  )(unlift(FullSurvey.unapply))

  implicit val surveyRecordWrites = new Writes[SurveyRecord] {
    def writes(survey: SurveyRecord) = Json.obj(
       "s_id" -> survey.s_id
      ,"name" -> survey.name
    )
  }

  val selectAllSurveysQuery = SQL("""SELECT S_ID,NAME FROM SURVEYS;""")

  private val surveyRecordRowParser = {
    get[String]("S_ID") ~
    get[String]("NAME") map {
      case a~b => SurveyRecord(a,b)
    }
  }

  def selectAll = {
    val list: List[SurveyRecord] = DB.withConnection{implicit connection =>
      selectAllSurveysQuery.as(surveyRecordRowParser *)
    }
    list match {
      case x if x.nonEmpty => Some(x)
      case _ => None
    }
  }
}

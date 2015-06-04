package models

import anorm.SqlParser._
import anorm._
import play.api.data.Form
import play.api.data.Forms._
import play.api.db.DB
import play.api.libs.json.{Json, Writes}
import play.api.Play.current

object QuestionModel {

  case class QuestionRecord(q_id: String = IdGenerator.newQuestion, question: Question)
  case class Question(text: String, a1: String, a2: String, a3: String)

  def insertQuestion(q: Question): Unit = {
    DB.withConnection {
      implicit connection =>
        SQL("INSERT INTO QUESTIONS (Q_ID,TEXT,a1,a2,a3) values ({q_id},{text},{a1},{a2},{a3})")
          .on("q_id"  -> IdGenerator.newQuestion)
          .on("text"  -> q.text)
          .on("a1" -> q.a1)
          .on("a2" -> q.a2)
          .on("a3" -> q.a3)
          .executeInsert()
    }
  }

  val questionForm = Form(
    mapping(
       "text"  -> text
      ,"a1" -> text
      ,"a2" -> text
      ,"a3" -> text
    )(Question.apply)(Question.unapply)
  )

  implicit val questionWrites = new Writes[Question] {
    def writes(x: Question) = Json.obj(
       "text"  -> x.text
      ,"a1" -> x.a1
      ,"a2" -> x.a2
      ,"a3" -> x.a3
    )
  }

  implicit val questionRecordWrites = new Writes[QuestionRecord] {
    def writes(x: QuestionRecord) = Json.obj(
       "q_id"  -> x.q_id
      ,"text"  -> x.question.text
      ,"a1" -> x.question.a1
      ,"a2" -> x.question.a2
      ,"a3" -> x.question.a3
    )
  }

  private val questionRowParser = {
    get[String]("q_id") ~
    get[String]("text") ~
    get[String]("a1") ~
    get[String]("a2") ~
    get[String]("a3") map {
      case a~b~c~d~e => QuestionRecord(a,Question(b,c,d,e))
    }
  }

  def getQuestions = {
    val list: List[QuestionRecord] = DB.withConnection{implicit connection =>
      SQL("""SELECT Q_ID,TEXT,a1,a2,a3 FROM QUESTIONS;""")
        .as(questionRowParser *)
    }
    list match {
      case x if x.nonEmpty => Some(x)
      case _ => None
    }
  }

  def getQuestionsBySurveyID(surveyId: String): Option[List[QuestionRecord]] = {
    val list: List[QuestionRecord] = DB.withConnection{implicit connection =>
      SQL("""
SELECT A.Q_ID,TEXT,a1,a2,a3
FROM QUESTIONS A
INNER JOIN SURVEY_QUESTIONS B
  ON A.Q_ID = B.Q_ID
WHERE B.S_ID = {S_ID}
;"""
      )
        .on("S_ID" -> surveyId)
        .as(questionRowParser *)
    }
    list match {
      case x if x.nonEmpty => Some(x)
      case _ => None
    }
  }

}

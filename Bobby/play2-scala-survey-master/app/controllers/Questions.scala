package controllers

import models.QuestionModel._
import play.api._
import play.api.libs.json.Json
import play.api.mvc._
import play.api.i18n.Messages.Implicits._
import play.api.Play.current

object Questions extends Controller {

  def get = Action {
    val items: Option[List[QuestionRecord]] = getQuestions
    Ok(views.html.questions(questionForm, items))
  }

  def post = Action {
    implicit request =>
      val question = questionForm.bindFromRequest.get
      insertQuestion(question)
      Ok("Question '" + question.text + "' inserted")
  }

  def json = Action {
    val data = getQuestions
    data match {
      case Some(list) => Ok(Json.toJson(list).toString())
      case _ => Ok
    }
  }

}
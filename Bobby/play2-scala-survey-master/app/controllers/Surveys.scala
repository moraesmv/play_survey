package controllers

import models.QuestionModel
import play.api._
import play.api.libs.json.{JsError, Json}
import play.api.mvc._
import models.SurveyModel._
import models.SurveyUpdateModel.{SurveyQuestions,addSurveyQuestions}
import models.CacheModel._
import play.api.i18n.Messages.Implicits._
import play.api.Play.current

object Surveys extends Controller {

  def get = Action {
    val items: Option[List[SurveyRecord]] = selectAll
    Ok(views.html.survey(surveyForm, items))
  }

  def editSurvey(s_id: String) = Action {
    val questions = QuestionModel.getQuestionsBySurveyID(s_id)
    val allQuestions = QuestionModel.getQuestions
    Ok(views.html.surveyedit(s_id,questions,allQuestions))
  }

  def post = Action {implicit request =>
    val credits: Int = 2 - actionCounter(request.remoteAddress, "postResponse")
    if(credits <= 0) {
      TooManyRequest("Too many requests")
    } else {
      val survey = surveyForm.bindFromRequest.get
      if (isDuplicate(survey.name.toString)) {
        Status(420)("Duplicate Request")
      } else {
        insertSurvey(survey)
        Ok("'" + survey.name + "' inserted").withHeaders(("X-RateLimit-Credits",credits.toString))
      }
    }
  }

  def json = Action {
    val data = selectAll
    data match {
      case Some(list) => Ok(Json.toJson(list).toString())
      case _ => Ok
    }
  }

  def getSurveyById(s_id: String) = Action {
    getFullSurvey(s_id) match {
      case Some(x) => Ok(Json.toJson(x).toString())
      case _ => NotFound("Survey with id " + s_id.toString + " not found")
    }
  }

  /**
   * Example insert script
   *
   * curl --include --request POST --header "Content-type: application/json" \
   * --data '{
   * "s_id" : "srv_g6NFlzCY8R9O0gBUmiQQpmje"
   * ,"q_ids":[
   * "qst_ZsNz8lFmjmFQL4NEhUdbgqBh"
   * ,"qst_TFqVM5ht6HPzrsE7NemNzH9T"
   * ,"qst_i5qafjyTFFKwfL8VETSVx0mQ"
   * ]
   * }' \
   * http://localhost:9000/v1/surveys/srv_g6NFlzCY8R9O0gBUmiQQpmje
   *
   */

  def addQuestionsToSurvey(id: String) = Action(BodyParsers.parse.json) {request =>
    val result = request.body.validate[SurveyQuestions]
    result.fold(
      errors =>
        BadRequest(
          Json.obj(
            "status"  -> "KO"
            ,"message" -> JsError.toFlatJson(errors))
        )
      ,surveyQuestions => {
        if (isDuplicate(id + "_" + surveyQuestions.hashCode().toString)) {
          Status(420)("Duplicate Request")
        } else {
          addSurveyQuestions(surveyQuestions)
          Ok(
            Json.obj(
              "status"  -> "OK"
              ,"message" -> (surveyQuestions.q_ids.length.toString + " questions for s_id " + surveyQuestions.s_id + " added.")
            )
          )
        }

      }
    )
  }

}
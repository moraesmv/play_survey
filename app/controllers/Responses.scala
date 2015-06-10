package controllers

import models.ResponseModel
import play.api._
import play.api.mvc._
import play.api.libs.json._
import models.ResponseModel._
import models.CacheModel._
import models.UserModel._
import play.api.i18n.Messages.Implicits._
import play.api.Play.current

object Responses extends Controller {

  def get = Action {
    val items: Option[List[ResponseRecord]] = ResponseModel.getResponses
    Ok(views.html.responses(items))
  }

  def getResponses(q_id: String) = Action {
    val items: Option[List[ResponseRecord]] = ResponseModel.getResponsesById(q_id)
    items match {
      case Some(list) => Ok(Json.toJson(list).toString())
      case _ => Ok
    }
  }

  def getResponsesByOrgId(o_id: String) = Action {
    val items: Option[List[ResponseRecord]] = ResponseModel.getResponsesByOrgId(o_id)
    items match {
      case Some(list) => Ok(Json.toJson(list).toString())
      case _ => Ok
    }
  }

  /**
   *  Test script:
   *  curl --include --request POST --header "Content-type: application/json" \
   *  --data '
   *  {
   *  "s_id": "srv_g6NFlzCY8R9O0gBUmiQQpmje"
   *  ,"responses":[
   *  {"q_id":"qst_ZsNz8lFmjmFQL4NEhUdbgqBh", "response":5}
   *  ,{"q_id":"qst_TFqVM5ht6HPzrsE7NemNzH9T", "response":4}
   *  ,{"q_id":"qst_i5qafjyTFFKwfL8VETSVx0mQ", "response":3}
   *  ]
   *  }' \
   *  http://localhost:9000/v1/responses
   */
  def post = Action(BodyParsers.parse.json) {request =>

    val u_id: String = request.session.get("u_id") match {
      case Some(x) => x
      case _ => getUidForIp(request.remoteAddress)
    }

    val credits: Int = 5 - actionCounter(request.remoteAddress, "postResponse")
    if (credits <= 0) {
      TooManyRequest("Too many requests")
    } else {
      val result = request.body.validate[Responses]
      result.fold(
        errors =>
          BadRequest(
            Json.obj(
              "status"  -> "KO"
              ,"message" -> JsError.toFlatJson(errors))
          ).withHeaders(("X-RateLimit-Credits",credits.toString))
        ,responses => {
            if (isDuplicate(u_id + "_" + responses.s_id)) {
              Status(420)("Duplicate Request")
            } else {
              saveResponses(responses, u_id)
              Ok(
                Json.obj(
                  "status"  -> "OK"
                  ,"message" -> ("Responses for s_id " + responses.s_id + " saved.")
                )
              ).withHeaders(("X-RateLimit-Credits",credits.toString))
            }
        }
      )
    }
  }

  def json = Action {
    val data: Option[List[ResponseRecord]] = ResponseModel.getResponses
    data match {
      case Some(list) => Ok(Json.toJson(list).toString())
      case _ => Ok
    }
  }

}
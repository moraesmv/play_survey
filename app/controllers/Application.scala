package controllers

import play.api._
import play.api.mvc._
import models.SurveyModel.selectAll
import play.api.i18n.Messages.Implicits._
import play.api.Play.current

object Application extends Controller {

  def index = Action {implicit request =>
    val u_id: String = request.session.get("u_id") match {
      case Some(x) => x
      case _ => models.IdGenerator.newUser
    }
    Ok(views.html.index(" "))
  }

  def responseForm(s_id: String) = Action {implicit request =>
    val u_id: String = request.session.get("u_id") match {
      case Some(x) => x
      case _ => models.IdGenerator.newUser
    }
    val o_id = "1"
    Ok(views.html.form(s_id,o_id))
  }
  
  //responseFormWithOrgId
  def responseFormWithOrgId(o_id: String) = Action {implicit request =>
    val s_id: String = selectAll.get.head.s_id
    val u_id: String = request.session.get("u_id") match {
      case Some(x) => x
      case _ => models.IdGenerator.newUser
    }
    Ok(views.html.form(s_id,o_id))
  }

}
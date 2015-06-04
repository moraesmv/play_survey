package controllers

import play.api._
import play.api.mvc._
import play.api.libs.json._
import models.OrgModel._
import play.api.i18n.Messages.Implicits._
import play.api.Play.current

object Orgs extends Controller {

  def get = Action {
    val orgs: Option[List[Org]] = getOrgs
    Ok(views.html.org(orgForm, orgs))
  }

  def post = Action {
    implicit request =>
      val org = orgForm.bindFromRequest.get
      insertOrg(org)
      Ok("Organization '" + org.name +"' inserted")
  }

  def json = Action {
    val data = getOrgs
    data match {
      case Some(list) => Ok(Json.toJson(list).toString())
      case _ => Ok
    }
  }

}
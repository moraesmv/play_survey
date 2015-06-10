package controllers

import models.UserModel._
import play.api._
import play.api.libs.json.Json
import play.api.mvc._
import play.api.i18n.Messages.Implicits._
import play.api.Play.current

object Security extends Controller {

  def get = Action {
    Ok(views.html.login(loginForm))
  }

  def regform = Action {
    Ok(views.html.register(loginForm))
  }

  def post = Action {
    implicit request =>
      val loginRequest = loginForm.bindFromRequest.get
      authenticate(loginRequest) match {
        case AuthSuccess(u_id) => Ok("Logged In").withSession("u_id" -> u_id)
        case _ => Unauthorized("Login Failed")
      }

  }

  def register = Action {
    implicit request =>
      val loginRequest = loginForm.bindFromRequest.get
      registerCredentials(loginRequest) match {
        case true => Ok("Registered")
        case _ => Unauthorized("Registration Failed")
      }

  }

}
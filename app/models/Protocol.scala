package models

import play.api.data.Form
import play.api.data.Forms._

object Protocol {

  case class UserOrg(u_id: Long, o_id: Long)
  case class AppUser(uuid: String)

  val appUserForm = Form(
    mapping(
      "uuid" -> text
    )(AppUser.apply)(AppUser.unapply)
  )

}



package controllers;

import play.*;
import play.mvc.*;

import views.html.*;

public class Surveys extends Controller 
{
	
	public Result show(Long id)
	{
		return ok(index.render("id"));
	}
	
}
package controllers;

import play.*;
import play.mvc.*;

import views.html.*;

public class Results extends Controller 
{
	public Result save()
	{
		return ok(index.render("saved"));
	}
	
	public Result list()
	{
		return ok(index.render("list"));
	}
	
	public Result selected()
	{
		return ok(index.render("selected"));
	}
	
}

package controllers;

import models.Person;
import play.*;
import play.data.Form;
import play.data.validation.*;
import play.db.ebean.Model;
import play.mvc.*;

import views.html.*;

import java.util.List;

import static play.libs.Json.toJson;


public class Application extends Controller {

    public static Result index() {

        return ok(index.render("besfgg."));
    }

    public static Result register() {
        return ok(register.render("bedrgg."));
    }

    public static Result menu() {

        Person person = Form.form(Person.class).bindFromRequest().get();
        List<Person> persons = new Model.Finder(String.class, Person.class).all();
        for (Person item : persons) {
            //was there someone with that address?
            if (item.email.equals(person.email)){
                //was the password correct for that user?
                if (item.pass.equals(person.pass)){
                    return ok(menu.render("baheht."));
                }else {//user exists, but wrong password
                    return redirect(routes.Application.index());
                }
            }

        }

        //no user has that address!

        return redirect(routes.Application.index());

        //return ok(menu.render("baheht."));
    }

    public static Result addPerson() {

        Person person = Form.form(Person.class).bindFromRequest().get();
        List<Person> persons = new Model.Finder(String.class, Person.class).all();
        int yes=1;//initially assume you will succeed
        for (Person item : persons) {
            if (person.email.equals(item.email)) {//whoops someone has that email already
            yes=0;
            break;
            }
        }

        if (yes==1){//no problems?
        person.save();
        return redirect(routes.Application.index());}
        else {//failed to create user
            return ok(register.render("bedrgg."));
        }

    }

    public static Result getPersons() {
        List<Person> persons = new Model.Finder(String.class, Person.class).all();
        return ok(toJson(persons));
    }

    public static Result logIn() {//oops disregard pls

        return redirect(routes.Application.index());
    }



}

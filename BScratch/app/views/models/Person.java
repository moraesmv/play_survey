package models;

import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
/**
 * Created by Guf on 24/5/2015.
 */
@Entity
public class Person extends Model{


    @Id
    public String id;
    public String email;
    public String pass;
    public String name;
    public int accesslevel;



    public void setEmail(String e){
        email=e;
    }

    public void setPass(String o, String n){
        if (pass.equals(o) || pass == null)
        pass=n;
    }

}

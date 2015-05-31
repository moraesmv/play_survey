package models;

import play.data.format.Formats;
import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class Results extends Model {

    @Id
    public Long id;

    @Constraints.Required
    public String name;

    @Formats.DateTime(pattern = "MM/dd/yy")
    public Date createDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public static Finder find = new Finder(Long.class, Budget.class);

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Budget");
        sb.append("{id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append('}');
        return sb.toString();
    }
}

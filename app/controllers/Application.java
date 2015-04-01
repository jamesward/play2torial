package controllers;

import play.*;
import play.mvc.*;

import views.html.*;

public class Application extends Controller {

    public static Result index() {
        return ok(index.render("hello, world"));
    }

    public static Result addTask() {
        play.data.Form<models.Task> form = play.data.Form.form(models.Task.class).bindFromRequest();
        models.Task task = form.get();
        task.save();
        return redirect(routes.Application.index());
    }

    public static Result getTasks() {
        java.util.List<models.Task> tasks = new play.db.ebean.Model.Finder(String.class, models.Task.class).all();
        return ok(play.libs.Json.toJson(tasks));
    }

}

# Welcome to the Play2torial for Java!
====================================

This tutorial will help teach you how to build Java web applications with Play Framework 2.3.

Before you get started you will need to install [git](http://git-scm.com/) and [Play 2.3](https://www.playframework.com/documentation/2.3.x/Installing). You will complete this by installing the Typesafe Activator tool.


Test that the `activator` command works by running:

    activator -help


You should see text displaying optional commands for the activator tool. If you do not, repeat the above process and make sure you are downloading the most recent version of Play (2.3.7).


Alright!  You are ready to go!



Create a Play App
-----------------

Create a new Play 2.3 application named "play2torial" by running:

    activator new play2torial

When prompted select option 5 `play-java` to create a Java application.


In the newly created `play2torial` directory create a new git repository by running:

    git init


Add the files to the git repo and commit them:

```sh
git add .
git commit -m init
```

Throughout this tutorial you will be able to check your progress against the official tutorial.  To do this add the official tutorial as a new git remote named `upstream` by running:

    git remote add upstream https://github.com/jamesward/play2torial.git


Fetch the remote repository:

    git fetch upstream


Now validate that your local project is correct by running:

    git diff upstream/java-new_project

Note: The `application.secret` config value will be different and that is fine.  Also sometimes git will tell you something is different even though it looks the same.  This will likely be due to differences in indentation and newlines.  You can ignore these differences.



Set up an IDE
-------------

Before we take a tour of the app you can optionally generate project files for Eclipse. This is highly recommended.


For Eclipse run:

    activator eclipse

For IntelliJ, install the Scala IntelliJ Plugin and then open the project's `build.sbt` file from the `File > Open...` dialog.

_Read more about [IDE's](https://www.playframework.com/documentation/2.3.x/IDE)_


Start the Play Server
---------------------

Now start the Play app by running:

    activator ~run

Open the following URL in your browser to verify the app is working:  
[http://localhost:9000/](http://localhost:9000/)

_Read more about the [Play Console](https://www.playframework.com/documentation/2.3.x/PlayConsole)_

Routes
------

Play routes HTTP requests to a controller using the routes defined in the `conf/routes` file.  The routes file maps HTTP verbs and URL patterns to controller methods.  The route that matched the request you just made was defined in the "routes" file with:

    GET     /                           controllers.Application.index()


That means that when a HTTP GET request for the URL `/` comes in, it will be routed to the method named `index` on the `controllers.Application` class.

Add a new route to handle GET requests to `/foo` with a call to `controllers.Application.index()` by adding the following line to the `conf/routes` file:

    GET     /foo                        controllers.Application.index()


Now try to open the following URL in your browser:  
[http://localhost:9000/foo](http://localhost:9000/foo)

You should see the same welcome message as your did when you made the request to the `/` URL.

_Read more about [Routing](https://www.playframework.com/documentation/2.3.x/JavaRouting)_

Commit and verify your changes:

```sh
git commit -am "added new route"
git diff upstream/java-foo_route
```


Test a Route
------------

Now that you have a new route for `/foo` lets create a test for that route.  Now create a new file `test/FooTest.java` file containing:

```java
import org.junit.Test;
import play.mvc.Result;
import play.test.Helpers;
import play.test.WithApplication;

import static play.test.Helpers.*;
import static org.fest.assertions.Assertions.*;

public class FooTest extends WithApplication {

    @Test
    public void testFooRoute() {
        Result result = route(fakeRequest(GET, "/foo"));
        assertThat(result).isNotNull();
    }

}
```

That simple test will simulate a call to `/foo` and validate that the result was not null.  Run the test (you can keep the Play server running in another window):

    activator test


You should see something similiar to:

    [info] Passed: Total 4, Failed 0, Errors 0, Passed 4

_Read more about [Testing Routes](https://www.playframework.com/documentation/2.3.x/JavaFunctionalTest)_

Commit and verify your changes:

```sh
git add test
git commit -m "added test for foo route"
git diff upstream/java-test_route
```


Update a Controller
-------------------

The `app` directory is the main source for a Play app.  In that directory there is a directory named `controllers` containing a file named `Application.java` which is the `controllers.Application` class.

The `index()` method body looks like:

```java
return ok(index.render("Your new application is ready."));
```

Edit the `Application.java` file and change the `Your new application is ready.` string to `hello, world`.  Save the file and then reload the following URL in your browser:  
[http://localhost:9000/](http://localhost:9000/)


Notice that the header at the top of the page now reads `hello, world`.  Play recompiled the Java controller behind the scenes.  If you had made a change that could not be compiled you would see the compile error in your browser and in your console.

This change will break two of the existing tests so change the `Your new application is ready.` string to `hello, world` in the `test/ApplicationTest.java` and `test/IntegrationTest.java` files.

To verify the tests pass, run:

    activator test

Commit and verify that your changes:

    git commit -am "updated controller"
    git diff upstream/java-hello_controller



Test a Controller
-----------------

You can do tests against a controller by simply creating a new JUnit Test.  Add a new test method to the `test/ApplicationTest.java` file:

```java
    @Test
    public void testIndex() {
        Result result = controllers.Application.index();
        assertThat(status(result)).isEqualTo(OK);
        assertThat(contentType(result)).isEqualTo("text/html");
        assertThat(charset(result)).isEqualTo("utf-8");
        assertThat(contentAsString(result)).contains("hello, world");
    }
```

This simulates a request to the `Application.index()` controller method and verifies that the response is what we expect.  Run the test:

    activator test


You should see something like:

    [info] Passed: Total 5, Failed 0, Errors 0, Passed 5

_Read more about [Testing Controllers](https://www.playframework.com/documentation/2.3.x/JavaFunctionalTest)_

Commit and verify that your changes:

```sh
git add test/ApplicationTest.java
git commit -am "added Application controller test"
git diff upstream/java-test_controller
```


Update a View
-------------

Play uses Scala for server-side templating.  The `Application` controller renders the `views.html.index` template which is compiled from the `app/views/index.scala.html` file.  The `index` template takes a String parameter:

    @(message: String)


Then the `index` template uses the `main` template (from `app/views/main.scala.html`) to get a base HTML page:

    @main("Welcome to Play") {


The main template is passed a String parameter for the page title and a Html parameter for the body of the page.  The body of the page is the Play welcome message which comes from:

    @play20.welcome(message, style = "Java")


Change the Play welcome message to uppercase:

    @play20.welcome(message.toUpperCase, style = "Java")

View your changes in the browser:  
[http://localhost:9000/](http://localhost:9000/)

Because the `test/ApplicationTest.java` and `test/IntegrationTest.java` tests use this updated template, update the tests to account for the upper cased string by changing all `hello, world` strings to `HELLO, WORLD`.

As you can see, the `test/ApplicationTest.java` file tests the server-side template directly:

```java
    @Test
    public void renderTemplate() {
        Content html = views.html.index.render("HELLO, WORLD");
        assertThat(contentType(html)).isEqualTo("text/html");
        assertThat(contentAsString(html)).contains("HELLO, WORLD");
    }
```

The `test/IntegrationTest.java` does an integration test by starting a Play server and using [HtmlUnit](http://htmlunit.sourceforge.net/) to make a headless browser request to the server, thus testing the routes, controller, and views:

```java
    @Test
    public void test() {
        running(testServer(3333, fakeApplication(inMemoryDatabase())), HTMLUNIT, new Callback<TestBrowser>() {
            public void invoke(TestBrowser browser) {
                browser.goTo("http://localhost:3333");
                assertThat(browser.pageSource()).contains("HELLO, WORLD");
            }
        });
    }
```

Run the tests and verify they all pass:

    activator test

_Read more about [Templating](https://www.playframework.com/documentation/2.3.x/JavaTemplates) and [Template Use Cases](https://www.playframework.com/documentation/2.3.x/JavaTemplateUseCases)_

Commit and verify that your changes:

```sh
git commit -am "change the view"
git diff upstream/java-hello_view
```


Deploy your app on the Cloud with Heroku
----------------------------------------

Deploying your Play app on the cloud is easy with Heroku!

1. [Install the Heroku Toolbelt](http://toolbelt.heroku.com)
2. [Signup for a free Heroku account](http://heroku.com/signup)
3. Login to Heroku from the command line:

        heroku login

4. Provision a new application on Heroku:

        heroku create

5. Push the application to Heroku:

        git push heroku master

Heroku will build the app with sbt and then run it on a [dyno](https://devcenter.heroku.com/articles/dynos).

Open the application, now running on the cloud, in your browser:

    heroku open

The page should say `HELLO, WORLD` but will look different from what you see locally since the `play20.welcome` template our app is using, displays different stuff depending on whether you are running in `DEV` or `PROD` mode.  To run locally in `PROD` mode, stop the `activator ~run` process and instead run `activator start`.  This mode does not do any auto-reloading.


Create a Model
--------------

By default, Play 2.3 with Java uses [Ebean](http://www.avaje.org/) for RDBMS persistence.  To setup a data source, edit the `conf/application.conf` file and uncomment these lines:

```properties
db.default.driver=org.h2.Driver
db.default.url="jdbc:h2:mem:play"
    
ebean.default="models.*"
```

This will use an in-memory database for a data source named "default".

Create a new Java class to hold `Task` objects in a package named `models` by creating a new `app/models/Task.java` file containing:

```java
package models;
    
import play.db.ebean.Model;
    
import javax.persistence.Entity;
import javax.persistence.Id;
    
@Entity
public class Task extends Model {
    
    @Id
    public String id;
    
    public String contents;
    
}
```

Create a test for your model by creating a new `test/TaskTest.java` file containing:

```java
import org.junit.Test;
    
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.running;
    
import static org.fest.assertions.Assertions.assertThat;
    
import models.Task;
    
public class TaskTest {
    
    @Test
    public void create() {
        running(fakeApplication(), new Runnable() {
            public void run() {
                Task task = new Task();
                task.contents = "Write a test";
                task.save();
                assertThat(task.id).isNotNull();
            }
        });
    }
    
}
```

Run the tests and make sure all four pass:

    activator test


Ebean will automatically create a new database evolution script for you in a file named `conf/evolutions/default/1.sql`.

_Read more about [EBean ORM Models](https://www.playframework.com/documentation/2.3.x/JavaEbean)_

Commit and verify your changes:

```sh
git add conf/evolutions app/models/Task.java test/TaskTest.java
git commit -am "added Task model, in-memory database config, and test"
git diff upstream/java-task_model
```


Create UI for Adding Tasks
--------------------------

Start by adding a new method to the `app/controllers/Application.java` file that will map request parameters to a `Task` object, save it, and then redirect to the `index()` page.

Now add a new method named `addTask`:

```java
    public static Result addTask() {
        play.data.Form<models.Task> form = play.data.Form.form(models.Task.class).bindFromRequest();
        models.Task task = form.get();
        task.save();
        return redirect(routes.Application.index());
    }
```

Now add a new route to the `conf/routes` file that will handle `POST` requests to `/task` and handle the request with the `Application.addTask()` method:

    POST    /task                       controllers.Application.addTask()


Now create a form in the `app/views/index.scala.html` template for adding new `Tasks`.  Replace the `@play20.welcome` line with:

```html
    @message.toUpperCase
          
    @helper.form(action = routes.Application.addTask()) {
        <input name="contents"/>
        <input type="submit"/>
    }
```

Test out the new UI by loading the app in your browser:  
[http://localhost:9000/](http://localhost:9000/)

You will be prompted to run the database evolutions.  Just click the `Apply this script now!` button.

Add a new `Task` and the browser should refresh the index page.

_Read more about [Forms](https://www.playframework.com/documentation/2.3.x/JavaForms) and [Templates](https://www.playframework.com/documentation/2.3.x/JavaFormHelpers)_

Commit and verify your changes:

```sh
git commit -am "add new UI to create Tasks"
git diff upstream/java-task_add
```


Get Tasks as JSON
-----------------

Create a new `getTasks` controller method in the `app/controllers/Application.java` file:

```java
    public static Result getTasks() {
        java.util.List<models.Task> tasks = new play.db.ebean.Model.Finder(String.class, models.Task.class).all();
        return ok(play.libs.Json.toJson(tasks));
    }
```

Add a new route to the `conf/routes` file to get the tasks as JSON serialized data:

    GET     /tasks                      controllers.Application.getTasks()


After adding a new `Task` load the following URL in your browser:  
[http://localhost:9000/tasks](http://localhost:9000/tasks)


Verify that you see a Task (or Tasks) in JSON form.  If the `contents` property in the JSON is null, then stop the `activator ~run` process, run `activator clean` and then run the app again: `activator ~run`


Commit and verify your changes:

```sh
git commit -am "get the tasks as JSON"
git diff upstream/java-task_json
```


Display the Tasks via CoffeeScript and jQuery
---------------------------------------------

Add the jQuery WebJar to your project by adding the following to your `build.sbt` file:

     
    libraryDependencies += "org.webjars" % "jquery" % "1.11.2"

Note: Make sure you have an empty line before the line above.

Restart the `activator ~run` process to reload the dependencies.

In the body of the `app/views/index.scala.html` file add a place to display the tasks above the form:

    <ul id="tasks"></ul>

Create a new file named `app/assets/javascripts/index.coffee` (and create the necessary directories) containing a simple CoffeeScript application that uses jQuery to load and display the tasks:

```coffee
$ ->
  $.get "/tasks", (data) ->
    $.each data, (index, task) ->
      $("#tasks").append $("<li>").text task.contents
```

This makes a `get` request to `/tasks` for the JSON serialized list of `Task` objects and then adds a new list item to the page element with the id of `tasks` for each `Task`.


Update the `app/views/main.scala.html` file to load jQuery from the WebJar and include the compiled version of the `index.coffee` JavaScript:

```html
        <script src="@routes.Assets.at("lib/jquery/jquery.min.js")" type="text/javascript"></script>
        <script src="@routes.Assets.at("javascripts/index.js")" type="text/javascript"></script>
```

Check out the app in your browser and verify that the list of tasks is displayed:  
[http://localhost:9000/](http://localhost:9000/)

To see the compiled version of the compiled javascript you can go to [http://localhost:9000/assets/javascripts/index.js](http://localhost:9000/assets/javascripts/index.js)

_Read more about [Assets](https://www.playframework.com/documentation/2.3.x/Assets) and [CoffeeScript](https://www.playframework.com/documentation/2.3.x/AssetsCoffeeScript)_

Commit and verify your changes:

```sh
git add app/views/index.scala.html app/assets/javascripts/index.coffee app/views/main.scala.html
git commit -am "Display the list of tasks using jQuery and CoffeeScript"
git diff upstream/java-task_coffeescript
```


Make the App Pretty with Twitter Bootstrap
------------------------------------------

[Bootstrap](http://getbootstrap.com) is a CSS library that makes it easy to make a web app look better.  To use Bootstrap start by adding the dependency to the `build.sbt` file:

```scala

libraryDependencies += "org.webjars" % "bootstrap" % "2.1.1"
```

Note: Make sure you have an empty line before the line above.

Now restart the Play server so that it will fetch the new dependency.  To use Bootstrap simply include the following line in the `app/views/main.scala.html` file, making sure it is between the `<title>` and the `main.css` lines:

```html
<link rel="stylesheet" media="screen" href="@routes.Assets.at("lib/bootstrap/css/bootstrap.min.css")">
```

Add the following to the `public/stylesheets/main.css` file in order to move the main content down to a viewable location:

```css
body {
    padding-top: 50px;
}
```

Create a new template component that will be used to create new Bootstrap-friendly form fields by creating a new file named `app/views/twitterBootstrapInput.scala.html` containing:

```html
@(elements: helper.FieldElements)
    
<div class="control-group @if(elements.hasErrors) {error}">
    <label for="@elements.id" class="control-label">@elements.label</label>
    <div class="controls">
        @elements.input
        <span class="help-inline">@elements.errors.mkString(", ")</span>
    </div>
</div>
```

Update the `app/views/index.scala.html` file to use Bootstrap for a nice header, better layout, and nicer default fonts:

```html
@(message: String, taskForm: Form[Task])
@implicitFieldConstructor = @{ helper.FieldConstructor(twitterBootstrapInput.render) }
    
@main("Welcome to Play 2.0") {
    
    <div class="navbar navbar-fixed-top">
        <div class="navbar-inner">
            <div class="container-fluid">
                <a id="titleLink" class="brand" href="/">@message.toUpperCase</a>
            </div>
        </div>
    </div>

    <div class="container">
        <ul id="tasks"></ul>

        @helper.form(action = routes.Application.addTask(), 'class -> "well form-horizontal") {
            @helper.inputText(taskForm("contents"), '_label -> "Contents")
            <div class="controls">
                <input type="submit" class="btn btn-primary"/>
            </div>
        }
    </div>
    
}
```

The template now takes a second parameter that is a `Form[Task]` and must be passed to the template.  This will be used for helping with form validation.  Update the `index()` method in the `app/controllers/Application.java` file to pass the new required parameter:

```java
    public static Result index() {
        return ok(index.render("hello, world", play.data.Form.form(models.Task.class)));
    }
```

In the `test/ApplicationTest.java` file, update the `indexTemplate` test method:

```java
        Content html = views.html.index.render("HELLO, WORLD", play.data.Form.form(models.Task.class));
```

Run the tests to make sure they still pass:

    activator test


Load the app in your browser and verify it still works:  
[http://localhost:9000/](http://localhost:9000/)

_Read more about [Templates](https://www.playframework.com/documentation/2.3.x/JavaTemplates)_

Commit and verify your changes:

```bash
git add app/views/twitterBootstrapInput.scala.html
git commit -am "Add Bootstrap"
git diff upstream/java-bootstrap
```


Add Form Validation
-------------------

Add a simple `Required` constraint to `contents` property on the `app/models/Task.java` class:

```java
    @play.data.validation.Constraints.Required
    public String contents;
```

Update the `addTask` method on the `app/controllers/Application.java` controller to check for form errors and if it sees any then render the form instead of trying to save the Task:

```java
    public static Result addTask() {
        play.data.Form<models.Task> form = play.data.Form.form(models.Task.class).bindFromRequest();
        if (form.hasErrors()) {
            return badRequest(index.render("hello, world", form));
        }
        else {
            models.Task task = form.get();
            task.save();
            return redirect(routes.Application.index());
        }
    }
```

Load the app in your browser verify that adding an empty Task displays an error:  
[http://localhost:9000/](http://localhost:9000/)

_Read more about [Validation](https://www.playframework.com/documentation/2.3.x/JavaForms)_

Commit and verify your changes:

```sh
git commit -am "Add validation"
git diff upstream/java-validation
```


Update the App on Heroku
------------------------

Heroku provides each application with a small, free PostgreSQL database.  To switch the application to use that database only when it's running on Heroku, two small changes need to be made.

First, add the PostgreSQL database driver as a dependency by adding the following to the `build.sbt` file:

```scala

libraryDependencies += "postgresql" % "postgresql" % "9.1-901-1.jdbc4"
```

Note: Make sure you have an empty line before the line above.

Create a new file named `Procfile` (make sure to capitalize the "P") in order to override the default database settings when Heroku runs the app:

    web: target/universal/stage/bin/play2torial -Dhttp.port=${PORT} -DapplyEvolutions.default=true -Ddb.default.driver=org.postgresql.Driver -Ddb.default.url=$DATABASE_URL


Commit and verify your changes:

```sh
git commit -am "Updates for PostgreSQL on Heroku"
git diff upstream/java-heroku_update
```

Push your updates to Heroku:

    git push heroku master


View your app on the cloud:

    heroku open

_Read more about [Play on Heroku](https://www.playframework.com/documentation/2.3.x/ProductionHeroku)_

Congratulations!
----------------

You've built a Play 2.3 app and deployed it on the cloud.  You've learned how to get started with Play 2.3, Ebean, CoffeeScript, Twitter Bootstrap, jQuery, RESTful JSON services, and Heroku.  Have fun as you continue to learn Play 2.3!

# Welcome to the Play2torial for Java!
====================================

This tutorial will help teach you how to build Java web applications with Play Framework 2.

Before you get started you will need to install [git](http://git-scm.com/) and [Play 2](http://www.playframework.org/documentation/2.0.1/Installing).


Test that the `play` command works by running:

    play help


You should see something like:

           _            _ 
     _ __ | | __ _ _  _| |
    | '_ \| |/ _' | || |_|
    |  __/|_|\____|\__ (_)
    |_|            |__/ 
             
    play! 2.0.1, http://www.playframework.org
    
    Welcome to Play 2.0!


Alright!  You are ready to go!



Creating a Play App
-------------------

Create a new Play 2 application named "play2torial" by running:

    play new play2torial

When prompted select option 2 to create a Java application.


In the newly created `play2torial` directory create a new git repository by running:

    git init


Add the files to the git repo and commit them:

    git add .
    git commit -m init


Throughout this tutorial you will be able to check your progress against the official tutorial.  To do this add the official tutorial as a new git remote named `upstream` by running:

    git remote add upstream git@github.com:jamesward/play2torial.git


Fetch the remote repository:

    git fetch upstream


Now validate that your local project is correct by running:

    git diff upstream/java-new_project

Note: The `application.secret` config value will be different and that is fine.  Also sometimes git will tell you something is different even though it looks the same.  This will likely be due to differences in indentation and newlines.  You can ignore these differences.



Setting up an IDE
-----------------

Before we take a tour of the app you can optionally generate project files for IntelliJ or Eclipse.

For IntelliJ run:

    play idea

Then create a new Java project from scratch in IntelliJ that uses this directory as the root.  Don't create a module with the new project.  Instead, once the project is created, import the generated play2torial.iml file as an existing module.

For Eclipse run:

    play eclipsify

If you do a `git status` then you will see some uncommitted files.  Those shouldn't go into the git repo so add the following lines to the `.gitignore` file:

    /.idea
    /*.iml
    /.settings
    /.project
    /.classpath

Then commit the change:

    git commit -am "ignore IDE files"



Starting the Play Server
------------------------

Now start the Play app by running:

    play ~run

Open the following URL in your browser to verify the app is working:  
[http://localhost:9000/](http://localhost:9000/)



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

Commit and verify your changes:

    git commit -am "added new route"
    git diff upstream/java-foo_route



Test a Route
------------

Now that you have a new route for `/foo` lets create a test for that route.  Now create a new file `test/FooTest.java` file containing:

    import org.junit.Test;
    import play.mvc.Result;
    
    import static play.test.Helpers.*;
    import static org.fest.assertions.Assertions.*;
    
    public class FooTest {
    
      @Test
      public void testFooRoute() {
        Result result = routeAndCall(fakeRequest(GET, "/foo"));
        assertThat(result).isNotNull();
      }
    
    }


That simple test will simulate a call to `/foo` and validate that the result was not null.  Run the test (you can keep the Play server running in another window):

    play test


You should see something like:

    [info] FooTest
    [info] + FooTest.testFooRoute
    [info]
    [info]
    [info] Total for test FooTest
    [info] Finished in 0.131 seconds
    [info] 1 tests, 0 failures, 0 errors
    [info] Passed: : Total 1, Failed 0, Errors 0, Passed 1, Skipped 0


Commit and verify your changes:

    git add test
    git commit -m "added test for foo route"
    git diff upstream/java-test_route



Update a Controller
-------------------

The `app` directory is the main source for a Play app.  In that directory there is a directory named `controllers` containing a file named `Application.java` which is the `controllers.Application` class.

The `index()` method body looks like:

    return ok(index.render("Your new application is ready."));

Edit the `Application.java` file and change the `Your new application is ready.` string to `hello, world`.  Save the file and then reload the following URL in your browser:  
[http://localhost:9000/](http://localhost:9000/)


Notice that the header at the top of the page now reads `hello, world`.  Play recompiled the Java controller behind the scenes.  If you had made a change that could not be compiled you would see the compile error in your browser and in your console.

Commit and verify that your changes:

    git commit -am "updated controller"
    git diff upstream/java-hello_controller



Test a Controller
-----------------

You can do functional tests against a controller by simply creating a new JUnit Test.  Create a new file named `test/ApplicationTest.java` file containing:

    import org.junit.Test;
    import play.mvc.Result;
    
    import static org.fest.assertions.Assertions.assertThat;
    import static play.mvc.Http.Status.OK;
    import static play.test.Helpers.*;
    
    public class ApplicationTest {
    
      @Test
      public void callIndex() {
        Result result = callAction(controllers.routes.ref.Application.index());
        assertThat(status(result)).isEqualTo(OK);
        assertThat(contentType(result)).isEqualTo("text/html");
        assertThat(charset(result)).isEqualTo("utf-8");
        assertThat(contentAsString(result)).contains("hello, world");
      }
    
    }

This simulates a request to the `Application.index()` controller method and verifies that the response is what we expect.  Run the test:

    play test


You should see something like:

    [info] FooTest
    [info] + FooTest.testFooRoute
    [info]
    [info]
    [info] Total for test FooTest
    [info] Finished in 0.191 seconds
    [info] 1 tests, 0 failures, 0 errors
    [info] ApplicationTest
    [info] + ApplicationTest.callIndex
    [info]
    [info]
    [info] Total for test ApplicationTest
    [info] Finished in 0.046 seconds
    [info] 1 tests, 0 failures, 0 errors
    [info] Passed: : Total 2, Failed 0, Errors 0, Passed 2, Skipped 0


Commit and verify that your changes:

    git add test/ApplicationTest.java
    git commit -am "added Application controller test"
    git diff upstream/java-test_controller



Update a View
-------------

Play uses Scala for server-side templating.  The `Application` controller renders the `views.html.index` template which is compiled from the `app/views/index.scala.html` file.  The `index` template takes a String parameter:

    @(message: String)


Then the `index` template uses the `main` template (from `app/views/main.scala.html`) to get a base HTML page:

    @main("Welcome to Play 2.0") {


The main template is passed a String parameter for the page title and a Html parameter for the body of the page.  The body of the page is the Play welcome message which comes from:

    @play20.welcome(message, style = "Java")


Change the Play welcome message to uppercase:

    @play20.welcome(message.toUpperCase, style = "Java")

View your changes in the browser:  
[http://localhost:9000/](http://localhost:9000/)


Because the `test/ApplicationTest.java` test uses this updated template, update the test to account for the upper cased string:

    assertThat(contentAsString(result)).contains("HELLO, WORLD");


Run the tests:

    play test


Commit and verify that your changes:

    git commit -am "change the view"
    git diff upstream/java-hello_view



Test a View
-----------

To test a template directly, create a new `test/IndexTest.java` file containing:

    import org.junit.Test;
    import play.mvc.Content;
    
    import static org.fest.assertions.Assertions.assertThat;
    import static play.test.Helpers.*;
    
    public class IndexTest {
    
      @Test
      public void indexTemplate() {
        Content html = views.html.index.render("test");
        assertThat(contentType(html)).isEqualTo("text/html");
        assertThat(contentAsString(html)).contains("TEST");
      }
    
    }

Run the tests:

    play test

You should see something like:

    [info] FooTest
    [info] + FooTest.testFooRoute
    [info]
    [info]
    [info] Total for test FooTest
    [info] Finished in 0.176 seconds
    [info] 1 tests, 0 failures, 0 errors
    [info] ApplicationTest
    [info] + ApplicationTest.callIndex
    [info]
    [info]
    [info] Total for test ApplicationTest
    [info] Finished in 0.041 seconds
    [info] 1 tests, 0 failures, 0 errors
    [info] IndexTest
    [info] + IndexTest.indexTemplate
    [info]
    [info]
    [info] Total for test IndexTest
    [info] Finished in 0.004 seconds
    [info] 1 tests, 0 failures, 0 errors
    [info] Passed: : Total 3, Failed 0, Errors 0, Passed 3, Skipped 0

Verify that your changes:

    git add test/IndexTest.java
    git commit -m "add index view test"
    git diff upstream/java-test_view



Deploy your app on the Cloud with Heroku
----------------------------------------

Deploying your Play app on the cloud is easy with Heroku!  First create a new file in the root directory named `Procfile` that contains:

    web: target/start -Dhttp.port=${PORT} ${JAVA_OPTS}

That tells Heroku how to start the Play application.


Commit and verify your changes:

    git add Procfile
    git commit -m "add Procfile for Heroku"
    git diff upstream/java-heroku_create


Now install the Heroku Toolbelt:  
[http://toolbelt.heroku.com](http://toolbelt.heroku.com)


Signup for a Heroku account:  
[http://heroku.com/signup](http://heroku.com/signup)


Login to Heroku from the command line:

    heroku login


Provision a new application on Heroku:

    heroku create --stack cedar


Now push this applicaiton to Heroku:

    git push heroku master

Heroku will build the app with SBT and then run it on a [dyno](https://devcenter.heroku.com/articles/dynos).


Open the application, now running on the cloud, in your browser:

    heroku open



Create a Model
--------------

Play 2 with Java uses [Ebean](http://www.avaje.org/) for RDBMS persistence.  To setup a data source edit the `conf/application.conf` file and uncomment these lines:

    db.default.driver=org.h2.Driver
    db.default.url="jdbc:h2:mem:play"
    
    ebean.default="models.*"


This will use an in-memory database for a data source named "default".

Create a new Java class to hold `Task` objects in a package named `models` by creating a new `app/models/Task.java` file containing:

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


Create a test for your model by creating a new `test/TaskTest.java` file containing:

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


Run the tests and make sure all four pass:

    play test


Ebean will automatically create a new database evolution script for you in a file named `conf/evolutions/default/1.sql`.


Commit and verify your changes:

    git add conf/evolutions app/models/Task.java test/TaskTest.java
    git commit -am "added Task model, in-memory database config, and test"
    git diff upstream/java-task_model



Create UI for Adding Tasks
--------------------------

Start by adding a new method to the `app/controllers/Application.java` file that will map request parameters to a `Task` object, save it, and then redirect to the `index()` page.  First add these imports:

    import models.Task;
    import play.data.Form;


Now add a new method named `addTask`:

      public static Result addTask() {
        Form<Task> form = form(Task.class).bindFromRequest();
        Task task = form.get();
        task.save();
        return redirect(routes.Application.index());
      }


Now add a new route to the `conf/routes` file that will handle `POST` requests to `/tasks` and handle the request with the `Application.addTask()` method:

    POST    /task                       controllers.Application.addTask()


Now create a form in the `app/views/index.scala.html` template for adding new `Tasks`.  Replace the `@play20.welcome` line with:

      @message.toUpperCase
      
      @helper.form(action = routes.Application.addTask()) {
        <input name="contents"/>
        <input type="submit"/>
      }


Test out the new UI by loading the app in your browser:  
[http://localhost:9000/](http://localhost:9000/)

You will be prompted to run the database evolutions.  Just click the `Apply this script now!` button.


Add a new `Task` and the browser should refresh the index page.

Commit and verify your changes:

    git commit -am "add new UI to create Tasks"
    git diff upstream/java-task_add



Get Tasks as JSON
-----------------

Create a new `getTasks` controller method in the `app/controllers/Application.java` file.  First add the imports:

    import play.db.ebean.Model;
    import java.util.List;
    import static play.libs.Json.toJson;

Then add the new `getTasks` method:

      public static Result getTasks() {
        List<Task> tasks = new Model.Finder(String.class, Task.class).all();
        return ok(toJson(tasks));
      }


Add a new route to the `conf/routes` file to get the tasks as JSON serialized data:

    GET     /tasks                      controllers.Application.getTasks()


After adding a new `Task` load the following URL in your browser:  
[http://localhost:9000/tasks](http://localhost:9000/tasks)


Verify that you see a Task (or Tasks) in JSON form.


Commit and verify your changes:

    git commit -am "get the tasks as JSON"
    git diff upstream/java-task_json



Display the Tasks via CoffeeScript and jQuery
---------------------------------------------

In the body of the `app/views/index.scala.html` file add a place to display the tasks about the form:

    <ul id="tasks"></ul>


Create a new file named `app/assets/javascripts/index.coffee` (and create the necessary directories) containing a simple CoffeeScript application that uses jQuery to load and display the tasks:

    $ ->
      $.get "/tasks", (data) ->
        $.each data, (index, task) ->
          $("#tasks").append $("<li>").text task.contents

This makes a `get` request to `/tasks` for the JSON serialized list of `Task` objects and then adds a new list item to the page element with the id of `tasks` for each `Task`.


Update the `app/views/main.scala.html` file to include the compiled and minified version of the `index.coffee` JavaScript by adding the following after the line that includes jQuery:

    <script src="@routes.Assets.at("javascripts/index.min.js")" type="text/javascript"></script>


Check out the app in your browser:  
[http://localhost:9000/](http://localhost:9000/)


Commit and verify your changes:

    git add app/views/index.scala.html app/assets/javascripts/index.coffee app/views/main.scala.html
    git commit -am "Display the list of tasks using jQuery and CoffeeScript"
    git diff upstream/java-task_coffeescript



Make the App Pretty with Twitter Bootstrap
------------------------------------------

[Twitter Bootstrap](http://twitter.github.com/bootstrap) is a CSS library that makes it easy to make a web app look better.  To use Twitter Bootstrap start by adding the [WebJar](http://webjar.github.com) dependency and repository resolver to the `project/Build.scala` file:

        val appDependencies = Seq(
          "com.github.twitter" % "bootstrap" % "2.0.2"
        )
    
        val main = PlayProject(appName, appVersion, appDependencies, mainLang = JAVA).settings(
          resolvers += "webjars" at "http://webjars.github.com/m2"
        )


Now restart the Play server so that it will fetch the new dependency.  To use Bootstrap simply include the following line in the `app/views/main.scala.html` file, making sure it is between the `<title>` and the `main.css` lines:

    <link rel="stylesheet" media="screen" href="@routes.Assets.at("stylesheets/bootstrap.min.css")">


Add the following to the `public/stylesheets/main.css` file in order to move the main content down to a viewable location:

    body {
        margin-top: 50px;
    }


Create a new template component that will be used to create new Bootstrap-friendly form fields by creating a new file named `app/views/twitterBootstrapInput.scala.html` containing:

    @(elements: helper.FieldElements)

    <div class="control-group @if(elements.hasErrors) {error}">
        <label for="@elements.id" class="control-label">@elements.label</label>
        <div class="controls">
            @elements.input
            <span class="help-inline">@elements.errors.mkString(", ")</span>
        </div>
    </div>


Update the `app/views/index.scala.html` file to use Bootstrap for a nice header, better layout, and nicer default fonts:

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


The template now takes a second parameter that is a `Form[Task]` and must be passed to the template.  This will be used for helping with form validation.  Update the `index()` method in the `app/controllers/Application.java` file to pass the new required parameter:

      public static Result index() {
        return ok(index.render("hello, world", form(Task.class)));
      }


Update the `test/IndexTest.java` file to pass the form to the template.  First add the import:

    import models.Task;

Update the first line of the `indexTemplate` test method:

          Content html = views.html.index.render("test", new Form(Task.class));


Run the tests to make sure they still pass:

    play test


Load the app in your browser and verify it still works:  
[http://localhost:9000/](http://localhost:9000/)


Commit and verify your changes:

    git add app/views/twitterBootstrapInput.scala.html
    git commit -am "Add Bootstrap"
    git diff upstream/java-bootstrap



Add Form Validation
-------------------

Add a simple `Required` constraint to the `app/models/Task.java` class.  First, add the import:

    import play.data.validation.Constraints;

Add the `@Constraints.Required` annotation to the `contents` field:

        @Constraints.Required
        public String contents;


Update the `addTask` method on the `app/controllers/Application.java` controller to check for form errors and if it sees any then render the form instead of trying to save the Task:

      public static Result addTask() {
        Form<Task> form = form(Task.class).bindFromRequest();
        if (form.hasErrors()) {
          return badRequest(index.render("hello, world", form));
        }
        else {
          Task task = form.get();
          task.save();
          return redirect(routes.Application.index());
        }
      }


Load the app in your browser verify that adding an empty Task displays an error:  
[http://localhost:9000/](http://localhost:9000/)


Commit and verify your changes:

    git commit -am "Add validation"
    git diff upstream/java-validation



Update the App on Heroku
------------------------

Heroku provides each application with a small, free PostgreSQL database.  To switch the application to use that database only when it's running on Heroku, two small changes need to be made.

First, add the PostgreSQL database driver as a dependency of the application by `project/Build.scala` file with the following:

        val appDependencies = Seq(
          "com.github.twitter" % "bootstrap" % "2.0.2",
          "postgresql" % "postgresql" % "9.1-901-1.jdbc4"
        )


Then update the `Procfile` to override the default database settings when Heroku runs the app:

    web: target/start -Dhttp.port=${PORT} -DapplyEvolutions.default=true -Ddb.default.driver=org.postgresql.Driver -Ddb.default.url=$DATABASE_URL ${JAVA_OPTS}


Commit and verify your changes:

    git commit -am "Updates for PostgreSQL on Heroku"
    git diff upstream/java-heroku_update


Push your updates to Heroku:

    git push heroku master


View your app on the cloud:

    heroku open



Congratulations!
----------------

You've built a Play 2 app and deployed it on the cloud.  You've learned how to get started with Play 2, Ebean, CoffeeScript, Twitter Bootstrap, jQuery, RESTful JSON services, and Heroku.  Have fun as you continue to learn Play 2!

# Welcome to the Play2torial for Java!
====================================

This tutorial will help teach you how to build Java web applications with Play Framework 2.

Before you get started you will need to install [git](http://gitscm.org) and [Play 2](http://playframework.org).


Test that the `play` command works by running:

    play help


You should see something like:

           _            _ 
     _ __ | | __ _ _  _| |
    | '_ \| |/ _' | || |_|
    |  __/|_|\____|\__ (_)
    |_|            |__/ 
             
    play! 2.0, http://www.playframework.org
    
    Welcome to Play 2.0!


Alright!  You are ready to go!


Creating a Play App
-------------------

Create a new Play 2 application named "play2torial" by running:

    play create play2torial

When prompted select option 2 to create a Java application.

In the newly created `play2torial` directory create a new git repository by running:

    git init

Add the files to the git repo and commit them:

    git add .
    git commit -m init

Throughout this tutorial you will be able to check your progress against the official tutorial.  To do this add the official tutorial as a new git remote named `upstream` by running:

    git remote add upstream git@github.com:jamesward/play2torial.git

Now validate that your local project is correct by running:

    git diff upstream java-new_project

If you see differences then fix them locally or pull them from the upstream repo into your local repo:

    git pull upstream java-new_project:master


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

http://localhost:9000/


Routes
------

Play routes HTTP requests to a controller using the routes defined in the `conf/routes` file.  The routes file maps HTTP verbs and URL patterns to controller methods.  The route that matched the request you just made was defined in the "routes" file with:

    GET     /                           controllers.Application.index()


That means that when a HTTP GET request for the URL `/` comes in, it will be routed to the method named `index` on the `controllers.Application` class.

Add a new route to handle GET requests to `/foo` with a call to `controllers.Application.index()` by adding the following line to the routes file:

    GET     /foo                        controllers.Application.index()


Now try to open the following URL in your browser:

http://localhost:9000/foo


You should see the same welcome message as your did when you made the request to the `/` URL.

Commit and verify your changes:

    git commit -am "added new route"
    git diff upstream java-foo_route


Test a Route
------------
test
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
    git diff upstream java-test_route


Update a Controller
-------------------

The `app` directory is the main source for a Play app.  In that directory there is a directory named `controllers` containing a file named `Application.java` which is the `controllers.Application` class.

The `index()` method body looks like:

    return ok(index.render("Your new application is ready."));

Edit the `Application.java` file and change the `Your new application is ready.` string to `hello, world`.  Save the file and then reload the following URL in your browser:

http://localhost:9000


Notice that the header at the top of the page now reads `hello, world`.  Play recompiled the Java controller behind the scenes.  If you had made a change that could not be compiled you would see the compile error in your browser and in your console.

Commit and verify that your changes:

    git commit -am "updated controller"
    git diff upstream java-hello_controller


Test a Controller
-----------------

You can do functional tests against a controller by simply creating a new JUnit Test.  Create a new file named "test/ApplicationTest.java" file containing:

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

    git add ` 
    git commit -am "added Application controller test"
    git diff upstream java-test_controller


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

http://localhost:9000

Because the `test/ApplicationTest.java` test uses this updated template, update the test to account for the upper cased string:

    assertThat(contentAsString(result)).contains("HELLO, WORLD");

Run the tests:

    play test

Commit and verify that your changes:

    git commit -am "change the view"
    git diff upstream java-hello_view


Test a View
-----------

To test a template directly, create a new "test/IndexTest.java" file containing:

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
    git diff upstream java-test_view


Deploy your app on the Cloud with Heroku
----------------------------------------

Deploying your Play app on the cloud is easy with Heroku!  First create a new file in the root directory named `Procfile` that contains:

    web: target/start -Dhttp.port=${PORT} ${JAVA_OPTS}


Commit and verify your changes:

    git add Procfile
    git commit -m "add Procfile for Heroku"
    git diff java-heroku_create


That tells Heroku how to start the Play application.

Now install the Heroku Toolbelt:

http://toolbelt.heroku.com


Signup for a Heroku account:

http://heroku.com/signup


Login to Heroku from the command line:

    heroku login


Provision a new application on Heroku:

    heroku create --stack cedar


Now push this applicaiton to Heroku:

    git push heroku master


Open the application, now running on the cloud, in your browser:

    heroku open



Create a Model
--------------

Play 2 with Java uses EBean for RDBMS persistence.  To setup a data source edit the `conf/application.conf` file and uncomment these lines:

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


EBean will automatically create a new database evolution script for you in a file named `conf/evolutions/default/1.sql`.


Commit and verify your changes:

    git add conf/evolutions app/models/Task.java test/TaskTest.java
    git commit -am "added Task model, in-memory database config, and test"
    git diff upstream java-task_model



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

    POST    /tasks                      controllers.Application.addTask()


Now create a form in the `app/views/index.scala.html` template for adding new `Tasks`.  Replace the `@play20.welcome` line with:

      @message.toUpperCase
      
      @helper.form(action = routes.Application.addTask()) {
        <input name="contents"/>
        <input type="submit"/>
      }


Test out the new UI by loading the app in your browser.  You will be prompted to run the database evolutions.  Just click the `Apply this script now!` button.

http://localhost:9000/


Add a new `Task` and the browser should refresh the index page.

Commit and verify your changes:

    git commit -am "add new UI to create Tasks"
    git diff upstream java-task_add



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

http://localhost:9000/tasks

Verify that you see a Task (or Tasks) in JSON form.


Commit and verify your changes:

    git commit -am "get the tasks as JSON"
    git diff upstream java-task_json



Display the Tasks via CoffeeScript and jQuery
---------------------------------------------




Commit and verify your changes:

    git add 
    git commit -am ""
    git diff upstream java-task_coffeescript



Make the App pretty with Twitter Bootstrap
------------------------------------------


Commit and verify your changes:

    git add 
    git commit -am ""
    git diff upstream java-bootstrap



Add Form Validation
-------------------


Commit and verify your changes:

    git add 
    git commit -am ""
    git diff upstream java-validation



Update the App on Heroku
------------------------


Commit and verify your changes:

    git add 
    git commit -am ""
    git diff upstream java-heroku_update



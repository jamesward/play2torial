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
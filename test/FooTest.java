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
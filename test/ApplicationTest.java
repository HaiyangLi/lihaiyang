import static org.fest.assertions.Assertions.assertThat;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.GET;
import static play.test.Helpers.callAction;
import static play.test.Helpers.charset;
import static play.test.Helpers.contentAsString;
import static play.test.Helpers.contentType;
import static play.test.Helpers.status;

import org.junit.Test;

import controllers.Application;

import play.mvc.Result;
import play.test.FakeRequest;
import play.twirl.api.Content;

/**
*
* Simple (JUnit) tests that can call all parts of a play app.
* If you are interested in mocking a whole application, see the wiki for more details.
*
*/
public class ApplicationTest {

    @Test
    public void simpleCheck() {
        int a = 1 + 1;
        assertThat(a).isEqualTo(2);
    }

    @Test
    public void renderTemplate() {
    }
    @Test
    public void testIndex() {
      Result result = Application.index(0);
      assertThat(status(result)).isEqualTo(OK);
      assertThat(contentType(result)).isEqualTo("text/html");
      assertThat(charset(result)).isEqualTo("utf-8");
      assertThat(contentAsString(result)).contains("Welcome");
    }


}

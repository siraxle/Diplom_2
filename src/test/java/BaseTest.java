import io.restassured.RestAssured;
import org.junit.Before;

public class BaseTest {
    public static final String BASE_URL = "https://stellarburgers.nomoreparties.site/api";

    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URL;
    }

}

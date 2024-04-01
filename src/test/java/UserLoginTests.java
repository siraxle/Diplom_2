import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import org.junit.Test;

import static org.hamcrest.Matchers.*;

@Feature("User Login")
public class UserLoginTests extends BaseTest {
    private static final UserHelper USER_HELPER = new UserHelper();

    @Test
    @Description("Test successful user login")
    public void testSuccessfulLogin() {
        String email = USER_HELPER.generateUniqueEmail();
        String password = USER_HELPER.generatePassword();
        String name = USER_HELPER.generateName();
        User user = new User(email, password, name);
        Response creatingUserResponse = USER_HELPER.createUser(user);

        USER_HELPER.loginUser(email, password)
                .then()
                .statusCode(200)
                .body("success", is(true))
                .body("accessToken", not(emptyString()))
                .body("refreshToken", not(emptyString()));

        USER_HELPER.deleteUser(creatingUserResponse);

    }

    @Test
    @Description("Test user login with incorrect credentials")
    public void testLoginWithIncorrectCredentials() {
        String email = USER_HELPER.generateUniqueEmail();
        String password = USER_HELPER.generatePassword();
        String name = USER_HELPER.generateName();
        User user = new User(email, password, name);
        Response creatingUserResponse = USER_HELPER.createUser(user);
        String incorrectPassword = USER_HELPER.generatePassword();

        USER_HELPER.loginUser(email, incorrectPassword)
                .then()
                .statusCode(401)
                .body("success", is(false))
                .body("message", equalTo("email or password are incorrect"));

        USER_HELPER.deleteUser(creatingUserResponse);

    }


}

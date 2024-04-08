import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;

import static org.hamcrest.Matchers.*;

@DisplayName("User Login Tests")
public class UserLoginTests extends BaseTest {
    private static final UserHelper USER_HELPER = new UserHelper();

    @Test
    @Description("Test successful user login")
    @DisplayName("Successful user login")
    public void testSuccessfulLogin() {
        User user = USER_HELPER.createUserObject();
        Response creatingUserResponse = USER_HELPER.createUser(user);

        USER_HELPER.loginUser(user.getEmail(), user.getPassword())
                .then()
                .statusCode(200)
                .body("success", is(true))
                .body("accessToken", not(emptyString()))
                .body("refreshToken", not(emptyString()));

        USER_HELPER.deleteUser(creatingUserResponse);

    }

    @Test
    @Description("Test user login with incorrect credentials")
    @DisplayName("User login with incorrect credentials")
    public void testLoginWithIncorrectCredentials() {
        User user = USER_HELPER.createUserObject();
        Response creatingUserResponse = USER_HELPER.createUser(user);
        String incorrectPassword = USER_HELPER.generatePassword();

        USER_HELPER.loginUser(user.getEmail(), incorrectPassword)
                .then()
                .statusCode(401)
                .body("success", is(false))
                .body("message", equalTo("email or password are incorrect"));

        USER_HELPER.deleteUser(creatingUserResponse);

    }


}

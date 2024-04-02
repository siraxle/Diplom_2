import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;

import static org.hamcrest.Matchers.*;


@DisplayName("User Creation Tests")
public class UserCreationTests extends BaseTest {
    private static final UserHelper USER_HELPER = new UserHelper();

    @Test
    @Description("Create a unique user")
    @DisplayName("Create user")
    public void testCreateUniqueUser() {
        String email = USER_HELPER.generateUniqueEmail();
        String password = USER_HELPER.generatePassword();
        String name = USER_HELPER.generateName();
        User user = new User(email, password, name);
        Response response = USER_HELPER.createUser(user);
        response.then()
                .statusCode(200)
                .body("success", is(true))
                .body("user.email", equalTo(email))
                .body("user.name", equalTo(name))
                .body("accessToken", not(emptyString()))
                .body("refreshToken", not(emptyString()));
        USER_HELPER.deleteUser(response);
    }


    @Test
    @Description("Create a user who is already registered")
    @DisplayName("Creation Existing User")
    public void testCreateAlreadyRegisteredUser() {
        String email = USER_HELPER.generateUniqueEmail();
        String password = USER_HELPER.generatePassword();
        String name = USER_HELPER.generateName();
        User user = new User(email, password, name);
        Response responseCreateUser_1 = USER_HELPER.createUser(user);
        Response responseCreateUser_2 = USER_HELPER.createUser(user);

        responseCreateUser_2
                .then()
                .statusCode(403)
                .body("success", is(false))
                .body("message", equalTo("User already exists"));

        USER_HELPER.deleteUser(responseCreateUser_1);
    }

    @Test
    @Description("Create a user without filling in one of the required fields")
    @DisplayName("User creation without filling in a required field")
    public void testCreateUserMissingField() {
        String email = USER_HELPER.generateUniqueEmail();
        String password = USER_HELPER.generatePassword();
        User user = new User(email, password);
        USER_HELPER.createUser(user);
        Response response = USER_HELPER.createUser(user);
        response
                .then()
                .statusCode(403)
                .body("success", is(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }
}

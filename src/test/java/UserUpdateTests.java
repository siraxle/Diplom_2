import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

@Feature("User Update")
public class UserUpdateTests extends BaseTest{

    private static final UserHelper USER_HELPER = new UserHelper();

    @Test
    @Description("Test update user name with authorization")
    public void testUpdateUserNameWithAuthorization() {
        String email = USER_HELPER.generateUniqueEmail();
        String password = USER_HELPER.generatePassword();
        String name = USER_HELPER.generateName();
        User user = new User(email, password, name);
        Response responseCreateUser = USER_HELPER.createUser(user);
        Response loginResponse = USER_HELPER.loginUser(email, password);
        String accessToken = USER_HELPER.getUserAccessToken(loginResponse);

        USER_HELPER.updateUserName(accessToken, "newName")
                .then()
                .statusCode(200)
                .body("success", is(true))
                .body("user.name", equalTo("newName"));
        USER_HELPER.deleteUser(responseCreateUser);
    }

    @Test
    @Description("Test update user email with authorization")
    public void testUpdateUserLoginWithAuthorization() {
        String email = USER_HELPER.generateUniqueEmail();
        String password = USER_HELPER.generatePassword();
        String name = USER_HELPER.generateName();
        User user = new User(email, password, name);
        Response responseCreateUser = USER_HELPER.createUser(user);
        Response loginResponse = USER_HELPER.loginUser(email, password);
        String accessToken = USER_HELPER.getUserAccessToken(loginResponse);

        USER_HELPER.updateUserEmail(accessToken, "newe@email.ru")
                .then()
                .statusCode(200)
                .body("success", is(true))
                .body("user.email", equalTo("newe@email.ru"));
        USER_HELPER.deleteUser(responseCreateUser);
    }


    @Test
    @Description("Test user name update without authorization")
    public void testUserNameUpdateWithoutAuthorization() {
        String email = USER_HELPER.generateUniqueEmail();
        String password = USER_HELPER.generatePassword();
        String name = USER_HELPER.generateName();
        User user = new User(email, password, name);
        Response responseCreateUser = USER_HELPER.createUser(user);

        USER_HELPER.updateUserName("accessToken", "name")
                .then()
                .statusCode(401)
                .body("success", is(false))
                .body("message", equalTo("You should be authorised"));
        USER_HELPER.deleteUser(responseCreateUser);
    }

    @Test
    @Description("Test user deletion without authorization")
    public void testUserEmailUpdateWithoutAuthorization() {
        String email = USER_HELPER.generateUniqueEmail();
        String password = USER_HELPER.generatePassword();
        String name = USER_HELPER.generateName();
        User user = new User(email, password, name);
        Response responseCreateUser = USER_HELPER.createUser(user);

        USER_HELPER.updateUserEmail("accessToken", "newe@email.ru")
                .then()
                .statusCode(401)
                .body("success", is(false))
                .body("message", equalTo("You should be authorised"));
        USER_HELPER.deleteUser(responseCreateUser);
    }


}

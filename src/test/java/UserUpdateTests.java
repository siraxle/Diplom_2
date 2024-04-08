import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

@DisplayName("User Update Tests")
public class UserUpdateTests extends BaseTest{

    private static final UserHelper USER_HELPER = new UserHelper();

    @Test
    @Description("Test update user name with authorization")
    @DisplayName("Update user name with authorization")
    public void testUpdateUserNameWithAuthorization() {
        User user = USER_HELPER.createUserObject();
        Response responseCreateUser = USER_HELPER.createUser(user);
        Response loginResponse = USER_HELPER.loginUser(user.getEmail(), user.getPassword());
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
    @DisplayName("Update user email with authorization")
    public void testUpdateUserLoginWithAuthorization() {
        User user = USER_HELPER.createUserObject();
        Response responseCreateUser = USER_HELPER.createUser(user);
        Response loginResponse = USER_HELPER.loginUser(user.getEmail(), user.getPassword());
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
    @DisplayName("Update user name without authorization")
    public void testUserNameUpdateWithoutAuthorization() {
        User user = USER_HELPER.createUserObject();
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
    @DisplayName("Delete user without authorization")
    public void testUserEmailUpdateWithoutAuthorization() {
        User user = USER_HELPER.createUserObject();
        Response responseCreateUser = USER_HELPER.createUser(user);

        USER_HELPER.updateUserEmail("accessToken", "newe@email.ru")
                .then()
                .statusCode(401)
                .body("success", is(false))
                .body("message", equalTo("You should be authorised"));
        USER_HELPER.deleteUser(responseCreateUser);
    }


}

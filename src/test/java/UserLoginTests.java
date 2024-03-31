import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.Test;

import static org.hamcrest.Matchers.*;


@Epic("User Management")
@Feature("User Creation")
public class UserLoginTests extends BaseTest {
    private static final UserHelper USER_HELPER = new UserHelper();

    @Test
    @Description("Test successful user login")
    public void testSuccessfulLogin() {
        String email = USER_HELPER.generateUniqueEmail();
        String password = USER_HELPER.generatePassword();
        String name = USER_HELPER.generateName();
        User user = new User(email, password, name);
        USER_HELPER.createUser(user);

        USER_HELPER.loginUser(email, password)
                .then()
                .statusCode(200)
                .body("success", is(true))
                .body("accessToken", not(emptyString()))
                .body("refreshToken", not(emptyString()));

//      TODO USER_HELPER.deleteCourier(loginResponse.getId());

    }

    @Test
    @Description("Test user login with incorrect credentials")
    public void testLoginWithIncorrectCredentials() {
        String email = USER_HELPER.generateUniqueEmail();
        String password = USER_HELPER.generatePassword();
        String name = USER_HELPER.generateName();
        User user = new User(email, password, name);
        USER_HELPER.createUser(user);

        USER_HELPER.loginUser(email, password + "w")
                .then()
                .statusCode(401)
                .body("success", is(false))
                .body("message", equalTo("email or password are incorrect"));

//      TODO USER_HELPER.deleteCourier(loginResponse.getId());

    }


}

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.*;

//TODO добавитиь удаление тестовых данных по окончанию теста

@Epic("User Management")
@Feature("User Creation")
public class UserCreationTests extends BaseTest {

    private static final UserHelper USER_HELPER = new UserHelper();


    @Test
    @Description("Create a unique user")
    public void testCreateUniqueUser() {
        String email = USER_HELPER.generateUniqueEmail();
        String password = USER_HELPER.generatePassword();
        String name = USER_HELPER.generateName();
        User user = new User(email, password, name);
        Response response = USER_HELPER.createUser(user);
//        System.out.println(response.body());
        response.then()
                .statusCode(200)
                .body("success", is(true))
                .body("user.email", equalTo(email))
                .body("user.name", equalTo(name))
                .body("accessToken", not(emptyString()))
                .body("refreshToken", not(emptyString()));;
//        TODO USER_HELPER.deleteCourier(loginResponse.getId());
//        {"success":true,"user":{"email":"email_03975573@test.com","name":"name_1711703976918"},"accessToken":"Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjY2MDY4N2JiOWVkMjgwMDAxYjNkNDNkYSIsImlhdCI6MTcxMTcwMzk5NSwiZXhwIjoxNzExNzA1MTk1fQ.ODiIqQeKdCtWJR-FDravqhhD7RSpTUTu3qzwDS-24DA","refreshToken":"dbe7e4754f49919caa63af7d143c047038f73a9108803dac4fdf23ea51ab91b33a79100f75a030f5"}

    }


    @Test
    @Description("Create a user who is already registered")
    public void testCreateAlreadyRegisteredUser() {
        String email = USER_HELPER.generateUniqueEmail();
        String password = USER_HELPER.generatePassword();
        String name = USER_HELPER.generateName();
        User user = new User(email, password, name);
        USER_HELPER.createUser(user);
        Response response = USER_HELPER.createUser(user);

        response
                .then()
                .statusCode(403)
                .body("success", is(false))
                .body("message", equalTo("User already exists"));
        //        TODO USER_HELPER.deleteCourier(loginResponse.getId());
    }

    @Test
    @Description("Create a user without filling in one of the required fields")
    public void testCreateUserMissingField() {
        String email = USER_HELPER.generateUniqueEmail();
        String password = USER_HELPER.generatePassword();
//        String name = USER_HELPER.generateName();
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

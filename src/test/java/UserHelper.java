import io.qameta.allure.Step;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

public class UserHelper {
    private static final String BASE_URL = BaseTest.BASE_URL;
    private static final String USER_REGISTER_URN = "/auth/register";
    private static final String USER_LOGIN_URN = "/auth/login";

    private static final String USER_URN = "/auth/user";



    @Step("Generate Unique Email")
    public String generateUniqueEmail() {
        long currentTime = System.currentTimeMillis();
        String timestamp = String.valueOf(currentTime).substring(5);
        return "email_" + timestamp + "@test.com";
    }

    @Step("Generate Password")
    public String generatePassword() {
        Random random = new Random();
        return String.valueOf(1000 + random.nextInt(9000)) + "password";
    }

    @Step("Generate First Name")
    public String generateName() {
        return "name_" + System.currentTimeMillis();
    }

    @Step("Create user")
    public Response createUser(User user) {
        return given()
                .contentType("application/json")
                .body(user)
                .when()
                .post(BASE_URL + USER_REGISTER_URN);
    }

    @Step("Login")
    public Response loginUser(String email, String password) {
        Map<String, Object> body = new HashMap<>();
        body.put("email", email);
        body.put("password", password);
        return given()
                .contentType("application/json")
                .body(body)
                .when()
                .post(BASE_URL + USER_LOGIN_URN);
    }
    @Step("Getting User Access Token")
    private String getUserAccessToken(Response response) {
        return response.then()
                .statusCode(200)
                .extract()
                .path("accessToken");
    }

    @Step("Deleting User")
    public void deleteUser(Response response) {
        // Получаем авторизационный токен
        String accessToken = getUserAccessToken(response);

        // Отправляем запрос на удаление пользователя
        given()
                .contentType("application/json")
//                .header("authorization", "Bearer " + accessToken)
                .header("authorization", accessToken)
                .when()
                .delete(BASE_URL+ USER_URN)
                .then()
                .statusCode(202);
    }

}

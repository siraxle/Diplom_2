import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
@DisplayName("Order Creation Tests")
public class OrderCreationTests extends BaseTest {

    private static final UserHelper USER_HELPER = new UserHelper();
    private static final OrderHelper ORDER_HELPER = new OrderHelper();

    @Test
    @Description("Test order creation with ingredients and authorization")
    @DisplayName("Order creation with ingredients, with auth")
    public void testCreateOrderWithIngredientsAndAuthorization() {
        User user = USER_HELPER.createUserObject();
        Response responseCreateUser = USER_HELPER.createUser(user);
        Response loginResponse = USER_HELPER.loginUser(user.getEmail(), user.getPassword());
        String accessToken = USER_HELPER.getUserAccessToken(loginResponse);

        // Определяем ингредиенты для заказа
        List<String> ingredients = new ArrayList<>();
        ingredients.add("61c0c5a71d1f82001bdaaa76");
        ingredients.add("61c0c5a71d1f82001bdaaa77");

        // Отправляем запрос на создание заказа
        ORDER_HELPER.createOrder(accessToken, ingredients)
                .then()
                .statusCode(200)
                .body("success", is(true))
                .body("name", equalTo("Фалленианский минеральный бургер"))
                .body("order.number", notNullValue());

        USER_HELPER.deleteUser(responseCreateUser);
    }

    @Test
    @Description("Test order creation with ingredients and without authorization")
    @DisplayName("Order creation with ingredients, without auth")
    public void testCreateOrderWithIngredientsAndWithoutAuthorization() {
        // Определяем ингредиенты для заказа
        List<String> ingredients = new ArrayList<>();
        ingredients.add("61c0c5a71d1f82001bdaaa7a");
        ingredients.add("61c0c5a71d1f82001bdaaa77");

        // Отправляем запрос на создание заказа
        ORDER_HELPER.createOrder("accessToken", ingredients)
                .then()
                .statusCode(200)
                .body("success", is(true))
                .body("name", equalTo("Фалленианский астероидный бургер"))
                .body("order.number", notNullValue());
    }

    @Test
    @Description("Test order creation without ingredients and with authorization")
    @DisplayName("Order creation without ingredients, with auth")
    public void testCreateOrderWithoutIngredientsAndWithAuthorization() {
        User user = USER_HELPER.createUserObject();
        Response responseCreateUser = USER_HELPER.createUser(user);
        Response loginResponse = USER_HELPER.loginUser(user.getEmail(), user.getPassword());
        String accessToken = USER_HELPER.getUserAccessToken(loginResponse);

        // Определяем ингредиенты для заказа
        List<String> ingredients = new ArrayList<>();

        // Отправляем запрос на создание заказа
        ORDER_HELPER.createOrder(accessToken, ingredients)
                .then()
                .statusCode(400)
                .body("success", is(false))
                .body("message", equalTo("Ingredient ids must be provided"));

        USER_HELPER.deleteUser(responseCreateUser);
    }

    @Test
    @Description("Test order creation without ingredients and without authorization")
    @DisplayName("Order creation without ingredients, no auth")
    public void testCreateOrderWithoutIngredientsAndWithoutAuthorization() {
//      Определяем ингредиенты для заказа
        List<String> ingredients = new ArrayList<>();

        // Отправляем запрос на создание заказа
        ORDER_HELPER.createOrder("accessToken", ingredients)
                .then()
                .statusCode(400)
                .body("success", is(false))
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @Description("Test order creation with wrong cash ingredients and without authorization")
    @DisplayName("Order creation with invalid ingredients, no auth")
    public void testCreateOrderWithWrongIngredientsAndWithoutAuthorization() {
        // Определяем ингредиенты для заказа - неверные хеши
        List<String> ingredients = new ArrayList<>();
        ingredients.add("61c0c5a12d1f82001bdaaa7a");

        // Отправляем запрос на создание заказа
        ORDER_HELPER.createOrder("accessToken", ingredients)
                .then()
                .statusCode(400)
                .body("success", is(false))
                .body("message", equalTo("One or more ids provided are incorrect"));
    }

    @Test
    @Description("Test order creation with wrong cash ingredients and authorization")
    @DisplayName("Order creation with wrong ingredients and auth")
    public void testCreateOrderWithWrongIngredientsAndAuthorization() {
        User user = USER_HELPER.createUserObject();
        Response responseCreateUser = USER_HELPER.createUser(user);
        Response loginResponse = USER_HELPER.loginUser(user.getEmail(), user.getPassword());
        String accessToken = USER_HELPER.getUserAccessToken(loginResponse);

        // Определяем ингредиенты для заказа - неверные хеши
        List<String> ingredients = new ArrayList<>();
        ingredients.add("61c0c5a12d1f82001bdaaa76");
        ingredients.add("61c0c5a12d1f82001bdaaa77");

        // Отправляем запрос на создание заказа
        ORDER_HELPER.createOrder(accessToken, ingredients)
                .then()
                .statusCode(400)
                .body("success", is(false))
                .body("message", equalTo("One or more ids provided are incorrect"));

        USER_HELPER.deleteUser(responseCreateUser);
    }


}

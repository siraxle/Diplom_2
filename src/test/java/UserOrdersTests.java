import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

@DisplayName("User Orders Tests")
public class UserOrdersTests extends BaseTest {

    private static final UserHelper USER_HELPER = new UserHelper();
    private static final OrderHelper ORDER_HELPER = new OrderHelper();

    @Test
    @Description("Test getting orders for an authorized user")
    @DisplayName("Getting orders for an authorized user")
    public void testGetOrdersForAuthorizedUser() {
        String email = USER_HELPER.generateUniqueEmail();
        String password = USER_HELPER.generatePassword();
        String name = USER_HELPER.generateName();
        User user = new User(email, password, name);
        Response responseCreateUser = USER_HELPER.createUser(user);
        Response loginResponse = USER_HELPER.loginUser(email, password);
        String accessToken = USER_HELPER.getUserAccessToken(loginResponse);

        // Определяем ингредиенты для заказа и создаем заказ
        List<String> ingredients = new ArrayList<>();
        ingredients.add("61c0c5a71d1f82001bdaaa76");
        ingredients.add("61c0c5a71d1f82001bdaaa77");
        ORDER_HELPER.createOrder(accessToken, ingredients);

        // Отправляем запрос на получение заказов для авторизованного пользователя
        Response ordersResponse = ORDER_HELPER.getOrders(accessToken)
                .then()
                .statusCode(200)
                .body("success", is(true))
                .extract().response();
        //Получаем список заказов и проверяем не пустой ли он
        List<Object> orders = ordersResponse.jsonPath().getList("orders");
        Assert.assertTrue(orders != null && orders.size() > 0);

        USER_HELPER.deleteUser(responseCreateUser);
    }

    @Test
    @Description("Test getting orders for an unauthorized user")
    @DisplayName("Getting orders for an unauthorized user")
    public void testGetOrdersForUnauthorizedUser() {
        // Отправляем запрос на получение заказов для неавторизованного пользователя
        Response ordersResponse = ORDER_HELPER.getOrders("accessToken")
                .then()
                .statusCode(401)
                .body("success", is(false))
                .extract().response();

        // Проверяем, что в ответе присутствует сообщение о необходимости авторизации
        ordersResponse.then().body("message", equalTo("You should be authorised"));
    }

}

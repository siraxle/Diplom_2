import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class OrderHelper {
    private static final String BASE_URL = BaseTest.BASE_URL;
    private static final String ORDER_URL = "/orders";

    @Step("Creating Order")
    public Response createOrder(String accessToken, List<String> ingredients) {
        Map<String, List<String>> requestBody = new HashMap<>();
        requestBody.put("ingredients", ingredients);
        return given()
                .contentType(ContentType.JSON)
                .header("authorization", accessToken)
                .body(requestBody)
                .when()
                .post(BASE_URL + ORDER_URL);
    }

    @Step("Get Orders")
    public Response getOrders(String accessToken) {
        return given()
                .contentType(ContentType.JSON)
                .header("authorization", accessToken)
                .when()
                .get(BASE_URL + ORDER_URL);
    }


}

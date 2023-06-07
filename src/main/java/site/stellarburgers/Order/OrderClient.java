package site.stellarburgers.Order;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import site.stellarburgers.RestClient;

import static io.restassured.RestAssured.given;

public class OrderClient extends RestClient {
    private static final String GET_ORDER_LIST = "api/orders";
    private static final String CREATE_ORDER = "api/orders";

    @Step("Создание заказа")
    public ValidatableResponse createOrder(Order order, String accessToken) {
        return given()
                .spec(getBaseSpec())
                .header("Authorization", "Bearer" + accessToken)
                .body(order)
                .when()
                .post(CREATE_ORDER)
                .then();
    }

    @Step("Получить список заказов авторизованного пользователя")
    public ValidatableResponse getOrderList(String accessToken) {
        return given()
                .spec(getBaseSpec())
                .header("Authorization", "Bearer" + accessToken)
                .when()
                .get(GET_ORDER_LIST)
                .then();
    }
}

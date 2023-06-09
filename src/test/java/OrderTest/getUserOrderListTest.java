package OrderTest;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import site.stellarburgers.Order.Order;
import site.stellarburgers.Order.OrderClient;
import site.stellarburgers.Order.OrderGenerator;
import site.stellarburgers.User.User;
import site.stellarburgers.User.UserClient;
import site.stellarburgers.User.UserGenerator;

import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

public class getUserOrderListTest {
    private User user;
    private UserClient userClient;
    private Order order;
    private String accessToken;
    private OrderClient orderClient;

    @Before
    public void setUp() {
        userClient = new UserClient();
        user = UserGenerator.getUserData();
        order = OrderGenerator.getOrderData();
        orderClient = new OrderClient();
    }

    @Test
    @DisplayName("Получение заказов авторизованного пользователя")
    @Description("Код ответа 200, поле owner отсутствует")
    public void getUserOrderWithAuth() {
        ValidatableResponse create = userClient.create(user);
        accessToken = create.extract().path("accessToken");
        orderClient.createOrder(order, accessToken);
        ValidatableResponse getOrderList = orderClient.getOrderList(accessToken);
        getOrderList.statusCode(200)
                .and()
                .assertThat().body("owner", nullValue());
    }

    @Test
    @DisplayName("Невозможно получить список заказов пользователя без авторизации")
    @Description("Код ответа 401, ошибка в теле ответа You should be authorised")
    public void getUserOrderWithoutAuth() {
        ValidatableResponse create = userClient.create(user);
        accessToken = create.extract().path("accessToken");
        ValidatableResponse orderCreate = orderClient.createOrder(order, accessToken);
        orderCreate.assertThat().body("order", notNullValue());
        ValidatableResponse getOrderList = orderClient.getOrderList(null);
        getOrderList.statusCode(401)
                .and()
                .log().all().body("message", Matchers.equalTo("You should be authorised"));
    }

    @After
    public void cleanUp() {
        userClient.delete(accessToken);
    }
}

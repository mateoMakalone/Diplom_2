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
import site.stellarburgers.User.UserCredentials;
import site.stellarburgers.User.UserGenerator;

public class OrderCreateTest {
    private User user;
    private UserClient userClient;
    private String accessToken;
    private Order order;
    private OrderClient orderClient;
    private Order emptyOrder;
    private Order incorrectOrder;

    @Before
    public void setUp() {
        user = UserGenerator.getUserData();
        userClient = new UserClient();
        order = OrderGenerator.getOrderData();
        orderClient = new OrderClient();
        emptyOrder = OrderGenerator.emptyOrderList();
        incorrectOrder = OrderGenerator.orderWithIncorrectHash();
    }

    @Test
    @DisplayName("Создание заказа с авторизацией")
    @Description("Код ответа 200, тело ответа success true")
    public void createOrderWithAuth() {
        ValidatableResponse createUser = userClient.create(user);
        accessToken = createUser.extract().path("accessToken");
        userClient.login(UserCredentials.from(user));
        ValidatableResponse createOrder = orderClient.createOrder(order, accessToken);
        createOrder.statusCode(200)
                .and()
                .log().all().body("success", Matchers.equalTo(true));
    }

    @Test
    @DisplayName("Создание заказа без авторизации")
    @Description("Код ответа 200, тело ответа success true")
    public void createOrderWithoutAuth() {
        ValidatableResponse createOrder = orderClient.createOrder(order, null);
        createOrder.statusCode(200)
                .and()
                .log().all().body("success", Matchers.equalTo(true));
    }

    @Test
    @DisplayName("Создание заказа с пустым списком ингредиентов")
    @Description("Код ответа 400, ошибка в теле ответа Ingredient ids must be provided")
    public void createOrderWithEmptyIngredientList() {
        ValidatableResponse createUser = userClient.create(user);
        accessToken = createUser.extract().path("accessToken");
        userClient.login(UserCredentials.from(user));
        ValidatableResponse createOrder = orderClient.createOrder(emptyOrder, accessToken);
        createOrder.statusCode(400)
                .and()
                .log().all().body("message", Matchers.equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Создание заказа с некорректным хэшем ингредиентов")
    @Description("Кот ответа 500")
    public void createOrderWithIncorrectIngredientHash() {
        ValidatableResponse createOrder = orderClient.createOrder(incorrectOrder, null);
        createOrder.statusCode(500);
    }

    @After
    public void cleanUp() {
        userClient.delete(accessToken);
    }
}

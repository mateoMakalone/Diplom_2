package UserTest;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import site.stellarburgers.User.User;
import site.stellarburgers.User.UserClient;
import site.stellarburgers.User.UserCredentials;
import site.stellarburgers.User.UserGenerator;

public class CreateLoginUserTest {
    String accessToken;
    private UserClient userClient;
    private User user;

    @Before
    public void setUp() {
        userClient = new UserClient();
        user = UserGenerator.getUserData();
    }

    @Test
    @DisplayName("Можно создать уникального пользователя")
    @Description("Код ответа: 200, в теле ответа присутсвует true")
    public void userCanBeCreated() {
        ValidatableResponse createResponse = userClient.create(user);
        accessToken = createResponse.extract().path("accessToken");
        createResponse.statusCode(200)
                .and()
                .log().all().body("success", Matchers.equalTo(true));
    }

    @Test
    @DisplayName("Логин под существущим пользователем")
    @Description("Код ответа: 200")
    public void existingUserCanLogin() {
        ValidatableResponse createResponse = userClient.create(user);
        accessToken = createResponse.extract().path("accessToken");
        ValidatableResponse loginResponse = userClient.login(UserCredentials.from(user));
        loginResponse.statusCode(200)
                .and()
                .log().all().body("success", Matchers.equalTo(true));
    }

    @Test
    @DisplayName("Невозможно создать уже существующего пользователя")
    @Description("Код ответа: 403, тело ответа: User already exists")
    public void createExistingUser() {
        ValidatableResponse createResponse = userClient.create(user);
        accessToken = createResponse.extract().path("accessToken");
        ValidatableResponse createClone = userClient.create(user);
        createClone.statusCode(403)
                .and()
                .log().all().body("message", Matchers.equalTo("User already exists"));
    }

    @After
    public void cleanUp() {
        userClient.delete(accessToken);
    }
}

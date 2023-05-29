package UserTest;

import org.junit.After;
import site.stellarburgers.User.User;
import site.stellarburgers.User.UserClient;
import site.stellarburgers.User.UserCredentials;
import site.stellarburgers.User.UserGenerator;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

public class CreateLoginUserTest {
    private UserClient userClient;
    private User user;
    String accessToken;
    @Before
    public void setUp(){
        userClient = new UserClient();
        user = UserGenerator.getUserData();
    }
    @Test
    @DisplayName("Можно создать уникального пользователя")
    @Description("Код ответа: 200, в теле ответа присутсвует true")
    public void userCanBeCreated(){
        ValidatableResponse createResponse = userClient.create(user);
        accessToken = createResponse.extract().path("accessToken");
        createResponse.log().all().body("success", Matchers.equalTo(true))
                .and()
                .statusCode(200);
    }
    @Test
    @DisplayName("Логин под существущим пользователем")
    @Description("Код ответа: 200")
    public void existingUserCanLogin(){
        ValidatableResponse createResponse = userClient.create(user);
        createResponse.statusCode(200);
        accessToken = createResponse.extract().path("accessToken");
        ValidatableResponse loginResponse = userClient.login(UserCredentials.from(user));
        loginResponse.log().all().body("success", Matchers.equalTo(true))
                .and()
                .statusCode(200);
    }
    @Test
    @DisplayName("Невозможно создать уже существующего пользователя")
    @Description("Код ответа: 403, тело ответа: User already exists")
    public void createExistingUser(){
        ValidatableResponse createResponse = userClient.create(user);
        createResponse.statusCode(200);
        accessToken = createResponse.extract().path("accessToken");
        ValidatableResponse createClone = userClient.create(user);
        createClone.log().all().body("message", Matchers.equalTo("User already exists"))
                .and()
                .statusCode(403);
    }
    @After
    public void cleanUp(){
        userClient.delete(accessToken);
    }
}

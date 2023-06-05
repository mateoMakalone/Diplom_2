package UserTest;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import site.stellarburgers.User.User;
import site.stellarburgers.User.UserClient;
import site.stellarburgers.User.UserCredentials;
import site.stellarburgers.User.UserGenerator;

@RunWith(Parameterized.class)
public class ChangeUserDataTest {
    private static User changedUser;
    private UserClient userClient;
    private User user;
    private String accessToken;

    public ChangeUserDataTest(User changedUser) {
        ChangeUserDataTest.changedUser = changedUser;
    }

    @Parameterized.Parameters(name = "{index} : UserChangedData = {0}")
    public static Object[][] getData() {
        return new Object[][]{
                {changedUser = UserGenerator.updateUserName()},
                {changedUser = UserGenerator.updateUserEmail()},
                {changedUser = UserGenerator.updateUserPassword()}
        };
    }

    @Before
    public void setUp() {
        userClient = new UserClient();
        user = UserGenerator.getUserData();
    }

    @Test
    @DisplayName("Изменение данных пользователя с авторизацией")
    @Description("Код ответа 200, тело ответа success true")
    public void changeUserDataWithAuth() {
        ValidatableResponse create = userClient.create(user);
        accessToken = create.extract().path("accessToken");
        userClient.login(UserCredentials.from(user));
        ValidatableResponse renameUser = userClient.changeUserData(accessToken, changedUser);
        renameUser.statusCode(200)
                .and()
                .log().all().body("success", Matchers.equalTo(true));
    }

    @Test
    @DisplayName("Изменение данных пользователя без авторизации")
    @Description("Код ответа 401, тело отвера success false")
    public void changeUserDataWithoutAuth() {
        ValidatableResponse create = userClient.create(user);
        accessToken = create.extract().path("accessToken");
        userClient.login(UserCredentials.from(user));
        ValidatableResponse renameUser = userClient.changeUserData(null, changedUser);
        renameUser.statusCode(401)
                .and()
                .log().all().body("success", Matchers.equalTo(false));
    }

    @After
    public void cleanUp() {
        userClient.delete(accessToken);
    }

}

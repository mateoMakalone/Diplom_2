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
    private UserClient userClient;
    private User user;
    private static User changedUser;
    private String accessToken;
    @Before
    public void setUp(){
        userClient = new UserClient();
        user = UserGenerator.getUserData();
    }
    public ChangeUserDataTest(User changedUser){
        this.changedUser = changedUser;
    }
    @Parameterized.Parameters
    public static Object[][] getData(){
        return new Object[][] {
                {changedUser = UserGenerator.updateUserName()},
                {changedUser = UserGenerator.updateUserEmail()},
                {changedUser = UserGenerator.updateUserPassword()}
        };
    }
    @Test
    @DisplayName("Изменение данных пользователя с авторизацией")
    @Description("Код ответа 200, тело ответа success true")
    public void changeUserDataWithAuth(){
        ValidatableResponse create = userClient.create(user);
        create.statusCode(200);
        accessToken = create.extract().path("accessToken");
        ValidatableResponse login = userClient.login(UserCredentials.from(user));
        login.statusCode(200);
        ValidatableResponse renameUser = userClient.changeUserData(accessToken,changedUser);
        renameUser.log().all().body("success", Matchers.equalTo(true))
                .and()
                .statusCode(200);
    }
    @Test
    @DisplayName("Изменение данных пользователя без авторизации")
    @Description("Код ответа 401, тело отвера success false")
    public void changeUserDataWithoutAuth(){
        ValidatableResponse create = userClient.create(user);
        create.log().all();
        accessToken = create.extract().path("accessToken");
        ValidatableResponse login = userClient.login(UserCredentials.from(user));
        login.statusCode(200);
        ValidatableResponse renameUser = userClient.changeUserData(null,changedUser);
        renameUser.log().all().body("success", Matchers.equalTo(false))
                .and()
                .statusCode(401);
    }
    @After
    public void cleanUp(){
        userClient.delete(accessToken);
    }

}

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

@RunWith(Parameterized.class)
public class LoginWithIncorrectDataTest {
    private UserClient userClient;
    private static User user;
    private User correctUser;
    private String accessToken;
    @Before
    public void setUp(){
        userClient = new UserClient();
        correctUser = new User("BorisTheBlade@yandex.ru", "TheBullet-Dodger", "Boris");
    }
    public LoginWithIncorrectDataTest(User user){
        this.user = user;
    }
    @Parameterized.Parameters
    public static Object[][] getData(){
            return new Object[][]{
                    {user = new User("BorisTheBlad@yandex.ru", "TheBullet-Dodger", "Boris")},
                    {user = new User("BorisTheBlade@yandex.ru", "TheBulletDodger", "Boris")}
            };
    }
    @Test
    @DisplayName("Пользователь не может авторизоваться с некорректными данными")
    @Description("Код ответа 401, тело ответа email or password are incorrect")
    public void userCantLoginWithIncorrectData(){
        ValidatableResponse createResponse = userClient.create(correctUser);
        createResponse.statusCode(200);
        accessToken = createResponse.extract().path("accessToken");
        ValidatableResponse loginResponse = userClient.login(UserCredentials.from(user));
        loginResponse.log().all().body("message", Matchers.equalTo("email or password are incorrect"))
                .and()
                .statusCode(401);
    }

    @After
    public void cleanUp(){
    userClient.delete(accessToken);
    }
}

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
    private static User user;
    private UserClient userClient;
    private User correctUser;
    private String accessToken;

    public LoginWithIncorrectDataTest(User user) {
        LoginWithIncorrectDataTest.user = user;
    }

    @Parameterized.Parameters(name = "{index} : incorrectFieldUser = {0}")
    public static Object[][] getData() {
        return new Object[][]{
                {user = new User("BorisTheBlad@yandex.ru", "TheBullet-Dodger", "Boris")},
                {user = new User("BorisTheBlade@yandex.ru", "TheBulletDodger", "Boris")}
        };
    }

    @Before
    public void setUp() {
        userClient = new UserClient();
        correctUser = new User("BorisTheBlade@yandex.ru", "TheBullet-Dodger", "Boris");
    }

    @Test
    @DisplayName("Пользователь не может авторизоваться с некорректными данными")
    @Description("Код ответа 401, тело ответа email or password are incorrect")
    public void userCantLoginWithIncorrectData() {
        ValidatableResponse createResponse = userClient.create(correctUser);
        accessToken = createResponse.extract().path("accessToken");
        ValidatableResponse loginResponse = userClient.login(UserCredentials.from(user));
        loginResponse.statusCode(401)
                .and()
                .log().all().body("message", Matchers.equalTo("email or password are incorrect"));
    }

    @After
    public void cleanUp() {
        userClient.delete(accessToken);
    }
}

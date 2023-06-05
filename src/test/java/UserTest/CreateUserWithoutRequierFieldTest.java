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
import site.stellarburgers.User.UserGenerator;

@RunWith(Parameterized.class)
public class CreateUserWithoutRequierFieldTest {

    private static User user;
    private UserClient userClient;
    private String accessToken;

    public CreateUserWithoutRequierFieldTest(User user) {
        CreateUserWithoutRequierFieldTest.user = user;
    }

    @Parameterized.Parameters(name = "{index} : User = {0}")
    public static Object[][] getData() {
        return new Object[][]{
                {user = UserGenerator.getUserWithoutEmail()},
                {user = UserGenerator.getUserWithoutPassword()},
                {user = UserGenerator.getUserWithoutName()},
        };
    }

    @Before
    public void setUp() {
        user = new User();
        userClient = new UserClient();
    }

    @Test
    @DisplayName("Невозможно создать пользователя без обязательных полей")
    @Description("Код ответа: 403")
    public void createUserWithoutRequierField() {
        ValidatableResponse createResponse = userClient.create(user);
        accessToken = createResponse.extract().path("accessToken");
        createResponse.statusCode(403)
                .and()
                .log().all().body("success", Matchers.equalTo(false));
    }

    @After
    public void cleanUp() {
        userClient.delete(accessToken);
    }
}

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
import site.stellarburgers.User.UserGenerator;

public class ChangeUserEmailTest {
    private User user;
    private User sameEmailUser;
    private UserClient userClient;
    private String accessToken;
    private String accessToken2;
    private User existingUser;

    @Before
    public void setUp() {
        userClient = new UserClient();
        user = UserGenerator.getUserData();
        sameEmailUser = UserGenerator.userWithSameEmail();
        existingUser = new User("iamacopy@ya.ya", "qwerty123", "Tony");
    }

    @Test
    @DisplayName("Невозможно смемнить эмейла пользователя на уже существующий")
    @Description("Код ответа 403, тело ответа User with such email already exists")
    public void changeEmailOnExistingAddressReturnFalse() {
        ValidatableResponse start = userClient.create(existingUser);
        accessToken2 = start.extract().path("accessToken");
        ValidatableResponse create = userClient.create(user);
        accessToken = create.extract().path("accessToken");
        ValidatableResponse renameUser = userClient.changeUserData(accessToken, sameEmailUser);
        renameUser.statusCode(403)
                .and()
                .log().all().body("message", Matchers.equalTo("User with such email already exists"));
    }

    @After
    public void cleanUp() {
        userClient.delete(accessToken);
        userClient.delete(accessToken2);
    }
}

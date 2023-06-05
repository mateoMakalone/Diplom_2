package site.stellarburgers.User;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import site.stellarburgers.RestClient;

import static io.restassured.RestAssured.given;

public class UserClient extends RestClient {
    private static final String USER_CREATE = "api/auth/register";
    private static final String USER_LOGIN = "api/auth/login";
    private static final String USER_LOGOUT = "api/auth/logout";
    private static final String USER_DELETE = "api/auth/user";
    private static final String USER_DATA = "api/auth/user";

    @Step("Создание пользователя")
    public ValidatableResponse create(User user) {
        return given()
                .spec(getBaseSpec())
                .body(user)
                .when()
                .post(USER_CREATE)
                .then();
    }

    @Step("Логин пользователя")
    public ValidatableResponse login(UserCredentials credentials) {
        return given()
                .spec(getBaseSpec())
                .body(credentials)
                .when()
                .post(USER_LOGIN)
                .then();
    }

    @Step("Выход из личного кабинета")
    public ValidatableResponse logout(RefreshToken refreshToken) {
        return given()
                .spec(getBaseSpec())
                .body(refreshToken)
                .when()
                .post(USER_LOGOUT)
                .then();
    }

    @Step("Получить данные пользователя")
    public ValidatableResponse getUserData(String accessToken) {
        return given()
                .spec(getBaseSpec())
                .header("Authorization", "Bearer" + accessToken)
                .when()
                .get(USER_DATA)
                .then();
    }

    @Step("Изменение данных пользователя")
    public ValidatableResponse changeUserData(String accessToken, User user) {
        return given()
                .spec(getBaseSpec())
                .header("Authorization", "Bearer" + accessToken)
                .body(user)
                .when()
                .patch(USER_DATA)
                .then();
    }

    @Step("Удаление пользователя")
    public ValidatableResponse delete(String accessToken) {
        return given()
                .spec(getBaseSpec())
                .header("Authorization", "Bearer" + accessToken)
                .when()
                .delete(USER_DELETE)
                .then();
    }
}

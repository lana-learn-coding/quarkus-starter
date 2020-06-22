package com.example;

import com.example.model.User;
import io.quarkus.test.junit.QuarkusTest;
import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
public class UserResourceTest {
    private static final String api = "/api/v1/users";
    private static User user;
    private static User updatedUser;

    @BeforeAll
    public static void before() {
        updatedUser = new User("updated Test", "updatedTest@email.domain");
        user = new User("test", "test@email.domain");
        user = UserEmbeddedDB.getInstance().save(user).orElseThrow();
    }

    @Test
    public void getAllUser_returnOk() {
        given()
            .when().get(api)
            .then()
            .statusCode(200);
    }

    @Test
    public void getUser_notExist_return404() {
        given()
            .when().get(api + "/fakeIdThatShouldNotExist")
            .then()
            .statusCode(404);
    }

    @Test
    public void updateUser_notExist_return404() {
        given()
            .when()
            .header(HttpHeaders.CONTENT_TYPE, "application/json")
            .body(updatedUser)
            .put(api + "/fakeIdThatShouldNotExist")
            .then()
            .statusCode(404);
    }

    @Test
    public void deleteUser_notExist_returnOk() {
        given()
            .when().delete(api + "/fakeIdThatShouldNotExist")
            .then()
            .statusCode(200);
    }

    @Test
    public void createUser_returnOk() {
        given()
            .when()
            .header(HttpHeaders.CONTENT_TYPE, "application/json")
            .body(user)
            .post(api)
            .then()
            .statusCode(201);
    }

    //  The body test make use of rest-assured
    @Test
    @Order(1)
    public void updateUser_exist_returnOk() {
        given()
            .when()
            .header(HttpHeaders.CONTENT_TYPE, "application/json")
            .body(updatedUser)
            .put(api + "/" + user.getId())
            .then()
            .body("name", equalTo(updatedUser.getName()))
            .statusCode(200);
    }

    @Test
    @Order(2)
    public void getUser_exist_returnOk() {
        assertNotNull(user.getId());
        given()
            .when().get(api + "/" + user.getId())
            .then()
            .statusCode(200);
    }

    @Test
    @Order(3)
    public void deleteUser_exist_returnOk() {
        assertNotNull(user.getId());
        given()
            .when().delete(api + "/" + user.getId())
            .then()
            .statusCode(200);
    }

}
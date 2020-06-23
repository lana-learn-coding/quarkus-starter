package com.example;

import com.example.model.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.Response;
import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.*;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserRouteTest {
    private static final String api = "/api/reactive/v1/users";
    private static User user;
    private static User updatedUser;

    @BeforeAll
    public static void before() {
        updatedUser = new User("updated Test", "updatedTest@email.domain");
        user = new User("test", "test@email.domain");
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
            .when().get(api + "/" + UUID.randomUUID())
            .then()
            .statusCode(404);
    }

    @Test
    public void updateUser_notExist_return404() {
        given()
            .when()
            .header(HttpHeaders.CONTENT_TYPE, "application/json")
            .body(updatedUser)
            .put(api + "/" + UUID.randomUUID())
            .then()
            .statusCode(404);
    }

    @Test
    public void deleteUser_notExist_returnOk() {
        // As the user use UUID as id, if we dont give the server
        // the correct uuid format, then it will reject with 404
        // without even run the
        given()
            .when().delete(api + "/" + UUID.randomUUID())
            .then()
            .statusCode(200);
    }

    @Test
    @Order(1)
    public void createUser_returnOk() throws JsonProcessingException {
        Response response = given()
            .when()
            .header(HttpHeaders.CONTENT_TYPE, "application/json")
            .body(user)
            .post(api);
        response.then().statusCode(201);

        String body = response.getBody().print();
        ObjectMapper mapper = new ObjectMapper();
        user = mapper.readValue(body, User.class);
    }

    //  The body test make use of rest-assured
    @Test
    @Order(2)
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
    @Order(3)
    public void getUser_exist_returnOk() {
        assertNotNull(user.getId());
        given()
            .when().get(api + "/" + user.getId())
            .then()
            .statusCode(200);
    }

    @Test
    @Order(4)
    public void deleteUser_exist_returnOk() {
        assertNotNull(user.getId());
        given()
            .when().delete(api + "/" + user.getId())
            .then()
            .statusCode(200);
    }

}
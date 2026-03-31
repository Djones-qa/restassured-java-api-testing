package tests;

import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.annotations.DataProvider;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class UsersTest extends BaseTest {

    @Test(description = "GET all users returns 200 and 10 records")
    public void testGetAllUsers() {
        given()
            .contentType("application/json")
        .when()
            .get("/users")
        .then()
            .statusCode(200)
            .body("size()", equalTo(10));
    }

    @Test(description = "GET single user returns correct data")
    public void testGetSingleUser() {
        given()
            .contentType("application/json")
        .when()
            .get("/users/1")
        .then()
            .statusCode(200)
            .body("id", equalTo(1))
            .body("name", notNullValue())
            .body("email", notNullValue())
            .body("username", notNullValue());
    }

    @Test(description = "GET user validates schema fields")
    public void testUserSchemaValidation() {
        given()
            .contentType("application/json")
        .when()
            .get("/users/1")
        .then()
            .statusCode(200)
            .body("containsKey('id')", is(true))
            .body("containsKey('name')", is(true))
            .body("containsKey('email')", is(true))
            .body("containsKey('phone')", is(true))
            .body("containsKey('website')", is(true));
    }

    @Test(description = "GET invalid user returns 404")
    public void testInvalidUserReturns404() {
        given()
            .contentType("application/json")
        .when()
            .get("/users/9999")
        .then()
            .statusCode(404);
    }

    @Test(description = "GET user response time under 2 seconds")
    public void testResponseTime() {
        given()
            .contentType("application/json")
        .when()
            .get("/users")
        .then()
            .statusCode(200)
            .time(lessThan(2000L));
    }

    @DataProvider(name = "userIds")
    public Object[][] userIds() {
        return new Object[][] {{1}, {2}, {3}, {4}, {5}};
    }

    @Test(dataProvider = "userIds", description = "GET multiple users data driven")
    public void testMultipleUsers(int userId) {
        given()
            .contentType("application/json")
        .when()
            .get("/users/" + userId)
        .then()
            .statusCode(200)
            .body("id", equalTo(userId));
    }
}

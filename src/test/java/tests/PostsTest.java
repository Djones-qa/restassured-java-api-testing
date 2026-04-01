package tests;

import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.ConfigReader;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class PostsTest extends BaseTest {

    @Test(description = "GET all posts returns correct count")
    public void testGetAllPosts() {
        given()
            .contentType(ConfigReader.getContentType())
        .when()
            .get("/posts")
        .then()
            .statusCode(200)
            .body("size()", equalTo(ConfigReader.getTotalPosts()));
    }

    @Test(description = "GET single post returns correct data")
    public void testGetSinglePost() {
        given()
            .contentType(ConfigReader.getContentType())
        .when()
            .get("/posts/" + ConfigReader.getTestPostId())
        .then()
            .statusCode(200)
            .body("id", equalTo(ConfigReader.getTestPostId()))
            .body("title", notNullValue())
            .body("body", notNullValue())
            .body("userId", equalTo(ConfigReader.getTestUserId()));
    }

    @Test(description = "POST create new post returns 201")
    public void testCreatePost() {
        Map<String, Object> payload = new HashMap<>();
        payload.put("title", "RestAssured Test Post");
        payload.put("body", "Created by RestAssured Java framework");
        payload.put("userId", ConfigReader.getTestUserId());

        given()
            .contentType(ConfigReader.getContentType())
            .body(payload)
        .when()
            .post("/posts")
        .then()
            .statusCode(201)
            .body("title", equalTo("RestAssured Test Post"))
            .body("userId", equalTo(ConfigReader.getTestUserId()))
            .body("id", notNullValue());
    }

    @Test(description = "PUT update post returns 200")
    public void testUpdatePost() {
        Map<String, Object> payload = new HashMap<>();
        payload.put("id", ConfigReader.getTestPostId());
        payload.put("title", "Updated Title");
        payload.put("body", "Updated body content");
        payload.put("userId", ConfigReader.getTestUserId());

        given()
            .contentType(ConfigReader.getContentType())
            .body(payload)
        .when()
            .put("/posts/" + ConfigReader.getTestPostId())
        .then()
            .statusCode(200)
            .body("title", equalTo("Updated Title"));
    }

    @Test(description = "PATCH partially update post returns 200")
    public void testPatchPost() {
        Map<String, Object> payload = new HashMap<>();
        payload.put("title", "Patched Title");

        given()
            .contentType(ConfigReader.getContentType())
            .body(payload)
        .when()
            .patch("/posts/" + ConfigReader.getTestPostId())
        .then()
            .statusCode(200)
            .body("title", equalTo("Patched Title"));
    }

    @Test(description = "DELETE post returns 200")
    public void testDeletePost() {
        given()
            .contentType(ConfigReader.getContentType())
        .when()
            .delete("/posts/" + ConfigReader.getTestPostId())
        .then()
            .statusCode(200);
    }

    @Test(description = "GET posts filtered by userId")
    public void testGetPostsByUser() {
        given()
            .contentType(ConfigReader.getContentType())
            .queryParam("userId", ConfigReader.getTestUserId())
        .when()
            .get("/posts")
        .then()
            .statusCode(200)
            .body("size()", greaterThan(0))
            .body("userId", everyItem(equalTo(ConfigReader.getTestUserId())));
    }

    @Test(description = "GET comments for a post")
    public void testGetCommentsForPost() {
        given()
            .contentType(ConfigReader.getContentType())
        .when()
            .get("/posts/" + ConfigReader.getTestPostId() + "/comments")
        .then()
            .statusCode(200)
            .body("size()", greaterThan(0))
            .body("email", everyItem(notNullValue()));
    }

    @Test(description = "Chained request - create post then verify")
    public void testChainedRequest() {
        Map<String, Object> payload = new HashMap<>();
        payload.put("title", "Chained Test");
        payload.put("body", "Testing chained requests");
        payload.put("userId", ConfigReader.getTestUserId());

        Response createResponse = given()
            .contentType(ConfigReader.getContentType())
            .body(payload)
        .when()
            .post("/posts")
        .then()
            .statusCode(201)
            .extract().response();

        String title = createResponse.jsonPath().getString("title");
        Assert.assertEquals(title, "Chained Test", "Title should match");
        Assert.assertNotNull(createResponse.jsonPath().getString("id"), "ID should not be null");
    }
}

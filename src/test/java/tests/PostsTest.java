package tests;

import io.restassured.response.Response;
import models.Post;
import models.Comment;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.ConfigReader;
import utils.LogUtils;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class PostsTest extends BaseTest {

    @Test(description = "GET all posts returns correct count")
    public void testGetAllPosts() {
        LogUtils.startTest("testGetAllPosts");
        LogUtils.request("GET", "/posts");
        given()
            .contentType(ConfigReader.getContentType())
        .when()
            .get("/posts")
        .then()
            .statusCode(200)
            .body("size()", equalTo(ConfigReader.getTotalPosts()));
        LogUtils.pass("testGetAllPosts");
        LogUtils.endTest("testGetAllPosts");
    }

    @Test(description = "GET single post deserializes into POJO correctly")
    public void testGetSinglePostAsPOJO() {
        LogUtils.startTest("testGetSinglePostAsPOJO");
        LogUtils.request("GET", "/posts/" + ConfigReader.getTestPostId());
        Response response = given()
            .contentType(ConfigReader.getContentType())
        .when()
            .get("/posts/" + ConfigReader.getTestPostId())
        .then()
            .statusCode(200)
            .extract().response();

        Post post = response.as(Post.class);
        Assert.assertEquals(post.getId(), ConfigReader.getTestPostId());
        Assert.assertNotNull(post.getTitle(), "Title should not be null");
        Assert.assertNotNull(post.getBody(), "Body should not be null");
        Assert.assertEquals(post.getUserId(), ConfigReader.getTestUserId());
        LogUtils.info("Post deserialized: " + post);
        LogUtils.pass("testGetSinglePostAsPOJO");
        LogUtils.endTest("testGetSinglePostAsPOJO");
    }

    @Test(description = "POST create new post using POJO returns 201")
    public void testCreatePostWithPOJO() {
        LogUtils.startTest("testCreatePostWithPOJO");
        LogUtils.request("POST", "/posts");
        Post newPost = new Post(ConfigReader.getTestUserId(), "RestAssured POJO Post", "Created using POJO class");

        Response response = given()
            .contentType(ConfigReader.getContentType())
            .body(newPost)
        .when()
            .post("/posts")
        .then()
            .statusCode(201)
            .extract().response();

        Post createdPost = response.as(Post.class);
        Assert.assertEquals(createdPost.getTitle(), "RestAssured POJO Post");
        Assert.assertEquals(createdPost.getUserId(), ConfigReader.getTestUserId());
        Assert.assertNotNull(createdPost.getId(), "ID should be assigned");
        LogUtils.info("Created post: " + createdPost);
        LogUtils.pass("testCreatePostWithPOJO");
        LogUtils.endTest("testCreatePostWithPOJO");
    }

    @Test(description = "PUT update post using POJO returns 200")
    public void testUpdatePostWithPOJO() {
        LogUtils.startTest("testUpdatePostWithPOJO");
        LogUtils.request("PUT", "/posts/" + ConfigReader.getTestPostId());
        Post updatedPost = new Post(ConfigReader.getTestUserId(), "Updated POJO Title", "Updated body content");

        Response response = given()
            .contentType(ConfigReader.getContentType())
            .body(updatedPost)
        .when()
            .put("/posts/" + ConfigReader.getTestPostId())
        .then()
            .statusCode(200)
            .extract().response();

        Post responsePost = response.as(Post.class);
        Assert.assertEquals(responsePost.getTitle(), "Updated POJO Title");
        LogUtils.pass("testUpdatePostWithPOJO");
        LogUtils.endTest("testUpdatePostWithPOJO");
    }

    @Test(description = "PATCH partially update post returns 200")
    public void testPatchPost() {
        LogUtils.startTest("testPatchPost");
        LogUtils.request("PATCH", "/posts/" + ConfigReader.getTestPostId());
        given()
            .contentType(ConfigReader.getContentType())
            .body("{\"title\": \"Patched Title\"}")
        .when()
            .patch("/posts/" + ConfigReader.getTestPostId())
        .then()
            .statusCode(200)
            .body("title", equalTo("Patched Title"));
        LogUtils.pass("testPatchPost");
        LogUtils.endTest("testPatchPost");
    }

    @Test(description = "DELETE post returns 200")
    public void testDeletePost() {
        LogUtils.startTest("testDeletePost");
        LogUtils.request("DELETE", "/posts/" + ConfigReader.getTestPostId());
        given()
            .contentType(ConfigReader.getContentType())
        .when()
            .delete("/posts/" + ConfigReader.getTestPostId())
        .then()
            .statusCode(200);
        LogUtils.pass("testDeletePost");
        LogUtils.endTest("testDeletePost");
    }

    @Test(description = "GET posts filtered by userId")
    public void testGetPostsByUser() {
        LogUtils.startTest("testGetPostsByUser");
        LogUtils.request("GET", "/posts?userId=" + ConfigReader.getTestUserId());
        given()
            .contentType(ConfigReader.getContentType())
            .queryParam("userId", ConfigReader.getTestUserId())
        .when()
            .get("/posts")
        .then()
            .statusCode(200)
            .body("size()", greaterThan(0))
            .body("userId", everyItem(equalTo(ConfigReader.getTestUserId())));
        LogUtils.pass("testGetPostsByUser");
        LogUtils.endTest("testGetPostsByUser");
    }

    @Test(description = "GET comments deserializes into POJO array")
    public void testGetCommentsAsPOJO() {
        LogUtils.startTest("testGetCommentsAsPOJO");
        LogUtils.request("GET", "/posts/" + ConfigReader.getTestPostId() + "/comments");
        Response response = given()
            .contentType(ConfigReader.getContentType())
        .when()
            .get("/posts/" + ConfigReader.getTestPostId() + "/comments")
        .then()
            .statusCode(200)
            .extract().response();

        Comment[] comments = response.as(Comment[].class);
        Assert.assertTrue(comments.length > 0, "Should have comments");
        for (Comment comment : comments) {
            Assert.assertNotNull(comment.getEmail(), "Email should not be null");
            Assert.assertEquals(comment.getPostId(), ConfigReader.getTestPostId());
        }
        LogUtils.info("First comment: " + comments[0]);
        LogUtils.pass("testGetCommentsAsPOJO");
        LogUtils.endTest("testGetCommentsAsPOJO");
    }

    @Test(description = "Chained request - create post then verify using POJO")
    public void testChainedRequestWithPOJO() {
        LogUtils.startTest("testChainedRequestWithPOJO");
        LogUtils.request("POST", "/posts");
        Post newPost = new Post(ConfigReader.getTestUserId(), "Chained POJO Test", "Testing chained requests with POJO");

        Response createResponse = given()
            .contentType(ConfigReader.getContentType())
            .body(newPost)
        .when()
            .post("/posts")
        .then()
            .statusCode(201)
            .extract().response();

        Post createdPost = createResponse.as(Post.class);
        Assert.assertEquals(createdPost.getTitle(), "Chained POJO Test");
        Assert.assertNotNull(createdPost.getId(), "ID should not be null");
        LogUtils.info("Chained test created post: " + createdPost);
        LogUtils.pass("testChainedRequestWithPOJO");
        LogUtils.endTest("testChainedRequestWithPOJO");
    }
}

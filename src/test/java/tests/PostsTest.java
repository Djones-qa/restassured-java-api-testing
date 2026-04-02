package tests;

import io.restassured.response.Response;
import models.Post;
import models.Comment;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.ConfigReader;

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

    @Test(description = "GET single post deserializes into POJO correctly")
    public void testGetSinglePostAsPOJO() {
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

        System.out.println("Post POJO: " + post);
    }

    @Test(description = "POST create new post using POJO returns 201")
    public void testCreatePostWithPOJO() {
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

        System.out.println("Created Post: " + createdPost);
    }

    @Test(description = "PUT update post using POJO returns 200")
    public void testUpdatePostWithPOJO() {
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
    }

    @Test(description = "PATCH partially update post returns 200")
    public void testPatchPost() {
        given()
            .contentType(ConfigReader.getContentType())
            .body("{\"title\": \"Patched Title\"}")
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

    @Test(description = "GET comments deserializes into POJO array")
    public void testGetCommentsAsPOJO() {
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

        System.out.println("First comment: " + comments[0]);
    }

    @Test(description = "Chained request - create post then verify using POJO")
    public void testChainedRequestWithPOJO() {
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

        System.out.println("Chained test created post: " + createdPost);
    }
}

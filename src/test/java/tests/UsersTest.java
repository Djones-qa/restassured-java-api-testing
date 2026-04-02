package tests;

import io.restassured.response.Response;
import models.User;
import models.Address;
import models.Company;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.annotations.DataProvider;
import utils.ConfigReader;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class UsersTest extends BaseTest {

    @Test(description = "GET all users returns 200 and correct count")
    public void testGetAllUsers() {
        given()
            .contentType(ConfigReader.getContentType())
        .when()
            .get("/users")
        .then()
            .statusCode(200)
            .body("size()", equalTo(ConfigReader.getTotalUsers()));
    }

    @Test(description = "GET single user deserializes into POJO correctly")
    public void testGetSingleUserAsPOJO() {
        Response response = given()
            .contentType(ConfigReader.getContentType())
        .when()
            .get("/users/" + ConfigReader.getTestUserId())
        .then()
            .statusCode(200)
            .extract().response();

        User user = response.as(User.class);

        Assert.assertEquals(user.getId(), ConfigReader.getTestUserId());
        Assert.assertNotNull(user.getName(), "Name should not be null");
        Assert.assertNotNull(user.getEmail(), "Email should not be null");
        Assert.assertNotNull(user.getUsername(), "Username should not be null");
        Assert.assertNotNull(user.getAddress(), "Address should not be null");
        Assert.assertNotNull(user.getCompany(), "Company should not be null");

        System.out.println("User POJO: " + user);
        System.out.println("Address: " + user.getAddress());
        System.out.println("Company: " + user.getCompany());
    }

    @Test(description = "GET user address validates city and zipcode")
    public void testUserAddressValidation() {
        Response response = given()
            .contentType(ConfigReader.getContentType())
        .when()
            .get("/users/" + ConfigReader.getTestUserId())
        .then()
            .statusCode(200)
            .extract().response();

        User user = response.as(User.class);
        Address address = user.getAddress();

        Assert.assertNotNull(address.getCity(), "City should not be null");
        Assert.assertNotNull(address.getZipcode(), "Zipcode should not be null");
        Assert.assertNotNull(address.getStreet(), "Street should not be null");
    }

    @Test(description = "GET user company validates name")
    public void testUserCompanyValidation() {
        Response response = given()
            .contentType(ConfigReader.getContentType())
        .when()
            .get("/users/" + ConfigReader.getTestUserId())
        .then()
            .statusCode(200)
            .extract().response();

        User user = response.as(User.class);
        Company company = user.getCompany();

        Assert.assertNotNull(company.getName(), "Company name should not be null");
        Assert.assertNotNull(company.getCatchPhrase(), "CatchPhrase should not be null");
    }

    @Test(description = "GET invalid user returns 404")
    public void testInvalidUserReturns404() {
        given()
            .contentType(ConfigReader.getContentType())
        .when()
            .get("/users/9999")
        .then()
            .statusCode(404);
    }

    @Test(description = "GET user response time under threshold")
    public void testResponseTime() {
        given()
            .contentType(ConfigReader.getContentType())
        .when()
            .get("/users")
        .then()
            .statusCode(200)
            .time(lessThan(ConfigReader.getResponseTimeout()));
    }

    @DataProvider(name = "userIds")
    public Object[][] userIds() {
        return new Object[][] {{1}, {2}, {3}, {4}, {5}};
    }

    @Test(dataProvider = "userIds", description = "GET multiple users as POJO data driven")
    public void testMultipleUsersAsPOJO(int userId) {
        Response response = given()
            .contentType(ConfigReader.getContentType())
        .when()
            .get("/users/" + userId)
        .then()
            .statusCode(200)
            .extract().response();

        User user = response.as(User.class);
        Assert.assertEquals(user.getId(), userId);
        Assert.assertNotNull(user.getName());
        Assert.assertNotNull(user.getEmail());
    }
}

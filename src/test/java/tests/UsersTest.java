package tests;

import io.restassured.response.Response;
import models.User;
import models.Address;
import models.Company;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import utils.ConfigReader;
import utils.LogUtils;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class UsersTest extends BaseTest {

    @Test(description = "GET all users returns 200 and correct count")
    public void testGetAllUsers() {
        LogUtils.startTest("testGetAllUsers");
        LogUtils.request("GET", "/users");
        given(requestSpec)
        .when()
            .get("/users")
        .then()
            .statusCode(200)
            .body("size()", equalTo(ConfigReader.getTotalUsers()));
        LogUtils.pass("testGetAllUsers");
        LogUtils.endTest("testGetAllUsers");
    }

    @Test(description = "GET single user deserializes into POJO correctly")
    public void testGetSingleUserAsPOJO() {
        LogUtils.startTest("testGetSingleUserAsPOJO");
        LogUtils.request("GET", "/users/" + ConfigReader.getTestUserId());
        Response response = given(requestSpec)
        .when()
            .get("/users/" + ConfigReader.getTestUserId())
        .then()
            .statusCode(200)
            .extract().response();

        User user = response.as(User.class);
        Assert.assertEquals(user.getId(), ConfigReader.getTestUserId());
        Assert.assertNotNull(user.getName());
        Assert.assertNotNull(user.getEmail());
        Assert.assertNotNull(user.getAddress());
        Assert.assertNotNull(user.getCompany());
        LogUtils.info("User deserialized: " + user);
        LogUtils.pass("testGetSingleUserAsPOJO");
        LogUtils.endTest("testGetSingleUserAsPOJO");
    }

    @Test(description = "GET user address validates city and zipcode")
    public void testUserAddressValidation() {
        LogUtils.startTest("testUserAddressValidation");
        LogUtils.request("GET", "/users/" + ConfigReader.getTestUserId());
        Response response = given(requestSpec)
        .when()
            .get("/users/" + ConfigReader.getTestUserId())
        .then()
            .statusCode(200)
            .extract().response();

        User user = response.as(User.class);
        Address address = user.getAddress();
        Assert.assertNotNull(address.getCity());
        Assert.assertNotNull(address.getZipcode());
        Assert.assertNotNull(address.getStreet());
        LogUtils.info("Address validated: " + address);
        LogUtils.pass("testUserAddressValidation");
        LogUtils.endTest("testUserAddressValidation");
    }

    @Test(description = "GET user company validates name")
    public void testUserCompanyValidation() {
        LogUtils.startTest("testUserCompanyValidation");
        LogUtils.request("GET", "/users/" + ConfigReader.getTestUserId());
        Response response = given(requestSpec)
        .when()
            .get("/users/" + ConfigReader.getTestUserId())
        .then()
            .statusCode(200)
            .extract().response();

        User user = response.as(User.class);
        Company company = user.getCompany();
        Assert.assertNotNull(company.getName());
        Assert.assertNotNull(company.getCatchPhrase());
        LogUtils.info("Company validated: " + company);
        LogUtils.pass("testUserCompanyValidation");
        LogUtils.endTest("testUserCompanyValidation");
    }

    @Test(description = "GET invalid user returns 404")
    public void testInvalidUserReturns404() {
        LogUtils.startTest("testInvalidUserReturns404");
        LogUtils.request("GET", "/users/9999");
        given(requestSpec)
        .when()
            .get("/users/9999")
        .then()
            .statusCode(404);
        LogUtils.pass("testInvalidUserReturns404");
        LogUtils.endTest("testInvalidUserReturns404");
    }

    @Test(description = "GET user response time under threshold")
    public void testResponseTime() {
        LogUtils.startTest("testResponseTime");
        LogUtils.request("GET", "/users");
        given(requestSpec)
        .when()
            .get("/users")
        .then()
            .statusCode(200)
            .time(lessThan(ConfigReader.getResponseTimeout()));
        LogUtils.pass("testResponseTime");
        LogUtils.endTest("testResponseTime");
    }

    @DataProvider(name = "userIds", parallel = true)
    public Object[][] userIds() {
        return new Object[][] {{1}, {2}, {3}, {4}, {5}};
    }

    @Test(dataProvider = "userIds", description = "GET multiple users as POJO data driven")
    public void testMultipleUsersAsPOJO(int userId) {
        LogUtils.startTest("testMultipleUsersAsPOJO - userId: " + userId);
        LogUtils.request("GET", "/users/" + userId);
        Response response = given(requestSpec)
        .when()
            .get("/users/" + userId)
        .then()
            .statusCode(200)
            .extract().response();

        User user = response.as(User.class);
        Assert.assertEquals(user.getId(), userId);
        Assert.assertNotNull(user.getName());
        Assert.assertNotNull(user.getEmail());
        LogUtils.pass("testMultipleUsersAsPOJO - userId: " + userId);
        LogUtils.endTest("testMultipleUsersAsPOJO - userId: " + userId);
    }
}

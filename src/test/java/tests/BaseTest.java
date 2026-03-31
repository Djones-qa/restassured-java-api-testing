package tests;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.testng.annotations.BeforeClass;

public class BaseTest {
    protected static final String BASE_URL = "https://jsonplaceholder.typicode.com";

    @BeforeClass
    public void setUp() {
        RestAssured.baseURI = BASE_URL;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }
}

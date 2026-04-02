package tests;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.testng.annotations.BeforeClass;
import utils.ConfigReader;
import utils.LogUtils;

public class BaseTest {

    protected static RequestSpecification requestSpec;

    @BeforeClass
    public synchronized void setUp() {
        LogUtils.info("Initializing RequestSpec for base URI: " + ConfigReader.getBaseUrl());
        requestSpec = new RequestSpecBuilder()
                .setBaseUri(ConfigReader.getBaseUrl())
                .setContentType(ConfigReader.getContentType())
                .addFilter(new RequestLoggingFilter())
                .addFilter(new ResponseLoggingFilter())
                .build();
        LogUtils.info("RequestSpec setup complete");
    }
}

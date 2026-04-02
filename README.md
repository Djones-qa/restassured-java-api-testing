# RestAssured Java API Testing

![CI](https://github.com/Djones-qa/restassured-java-api-testing/actions/workflows/api-tests.yml/badge.svg)

Enterprise-grade REST API testing framework using RestAssured and TestNG with Java 17. Features POJO deserialization, Log4j2 logging, parallel execution, configuration management via properties files, and data-driven testing with TestNG DataProvider.

## Tech Stack
- Java 17
- RestAssured 5.4.0
- TestNG 7.9.0 (parallel execution)
- Jackson Databind 2.17.0
- Log4j2 2.23.1
- Maven
- GitHub Actions CI

## Project Structure
```
restassured-java-api-testing/
├── src/test/java/
│   ├── models/
│   │   ├── Address.java
│   │   ├── Comment.java
│   │   ├── Company.java
│   │   ├── Post.java
│   │   └── User.java
│   ├── tests/
│   │   ├── BaseTest.java
│   │   ├── PostsTest.java
│   │   └── UsersTest.java
│   └── utils/
│       ├── ConfigReader.java
│       └── LogUtils.java
├── src/test/resources/
│   ├── config.properties
│   ├── log4j2.xml
│   └── testng.xml
└── .github/workflows/
    └── api-tests.yml
```

## Configuration
All test configuration is externalized in `config.properties`:
- Base URL and content type
- Connection and response timeouts
- Test data (user IDs, expected counts)
- Report settings

## Parallel Execution
Tests run in parallel at the method level using TestNG with 3 threads configured in `testng.xml`. Thread safety is achieved via a shared `RequestSpecification` built in `BaseTest` using `RequestSpecBuilder`, avoiding race conditions on RestAssured's static global state.

## Logging
Log4j2 is configured via `log4j2.xml` with two appenders:
- Console output for real-time feedback
- File output to `reports/api-tests.log`

Each test logs start, request, response, pass/fail, and end events via `LogUtils`.

## Test Coverage (24 tests)

### Users API (11 tests)
- GET all users returns 200 and correct count
- GET single user deserializes into POJO correctly
- GET user address validates city, zipcode, and street
- GET user company validates name and catchPhrase
- GET invalid user returns 404
- GET response time under threshold
- Data driven tests across 5 users (TestNG DataProvider, parallel)

### Posts API (9 tests)
- GET all posts returns correct count
- GET single post deserializes into POJO correctly
- POST create new post using POJO returns 201
- PUT update post using POJO returns 200
- PATCH partially update post returns 200
- DELETE post returns 200
- GET posts filtered by userId
- GET comments deserializes into POJO array
- Chained request - create post then verify using POJO

## Run Tests
```bash
mvn test
```

## Author
Darrius Jones - github.com/Djones-qa

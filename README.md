# RestAssured Java API Testing

![CI](https://github.com/Djones-qa/restassured-java-api-testing/actions/workflows/api-tests.yml/badge.svg)

Enterprise-grade REST API testing framework using RestAssured and TestNG with Java 17.

## Tech Stack
- Java 17
- RestAssured 5.4.0
- TestNG 7.9.0
- Jackson Databind
- Maven
- GitHub Actions CI

## Test Coverage (19 tests)

### Users API (6 tests)
- GET all users returns 200 and 10 records
- GET single user validates correct data
- GET user schema field validation
- GET invalid user returns 404
- GET response time under 2 seconds
- Data driven tests across 5 users (TestNG DataProvider)

### Posts API (9 tests)
- GET all posts returns 100 records
- GET single post validates correct data
- POST create new post returns 201
- PUT update post returns 200
- PATCH partially update post
- DELETE post returns 200
- GET posts filtered by userId
- GET comments for a post
- Chained request - create then verify

## Run Tests
```bash
mvn test
```

## Author
Darrius Jones - github.com/Djones-qa


package com.example.api.test.automation;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class UserApiAutomationTest {

    @BeforeAll
    public static void setUp() {
        RestAssured.baseURI = "http://localhost:8080";
        RestAssured.basePath = "/api/users";
    }

    @Test
    void testCreateAndGetUser() {
        String userJson = """
            {
                "name": "Test User",
                "email": "test.user@example.com",
                "age": 25,
                "address": "123 Test St",
                "phone": "+84912345678"
            }
            """;

        // Create user
        Response createResponse = given()
                .contentType(ContentType.JSON)
                .body(userJson)
                .when()
                .post()
                .then()
                .statusCode(201)
                .body("name", equalTo("Test User"))
                .body("email", equalTo("test.user@example.com"))
                .extract().response();

        Long userId = createResponse.jsonPath().getLong("id");

        // Get user by ID
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/" + userId)
                .then()
                .statusCode(200)
                .body("name", equalTo("Test User"))
                .body("email", equalTo("test.user@example.com"));
    }

    @Test
    void testCreateUserWithInvalidEmail() {
        String invalidUserJson = """
            {
                "name": "Invalid User",
                "email": "invalid-email",
                "age": 25
            }
            """;

        given()
                .contentType(ContentType.JSON)
                .body(invalidUserJson)
                .when()
                .post()
                .then()
                .statusCode(400);
    }

    @Test
    void testGetAllUsers() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .get()
                .then()
                .statusCode(200)
                .body("size()", greaterThanOrEqualTo(0));
    }

    @Test
    void testUpdateUser() {
        // First create a user
        String userJson = """
            {
                "name": "Update User",
                "email": "update.user@example.com",
                "age": 30
            }
            """;

        Response createResponse = given()
                .contentType(ContentType.JSON)
                .body(userJson)
                .when()
                .post()
                .then()
                .statusCode(201)
                .extract().response();

        Long userId = createResponse.jsonPath().getLong("id");

        // Update the user
        String updatedUserJson = """
            {
                "name": "Updated User",
                "email": "updated.user@example.com",
                "age": 31,
                "address": "456 Update St",
                "phone": "+84987654321"
            }
            """;

        given()
                .contentType(ContentType.JSON)
                .body(updatedUserJson)
                .when()
                .put("/" + userId)
                .then()
                .statusCode(200)
                .body("name", equalTo("Updated User"))
                .body("email", equalTo("updated.user@example.com"));
    }

    @Test
    void testDeleteUser() {
        // Create user
        String userJson = """
            {
                "name": "Delete User",
                "email": "delete.user@example.com",
                "age": 25
            }
            """;

        Response createResponse = given()
                .contentType(ContentType.JSON)
                .body(userJson)
                .when()
                .post()
                .then()
                .statusCode(201)
                .extract().response();

        Long userId = createResponse.jsonPath().getLong("id");

        // Delete user
        given()
                .contentType(ContentType.JSON)
                .when()
                .delete("/" + userId)
                .then()
                .statusCode(204);

        // Verify user is deleted
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/" + userId)
                .then()
                .statusCode(404);
    }

    @Test
    void testSearchUsersByName() {
        given()
                .contentType(ContentType.JSON)
                .queryParam("keyword", "Test")
                .when()
                .get("/search")
                .then()
                .statusCode(200);
    }

    @Test
    void testGetUsersByAgeRange() {
        given()
                .contentType(ContentType.JSON)
                .queryParam("minAge", 20)
                .queryParam("maxAge", 30)
                .when()
                .get("/age-range")
                .then()
                .statusCode(200);
    }
}
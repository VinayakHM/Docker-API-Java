package org.example;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class UserApiTest {

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = "https://reqres.in/api";
    }

    //========================================================
    // Test Case 1
    // GET User by ID
    //========================================================

    @Test
    public void getUserById() {

        given()
                .when()
                .get("/users/2")
                .then()
                .statusCode(200)
                .body("data.id", equalTo(2))
                .body("data.first_name", equalTo("Janet"))
                .body("data.email", containsString("@reqres.in"));
    }


    //========================================================
    // Test Case 2
    // Create User
    //========================================================

    @Test
    public void createUser() {

        Map<String, Object> request = new HashMap<>();

        request.put("name", "Vinayak");
        request.put("job", "QA Engineer");

        given()
                .contentType(ContentType.JSON)
                .body(request)

                .when()
                .post("/users")

                .then()
                .statusCode(201)
                .body("name", equalTo("Vinayak"))
                .body("job", equalTo("QA Engineer"))
                .body("id", notNullValue())
                .body("createdAt", notNullValue());
    }


    //========================================================
    // Test Case 3
    // Update User
    //========================================================

    @Test
    public void updateUser() {

        Map<String, Object> request = new HashMap<>();

        request.put("name", "Vinayak");
        request.put("job", "Senior QA");

        given()
                .contentType(ContentType.JSON)
                .body(request)

                .when()
                .put("/users/2")

                .then()
                .statusCode(200)
                .body("job", equalTo("Senior QA"))
                .body("updatedAt", notNullValue());
    }


    //========================================================
    // Test Case 4
    // Delete User
    //========================================================

    @Test
    public void deleteUser() {

        Response response =

                given()

                        .when()
                        .delete("/users/2");

        Assert.assertEquals(response.getStatusCode(), 204);
    }

    @Test
    public void getPostById() {

        given()
                .when()
                .get("https://jsonplaceholder.typicode.com/posts/1")
                .then()
                .statusCode(200)
                .body("id", equalTo(1))
                .body("userId", equalTo(1))
                .body("title", notNullValue());
    }

    @Test
    public void getAllPosts() {

        given()
                .when()
                .get("https://jsonplaceholder.typicode.com/posts")
                .then()
                .statusCode(200)
                .body("size()", greaterThan(0));
    }

    @Test
    public void createPost() {

        String requestBody = "{\n" +
                "    \"title\":\"Rest Assured\",\n" +
                "    \"body\":\"Learning API Testing\",\n" +
                "    \"userId\":1\n" +
                "}";

        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("https://jsonplaceholder.typicode.com/posts")
                .then()
                .statusCode(201)
                .body("title", equalTo("Rest Assured"))
                .body("body", equalTo("Learning API Testing"))
                .body("userId", equalTo(1));
    }

    @Test
    public void deletePost() {

        given()
                .when()
                .delete("https://jsonplaceholder.typicode.com/posts/1")
                .then()
                .statusCode(200);
    }

}

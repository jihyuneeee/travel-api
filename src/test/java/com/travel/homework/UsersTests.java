package com.travel.homework;

import java.util.HashMap;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.boot.test.context.SpringBootTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;

@SpringBootTest
public class UsersTests {

    @DisplayName("사용자 등록")
    @Test
    public void joinUser() {

        HashMap<String, String> requestData = new HashMap<>();
        requestData.put("username", "kuku");

        RestAssured
                .given().port(8080).contentType(ContentType.JSON).body(requestData)
                .when().post("/api/user")
                .then().statusCode(200);

    }

}

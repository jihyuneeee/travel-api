package com.travel.homework;

import java.util.HashMap;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

@SpringBootTest
public class TravelTests {

    @DisplayName("여행 등록")
    @Test
    public void registerTravel() {

        HashMap<String, String> requestData = new HashMap<>();
        requestData.put("city_id", "1");
        requestData.put("user_id", "1");
        requestData.put("travelname", "testtravel");
        requestData.put("startDate", "2022-12-30 00:00:00");
        requestData.put("endDate", "2023-01-30 00:00:00");

        Response response = RestAssured
                .given().contentType(ContentType.JSON).body(requestData)
                .when().post("/api/travel")
                .then().statusCode(200).extract().response();

        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertEquals("testtravel", response.jsonPath().getString("data.travelname"));

    }

    @DisplayName("여행 수정")
    @Test
    public void 여행수정() {

        HashMap<String, String> requestData = new HashMap<>();
        requestData.put("city_id", "1");
        requestData.put("travelname", "testname2");
        requestData.put("startDate", "2022-12-30 00:00:00");
        requestData.put("endDate", "2023-02-28 00:00:00");

        Response response = RestAssured.given().contentType(ContentType.JSON).body(requestData)
                .when().put("/api/travel/1")
                .then().extract().response();

        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertEquals("1", response.jsonPath().getString("data.travel_id"));
        Assertions.assertEquals("testname2", response.jsonPath().getString("data.travelname"));

    }

    @DisplayName("여행조회")
    @Test
    public void selectTravel() {

        Response response = RestAssured.given().contentType(ContentType.JSON)
                .when().get("/api/travel/1")
                .then().extract().response();

        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertEquals("1", response.jsonPath().getString("data.travel_id"));
        Assertions.assertEquals("1", response.jsonPath().getString("data.city_id"));
        Assertions.assertEquals("1", response.jsonPath().getString("data.user_id"));
        Assertions.assertEquals("testtravel", response.jsonPath().getString("data.travelname"));

    }

    @DisplayName("여행 삭제")
    @Test
    public void deleteTravel() {

        Response response = RestAssured.given().contentType(ContentType.JSON)
                .when().delete("/api/travel/1")
                .then().extract().response();

        Assertions.assertEquals(200, response.statusCode());

    }
}

package com.travel.homework;

import java.util.HashMap;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

@SpringBootTest
public class CityTests {

    @PersistenceContext
    EntityManager entityManager;

    @DisplayName("도시 등록")
    @Test
    public void registerCity() {

        HashMap<String, String> requestData = new HashMap<>();
        requestData.put("cityname", "Seoul");

        RestAssured
                .given().contentType(ContentType.JSON).body(requestData)
                .when().post("/api/city")
                .then().statusCode(200);
    }

    @DisplayName("도시 수정")
    @Test
    public void updateCity() {

        HashMap<String, String> requestData = new HashMap<>();
        requestData.put("cityname", "GangNam");

        Response response = RestAssured.given().contentType(ContentType.JSON).body(requestData)
                .when().put("/api/city/1")
                .then().extract().response();

        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertEquals("GangNam", response.jsonPath().getString("data.cityname"));
    }

    @DisplayName("단일 도시 조회")
    @Test
    public void getCityInfo() {

        Response response = RestAssured.given().contentType(ContentType.JSON)
                .when().get("/api/city/1")
                .then().extract().response();

        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertEquals("1", response.jsonPath().getString("data.city_id"));
        Assertions.assertEquals("GangNam", response.jsonPath().getString("data.cityname"));
    }

    @DisplayName("도시 삭제")
    @Test
    public void deleteCity() {

        Response response = RestAssured.given().contentType(ContentType.JSON)
                .when().delete("/api/city/1")
                .then().extract().response();

        Assertions.assertEquals(200, response.statusCode());

    }

    // @DisplayName("사용자별 도시 조회")
    // @Test
    // public void getCityTravelList() {

    // Response response = RestAssured.given().contentType(ContentType.JSON)
    // .when().get("/api/city/list/1")
    // .then().extract().response();

    // Assertions.assertEquals(200, response.statusCode());

    // }
}

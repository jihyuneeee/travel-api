package com.travel.homework.response;

import java.util.List;
import org.json.simple.JSONObject;
import org.springframework.http.HttpStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Response {

    private HttpStatus code;
    private String message;

    private JSONObject data;
    // private List<Object> data;

}

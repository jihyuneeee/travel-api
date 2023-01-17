package com.travel.homework.api;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.travel.homework.domain.entity.City;
import com.travel.homework.domain.entity.Travel;
import com.travel.homework.response.Response;
import com.travel.homework.service.CityService;
import com.travel.homework.service.TravelService;
import com.travel.homework.util.Messages;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class TravelApiController {

    @Autowired
    TravelService travelService;

    @Autowired
    CityService cityService;

    /**
     * 여행 등록 API
     */
    @PostMapping("/api/travel")
    public ResponseEntity<Response> saveCity(@RequestBody @Valid CreateTravelRequest request,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            List<ObjectError> errorList = bindingResult.getAllErrors();
            for (ObjectError objectError : errorList) {

                Response response = Response.builder()
                        .code(HttpStatus.BAD_REQUEST)
                        .message(objectError.getDefaultMessage())
                        .build();

                return ResponseEntity.internalServerError().body(response);

            }
        }

        // 해당 city id 조회
        City city = cityService.findOne(request.getCity_id());

        if (city == null) {
            Response response = Response.builder()
                    .code(HttpStatus.BAD_REQUEST)
                    .message(Messages.CITY_CANT_FIND)
                    .build();
            return ResponseEntity.internalServerError().body(response);
        }

        // endDate 미래만 허용 추가
        LocalDateTime now = LocalDateTime.now();

        if (!request.getEndDate().isAfter(now)) {
            Response response = Response.builder()
                    .code(HttpStatus.BAD_REQUEST)
                    .message(Messages.TRAVEL_ENDDATE_INVALID)
                    .build();
            return ResponseEntity.internalServerError().body(response);
        }

        // 여행 등록
        Long id = travelService.planTravel(
                request.getTravelname(), request.getCity_id(), request.getUser_id(), request.getStartDate(),
                request.getEndDate());

        JSONObject data = new JSONObject();
        data.put("travel_id", id);
        data.put("travelname", request.getTravelname());

        Response response = Response.builder()
                .code(HttpStatus.CREATED)
                .message(Messages.TRAVEL_CREATED)
                .data(data)
                .build();

        return ResponseEntity.ok().body(response);
    }

    /**
     * 여행 수정 API
     */
    @PutMapping("/api/travel/{id}")
    public ResponseEntity<Response> updateCity(@PathVariable("id") Long id,
            @RequestBody @Valid UpdateTravelRequest request, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            List<ObjectError> errorList = bindingResult.getAllErrors();
            for (ObjectError objectError : errorList) {

                Response response = Response.builder()
                        .code(HttpStatus.BAD_REQUEST)
                        .message(objectError.getDefaultMessage())
                        .build();

                return ResponseEntity.internalServerError().body(response);

            }
        }

        // 해당 city id 조회
        City city = cityService.findOne(request.getCity_id());

        if (city == null) {
            Response response = Response.builder()
                    .code(HttpStatus.BAD_REQUEST)
                    .message(Messages.CITY_CANT_FIND)
                    .build();
            return ResponseEntity.internalServerError().body(response);
        }

        // endDate 미래만 허용 추가
        LocalDateTime now = LocalDateTime.now();

        if (!request.getEndDate().isAfter(now)) {
            Response response = Response.builder()
                    .code(HttpStatus.BAD_REQUEST)
                    .message(Messages.TRAVEL_ENDDATE_INVALID)
                    .build();
            return ResponseEntity.internalServerError().body(response);
        }

        // 여행정보 업데이트
        travelService.updateTravel(id, request.getCity_id(), request.getTravelname(), request.getStartDate(),
                request.getEndDate());

        JSONObject data = new JSONObject();
        data.put("travel_id", id);
        data.put("travelname", request.getTravelname());

        Response response = Response.builder()
                .code(HttpStatus.CREATED)
                .message(Messages.TRAVEL_UPDATED)
                .data(data)
                .build();

        return ResponseEntity.ok().body(response);
    }

    /**
     * 단일 조회 API
     */
    @GetMapping("/api/travel/{id}")
    public ResponseEntity<Response> selectTravel(@PathVariable("id") Long id) {

        Travel travel = travelService.findOne(id);

        if (travel == null) {
            Response response = Response.builder()
                    .code(HttpStatus.NOT_FOUND)
                    .message(Messages.TRAVEL_CANT_FIND)
                    .build();
            return ResponseEntity.internalServerError().body(response);
        }

        JSONObject data = new JSONObject();
        data.put("travel_id", travel.getId());
        data.put("city_id", travel.getCity().getId());
        data.put("user_id", travel.getUser().getId());
        data.put("travelname", travel.getTravelname());
        data.put("startDate", travel.getStartDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        data.put("endDate", travel.getEndDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        Response response = Response.builder()
                .code(HttpStatus.OK)
                .message(Messages.TRAVEL_FIND)
                .data(data)
                .build();

        return ResponseEntity.ok().body(response);
    }

    /*
     * 여행 삭제 API
     */
    @DeleteMapping("/api/travel/{id}")
    public ResponseEntity<Response> deleteTravel(@PathVariable("id") Long id) {

        // 여행 조회
        Travel travel = travelService.findOne(id);

        if (travel == null) {
            Response response = Response.builder()
                    .code(HttpStatus.NOT_FOUND)
                    .message(Messages.TRAVEL_CANT_FIND)
                    .build();
            return ResponseEntity.internalServerError().body(response);
        }

        travelService.deleteTravel(travel);

        Response response = Response.builder()
                .code(HttpStatus.OK)
                .message(Messages.TRAVEL_DELETED)
                .build();

        return ResponseEntity.ok().body(response);

    }

    @Data
    static class UpdateTravelRequest {

        @NotBlank(message = "여행명을 입력해주세요.")
        private String travelname;

        @NotNull(message = "도시를 선택해주세요.")
        private Long city_id;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime startDate;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime endDate;
    }

    @Data
    static class CreateTravelRequest {

        @NotBlank(message = "여행명을 입력해주세요.")
        private String travelname;

        @NotNull(message = "도시를 선택해주세요.")
        private Long city_id;

        @NotNull(message = "사용자를 선택해주세요.")
        private Long user_id;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime startDate;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime endDate;
    }

}

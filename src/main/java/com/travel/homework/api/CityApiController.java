package com.travel.homework.api;

import java.util.List;
import java.util.stream.Collectors;
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
import com.travel.homework.domain.entity.City;
import com.travel.homework.domain.entity.Travel;
import com.travel.homework.response.Response;
import com.travel.homework.service.CityService;
import com.travel.homework.util.Messages;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import static java.util.stream.Collectors.toList;

@RestController
public class CityApiController {

    @Autowired
    CityService cityService;

    /**
     * 도시 등록 API
     */
    @PostMapping("/api/city")
    public ResponseEntity<Response> registerCity(@RequestBody @Valid CreateCityRequest request,
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

        City city = new City();
        city.setCityname(request.getCityname());

        // 중복 도시 검증
        if (!cityService.validateDuplicateCity(city.getCityname())) {
            Response response = Response.builder()
                    .code(HttpStatus.BAD_REQUEST)
                    .message(Messages.CITY_EXISIT)
                    .build();
            return ResponseEntity.internalServerError().body(response);
        }

        // 도시 등록
        Long id = cityService.registerCity(city);

        JSONObject data = new JSONObject();
        data.put("city_id", id);
        data.put("cityname", request.getCityname());

        Response response = Response.builder()
                .code(HttpStatus.CREATED)
                .message(Messages.CITY_CREATED)
                .data(data)
                .build();

        return ResponseEntity.ok().body(response);

    }

    /**
     * 도시 수정 API
     */
    @PutMapping("/api/city/{id}")
    public ResponseEntity<Response> updateCity(@PathVariable("id") Long id,
            @RequestBody @Valid UpdateMemberRequest request, BindingResult bindingResult) {

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
        City findCity = cityService.findOne(id);

        if (findCity == null) {
            Response response = Response.builder()
                    .code(HttpStatus.BAD_REQUEST)
                    .message(Messages.CITY_CANT_FIND)
                    .build();
            return ResponseEntity.internalServerError().body(response);
        }

        // 중복 도시 검증
        if (!cityService.validateDuplicateCity(request.getCityname())) {
            Response response = Response.builder()
                    .code(HttpStatus.BAD_REQUEST)
                    .message(Messages.CITY_EXISIT)
                    .build();
            return ResponseEntity.internalServerError().body(response);
        }

        // 도시 이름 수정
        cityService.updateCity(id, request.getCityname());

        JSONObject data = new JSONObject();
        data.put("city_id", id);
        data.put("cityname", request.getCityname());

        Response response = Response.builder()
                .code(HttpStatus.CREATED)
                .message(Messages.CITY_UPATED)
                .data(data)
                .build();

        return ResponseEntity.ok().body(response);

    }

    /**
     * 단일 도시 조회 API
     */
    @GetMapping("/api/city/{id}")
    public ResponseEntity<Response> selectCity(@PathVariable("id") Long id) {

        // 해당 city id 조회
        City city = cityService.findCity(id);

        if (city == null) {
            Response response = Response.builder()
                    .code(HttpStatus.NOT_FOUND)
                    .message(Messages.CITY_CANT_FIND)
                    .build();
            return ResponseEntity.internalServerError().body(response);
        }

        JSONObject data = new JSONObject();
        data.put("city_id", city.getId());
        data.put("cityname", city.getCityname());

        Response response = Response.builder()
                .code(HttpStatus.OK)
                .message(Messages.CITY_FIND)
                .data(data)
                .build();

        return ResponseEntity.ok().body(response);

    }

    /**
     * 도시 삭제 API
     * 
     */
    @DeleteMapping("/api/city/{id}")
    public ResponseEntity<Response> deleteCity(@PathVariable("id") Long id) {

        // 도시에 지정되어있는 여행 조회
        List<City> travel = cityService.findCityJoinTravel(id);

        if (!travel.isEmpty()) { // 해당 도시가 지정된 여행이 없을 경우만 삭제 가능
            Response response = Response.builder()
                    .code(HttpStatus.UNPROCESSABLE_ENTITY)
                    .message(Messages.CITY_USED)
                    .build();
            return ResponseEntity.internalServerError().body(response);
        }

        // 도시 삭제
        cityService.deleteCity(id);

        Response response = Response.builder()
                .code(HttpStatus.OK)
                .message(Messages.CITY_DELETED)
                .build();

        return ResponseEntity.ok().body(response);

    }

    /**
     * 사용자별 도시 목록 조회 API
     */
    @GetMapping("/api/city/list/{id}")
    public List<CityListDto> selectCityList(@PathVariable("id") Long id) {

        // 1. 여행 중인 도시 조회
        List<Travel> city = cityService.findTravelingCityWithUserId(id);

        // 2. 나머지 도시 조회
        List<City> city2 = cityService.findOtherCityWithUserId(id);

        List<CityListDto> result = city.stream()
                .map(o -> new CityListDto(o))
                .collect(toList());

        result.addAll(city2.stream()
                .map(o -> new CityListDto(o))
                .collect(Collectors.toList()));

        return result;

    }

    @Data
    static class CityListDto {
        private Long city_id;
        private String cityname;

        public CityListDto(Travel o) {
            city_id = o.getCity().getId();
            cityname = o.getCity().getCityname();
        }

        public CityListDto(City o) {
            city_id = o.getId();
            cityname = o.getCityname();
        }
    }

    @Data
    static class UpdateMemberRequest {

        @NotBlank(message = "도시명을 입력해주세요.")
        private String cityname;
    }

    @Data
    static class CreateCityRequest {
        @NotBlank(message = "도시명을 입력해주세요.")
        private String cityname;
    }

}

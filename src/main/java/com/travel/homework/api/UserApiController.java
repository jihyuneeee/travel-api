package com.travel.homework.api;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.travel.homework.domain.entity.Users;
import com.travel.homework.response.Response;
import com.travel.homework.service.UserService;
import com.travel.homework.util.Messages;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import lombok.Data;

@RestController
public class UserApiController {

    @Autowired
    UserService userService;

    /**
     * 회원 등록 API
     */
    @PostMapping("/api/user")
    public ResponseEntity<Response> registerUser(@RequestBody @Valid CreateUserRequest requset,
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

        Users user = new Users();
        user.setUsername(requset.getUsername());

        // 중복회원 검증
        if (!userService.validateDuplicateUser(user.getUsername())) {
            Response response = Response.builder()
                    .code(HttpStatus.BAD_REQUEST)
                    .message(Messages.USER_EXISIT)
                    .build();
            return ResponseEntity.internalServerError().body(response);
        }

        // 회원 등록
        Long id = userService.registerUser(user);
        JSONObject data = new JSONObject();
        data.put("user_id", id);
        data.put("username", requset.getUsername());

        Response response = Response.builder()
                .code(HttpStatus.CREATED)
                .message(Messages.USER_CREATED)
                .data(data)
                .build();

        return ResponseEntity.ok().body(response);

    }

    @Data
    static class CreateUserRequest {

        @NotBlank(message = "사용자이름을 입력해주세요.")
        private String username;
    }

}

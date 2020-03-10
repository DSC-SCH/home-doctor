package homedoctor.medicine.api.controller;

import homedoctor.medicine.api.dto.DefaultApiResponse;
import homedoctor.medicine.api.dto.user.response.DeleteUserResponse;
import homedoctor.medicine.api.dto.user.request.CreateUserRequest;
import homedoctor.medicine.api.dto.user.response.CreateUserResponse;
import homedoctor.medicine.common.ResponseMessage;
import homedoctor.medicine.common.StatusCode;
import homedoctor.medicine.domain.User;
import homedoctor.medicine.api.dto.user.*;
import homedoctor.medicine.dto.DefaultUserResponse;
import homedoctor.medicine.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserApiController {

    private final UserService userService;

    // 엔티티 직접 노출되지 않게 바꾸기.
    // api 스펙이 변하면 안됨.
    @GetMapping("/user/{user_id}")
    public DefaultApiResponse findUserOne(
            @PathVariable("user_id") Long id) {
        try {

            DefaultUserResponse response = userService.findOneById(id);
            User findUser = response.getUser();

            UserDto userDto = UserDto.builder()
                    .id(findUser.getId())
                    .username(findUser.getUsername())
                    .birthday(findUser.getBirthday())
                    .email(findUser.getEmail())
                    .snsType(findUser.getSnsType())
                    .genderType(findUser.getGenderType())
                    .phoneNum(findUser.getPhoneNum())
                    .build();

            return DefaultApiResponse.response(response.getStatus(),
                    response.getResponseMessgae(),
                    userDto);
        } catch (Exception e) {
            log.error(e.getMessage());

            return DefaultApiResponse.response(StatusCode.INTERNAL_SERVER_ERROR,
                    ResponseMessage.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/user")
    public DefaultApiResponse userResult() {
        try {
            DefaultUserResponse response = userService.findAllUsers();
            List<User> findAllUser = response.getUserList();

            List<UserDto> collect = findAllUser.stream()
                    .map(m -> UserDto.builder()
                            .id(m.getId())
                            .username(m.getUsername())
                            .birthday(m.getBirthday())
                            .email(m.getEmail())
                            .snsType(m.getSnsType())
                            .genderType(m.getGenderType())
                            .phoneNum(m.getPhoneNum())
                            .build())
                    .collect(Collectors.toList());

            return DefaultApiResponse.response(response.getStatus(),
                    response.getResponseMessgae(),
                    new UserAllResult(collect.size(), collect));
        } catch (Exception e) {
            log.error(e.getMessage());
            return DefaultApiResponse.response(StatusCode.INTERNAL_SERVER_ERROR,
                    ResponseMessage.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/user")
    public DefaultApiResponse saveUser(
            @RequestBody @Valid CreateUserRequest request) {
        try {

            CreateUserRequest user = CreateUserRequest.builder()
                    .username(request.getUsername())
                    .birthday(request.getBirthday())
                    .email(request.getEmail())
                    .snsType(request.getSnsType())
                    .genderType(request.getGenderType())
                    .phoneNum(request.getPhoneNum())
                    .build();
            DefaultUserResponse response = userService.join(user);

            return DefaultApiResponse.response(response.getStatus(),
                    response.getResponseMessgae(),
                    user);
        } catch (Exception e) {
            log.error(e.getMessage());
            return DefaultApiResponse.response(StatusCode.INTERNAL_SERVER_ERROR,
                    ResponseMessage.INTERNAL_SERVER_ERROR);
        }
    }

//    @PutMapping("/user/{user_id}")
//    public UpdateMemberResponse updateMemberResponse(
//            @PathVariable("user_id") Long id,
//            @RequestBody @Valid UpdateMemberRequest request) {
//
//        userService.update(id, request.getName());
//        User findUser = userService.findOne(id);
//
//        return new UpdateMemberResponse(findUser.getId(), findUser.getUsername());
//    }

    @DeleteMapping("/user/{user_id}")
    public DefaultApiResponse deleteUserResponse(
            @PathVariable("user_id") Long id) {
        try {
            DefaultUserResponse response = userService.findOneById(id);
            User user = response.getUser();
            userService.deleteById(id);

            return DefaultApiResponse.response(response.getStatus(),
                    response.getResponseMessgae(),
                    id);
        } catch (Exception e) {
            log.error(e.getMessage());
            return DefaultApiResponse.response(StatusCode.INTERNAL_SERVER_ERROR,
                    ResponseMessage.INTERNAL_SERVER_ERROR);
        }
    }
}

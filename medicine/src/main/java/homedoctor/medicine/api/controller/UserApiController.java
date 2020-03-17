package homedoctor.medicine.api.controller;

import homedoctor.medicine.api.dto.DefaultResponse;
import homedoctor.medicine.api.dto.user.CreateUserRequest;
import homedoctor.medicine.common.ResponseMessage;
import homedoctor.medicine.common.StatusCode;
import homedoctor.medicine.common.auth.Auth;
import homedoctor.medicine.domain.Terms;
import homedoctor.medicine.domain.User;
import homedoctor.medicine.api.dto.user.*;
import homedoctor.medicine.service.AuthService;
import homedoctor.medicine.service.JwtService;
import homedoctor.medicine.service.TermService;
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

    private final AuthService authService;

    private final JwtService jwtService;

    private final TermService termService;

    @GetMapping("/terms")
    public DefaultResponse getTerms() {
        try {

            List<Terms> terms = (List<Terms>) termService.findTermsAll().getData();

            if (terms.isEmpty()) {
                String[] empty = new String[0];
                return DefaultResponse.response(StatusCode.OK,
                        ResponseMessage.NOT_FOUND_TERMS, empty);
            }

            List<TermsDto> termsDtoList = terms.stream()
                    .map(m -> TermsDto.builder()
                    .title(m.getTitle())
                    .content(m.getContent())
                    .build()).collect(Collectors.toList());

            return DefaultResponse.response(StatusCode.OK,
                    ResponseMessage.FOUND_TERMS,
                    termsDtoList);

        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            System.err.println(e.getMessage());

            return DefaultResponse.response(StatusCode.INTERNAL_SERVER_ERROR,
                    ResponseMessage.INTERNAL_SERVER_ERROR);
        }
    }

//    @GetMapping("/privacy")
//    public DefaultResponse getPrivacyInfo() {
//        try {
//            Terms privacyInfo = (Terms) termService.findPrivacy().getData();
//
//            if (privacyInfo == null) {
//                String[] empty = new String[0];
//                return DefaultResponse.response(StatusCode.OK,
//                        ResponseMessage.NOT_FOUND_TERMS, empty);
//            }
//
//            TermsDto privacyDto = TermsDto.builder()
//                    .title(privacyInfo.getTitle())
//                    .content(privacyInfo.getContent())
//                    .build();
//
//            return DefaultResponse.response(StatusCode.OK,
//                    ResponseMessage.FOUND_TERMS,
//                    privacyDto);
//        } catch (Exception e) {
//            log.error(e.getMessage());
//            e.printStackTrace();
//            System.err.println(e.getMessage());
//
//            return DefaultResponse.response(StatusCode.INTERNAL_SERVER_ERROR,
//                    ResponseMessage.INTERNAL_SERVER_ERROR);
//        }
//    }


    @PostMapping("login")
    public DefaultResponse login(@RequestBody final LoginRequest loginRequest) {
        try {
            User findUser = (User) userService.findOneSnsId(loginRequest.getSnsId(), loginRequest.getSnsType()).getData();

            if (findUser == null) {
                return DefaultResponse.response(StatusCode.UNAUTHORIZED,
                        ResponseMessage.NOT_EXIST_USER);
            }
            DefaultResponse response = authService.login(loginRequest);

            return response;
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return DefaultResponse.response(StatusCode.INTERNAL_SERVER_ERROR,
                ResponseMessage.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("/user")
    public DefaultResponse saveUser(
            @RequestBody @Valid CreateUserRequest request) {
        try {

            if (!request.validProperties()) {
                return DefaultResponse.response(StatusCode.BAD_REQUEST,
                        ResponseMessage.NOT_CONTENT);
            }

            User user = User.builder()
                    .username(request.getUsername())
                    .birthday(request.getBirthday())
                    .email(request.getEmail())
                    .snsId(request.getSnsId())
                    .snsType(request.getSnsType())
                    .genderType(request.getGenderType())
                    .phoneNum(request.getPhoneNum())
                    .build();
            DefaultResponse saveResponse = userService.save(user);

            UserCreateDto userCreateDto = UserCreateDto.builder()
                    .token(user.getToken())
                    .userId(user.getId())
                    .build();

            return DefaultResponse.response(saveResponse.getStatus(),
                    saveResponse.getMessage(),
                    userCreateDto);
        } catch (Exception e) {
            log.error(e.getMessage());
            return DefaultResponse.response(StatusCode.INTERNAL_SERVER_ERROR,
                    ResponseMessage.INTERNAL_SERVER_ERROR);
        }
    }

    // 엔티티 직접 노출되지 않게 바꾸기.
    // api 스펙이 변하면 안됨.
    @Auth
    @GetMapping("/user")
    public DefaultResponse getUserOne(
            @RequestHeader("Authorization") final String header) {
        try {
            if (header == null) {
                return DefaultResponse.response(StatusCode.UNAUTHORIZED,
                        ResponseMessage.UNAUTHORIZED);
            }
            User findUser = (User) userService.findOneById(jwtService.decode(header)).getData();

            if (findUser == null) {
                return DefaultResponse.response(StatusCode.OK,
                        ResponseMessage.NOT_FOUND_USER);
            }

            UserInfoResponse userInfoResponse = UserInfoResponse.builder()
                    .username(findUser.getUsername())
                    .birthday(findUser.getBirthday())
                    .email(findUser.getEmail())
                    .snsType(findUser.getSnsType())
                    .snsId(findUser.getSnsId())
                    .genderType(findUser.getGenderType())
                    .phoneNum(findUser.getPhoneNum())
                    .build();

            return DefaultResponse.response(StatusCode.OK,
                    ResponseMessage.USER_SEARCH_SUCCESS,
                    userInfoResponse);
        } catch (Exception e) {
            log.error(e.getMessage());

            return DefaultResponse.response(StatusCode.INTERNAL_SERVER_ERROR,
                    ResponseMessage.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/user/all")
    public DefaultResponse getUserAll() {
        try {
            DefaultResponse response = userService.findAllUsers();
            List<User> findAllUser = (List<User>) response.getData();

            if (findAllUser == null) {
                String[] empty = new String[0];
                return DefaultResponse.response(StatusCode.OK,
                        ResponseMessage.NOT_FOUND_USER,
                        empty);
            }

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

            return DefaultResponse.response(response.getStatus(),
                    response.getMessage(),
                    collect);
        } catch (Exception e) {
            log.error(e.getMessage());
            return DefaultResponse.response(StatusCode.INTERNAL_SERVER_ERROR,
                    ResponseMessage.INTERNAL_SERVER_ERROR);
        }
    }

    @Auth
    @DeleteMapping("/user")
    public DefaultResponse deleteUser(
             @RequestHeader("Authorization") final String header) { // token 인증 방식으로 변경
        try {
            if (header == null) {
                return DefaultResponse.response(StatusCode.UNAUTHORIZED,
                        ResponseMessage.UNAUTHORIZED);
            }
            User findUser = (User) userService.findOneById(jwtService.decode(header)).getData();

            if (findUser == null) {
                String[] empty = new String[0];
                return DefaultResponse.response(StatusCode.OK,
                        ResponseMessage.NOT_EXIST_USER);
            }

            userService.delete(findUser.getId());

            return DefaultResponse.response(StatusCode.OK,
                    ResponseMessage.USER_DELETE_SUCCESS);
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            return DefaultResponse.response(StatusCode.INTERNAL_SERVER_ERROR,
                    ResponseMessage.INTERNAL_SERVER_ERROR);
        }
    }
}

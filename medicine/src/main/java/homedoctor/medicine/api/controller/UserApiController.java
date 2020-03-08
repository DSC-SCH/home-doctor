package homedoctor.medicine.api.controller;

import homedoctor.medicine.domain.User;
import homedoctor.medicine.api.dto.user.*;
import homedoctor.medicine.service.ConnectionUserService;
import homedoctor.medicine.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class UserApiController {

    private final UserService userService;

    // 엔티티 직접 노출되지 않게 바꾸기.
    // api 스펙이 변하면 안됨.
    @GetMapping("/user/{user_id}")
    public User findUserOne(
            @PathVariable("user_id") Long id) {
        return userService.findOne(id);
    }

    @GetMapping("/api/v1/users")
    public UserAllResult userResult() {
        List<User> findAllUser = userService.findUsers();
        List<UserDto> collect = findAllUser.stream()
                .map(m -> new UserDto(m.getUsername(), m.getPassword()))
                .collect(Collectors.toList());

        return new UserAllResult(collect.size(), collect);
    }

    @PostMapping("/user")
    public CreateUserResponse saveUser(
            @RequestBody @Valid CreateUserRequest request) {
        User user = User.builder().username(request.getName())
                .build();
        Long id = userService.join(user);
        return new CreateUserResponse(id);
    }

    @PutMapping("/user/{user_id}")
    public UpdateMemberResponse updateMemberResponse(
            @PathVariable("user_id") Long id,
            @RequestBody @Valid UpdateMemberRequest request) {

        userService.update(id, request.getName());
        User findUser = userService.findOne(id);

        return new UpdateMemberResponse(findUser.getId(), findUser.getUsername());
    }

    @DeleteMapping("/user/{user_id}")
    public void deleteUserResponse(
            @PathVariable("user_id") Long id) {
        User findUser = userService.findOne(id);
        userService.deleteById(findUser);
    }



}

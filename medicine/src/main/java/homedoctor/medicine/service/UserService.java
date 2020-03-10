package homedoctor.medicine.service;

import homedoctor.medicine.api.dto.user.request.CreateUserRequest;
import homedoctor.medicine.common.ResponseMessage;
import homedoctor.medicine.common.StatusCode;
import homedoctor.medicine.domain.User;
import homedoctor.medicine.dto.DefaultUserResponse;
import homedoctor.medicine.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    /**
     * 회원 가입
     */
    @Transactional
    public DefaultUserResponse join(CreateUserRequest request) {
        try {
            if (request.validProperties()) {
                User user = User.builder()
                        .username(request.getUsername())
                        .birthday(request.getBirthday())
                        .email(request.getEmail())
                        .snsId(request.getSnsId())
                        .genderType(request.getGenderType())
                        .phoneNum(request.getPhoneNum())
                        .snsType(request.getSnsType())
                        .token(request.getToken())
                        .build();

                if (!validateDuplicateUser(user)) {
                    return DefaultUserResponse.builder()
                            .status(StatusCode.METHOD_NOT_ALLOWED)
                            .responseMessgae(ResponseMessage.DUPLICATED_USER)
                            .build();
                };

                userRepository.save(user);
                return DefaultUserResponse.builder()
                        .status(StatusCode.OK)
                        .responseMessgae(ResponseMessage.USER_CREATE_SUCCESS)
                        .build();
            }

            return DefaultUserResponse.builder()
                    .status(StatusCode.METHOD_NOT_ALLOWED)
                    .responseMessgae(ResponseMessage.NOT_CONTENT)
                    .build();
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());
            return DefaultUserResponse.builder()
                    .status(StatusCode.DB_ERROR)
                    .responseMessgae(ResponseMessage.DB_ERROR)
                    .build();
        }
    }

    private boolean validateDuplicateUser(User user) {
        List<User> findUsers =
                userRepository.findByEmail(user);

        if (!findUsers.isEmpty()) {
            return false;
        }
        return true;
    }

    // 회원 전체 조회
    public DefaultUserResponse findAllUsers() {
        try {
            List<User> findAllUser = userRepository.findAll();

            if (!findAllUser.isEmpty()) {
                return DefaultUserResponse.builder()
                        .status(StatusCode.OK)
                        .responseMessgae(ResponseMessage.USER_SEARCH_SUCCESS)
                        .userList(findAllUser)
                        .build();
            }
            return DefaultUserResponse.builder()
                    .status(StatusCode.METHOD_NOT_ALLOWED)
                    .responseMessgae(ResponseMessage.USER_SEARCH_FAIL)
                    .build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return DefaultUserResponse.builder()
                    .status(StatusCode.DB_ERROR)
                    .responseMessgae(ResponseMessage.DB_ERROR)
                    .build();
        }
    }

    public DefaultUserResponse findOneById(Long id) {
        try {
            User findUser = userRepository.findOneById(id);

            if (findUser != null) {
                return DefaultUserResponse.builder()
                        .status(StatusCode.OK)
                        .responseMessgae(ResponseMessage.USER_SEARCH_SUCCESS)
                        .user(findUser)
                        .build();
            }
            return DefaultUserResponse.builder()
                    .status(StatusCode.METHOD_NOT_ALLOWED)
                    .responseMessgae(ResponseMessage.USER_SEARCH_FAIL)
                    .build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return DefaultUserResponse.builder()
                    .status(StatusCode.DB_ERROR)
                    .responseMessgae(ResponseMessage.DB_ERROR)
                    .build();
        }
    }

//    @Transactional
//    public void update(User user, String name) {
//        User findUser = userRepository.findOneById(user);
//    }

    @Transactional
    public DefaultUserResponse deleteById(Long id) {
        try {
            User findUser = userRepository.findOneById(id);
            if (findUser != null) {
                userRepository.deleteByUserId(findUser);

                return DefaultUserResponse.builder()
                        .status(StatusCode.OK)
                        .responseMessgae(ResponseMessage.USER_DELETE_SUCCESS)
                        .build();
            }
            return DefaultUserResponse.builder()
                    .status(StatusCode.METHOD_NOT_ALLOWED)
                    .responseMessgae(ResponseMessage.USER_DELETE_FAIL)
                    .build();

        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());

            return DefaultUserResponse.builder()
                    .status(StatusCode.DB_ERROR)
                    .responseMessgae(ResponseMessage.DB_ERROR)
                    .build();
        }

    }
}

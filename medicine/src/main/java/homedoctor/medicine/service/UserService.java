package homedoctor.medicine.service;

import homedoctor.medicine.api.dto.DefaultResponse;
import homedoctor.medicine.common.ResponseMessage;
import homedoctor.medicine.common.StatusCode;
import homedoctor.medicine.domain.SnsType;
import homedoctor.medicine.domain.User;
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

    private final LabelService labelService;

    private final JwtService jwtService;

    /**
     * 회원 가입
     */
    @Transactional
    public DefaultResponse save(User user) {
        try {
            if (!validateDuplicateUser(user)) {
                return DefaultResponse.builder()
                        .status(StatusCode.CONFLICT)
                        .message(ResponseMessage.DUPLICATED_USER)
                        .build();
            }

            userRepository.save(user);

            // Create Default Label
            labelService.createDefaultLabel(user);

            return DefaultResponse.builder()
                    .status(StatusCode.OK)
                    .message(ResponseMessage.USER_CREATE_SUCCESS)
                    .build();
     } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());
            return DefaultResponse.builder()
                    .status(StatusCode.DB_ERROR)
                    .message(ResponseMessage.DB_ERROR)
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
    public DefaultResponse findAllUsers() {
        try {
            List<User> findAllUser = userRepository.findAll();

            if (!findAllUser.isEmpty()) {
                return DefaultResponse.builder()
                        .status(StatusCode.OK)
                        .message(ResponseMessage.USER_SEARCH_SUCCESS)
                        .data(findAllUser)
                        .build();
            }
            return DefaultResponse.builder()
                    .status(StatusCode.METHOD_NOT_ALLOWED)
                    .message(ResponseMessage.USER_SEARCH_FAIL)
                    .build();

        } catch (Exception e) {
            log.error(e.getMessage());
            return DefaultResponse.builder()
                    .status(StatusCode.DB_ERROR)
                    .message(ResponseMessage.DB_ERROR)
                    .build();
        }
    }

    public DefaultResponse findOneById(Long id) {
        try {
            User findUser = userRepository.findOneById(id);

            if (findUser != null) {
                return DefaultResponse.builder()
                        .status(StatusCode.OK)
                        .message(ResponseMessage.USER_SEARCH_SUCCESS)
                        .data(findUser)
                        .build();
            }
            return DefaultResponse.builder()
                    .status(StatusCode.UNAUTHORIZED)
                    .message(ResponseMessage.UNAUTHORIZED)
                    .build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return DefaultResponse.builder()
                    .status(StatusCode.DB_ERROR)
                    .message(ResponseMessage.DB_ERROR)
                    .build();
        }
    }

    public DefaultResponse findOneSnsId(String snsId, SnsType snsType) {
        try {
            User findUser = userRepository.findOneBySnsId(snsId, snsType);

            if (findUser != null) {
                return DefaultResponse.builder()
                        .status(StatusCode.OK)
                        .message(ResponseMessage.USER_SEARCH_SUCCESS)
                        .data(findUser)
                        .build();
            }
            return DefaultResponse.builder()
                    .status(StatusCode.UNAUTHORIZED)
                    .message(ResponseMessage.UNAUTHORIZED)
                    .build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return DefaultResponse.builder()
                    .status(StatusCode.DB_ERROR)
                    .message(ResponseMessage.DB_ERROR)
                    .build();
        }
    }


//    @Transactional
//    public void update(User user, String name) {
//        User findUser = userRepository.findOneById(user);
//    }

    @Transactional
    public DefaultResponse delete(Long id) {
        try {
            User findUser = userRepository.findOneById(id);
            if (findUser != null) {
                userRepository.deleteByUserId(findUser);

                return DefaultResponse.builder()
                        .status(StatusCode.OK)
                        .message(ResponseMessage.USER_DELETE_SUCCESS)
                        .build();
            }
            return DefaultResponse.builder()
                    .status(StatusCode.UNAUTHORIZED)
                    .message(ResponseMessage.UNAUTHORIZED)
                    .build();

        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());

            return DefaultResponse.builder()
                    .status(StatusCode.DB_ERROR)
                    .message(ResponseMessage.DB_ERROR)
                    .build();
        }

    }
}

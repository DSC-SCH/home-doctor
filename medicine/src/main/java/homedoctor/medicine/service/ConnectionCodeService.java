package homedoctor.medicine.service;

import homedoctor.medicine.api.dto.DefaultResponse;
import homedoctor.medicine.domain.ConnectionCode;
import homedoctor.medicine.domain.User;
import homedoctor.medicine.repository.ConnectionCodeRepository;
import homedoctor.medicine.common.ResponseMessage;
import homedoctor.medicine.common.StatusCode;
import homedoctor.medicine.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.Date;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ConnectionCodeService {

    private final ConnectionCodeRepository connectionCodeRepository;

    private final UserRepository userRepository;

    public String createRandomCode() {
        int codeLen = 4;
        Random random = new Random((System.currentTimeMillis()));
        int range = (int) Math.pow(10 ,codeLen);
        int trim = (int) Math.pow(10 ,codeLen - 1);
        int result = random.nextInt(range) + trim;

        if (result > range) {
            result = result - 1;
        }

        return String.valueOf(result).substring(0, 4);
    }

    @Transactional
    public DefaultResponse saveCode(Long userId) {
        try {
            ConnectionCode checkPreCode = connectionCodeRepository.findOneByUser(userId);

            // 유저가 코드 재생성시 기존에 생성한 코드 삭제.
            if (checkPreCode != null) {
                connectionCodeRepository.delete(checkPreCode.getId());
            }

            String createdCode;
            createdCode = createRandomCode();


            // 코드 중복 확인
            while (true) {
                boolean isSameCode = connectionCodeRepository.isSameCode(createdCode);
                if (!isSameCode) {
                    break;
                }
                createdCode = createRandomCode();
            }

            ConnectionCode findCodeByUser = connectionCodeRepository.findOneByUser(userId);
            // 해당 유저의 기존 연결 코드 삭제
            if (findCodeByUser != null) {
                connectionCodeRepository.delete(findCodeByUser.getId());
            }


            User createUser = userRepository.findOneById(userId);
            // life 에 현재 datetime 저장. 나중에 승인 요청시 비교.
            Date currentDate = new Date();
            ConnectionCode connectionCode = ConnectionCode.builder()
                    .user(createUser)
                    .code(createdCode)
                    .life(currentDate)
                    .build();

            connectionCodeRepository.save(connectionCode);
            return DefaultResponse.builder()
                    .status(StatusCode.OK)
                    .message(ResponseMessage.CODE_CREATE_SUCCESS)
                    .data(connectionCode)
                    .build();

        } catch (Exception e) {
            //Rollback
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());

            return DefaultResponse.builder()
                    .status(StatusCode.DB_ERROR)
                    .message(ResponseMessage.DB_ERROR)
                    .build();
        }
    }

    public DefaultResponse isSameCode(String code) {
        try {
            boolean isSameCode = connectionCodeRepository.isSameCode(code);

            if (isSameCode) {
                return DefaultResponse.builder()
                        .status(StatusCode.OK)
                        .message(ResponseMessage.CODE_SEARCH_SUCCESS)
                        .data(true)
                        .build();
            }

            return DefaultResponse.builder()
                    .status(StatusCode.METHOD_NOT_ALLOWED)
                    .message(ResponseMessage.NOT_FOUND_CODE)
                    .data(false)
                    .build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return DefaultResponse.builder()
                    .status(StatusCode.DB_ERROR)
                    .message(ResponseMessage.DB_ERROR)
                    .build();
        }

    }

    public DefaultResponse getCode(String code) {
        try {
            ConnectionCode findCode = connectionCodeRepository.getCode(code);

            if (findCode == null) {
                return DefaultResponse.builder()
                        .status(StatusCode.OK)
                        .message(ResponseMessage.CODE_SEARCH_SUCCESS)
                        .build();
            }

            return DefaultResponse.builder()
                    .status(StatusCode.METHOD_NOT_ALLOWED)
                    .message(ResponseMessage.NOT_FOUND_CODE)
                    .data(findCode)
                    .build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return DefaultResponse.builder()
                    .status(StatusCode.DB_ERROR)
                    .message(ResponseMessage.DB_ERROR)
                    .build();
        }

    }

    public DefaultResponse findConnectionCode(Long codeId) {
        try {
            ConnectionCode findCode = connectionCodeRepository.findOne(codeId);
            if (findCode != null) {
                return DefaultResponse.builder()
                        .status(StatusCode.OK)
                        .message(ResponseMessage.CODE_SEARCH_SUCCESS)
                        .data(findCode)
                        .build();
            }

            return DefaultResponse.builder()
                    .status(StatusCode.METHOD_NOT_ALLOWED)
                    .message(ResponseMessage.NOT_FOUND_CODE)
                    .build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return DefaultResponse.builder()
                    .status(StatusCode.DB_ERROR)
                    .message(ResponseMessage.DB_ERROR)
                    .build();
        }
    }
}

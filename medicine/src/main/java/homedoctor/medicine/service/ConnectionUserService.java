package homedoctor.medicine.service;

import homedoctor.medicine.api.dto.DefaultResponse;
import homedoctor.medicine.domain.ConnectionUser;
import homedoctor.medicine.domain.User;
import homedoctor.medicine.repository.ConnectionUserRepository;
import homedoctor.medicine.common.ResponseMessage;
import homedoctor.medicine.common.StatusCode;
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
public class ConnectionUserService {

    // 의존성 주입은 어노테이션이 해줌
    private ConnectionUserRepository connectionUserRepository;

    @Transactional
    public DefaultResponse save(ConnectionUser connectionUser) {
        try {
            connectionUserRepository.save(connectionUser);

            return DefaultResponse.builder()
                    .status(StatusCode.OK)
                    .message(ResponseMessage.CONNECTION_CREATE_SUCCESS)
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

    public DefaultResponse findConnectionById(Long id) {
        try {
            ConnectionUser findConnect = connectionUserRepository.findConnection(id);

            if (findConnect != null) {
                return DefaultResponse.builder()
                        .status(StatusCode.OK)
                        .message(ResponseMessage.CONNECTION_SEARCH_SUCCESS)
                        .data(findConnect)
                        .build();
            }

            return DefaultResponse.builder()
                    .status(StatusCode.OK)
                    .message(ResponseMessage.CONNECTION_SEARCH_FAIL)
                    .build();

        } catch (Exception e) {
            log.error(e.getMessage());
            return DefaultResponse.builder()
                    .status(StatusCode.DB_ERROR)
                    .message(ResponseMessage.DB_ERROR)
                    .build();
        }
    }

    public DefaultResponse findALlManagerByUser(User user) {
        try {
            List<User> managerList = connectionUserRepository.findAllByManagerUser(user);
            if (!managerList.isEmpty()) {
                return DefaultResponse.builder()
                        .status(StatusCode.OK)
                        .message(ResponseMessage.CONNECTION_SEARCH_SUCCESS)
                        .data(managerList)
                        .build();
            }

            return DefaultResponse.builder()
                    .status(StatusCode.OK)
                    .message(ResponseMessage.NOT_FOUND_CONNECTION)
                    .build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return DefaultResponse.builder()
                    .status(StatusCode.DB_ERROR)
                    .message(ResponseMessage.DB_ERROR)
                    .build();
        }
    }

    public DefaultResponse findAllReceiverByUser(User user) {
        try {
            List<User> receiverList = connectionUserRepository.findAllByCareUser(user);
            if (!receiverList.isEmpty()) {
                return DefaultResponse.builder()
                        .status(StatusCode.OK)
                        .message(ResponseMessage.CONNECTION_SEARCH_SUCCESS)
                        .data(receiverList)
                        .build();
            }

            return DefaultResponse.builder()
                    .status(StatusCode.OK)
                    .message(ResponseMessage.NOT_FOUND_CONNECTION)
                    .build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return DefaultResponse.builder()
                    .status(StatusCode.DB_ERROR)
                    .message(ResponseMessage.DB_ERROR)
                    .build();
        }
    }

    @Transactional
    public DefaultResponse delete(ConnectionUser connectionUser) {
        connectionUserRepository.delete(connectionUser);
        try {
            ConnectionUser findConnection = connectionUserRepository.findConnection(connectionUser.getId());
            if (findConnection == null) {
                return DefaultResponse.builder()
                        .status(StatusCode.METHOD_NOT_ALLOWED)
                        .message(ResponseMessage.CONNECTION_DELETE_FAIL)
                        .build();
            }
            return DefaultResponse.builder()
                    .status(StatusCode.OK)
                    .message(ResponseMessage.CONNECTION_DELETE_SUCCESS)
                    .build();
        } catch (Exception e) {
            log.error(e.getMessage());
            // RollBack
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DefaultResponse.builder()
                    .status(StatusCode.DB_ERROR)
                    .message(ResponseMessage.DB_ERROR)
                    .build();
        }
    }


}

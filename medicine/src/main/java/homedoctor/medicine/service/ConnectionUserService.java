package homedoctor.medicine.service;

import homedoctor.medicine.api.dto.DefaultResponse;
import homedoctor.medicine.domain.ConnectionCode;
import homedoctor.medicine.domain.ConnectionUser;
import homedoctor.medicine.domain.User;
import homedoctor.medicine.repository.ConnectionCodeRepository;
import homedoctor.medicine.repository.ConnectionUserRepository;
import homedoctor.medicine.common.ResponseMessage;
import homedoctor.medicine.common.StatusCode;
import homedoctor.medicine.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
public class ConnectionUserService {

    @Autowired
    private ConnectionUserRepository connectionUserRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ConnectionCodeRepository connectionCodeRepository;

    @Transactional
    public DefaultResponse save(Long managerUserId, Long receiverUserId) {
        try {
            ConnectionUser findConnectionIsManager = connectionUserRepository.findConnectionByUser(managerUserId);
            User managerUser = userRepository.findOneById(managerUserId);
            User receiverUser = userRepository.findOneById(receiverUserId);

            // 이미 등록된 유저인지 확인.
            if (findConnectionIsManager != null) {
                if (receiverUserId == findConnectionIsManager.getCareUser().getId()) {
                    return DefaultResponse.response(StatusCode.METHOD_NOT_ALLOWED,
                            ResponseMessage.DUPLICATED_CONNECTION);
                }
            }


            ConnectionUser connectionUser = ConnectionUser.createConnection(managerUser, receiverUser);
            ConnectionCode connectionCode = connectionCodeRepository.findOneByUser(receiverUserId);

            connectionUserRepository.save(connectionUser);
            // 연동 성공과 동시에 삭제.
            connectionCodeRepository.delete(connectionCode.getId());

            return DefaultResponse.builder()
                    .status(StatusCode.OK)
                    .message(ResponseMessage.CONNECTION_CREATE_SUCCESS)
                    .build();

        } catch (Exception e) {
            //Rollback
            e.printStackTrace();
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

    public DefaultResponse findAllManagerByUser(User user) {
        try {
            List<ConnectionUser> managerList = connectionUserRepository.findAllByManagerUser(user);
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
            e.printStackTrace();
            return DefaultResponse.builder()
                    .status(StatusCode.DB_ERROR)
                    .message(ResponseMessage.DB_ERROR)
                    .build();
        }
    }

    public DefaultResponse findAllReceiverByUser(User user) {
        try {
            List<ConnectionUser> receiverList = connectionUserRepository.findAllByCareUser(user);
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
            e.printStackTrace();
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

            connectionUserRepository.delete(connectionUser);
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

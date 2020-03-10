package homedoctor.medicine.service;

import homedoctor.medicine.api.dto.connection.request.CreateConnectionRequest;
import homedoctor.medicine.domain.ConnectionUser;
import homedoctor.medicine.domain.User;
import homedoctor.medicine.dto.DefaultConnectionResponse;
import homedoctor.medicine.repository.ConnectionUserRepository;
import homedoctor.medicine.common.ResponseMessage;
import homedoctor.medicine.common.StatusCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private ConnectionUserRepository connectionUserRepository;

    @Transactional
    public DefaultConnectionResponse save(CreateConnectionRequest request) {
        try {
            if (request.validProperties()) {
                ConnectionUser connectionUser = ConnectionUser.createConnection(request.getManager(), request.getReceiver());
                connectionUserRepository.save(connectionUser);

                return DefaultConnectionResponse.builder()
                        .status(StatusCode.OK)
                        .responseMessage(ResponseMessage.CONNECTION_CREATE_SUCCESS)
                        .manager(request.getManager())
                        .receiver(request.getReceiver())
                        .build();
            }

            return DefaultConnectionResponse.builder()
                    .status(StatusCode.METHOD_NOT_ALLOWED)
                    .responseMessage(ResponseMessage.NOT_CONTENT)
                    .manager(null)
                    .receiver(null)
                    .build();
        } catch (Exception e) {
            //Rollback
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());

            return DefaultConnectionResponse.builder()
                    .status(StatusCode.DB_ERROR)
                    .responseMessage(ResponseMessage.DB_ERROR)
                    .manager(null)
                    .receiver(null)
                    .build();
        }
    }

    public DefaultConnectionResponse findConnectionById(Long id) {
        try {
            ConnectionUser findConnect = connectionUserRepository.findConnection(id);

            if (findConnect != null) {
                return DefaultConnectionResponse.builder()
                        .status(StatusCode.OK)
                        .responseMessage(ResponseMessage.ALARM_SEARCH_SUCCESS)
                        .connectionUser(findConnect)
                        .receiver(findConnect.getCareUser())
                        .manager(findConnect.getUser())
                        .build();
            }

            return DefaultConnectionResponse.builder()
                    .status(StatusCode.OK)
                    .responseMessage(ResponseMessage.ALARM_SEARCH_FAIL)
                    .build();

        } catch (Exception e) {
            log.error(e.getMessage());
            return DefaultConnectionResponse.builder()
                    .status(StatusCode.DB_ERROR)
                    .responseMessage(ResponseMessage.DB_ERROR)
                    .build();
        }
    }

    @Transactional
    public DefaultConnectionResponse delete(ConnectionUser connectionUser) {
        connectionUserRepository.delete(connectionUser);
        try {
            ConnectionUser findConnection = connectionUserRepository.findConnection(connectionUser.getId());
            if (findConnection == null) {
                return DefaultConnectionResponse.builder()
                        .status(StatusCode.METHOD_NOT_ALLOWED)
                        .responseMessage(ResponseMessage.NOT_FOUND_ALARM)
                        .manager(null)
                        .receiver(null)
                        .build();
            }
            return DefaultConnectionResponse.builder()
                    .status(StatusCode.OK)
                    .responseMessage(ResponseMessage.ALARM_DELETE_SUCCESS)
                    .manager(null)
                    .manager(null)
                    .build();
        } catch (Exception e) {
            log.error(e.getMessage());
            // RollBack
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DefaultConnectionResponse.builder()
                    .status(StatusCode.DB_ERROR)
                    .responseMessage(ResponseMessage.DB_ERROR)
                    .manager(connectionUser.getUser())
                    .receiver(connectionUser.getCareUser())
                    .build();
        }
    }

    public DefaultConnectionResponse findAllReceiverByUser(User user) {
        try {
            List<User> receiverList = connectionUserRepository.findAllByCareUser(user);
            if (!receiverList.isEmpty()) {
                return DefaultConnectionResponse.builder()
                        .status(StatusCode.OK)
                        .responseMessage(ResponseMessage.ALARM_SEARCH_SUCCESS)
                        .managerList(null)
                        .receiverList(receiverList)
                        .build();
            }

            return DefaultConnectionResponse.builder()
                    .status(StatusCode.OK)
                    .responseMessage(ResponseMessage.NOT_FOUND_ALARM)
                    .managerList(null)
                    .receiverList(null)
                    .build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return DefaultConnectionResponse.builder()
                    .status(StatusCode.DB_ERROR)
                    .responseMessage(ResponseMessage.DB_ERROR)
                    .managerList(null)
                    .receiverList(null)
                    .build();
        }
    }

    public DefaultConnectionResponse findALlManagerByUser(User user) {
        try {
            List<User> managerList = connectionUserRepository.findAllByManagerUser(user);
            if (!managerList.isEmpty()) {
                return DefaultConnectionResponse.builder()
                        .status(StatusCode.OK)
                        .responseMessage(ResponseMessage.ALARM_SEARCH_SUCCESS)
                        .managerList(managerList)
                        .receiverList(null)
                        .build();
            }

            return DefaultConnectionResponse.builder()
                    .status(StatusCode.OK)
                    .responseMessage(ResponseMessage.NOT_FOUND_ALARM)
                    .managerList(null)
                    .receiverList(null)
                    .build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return DefaultConnectionResponse.builder()
                    .status(StatusCode.DB_ERROR)
                    .responseMessage(ResponseMessage.DB_ERROR)
                    .managerList(null)
                    .receiverList(null)
                    .build();
        }
    }

}

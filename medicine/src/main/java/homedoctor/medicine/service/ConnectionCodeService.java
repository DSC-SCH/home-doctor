package homedoctor.medicine.service;

import homedoctor.medicine.api.dto.DefaultResponse;
import homedoctor.medicine.domain.ConnectionCode;
import homedoctor.medicine.repository.ConnectionCodeRepository;
import homedoctor.medicine.common.ResponseMessage;
import homedoctor.medicine.common.StatusCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ConnectionCodeService {

    private final ConnectionCodeRepository connectionCodeRepository;

    @Transactional
    public DefaultResponse saveCode(ConnectionCode code) {
        try {
            connectionCodeRepository.save(code);

            return DefaultResponse.builder()
                    .status(StatusCode.OK)
                    .message(ResponseMessage.CODE_CREATE_SUCCESS)
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

    public DefaultResponse findCode(Long codeId) {
        try {
            ConnectionCode findCode = connectionCodeRepository.findOne(codeId);
            if (findCode != null) {
                return DefaultResponse.builder()
                        .status(StatusCode.OK)
                        .message(ResponseMessage.CODE_SEARCH_SUCCESS)
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

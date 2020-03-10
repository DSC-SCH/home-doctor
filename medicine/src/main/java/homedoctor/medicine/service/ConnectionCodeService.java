package homedoctor.medicine.service;

import homedoctor.medicine.api.dto.code.request.CreateCodeRequest;
import homedoctor.medicine.domain.ConnectionCode;
import homedoctor.medicine.dto.DefaultCodeResponse;
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
    public DefaultCodeResponse saveCode(CreateCodeRequest request) {
        try {
            if (request.validProperties()) {
                ConnectionCode code = ConnectionCode.builder()
                        .user(request.getUser())
                        .code(request.getCode())
                        .life(request.getLife())
                        .build();

                connectionCodeRepository.save(code);

                return DefaultCodeResponse.builder()
                        .status(StatusCode.OK)
                        .responseMessage(ResponseMessage.CODE_CREATE_SUCCESS)
                        .createUser(code.getUser())
                        .life(code.getLife())
                        .connectionCode(code)
                        .build();
            }

            return DefaultCodeResponse.builder()
                    .status(StatusCode.METHOD_NOT_ALLOWED)
                    .responseMessage(ResponseMessage.NOT_CONTENT)
                    .build();
        } catch (Exception e) {
            //Rollback
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());

            return DefaultCodeResponse.builder()
                    .status(StatusCode.DB_ERROR)
                    .responseMessage(ResponseMessage.DB_ERROR)
                    .build();
        }
    }

    public DefaultCodeResponse findCode(Long codeId) {
        try {
            ConnectionCode findCode = connectionCodeRepository.findOne(codeId);
            if (findCode != null) {
                return DefaultCodeResponse.builder()
                        .status(StatusCode.OK)
                        .responseMessage(ResponseMessage.CODE_SEARCH_SUCCESS)
                        .build();
            }

            return DefaultCodeResponse.builder()
                    .status(StatusCode.METHOD_NOT_ALLOWED)
                    .responseMessage(ResponseMessage.NOT_FOUND_CODE)
                    .build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return DefaultCodeResponse.builder()
                    .status(StatusCode.DB_ERROR)
                    .responseMessage(ResponseMessage.DB_ERROR)
                    .build();
        }
    }
}

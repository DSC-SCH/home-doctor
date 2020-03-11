package homedoctor.medicine.service;

import homedoctor.medicine.api.dto.DefaultResponse;
import homedoctor.medicine.domain.Label;
import homedoctor.medicine.domain.User;
import homedoctor.medicine.repository.LabelRepository;
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
public class LabelService {

    private final LabelRepository labelRepository;

    @Transactional
    public DefaultResponse save(Label label) {
        try {
            labelRepository.save(label);

            return DefaultResponse.builder()
                    .status(StatusCode.OK)
                    .message(ResponseMessage.LABEL_CREATE_SUCCESS)
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

    @Transactional
    public DefaultResponse createDefaultLabel(User user) {
        try {
            Label labelOnce = Label.builder()
                    .user(user)
                    .title("없음")
                    .color("#FFFFFF")
                    .build();

            Label labelTwice = Label.builder()
                    .user(user)
                    .title("감기")
                    .color("#FFD5D5")
                    .build();

            Label labelThird = Label.builder()
                    .user(user)
                    .title("비염")
                    .color("#B6F6B2")
                    .build();

            Label labelFourth = Label.builder()
                    .user(user)
                    .title("알레르기")
                    .color("#D6D6FF")
                    .build();
            labelRepository.save(labelOnce);
            labelRepository.save(labelTwice);
            labelRepository.save(labelThird);
            labelRepository.save(labelFourth);

            return DefaultResponse.response(StatusCode.OK,
                    ResponseMessage.LABEL_CREATE_SUCCESS);
        } catch (Exception e) {
            return DefaultResponse.response(StatusCode.DB_ERROR,
                    ResponseMessage.DB_ERROR);
        }
    }

    public DefaultResponse fineLabelsByUser(User user) {
        try {
            List<Label> labels = labelRepository.findAllByUser(user);

            if (!labels.isEmpty()) {
                return DefaultResponse.builder()
                        .status(StatusCode.OK)
                        .message(ResponseMessage.LABEL_SEARCH_SUCCESS)
                        .data(labels)
                        .build();
            }

            return DefaultResponse.builder()
                    .status(StatusCode.METHOD_NOT_ALLOWED)
                    .message(ResponseMessage.NOT_FOUND_LABEL)
                    .build();
        } catch (Exception e) {
            log.error(e.getMessage());

            return DefaultResponse.builder()
                    .status(StatusCode.DB_ERROR)
                    .message(ResponseMessage.DB_ERROR)
                    .build();
        }
    }

    public DefaultResponse findOne(Long labelId) {
        try {
            Label findLabel = labelRepository.findOne(labelId);

            if (findLabel != null) {
                return DefaultResponse.builder()
                        .status(StatusCode.OK)
                        .message(ResponseMessage.LABEL_SEARCH_SUCCESS)
                        .data(findLabel)
                        .build();
            }

            return DefaultResponse.builder()
                    .status(StatusCode.METHOD_NOT_ALLOWED)
                    .message(ResponseMessage.NOT_FOUND_LABEL)
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
    public DefaultResponse update(Long labelId, Label label) {
        try {
            Label findLabel = labelRepository.findOne(labelId);

            if (findLabel == null) {
                return DefaultResponse.builder()
                        .status(StatusCode.OK)
                        .message(ResponseMessage.NOT_FOUND_ALARM)
                        .build();
            }

            findLabel.setTitle(label.getTitle());
            findLabel.setColor(label.getColor());
            labelRepository.save(findLabel);

            return DefaultResponse.builder()
                    .status(StatusCode.OK)
                    .message(ResponseMessage.ALARM_UPDATE_SUCCESS)
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

    @Transactional
    public DefaultResponse delete(Long labelId) {

        try {
            Label findLabel = labelRepository.findOne(labelId);

            if (findLabel != null) {
                return DefaultResponse.builder()
                        .status(StatusCode.OK)
                        .message(ResponseMessage.LABEL_DELETE_SUCCESS)
                        .build();
            }

            return DefaultResponse.builder()
                    .status(StatusCode.METHOD_NOT_ALLOWED)
                    .message(ResponseMessage.LABEL_DELETE_FAIL)
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

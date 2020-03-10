package homedoctor.medicine.service;

import homedoctor.medicine.api.dto.label.reqeust.CreateLabelRequest;
import homedoctor.medicine.api.dto.label.reqeust.UpdateLabelRequest;
import homedoctor.medicine.domain.Label;
import homedoctor.medicine.domain.User;
import homedoctor.medicine.dto.DefaultLabelResponse;
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
public class    LabelService {

    private final LabelRepository labelRepository;

    @Transactional
    public DefaultLabelResponse saveLabel(CreateLabelRequest request) {
        try {
            if (request.validProperties()) {
                Label label = Label.builder()
                        .user(request.getUser())
                        .title(request.getTitle())
                        .color(request.getColor())
                        .build();

                labelRepository.save(label);

                return DefaultLabelResponse.builder()
                        .status(StatusCode.OK)
                        .responseMessage(ResponseMessage.LABEL_CREATE_SUCCESS)
                        .label(label)
                        .labelList(null)
                        .build();
            }

            return DefaultLabelResponse.builder()
                    .status(StatusCode.METHOD_NOT_ALLOWED)
                    .responseMessage(ResponseMessage.NOT_CONTENT)
                    .label(null)
                    .labelList(null)
                    .build();
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());

            return DefaultLabelResponse.builder()
                    .status(StatusCode.DB_ERROR)
                    .responseMessage(ResponseMessage.DB_ERROR)
                    .label(null)
                    .labelList(null)
                    .build();
        }

    }

    public DefaultLabelResponse fineLabelsByUser(User user) {
        try {
            List<Label> labels = labelRepository.findAllByUser(user);

            if (!labels.isEmpty()) {
                return DefaultLabelResponse.builder()
                        .status(StatusCode.OK)
                        .responseMessage(ResponseMessage.LABEL_SEARCH_SUCCESS)
                        .labelList(labels)
                        .label(null)
                        .build();
            }

            return DefaultLabelResponse.builder()
                    .status(StatusCode.METHOD_NOT_ALLOWED)
                    .responseMessage(ResponseMessage.NOT_FOUND_LABEL)
                    .label(null)
                    .labelList(null)
                    .build();
        } catch (Exception e) {
            log.error(e.getMessage());

            return DefaultLabelResponse.builder()
                    .status(StatusCode.DB_ERROR)
                    .responseMessage(ResponseMessage.DB_ERROR)
                    .label(null)
                    .labelList(null)
                    .build();
        }
    }

    public DefaultLabelResponse findOne(Long labelId) {
        try {
            Label findLabel = labelRepository.findOne(labelId);

            if (findLabel != null) {
                return DefaultLabelResponse.builder()
                        .status(StatusCode.OK)
                        .responseMessage(ResponseMessage.LABEL_SEARCH_SUCCESS)
                        .labelList(null)
                        .label(findLabel)
                        .build();
            }

            return DefaultLabelResponse.builder()
                    .status(StatusCode.METHOD_NOT_ALLOWED)
                    .responseMessage(ResponseMessage.NOT_FOUND_LABEL)
                    .label(null)
                    .labelList(null)
                    .build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return DefaultLabelResponse.builder()
                    .status(StatusCode.DB_ERROR)
                    .responseMessage(ResponseMessage.DB_ERROR)
                    .label(null)
                    .labelList(null)
                    .build();
        }
    }

    @Transactional
    public DefaultLabelResponse delete(Label label) {

        try {
            Label findLabel = labelRepository.findOne(label.getId());

            if (findLabel != null) {
                return DefaultLabelResponse.builder()
                        .status(StatusCode.OK)
                        .responseMessage(ResponseMessage.LABEL_DELETE_SUCCESS)
                        .labelList(null)
                        .label(null)
                        .build();
            }

            return DefaultLabelResponse.builder()
                    .status(StatusCode.METHOD_NOT_ALLOWED)
                    .responseMessage(ResponseMessage.NOT_FOUND_LABEL)
                    .label(null)
                    .labelList(null)
                    .build();
        } catch (Exception e) {
            log.error(e.getMessage());
            // RollBack
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DefaultLabelResponse.builder()
                    .status(StatusCode.DB_ERROR)
                    .responseMessage(ResponseMessage.DB_ERROR)
                    .label(null)
                    .labelList(null)
                    .build();
        }
    }

    @Transactional
    public DefaultLabelResponse update(Long labelId, UpdateLabelRequest request) {
        try {
            Label findLabel = labelRepository.findOne(labelId);

            if (findLabel == null) {
                return DefaultLabelResponse.builder()
                        .status(StatusCode.OK)
                        .responseMessage(ResponseMessage.NOT_FOUND_ALARM)
                        .label(null)
                        .labelList(null)
                        .build();
            }

            if (request.validProperties() == true) {
                findLabel.setTitle(request.getTitle());
                findLabel.setColor(request.getColor());
                labelRepository.save(findLabel);

                return DefaultLabelResponse.builder()
                        .status(StatusCode.OK)
                        .responseMessage(ResponseMessage.ALARM_UPDATE_SUCCESS)
                        .label(findLabel)
                        .labelList(null)
                        .build();
            }

            return DefaultLabelResponse.builder()
                    .status(StatusCode.METHOD_NOT_ALLOWED)
                    .responseMessage(ResponseMessage.NOT_CONTENT)
                    .label(null)
                    .labelList(null)
                    .build();
        } catch (Exception e) {
            log.error(e.getMessage());
            // RollBack
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();

            return DefaultLabelResponse.builder()
                    .status(StatusCode.DB_ERROR)
                    .responseMessage(ResponseMessage.DB_ERROR)
                    .label(null)
                    .labelList(null)
                    .build();
        }
    }
}

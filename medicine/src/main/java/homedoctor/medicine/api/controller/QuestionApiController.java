package homedoctor.medicine.api.controller;

import homedoctor.medicine.api.dto.DefaultResponse;
import homedoctor.medicine.api.dto.question.QuestionRequest;
import homedoctor.medicine.common.ResponseMessage;
import homedoctor.medicine.common.StatusCode;
import homedoctor.medicine.domain.Question;
import homedoctor.medicine.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Arrays;

@Slf4j
@RestController
@RequiredArgsConstructor
public class QuestionApiController {

    @Autowired
    private QuestionRepository questionRepository;

    @PostMapping("/question")
    public DefaultResponse saveQuestion(
            @RequestBody @Valid QuestionRequest request) {
        try {

            if (!request.validProperties()) {
                return DefaultResponse.response(StatusCode.BAD_REQUEST,
                        ResponseMessage.NOT_CONTENT);
            }

            Question question = Question.builder()
                    .content(request.getContent())
                    .email(request.getEmail())
                    .build();

            questionRepository.save(question);
            return DefaultResponse.response(StatusCode.OK,
                    ResponseMessage.SUCCESS_QUESTION);

        } catch (Exception e) {
            log.error(e.getMessage());
            log.error(Arrays.toString(e.getStackTrace()));

            return DefaultResponse.response(StatusCode.INTERNAL_SERVER_ERROR,
                    ResponseMessage.INTERNAL_SERVER_ERROR);
        }
    }
}

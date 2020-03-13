package homedoctor.medicine.service;

import homedoctor.medicine.api.dto.DefaultResponse;
import homedoctor.medicine.common.ResponseMessage;
import homedoctor.medicine.common.StatusCode;
import homedoctor.medicine.domain.Terms;
import homedoctor.medicine.repository.TermRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TermService {

    @Autowired
    private final TermRepository termRepository;

    public DefaultResponse findTerms() {
        try {
            List<Terms> terms = termRepository.findTerms();
            return DefaultResponse.response(StatusCode.OK,
                    ResponseMessage.FOUND_TERMS,
                    terms);
        } catch (Exception e) {
            return DefaultResponse.response(StatusCode.DB_ERROR,
                    ResponseMessage.DB_ERROR);
        }
    }
}

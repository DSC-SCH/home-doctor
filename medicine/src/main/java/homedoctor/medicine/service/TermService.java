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

    public DefaultResponse findTermsAll() {
        try {
            List<Terms> terms = termRepository.findTerms();

            if (terms == null || terms.isEmpty()) {
                return DefaultResponse.response(StatusCode.OK,
                        ResponseMessage.NOT_FOUND_TERMS);
            }

            return DefaultResponse.response(StatusCode.OK,
                    ResponseMessage.FOUND_TERMS,
                    terms);
        } catch (Exception e) {
            return DefaultResponse.response(StatusCode.DB_ERROR,
                    ResponseMessage.DB_ERROR);
        }
    }

    public DefaultResponse findTerm() {
        try {
            Terms terms = termRepository.findTermsByTitle("이용약관");

            if (terms == null) {
                return DefaultResponse.response(StatusCode.OK,
                        ResponseMessage.NOT_FOUND_TERMS);
            }

            return DefaultResponse.response(StatusCode.OK,
                    ResponseMessage.FOUND_TERMS,
                    terms);
        } catch (Exception e) {
            return DefaultResponse.response(StatusCode.DB_ERROR,
                    ResponseMessage.DB_ERROR);
        }
    }


    public DefaultResponse findPrivacy() {
        try {
            Terms privacyInfo = termRepository.findTermsByTitle("개인정보처리방침");

            if (privacyInfo == null) {
                return DefaultResponse.response(StatusCode.OK,
                        ResponseMessage.NOT_FOUND_TERMS);
            }

                return DefaultResponse.response(StatusCode.OK,
                    ResponseMessage.FOUND_TERMS,
                    privacyInfo);
        } catch (Exception e) {
            return DefaultResponse.response(StatusCode.DB_ERROR,
                    ResponseMessage.DB_ERROR);
        }
    }
}

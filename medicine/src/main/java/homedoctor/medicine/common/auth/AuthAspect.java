package homedoctor.medicine.common.auth;

import homedoctor.medicine.api.dto.DefaultApiResponse;
import homedoctor.medicine.common.ResponseMessage;
import homedoctor.medicine.common.StatusCode;
import homedoctor.medicine.domain.User;
import homedoctor.medicine.repository.UserRepository;
import homedoctor.medicine.service.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Component
@Aspect
@RequiredArgsConstructor
public class AuthAspect {

    private final static String AUTHORIZATION = "Authorization";

    private final static DefaultApiResponse DEFAULT_RES =
            DefaultApiResponse.builder()
                    .status(StatusCode.UNAUTHORIZED)
                    .responseMessage(ResponseMessage.UNAUTHORIZED).build();
    private final static ResponseEntity<DefaultApiResponse> RES_RESPONSE_ENTITY = new ResponseEntity<>(DEFAULT_RES, HttpStatus.UNAUTHORIZED);

    private final HttpServletRequest httpServletRequest;

    private final UserRepository userRepository;

    private final JwtService jwtService;

    // 토큰 유효성 검사
    @Around("@annotation(homedoctor.medicine.common.auth.Auth)")
    public Object around(final ProceedingJoinPoint pjp) throws Throwable{
        final String jwt = httpServletRequest.getHeader(AUTHORIZATION);
        if(jwt == null) return RES_RESPONSE_ENTITY;
        final Long token = jwtService.decode(jwt);
        if(token == 0){
            return RES_RESPONSE_ENTITY;
        }else{
            final User user = userRepository.findOneById(token);
            if(user == null) return  RES_RESPONSE_ENTITY;
            return pjp.proceed(pjp.getArgs());
        }

    }
}

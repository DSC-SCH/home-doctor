package homedoctor.medicine.service;

import homedoctor.medicine.api.dto.DefaultResponse;
import homedoctor.medicine.api.dto.user.LogInResponse;
import homedoctor.medicine.api.dto.user.LoginRequest;
import homedoctor.medicine.common.ResponseMessage;
import homedoctor.medicine.common.StatusCode;
import homedoctor.medicine.domain.User;
import homedoctor.medicine.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    private final JwtService jwtService;


    public DefaultResponse login(final LoginRequest loginRequest){
        final User user = userRepository.findOneById(loginRequest.getUserId());

        if(user !=null){
            final String tokenDto = jwtService.create(user.getId());
            LogInResponse logInResponse = new LogInResponse(user.getId(), user.getToken());
            return DefaultResponse.response(StatusCode.OK,
                    ResponseMessage.LOGIN_SUCCESS, logInResponse);

        }
        return DefaultResponse.response(StatusCode.UNAUTHORIZED, ResponseMessage.NOT_FOUND_USER);
    }
}

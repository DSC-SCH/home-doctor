package homedoctor.medicine.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;

import static com.auth0.jwt.JWT.require;

@Slf4j
@Service
public class JwtService {

//    @Value("${jwt.issuer}")
    private String ISSUER = "Test";

//    @Value("${jwt.secret}")
    private String SECRET = "Test";


    //토큰 생성
    public String create(final Long id){
        try{
            JWTCreator.Builder b = JWT.create();
            b.withIssuer(ISSUER);
            b.withClaim("id", id);
            //만료 날짜 지정, 1달
            b.withExpiresAt(expiresAt());
            return b.sign(Algorithm.HMAC256(SECRET));
        }catch (JWTCreationException jwtCreationException){
            log.info(jwtCreationException.getMessage());
        }
        return null;
    }

    private Date expiresAt(){
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        //한달 : 24*31
        cal.add(Calendar.HOUR, 744);
        return cal.getTime();
    }

    //토큰 해독
    public Long decode(final String token) {
        try {
            final JWTVerifier jwtVerifier = require(Algorithm.HMAC256(SECRET))
                    .withIssuer(ISSUER)
                    .build();
            DecodedJWT decodedJWT = jwtVerifier.verify(token);
            return decodedJWT.getClaim("id").asLong();
        } catch (JWTVerificationException jve) {
            log.error(jve.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return 0L;
    }

    public int isUser(final String header, final Long user_id) { //userIdx 내가 수정하려는!!
        Long curId = decode(header);
        if (curId != -1) {
            if (curId == user_id) {
                return 1;
            }
        }
        return 0;
    }

    public static class Token{
        private Long id = -1L;

        public Token(){}

        public Token(final Long user_id){
            this.id = user_id;
        }
        public Long getId(){return this.id; }

    }

    public static class TokenRes{
        private String token;

        public TokenRes(){}
        public TokenRes(final String token){
            this.token = token;
        }
        public String getToken(){
            return token;
        }
        public void setToken(String token){
            this.token = token;
        }
    }
}

package homedoctor.medicine.common;

public class ResponseMessage {

    // USER RESPONSE MESSAGE
    public static final String USER_CREATE_SUCCESS        = "회원 등록 성공";
    public static final String USER_CREATE_FAIL           = "회원 등록 실패";
    public static final String USER_DELETE_SUCCESS        = "회원 삭제 성공";
    public static final String USER_DELETE_FAIL           = "회원 삭제 실패";
    public static final String USER_SEARCH_SUCCESS        = "회원 조회 성공";
    public static final String USER_SEARCH_FAIL           = "회원 조회 실패";
    public static final String NOT_FOUND_USER             = "등록된 회원이 없습니다.";
    public static final String DUPLICATED_USER            = "이미 등록된 회원입니다.";

    // ALARM RESPONSE MESSAGE
    public static final String ALARM_CREATE_SUCCESS        = "알람 등록 성공";
    public static final String ALARM_CREATE_FAIL           = "알람 등록 실패";
    public static final String ALARM_UPDATE_SUCCESS        = "알람 수정 성공";
    public static final String ALARM_UPDATE_FAIL           = "알람 수정 실패";
    public static final String ALARM_DELETE_SUCCESS        = "알람 삭제 성공";
    public static final String ALARM_DELETE_FAIL           = "알람 삭제 실패";
    public static final String ALARM_SEARCH_SUCCESS        = "알람 조회 성공";
    public static final String ALARM_SEARCH_FAIL           = "알람 조회 실패";
    public static final String NOT_FOUND_ALARM             = "등록된 알람이 없습니다.";

    // LABEL
    public static final String LABEL_CREATE_SUCCESS        = "라벨 등록 성공";
    public static final String LABEL_CREATE_FAIL           = "라벨 등록 실패";
    public static final String LABEL_UPDATE_SUCCESS        = "라벨 수정 성공";
    public static final String LABEL_UPDATE_FAIL           = "라벨 수정 실패";
    public static final String LABEL_DELETE_SUCCESS        = "라벨 삭제 성공";
    public static final String LABEL_DELETE_FAIL           = "라벨 삭제 실패";
    public static final String LABEL_SEARCH_SUCCESS        = "라벨 조회 성공";
    public static final String NOT_FOUND_LABEL             = "등록된 라벨이 없습니다.";

    // CONNECT
    public static final String CONNECTION_CREATE_SUCCESS   = "연동 성공";
    public static final String CONNECTION_CREATE_FAIL      = "연동 실패";
    public static final String CONNECTION_SEARCH_SUCCESS   = "연동 조회 성공";
    public static final String CONNECTION_SEARCH_FAIL      = "연동 조회 실패";
    public static final String CONNECTION_DELETE_SUCCESS   = "연동 해제 성공";
    public static final String CONNECTION_DELETE_FAIL      = "연동 해제 실패";
    public static final String NOT_FOUND_CONNECTION        = "연동된 유저가 없습니다.";

    // CONNECT CODE
    public static final String CODE_CREATE_SUCCESS         = "코드 생성 성공";
    public static final String CODE_CREATE_FAIL            = "코드 생성 실패";
    public static final String CODE_SEARCH_SUCCESS         = "코드 조회 성공";
    public static final String CODE_SEARCH_FAIL            = "코드 조회 실패";
    public static final String NOT_FOUND_CODE              = "등록된 코드가 없습니다.";

    // Prescription Image
    public static final String PRESCRIPTION_CREATE_SUCCESS = "처방전 등록 성공";
    public static final String PRESCRIPTION_CREATE_FAIL    = "처방전 등록 실패";
    public static final String PRESCRIPTION_UPDATE_SUCCESS = "처방전 수정 성공";
    public static final String PRESCRIPTION_UPDATE_FAIL    = "처방전 수정 실패";
    public static final String PRESCRIPTION_DELETE_SUCCESS = "처방전 삭제 성공";
    public static final String PRESCRIPTION_DELETE_FAIL    = "처방전 삭제 실패";
    public static final String PRESCRIPTION_SEARCH_SUCCESS = "처방전 조회 성공";
    public static final String PRESCRIPTION_SEARCH_FAIL    = "처방전 조회 실패";
    public static final String NOT_FOUND_PRESCRIPTION      = "등록된 처방전이 없습니다.";

    // MEDICINE
    public static final String MEDICINE_SEARCH_SUCCESS     = "약품 조회 성공";
    public static final String NOT_FOUND_MEDICINE          = "입력된 정보가 포함된 약품이 없습니다.";

    // LOGIN
    public static final String LOGIN_SUCCESS               = "로그인 성공";
    public static final String LOGIN_FAIL                  = "존재하지 않는 유저입니다.";

    // OTHER MESSAGE
    public static final String DB_ERROR                    = "디비 에러";
    public static final String INTERNAL_SERVER_ERROR       = "서버 내부 에러";
    public static final String NOT_CONTENT                 = "입력 정보가 부족합니다.";
    public static final String BAD_REQUEST                 = "잘못된 요청입니다.";
    public static final String UNAUTHORIZED                = "인증 실패.";
    public static final String AUTHORIZED                  = "인증 성공.";
}

package backend.dev.setting.exception;

import static org.springframework.http.HttpStatus.*;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    NOT_FOUND_USER(NOT_FOUND, "가입된 회원을 찾을 수 없습니다"),
    NOT_FOUND_EMAIL(NOT_FOUND, "가입된 이메일이 아닙니다"),
    DUPLICATE_EMAIL(BAD_REQUEST, "이미 가입된 이메일입니다"),
    NOT_MATCH_PASSWORD(BAD_REQUEST, "암호가 일치하지 않습니다"),
    BAD_NICKNAME(BAD_REQUEST, "닉네임을 다시 설정해주세요"),
    PROFILE_CREATE_DIRECTORY_FAIL(INTERNAL_SERVER_ERROR, "디렉토리 설정이 실패했습니다"),
    PROFILE_DELETE_FAIL(INTERNAL_SERVER_ERROR, "프로필 삭제가 실패했습니다. 다시 시도해주세요"),
    PROFILE_INVALID_FILE(BAD_REQUEST, "파일이 존재하지 않거나, 파일타입이 맞지 않습니다"),
    NOT_MATCH_EMAIL_OR_PASSWORD(BAD_REQUEST, "이메일이나 암호가 맞지 않습니다"),
    INVALID_TOKEN(BAD_REQUEST, "유효하지 않은 토큰입니다"),
    EXPIRED_TOKEN(BAD_REQUEST, "만료된 토큰입니다"),
    UNSUPPORTED_TOKEN(BAD_REQUEST, "지원하지 않는 토큰입니다"),
    NOT_FOUND_TOKEN(BAD_REQUEST, "토큰을 찾을 수 없습니다"),
    SERVER_ERROR(INTERNAL_SERVER_ERROR, "서버에 문제가 발생했습니다"),
    ALWAYS_SEND_EMAIL(BAD_REQUEST, "이미 인증을 요청한 이메일입니다"),
    FAIL_SEND_EMAIL(INTERNAL_SERVER_ERROR, "메세지 전송에 실패했습니다"),
    CERTIFICATION_TIME_OVER(BAD_REQUEST, "메일 인증 시간이 초과됐습니다"),
    NOT_MATCH_CERTIFICATION(BAD_REQUEST, "인증번호가 일치하지 않습니다"),
    PASSWORD_NOT_EMPTY(BAD_REQUEST, "비밀번호는 공란이 불가능합니다"),
    NOT_EMPTY(BAD_REQUEST, "빈 칸을 입력해주세요"),
    //notification 예외
    NOT_FOUND_FCM_TOKEN(NOT_FOUND,"FCM토큰을 찾을 수 없습니다"),
    NOTIFICATION_NOT_FOUND(NOT_FOUND,"알림을 찾을 수 없습니다"),
    // facility 예외
    FACILITY_NOT_FOUND(NOT_FOUND, "Facility not found"),
    FACILITY_EXCEPTION(UNAUTHORIZED,"정해지지 않은 시설 오류입니다 발견하시면 카톡 부탁드려요"),
    DUPLICATE_FACILITY(CONFLICT, "이미 있는 시설입니다"),
    INVALID_FACILITY_DATA(BAD_REQUEST, "시설 데이터 형식에 맞지 않습니다"),
    INVALID_CATEGORY(CONFLICT, "카테고리형식이 맞지 않습니다"),
    FACILITY_ADD_FAILED(PAYMENT_REQUIRED,"시설 추가에 실패했습니다"),
    NOT_MODIFIED(UNAUTHORIZED,"업데이트에 실패했습니다"),
    NOT_DELETED(UNAUTHORIZED,"삭제에 실패했습니다"),
    INVALID_FILTER(BAD_REQUEST,"올바른 필터 설정이 아닙니다"),
    // activity 예외
    ACTIVITY_NOT_FOUND(NOT_FOUND,"모임을 찾을 수 없습니다"),
    ACTIVITY_NOT_DELETED(BAD_REQUEST,"모임이 삭제되지 않았습니다"),
    ACTIVITY_NOT_MODIFIED(BAD_REQUEST,"모임이 변경되지 않았습니다"),
    ACTIVITY_FULL(BAD_REQUEST, "모임에 더 참가할 수 없습니다."),
    ACTIVITY_NOT_JOINED(BAD_REQUEST,"모임에 참가하고 있지 않습니다"),
    ACTIVITY_CATEGORY_EXCEPTION(BAD_REQUEST, "올바른 카테고리가 아닙니다"),
    // like 예외
    DUPLICATE_LIKE(BAD_REQUEST,"이미 좋아요를 눌렀습니다"),

    //chatroom 예외
    CHAT_NOT_DELETE(UNAUTHORIZED,"메시지 작성자 또는 방장만 메시지를 삭제할 수 있습니다."),
    CHAT_NOT_FOUND(UNAUTHORIZED,"메세지를 찾을 수 없습니다."),
    NOT_USER_INFORMATION(UNAUTHORIZED,"사용자 정보가 누락되었습니다."),
    CHATROOM_NOT_FOUND(NOT_FOUND,"채팅방을 찾을 수 없습니다."),
    PARTICIPANT_NOT_FOUND(NOT_FOUND,"참가자를 찾을 수 없습니다."),
    INVALID_CHATROOM(BAD_REQUEST,"참가자가 채팅방에 참여하고 있지 않습니다."),

    //meetingBoard 예외
    BOARD_NOT_DELETE(UNAUTHORIZED,"모임을 삭제할 권한이 없습니다."),
    UNAUTHORIZED_USER(UNAUTHORIZED,"인증되지 않은 사용자입니다."),
    BOARD_NOT_FOUND(NOT_FOUND,"모임을 찾을 수 없습니다."),
    LOGIN_REQUIRED(UNAUTHORIZED,"로그인이 필요합니다."),
    ;
    private final HttpStatus httpStatus;
    private final String message;
}

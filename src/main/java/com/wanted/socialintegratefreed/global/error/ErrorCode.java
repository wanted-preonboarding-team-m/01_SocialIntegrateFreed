package com.wanted.socialintegratefreed.global.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * 오류 메시지와 상태를 쉽게 추가하기 위한 Enum
 */
@Getter
public enum ErrorCode {
    FEED_NOT_FOUND("해당 게시물을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    DUPLICATE_EMAIL("중복 된 이메일 입니다.", HttpStatus.BAD_REQUEST),
    PASSWORD_NOT_MATCH("비밀번호가 맞지 않습니다.", HttpStatus.BAD_REQUEST),
    ACCESS_DENIED_EXCEPTION("접근이 거부되었습니다.", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND("해당 유저는 존재하지 않습니다.", HttpStatus.BAD_REQUEST),
    EMAIL_NOT_EXIST("이메일이 존재하지 않습니다.", HttpStatus.BAD_REQUEST),
    CODE_MISMATCH("일치하지 않는 인증번호 입니다.", HttpStatus.BAD_REQUEST),
    EXIST_NOT_SESSION("세션이 만료되었습니다. 다시 인증코드를 발급해주세요.", HttpStatus.BAD_REQUEST),
    INVALID_JWT_TOKEN("유효하지 않는 토큰 입니다.", HttpStatus.BAD_REQUEST),
    EXPIRED_JWT_TOKEN("만료된 토큰 입니다. 다시 로그인 해주세요.", HttpStatus.BAD_REQUEST),
    UNSUPPORT_JWT_TOKEN("지원하지 않는 JWT 토큰 입니다.", HttpStatus.BAD_REQUEST),
    NOT_JWT_CLAIM("해당 jwt에 정보가 없습니다.", HttpStatus.BAD_REQUEST),
    INVALID_SEARCH_TYPE("잘못된 검색 타입입니다. date 혹은 hour로 입력해주세요.", HttpStatus.BAD_REQUEST),
    INVALID_DATE("잘못된 날짜 형식입니다. 2022-09-10의 형태로 입력해주세요.", HttpStatus.BAD_REQUEST),
    INVALID_VALUE("value는 count, view_count, like_count, share_count 중 하나여야 합니다.", HttpStatus.BAD_REQUEST),
    OVER_PERIOD("시작일과 종료일의 차이가 일자별 조회는 30일, 시간별 조회는 7일 이내여야 합니다.", HttpStatus.BAD_REQUEST);


    //오류 메시지
    private final String message;
    //오류 상태코드
    private final HttpStatus httpStatus;

    ErrorCode(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }

}

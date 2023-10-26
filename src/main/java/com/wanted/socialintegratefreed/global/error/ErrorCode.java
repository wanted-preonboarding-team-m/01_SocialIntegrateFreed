package com.wanted.socialintegratefreed.global.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * 오류 메시지와 상태를 쉽게 추가하기 위한 Enum
 */
@Getter
public enum ErrorCode {

    DUPLICATE_EMAIL("중복 된 이메일 입니다.", HttpStatus.BAD_REQUEST),
    PASSWORD_NOT_MATCH("비밀번호가 맞지 않습니다.", HttpStatus.BAD_REQUEST),
    ACCESS_DENIED_EXCEPTION("접근이 거부되었습니다.", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND("해당 유저는 존재하지 않습니다.", HttpStatus.BAD_REQUEST),
    EMAIL_NOT_EXIST("이메일이 존재하지 않습니다.", HttpStatus.BAD_REQUEST),
    CODE_MISMATCH("일치하지 않는 인증번호 입니다.", HttpStatus.BAD_REQUEST),
    EXIST_NOT_SESSION("세션이 만료되었습니다. 다시 인증코드를 발급해주세요.", HttpStatus.BAD_REQUEST);


    //오류 메시지
    private final String message;
    //오류 상태코드
    private final HttpStatus httpStatus;

    ErrorCode(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }

}

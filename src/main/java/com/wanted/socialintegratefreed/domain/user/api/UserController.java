package com.wanted.socialintegratefreed.domain.user.api;


import com.wanted.socialintegratefreed.domain.user.application.UserService;
import com.wanted.socialintegratefreed.domain.user.dto.request.UserRequestAuthCodeDto;
import com.wanted.socialintegratefreed.domain.user.dto.request.UserRequestDto;
import com.wanted.socialintegratefreed.domain.user.dto.response.UserAccessTokenDto;
import com.wanted.socialintegratefreed.domain.user.dto.response.UserResponseAuthCodeDto;
import com.wanted.socialintegratefreed.global.format.response.ApiResponse;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.net.http.HttpResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;


    /**
     * signUp : 회원가입
     *
     * @param userRequestDto     요청 받은 정보
     * @param httpServletRequest 요청받은 servletRequest
     * @return 임시 인증번호
     */
    @PostMapping("/sign-up")
    public ResponseEntity<ApiResponse> signUp(final @RequestBody @Valid UserRequestDto userRequestDto,
            HttpServletRequest httpServletRequest) {

        int userResponseDto = userService.signUp(userRequestDto, httpServletRequest);
        return ResponseEntity.ok(ApiResponse.toSuccessForm(userResponseDto));
    }


    /**
     * verifyAuthenticationCode : 회원가입
     *
     * @param userRequestAuthCodeDto 요청 받은 정보
     * @param httpServletRequest     요청받은 servletRequest
     * @return responseEntity
     */

    @PostMapping("/verify-user-code")
    public ResponseEntity<ApiResponse> verifyAuthenticationCode(
            final @RequestBody UserRequestAuthCodeDto userRequestAuthCodeDto, HttpServletRequest httpServletRequest) {
        userService.verifyAuthenticationCodeAndRequestInput(userRequestAuthCodeDto, httpServletRequest);
        return ResponseEntity.ok(ApiResponse.toSuccessForm("정상 처리 되었습니다."));
    }

    /**
     * login : 로그인
     *
     * @param userRequestDto 요청 받은 정보
     * @return accesstoken
     */

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(final @RequestBody UserRequestDto userRequestDto) {
        UserAccessTokenDto userAccessTokenDto = userService.login(userRequestDto);
        return ResponseEntity.ok(ApiResponse.toSuccessForm(userAccessTokenDto));
    }

}

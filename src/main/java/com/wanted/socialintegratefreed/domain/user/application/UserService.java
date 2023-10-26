package com.wanted.socialintegratefreed.domain.user.application;


import static com.wanted.socialintegratefreed.domain.user.utils.UserUtils.generateAuthRandomNumber;

import com.wanted.socialintegratefreed.domain.user.constant.UserEnable;
import com.wanted.socialintegratefreed.domain.user.dao.UserRepository;
import com.wanted.socialintegratefreed.domain.user.dto.request.UserRequestAuthCodeDto;
import com.wanted.socialintegratefreed.domain.user.dto.request.UserRequestDto;
import com.wanted.socialintegratefreed.domain.user.dto.response.UserAccessTokenDto;
import com.wanted.socialintegratefreed.domain.user.entity.User;
import com.wanted.socialintegratefreed.domain.user.jwt.JwtTokenProvider;
import com.wanted.socialintegratefreed.global.error.BusinessException;
import com.wanted.socialintegratefreed.global.error.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * 전반적인 User 비지니스 로직
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {


    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;


    /**
     * @param userSaveRequestDto 사용자 입력값
     * @param httpServletRequest 인증코드를 사용하기 위한 httpServletRequest
     * @return 인증코드
     */
    @Transactional
    public int signUp(UserRequestDto userSaveRequestDto, HttpServletRequest httpServletRequest) {
        Map<String, Object> userSessionMap = new HashMap<>();
        duplicateEmail(userSaveRequestDto.getEmail()); // 이메일 중복체크
        User user = User.builder()
                .email(userSaveRequestDto.getEmail())
                .password(passwordEncoder.encode(userSaveRequestDto.getPassword()))
                .userEnable(UserEnable.USER_DISABLED)
                .build();

        User saveUser = userRepository.save(user); // 사용자 저장
        int authCode = generateAuthRandomNumber(); // 랜덤 6자리수 발급

        userSessionMap.put("user", saveUser); //map에 담을 "user"라는 키 , 값으로는 db에 저장한 user값
        userSessionMap.put("authCode", authCode); // map에 "authCode"라는 키,값으로는 발급된 랜덤 6자리 저장
        storeUserInSession(userSessionMap, httpServletRequest); // 세션에 저장된 값을 저장

        return authCode;
    }

    //세션 저장소
    public void storeUserInSession(Map<String, Object> userMap, HttpServletRequest httpServletRequest) {
        HttpSession httpSession = httpServletRequest.getSession(); // 세션을 가져와서
        httpSession.setAttribute("user", userMap); // 유저라는
        httpSession.setMaxInactiveInterval(60); // 세션에 60초동안 저장
    }

    /**
     * verifyAuthenticationCodeAndRequestInput : 60초동안 발급받았던 코드 이메일 체킹 이후 USER 업데이트
     *
     * @param userRequestAuthCodeDto 체킹 할 코드, 체킹 할 email
     * @param httpServletRequest     // 세션
     */
    public void verifyAuthenticationCodeAndRequestInput(UserRequestAuthCodeDto userRequestAuthCodeDto,
            HttpServletRequest httpServletRequest) {
        HttpSession httpSession = httpServletRequest.getSession();
        Map<String, Object> userInSessionMap = (Map<String, Object>) httpSession.getAttribute("user");
        if (userInSessionMap != null && !userInSessionMap.isEmpty()) {
            int authCodeInSession = (int) userInSessionMap.get("authCode");
            User userInSession = (User) userInSessionMap.get("user");
            processVerifiedAuthCode(authCodeInSession, userRequestAuthCodeDto.getCode());
            if (userInSession != null) { // 인증코드가 맞고, 세션이 없지않으면
                processVerifiedUser(userInSession); // USER_ENABLE로 유저 업데이트
                httpSession.removeAttribute("user"); // 이후 세션 삭제
            }
        } else {
            throw new BusinessException(userInSessionMap, "session", ErrorCode.EXIST_NOT_SESSION);
        }
    }

    public void processVerifiedAuthCode(int sessionCode, int dtoCode) {
        if (sessionCode != dtoCode) {
            throw new BusinessException(dtoCode, "code", ErrorCode.CODE_MISMATCH);
        }

    }

    /**
     * @param userInSession dto code로 부터 세션에 담겨있는 code와 맞으며 아직 session 으로부터 살아있는 유효한 유저 객체를 가져옴
     */
    private void processVerifiedUser(User userInSession) {
        User findUser = findUserIdReturnUser(userInSession.getUserId());
        findUser.updateEnableUser(UserEnable.USER_ENABLED);
        userRepository.save(findUser);
    }

    private void requestUserToInputCodeAgain() {
        // 맞지 않을 경우, 사용자에게 다시 입력 요청하는 로직을 구현
        // 예를 들어, 다시 입력해달라는 메시지를 보낸다거나
    }


    /**
     * UserAccessTokenDto email,password 검증을 거친 이후 토큰 발급
     */
    public UserAccessTokenDto login(UserRequestDto userRequestDto) {
        User findUser = existEmailReturnUser(userRequestDto.getEmail());
        isPasswordMatches(userRequestDto.getPassword(), findUser.getPassword());
        return UserAccessTokenDto.builder()
                .accessToken(jwtTokenProvider.createJwt(findUser.getEmail()))
                .build();
    }

    /**
     * findUserIdReturnUser : repository에 user가 있는지 확인
     *
     * @param userId session 및 저장된 db로부터 가져온 id
     * @return UserEntity
     */

    public User findUserIdReturnUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(userId, "userId", ErrorCode.USER_NOT_FOUND));
    }

    /**
     * isPasswordMatches : request와 db 비교
     *
     * @param dtoPassword request 로 받아온 비밀번호
     * @param dbPassword  db에서 가져온 비밀번호
     */
    public void isPasswordMatches(String dtoPassword, String dbPassword) {
        if (!passwordEncoder.matches(dtoPassword, dbPassword)) {
            throw new BusinessException(dtoPassword, "password", ErrorCode.PASSWORD_NOT_MATCH);
        }
    }

    /**
     * @param email dto 이메일 duplicateEmail: 중복된 이메일 찾아 없을시 예외 발생
     */
    public void duplicateEmail(String email) {
        userRepository.findByEmail(email).orElseThrow(() -> new BusinessException(email, "email",
                ErrorCode.DUPLICATE_EMAIL));
    }

    /**
     * @param email dto 이메일 existEmailReturnUser: 이메일이 없을때 예외 발생
     */

    public User existEmailReturnUser(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new BusinessException(email, "email",
                ErrorCode.EMAIL_NOT_EXIST));
    }
}

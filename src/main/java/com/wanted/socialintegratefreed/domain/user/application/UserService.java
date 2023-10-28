package com.wanted.socialintegratefreed.domain.user.application;


import static com.wanted.socialintegratefreed.domain.user.utils.UserUtils.generateAuthRandomNumber;

import com.wanted.socialintegratefreed.domain.user.constant.UserEnable;
import com.wanted.socialintegratefreed.domain.user.dao.UserRepository;
import com.wanted.socialintegratefreed.domain.user.dto.request.UserRequestAuthCodeDto;
import com.wanted.socialintegratefreed.domain.user.dto.request.UserSignUpRequestDto;
import com.wanted.socialintegratefreed.domain.user.dto.response.UserAccessTokenDto;
import com.wanted.socialintegratefreed.domain.user.dto.response.UserResponseAuthCodeDto;
import com.wanted.socialintegratefreed.domain.user.entity.User;
import com.wanted.socialintegratefreed.domain.user.jwt.JwtTokenProvider;
import com.wanted.socialintegratefreed.global.error.BusinessException;
import com.wanted.socialintegratefreed.global.error.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * 전반적인 User 비지니스 로직
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    //유저 Repo
    private final UserRepository userRepository;

    //bean 비밀번호 확인
    private final BCryptPasswordEncoder passwordEncoder;

    //Jwt 발급 class
    private final JwtTokenProvider jwtTokenProvider;

    //세션 max Interval
    private static final int MAX_INACTIVATE_INTERVAL = 60;

    /**
     * @param userSignUpRequestDto 사용자 입력값
     * @param httpServletRequest   인증코드를 사용하기 위한 httpServletRequest
     * @return 인증코드
     */
    public int signUp(UserSignUpRequestDto userSignUpRequestDto, HttpServletRequest httpServletRequest) {
        validateUserEmail(userSignUpRequestDto.getEmail());
        Map<String, Object> userSessionMap = new HashMap<>();
        User user = User.builder()
                .email(userSignUpRequestDto.getEmail())
                .password(passwordEncoder.encode(userSignUpRequestDto.getPassword()))
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
        httpSession.setMaxInactiveInterval(MAX_INACTIVATE_INTERVAL); // 세션에 60초동안 저장
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
        isUserSessionNullOrEmpty(userInSessionMap);
        int authCodeInSession = (int) userInSessionMap.get("authCode");
        User userInSession = (User) userInSessionMap.get("user");
        processVerifiedAuthCode(authCodeInSession, userRequestAuthCodeDto.getCode());
        isAuthenticationCodeValidAndSessionExists(userInSession, httpSession);
    }

    /**
     * @param getUserSession session에서 가져온 User
     */
    public void isAuthenticationCodeValidAndSessionExists(User getUserSession, HttpSession session) {
        if (getUserSession != null) { // 인증코드가 맞고, 세션이 없지않으면
            processVerifiedUser(getUserSession); // USER_ENABLE로 유저 업데이트
            session.removeAttribute("user"); // 이후 세션 삭제

        }
    }

    /**
     * @param userSession userssion을통해 null or empty 체크
     */
    public void isUserSessionNullOrEmpty(Map<String, Object> userSession) {
        if (userSession == null || userSession.isEmpty()) {
            throw new BusinessException(userSession, "session", ErrorCode.EXIST_NOT_SESSION);
        }
    }

    /**
     * processVerifiedAuthCode: 인증번호 검증
     *
     * @param sessionCode :세션에서 가져온 authCode
     * @param dtoCode     : return으로 넘겨준 code를 다시 dto로 받을때 code
     */

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

    /**
     * UserAccessTokenDto email,password 검증을 거친 이후 토큰 발급
     */
    public UserAccessTokenDto login(UserSignUpRequestDto userSignUpRequestDto) {
        User findUser = existEmailReturnUser(userSignUpRequestDto.getEmail());
        isPasswordMatches(userSignUpRequestDto.getPassword(), findUser.getPassword());
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
    public void validateUserEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            throw new BusinessException("email", email, ErrorCode.DUPLICATE_EMAIL);
        }
    }

    /**
     * @param email dto 이메일 existEmailReturnUser: 이메일이 없을때 예외 발생
     */

    public User existEmailReturnUser(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new BusinessException(email, "email",
                ErrorCode.EMAIL_NOT_EXIST));
    }

    /**
     * UserResponseAuthCodeDto : 인증 번호가 다시필요할때 다시 세션에 넣어준다
     *
     * @param email          dto email
     * @param servletRequest
     * @return
     * @mothod storeUserInSession : 세션60초 저장소
     */
    public UserResponseAuthCodeDto refreshCode(String email, HttpServletRequest servletRequest) {
        int code = generateAuthRandomNumber();
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("authCode", code);
        userMap.put("user", existEmailReturnUser(email));
        servletRequest.setAttribute("authCode", userMap);
        storeUserInSession(userMap, servletRequest);
        return UserResponseAuthCodeDto.builder()
                .code(code).build();
    }
}

package com.wanted.socialintegratefreed.domain.auth.application;

import com.wanted.socialintegratefreed.domain.auth.entity.principal.UserPrincipal;
import com.wanted.socialintegratefreed.domain.user.dao.UserRepository;
import com.wanted.socialintegratefreed.domain.user.entity.User;
import com.wanted.socialintegratefreed.global.error.BusinessException;
import com.wanted.socialintegratefreed.global.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * CustomUserDetailService : springSecurity 내 UserDetailsService 오버라이딩 하여 커스텀
 */
@RequiredArgsConstructor
@Service
public class CustomUserDetailService implements UserDetailsService {

  private final UserRepository userRepository;


  @Override
  public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
    User findUser =
        userRepository.findByEmail(userEmail)
            .orElseThrow(() -> new BusinessException("user email", userEmail,
                ErrorCode.EMAIL_NOT_EXIST));
    // 유저를 찾은 이후 security 인증을위해 구현체 Principal 에 넣어줌
    return new UserPrincipal(findUser);
  }

}

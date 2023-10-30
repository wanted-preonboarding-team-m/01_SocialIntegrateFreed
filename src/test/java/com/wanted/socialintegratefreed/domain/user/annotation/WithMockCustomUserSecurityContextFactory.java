package com.wanted.socialintegratefreed.domain.user.annotation;

import com.wanted.socialintegratefreed.domain.auth.entity.principal.UserPrincipal;
import com.wanted.socialintegratefreed.domain.user.entity.User;
import com.wanted.socialintegratefreed.domain.user.entity.UserEnable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithMockCustomUserSecurityContextFactory implements
    WithSecurityContextFactory<WithMockCustomUser> {

  @Override
  public SecurityContext createSecurityContext(WithMockCustomUser customUser) {
    SecurityContext context = SecurityContextHolder.createEmptyContext();

    UserPrincipal principal = new UserPrincipal(User.builder()
        .password("1234dsafweg")
        .userEnable(UserEnable.USER_DISABLED)
        .email("junghun8158@naver.com").build());
    Authentication auth = new UsernamePasswordAuthenticationToken(principal,
        principal.getPassword(),
        principal.getAuthorities());
    context.setAuthentication(auth);
    return context;
  }
}
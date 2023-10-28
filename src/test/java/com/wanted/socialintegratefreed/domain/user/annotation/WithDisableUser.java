package com.wanted.socialintegratefreed.domain.user.annotation;

import com.wanted.socialintegratefreed.domain.user.constant.UserEnable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.springframework.security.test.context.support.WithMockUser;

@Retention(RetentionPolicy.RUNTIME)
@WithMockCustomUser(first = "ROLE_USER_ENABLED")
public @interface WithDisableUser {

}
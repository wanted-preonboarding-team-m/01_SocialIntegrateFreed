package com.wanted.socialintegratefreed.domain.user.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithMockCustomUser(first = "ROLE_USER_ENABLED")
public @interface WithDisableUser {

}
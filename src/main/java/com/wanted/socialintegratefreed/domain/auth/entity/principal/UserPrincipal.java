package com.wanted.socialintegratefreed.domain.auth.entity.principal;

import com.wanted.socialintegratefreed.domain.user.entity.User;
import java.util.ArrayList;
import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * User를 SpringSecurity 권한을 주기위해
 */

public class UserPrincipal implements UserDetails {

  private User user;

  public UserPrincipal(User user) {
    this.user = user;
  }


  public User getuser() {
    return user;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    //loadByUserName으로 부터 받은 객체가 ENABLE인 부분을 Authority에 넣어줌으로써 권한이 부어됨을 인증
    Collection<GrantedAuthority> authorities = new ArrayList<>();
    authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getUserEnable().toString()));
    return authorities;
  }

  @Override
  public String getPassword() {
    return null;
  }

  @Override
  public String getUsername() {
    return user.getEmail();
  }

  @Override
  public boolean isAccountNonExpired() {
    //사용자 계정 만료 여부
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    //사용자 계정 잠금 여부
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    // 사용자 자격증명 여부
    return true;
  }


  @Override
  public boolean isEnabled() {
    //사용자 계정 활성화 및 비활성화
    return true;
  }
}

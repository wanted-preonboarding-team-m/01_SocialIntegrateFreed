package com.wanted.socialintegratefreed.global.config.security.jwt;

import com.wanted.socialintegratefreed.domain.auth.application.CustomUserDetailService;
import com.wanted.socialintegratefreed.global.error.BusinessException;
import com.wanted.socialintegratefreed.global.error.ErrorCode;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/**
 * @slf4j 로그 확인하기위해서
 * @Component 외부 라이브러리인만큼 빈주입을위해 토큰 생성을 담당하는 클래스
 */
@Slf4j
@Component
public class JwtTokenProvider {

  // 암호화키
  private final String JWT_SECRET_KEY;

  //application.yml에 등록한 value
  private final int JWT_EXPIRE_MS;

  // 시큐리티 자체 내 인증된 사람으로 하기위해
  private final CustomUserDetailService customUserDetailService;


  public JwtTokenProvider(@Value("${jwt.secret}") String JWT_SECRET_KEY,
      @Value("${jwt.expired}") final int JWT_EXPIRE_MS,
      final CustomUserDetailService customUserDetailService) {
    this.JWT_SECRET_KEY = JWT_SECRET_KEY; // 이 부분을 수정
    this.JWT_EXPIRE_MS = JWT_EXPIRE_MS;
    this.customUserDetailService = customUserDetailService;
  }

  /**
   * @return jwt서명 키를 생성하고 반환
   * @Variable keyBytes: BASSE64 디코딩해서 byte 배열로 변환 (secretkey를통해)
   */
  private Key createSignInKey() {

    byte[] keyBytes = Decoders.BASE64.decode(JWT_SECRET_KEY);
    return Keys.hmacShaKeyFor(keyBytes);
  }

  /**
   * jwt 생성기
   *
   * @param email : 사용자 이메일
   * @return JWts
   */
  public String createJwt(String email) {

    Date expiration = new Date(System.currentTimeMillis() + JWT_EXPIRE_MS); // jwt 만료시간은 현재시간 + 1시간
    return Jwts.builder()
        .setHeaderParam("type", "jwt") // 어떠한 type을 갖는 유형인지
        .claim("userEmail", email) // 어떤 정보를 Jwt에 넣을건지
        .setIssuedAt(new Date()) // 언제 토큰이 발급되었는지
        .setExpiration(expiration) // 언제 토큰이 만료되는지
        .signWith(SignatureAlgorithm.HS256, createSignInKey()) // 어떤 알고리즘을 쓸거고, 어떤 key를 통해 파싱해볼건지 등록
        .compact();
  }

  /**
   * validateToken: 토큰에 대한 예외케이스
   *
   * @param token 토큰
   * @return boolean
   */
  public boolean validateToken(String token) {
    try {
      Jwts.parserBuilder().setSigningKey(JWT_SECRET_KEY).build().parseClaimsJws(token);
      return true;
    } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
      throw new BusinessException(e, "Invalid token", ErrorCode.INVALID_JWT_TOKEN);
    } catch (ExpiredJwtException e) {
      throw new BusinessException(e, "Expired JWT Token", ErrorCode.EXPIRED_JWT_TOKEN);
    } catch (UnsupportedJwtException e) {
      throw new BusinessException(e, "Unsupported JWT Token", ErrorCode.UNSUPPORT_JWT_TOKEN);
    } catch (IllegalArgumentException e) {
      throw new BusinessException(e, "Token claim empty", ErrorCode.ACCESS_DENIED_EXCEPTION);
    }
  }

  /**
   * getUserClaimEmail: 토큰을 만들때 사용한 jwt secretkey 를 통해 파싱 후 그 안에 있는 정보를 가져온다
   *
   * @param token
   * @return userEmail
   */
  public String getUserClaimEmail(String token) {
    return Jwts.parser()
        .setSigningKey(createSignInKey())
        .parseClaimsJws(token)
        .getBody()
        .get("userEmail", String.class);
  }

  /**
   * getAuthentication :  토큰을 받은 이후 사용자가 user url 제외하고 요청시 doFilter에서 헤더값을 체크하는데 이때 토큰이 있을경우
   * UserEmail return email을 통해 인증된 사람인가를 확인하는 작업
   *
   * @param token
   * @return
   */
  public Authentication getAuthentication(String token) {
    UserDetails userDetails = customUserDetailService.loadUserByUsername(
        this.getUserClaimEmail(token));
    return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
  }
}

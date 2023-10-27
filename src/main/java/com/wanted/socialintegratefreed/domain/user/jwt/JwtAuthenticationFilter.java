package com.wanted.socialintegratefreed.domain.user.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wanted.socialintegratefreed.global.error.BusinessException;
import com.wanted.socialintegratefreed.global.format.response.ApiResponse;
import io.jsonwebtoken.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * JwtAuthenticationFilter: token valid 및 인증/인가 된 유저를 Filter
 */
@RequiredArgsConstructor
@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    //토큰생성
    private final JwtTokenProvider jwtTokenProvider;
    // object Mapper - json
    private ObjectMapper mapper = new ObjectMapper();


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, java.io.IOException {
        String token = resolveToken((HttpServletRequest) request);
        // 토큰 유효성 검사
        try {
            if (token != null && jwtTokenProvider.validateToken(token)) {
                //인증이 완료된 사람이라면
                Authentication authentication = jwtTokenProvider.getAuthentication(token);
                //securityContext 내 해당 인증된 사람을 넣어준다.
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            filterChain.doFilter(request, response);
        } catch (BusinessException e) {
            System.out.println(e.getMessage());
            log.info(e.getMessage());
            setResponse(response, e);
        }
    }

    /**
     * @setResponse 토큰이 없을경우 권한이 없는걸로 인지 이후 401 error
     */
    private void setResponse(HttpServletResponse response, BusinessException e)
            throws IOException, java.io.IOException, BusinessException {
        response.setContentType("application/json; charset=UTF-8");
        ResponseEntity<ApiResponse> errorResponse =
                ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.toErrorForm(
                        e.getMessage()));
        response.setStatus(e.getHttpStatus().value());

        response.getWriter().write(mapper.writeValueAsString(errorResponse));
    }

    // 헤더에서 토큰 추출
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
            return bearerToken.substring(6);
        }
        return null;
    }
}
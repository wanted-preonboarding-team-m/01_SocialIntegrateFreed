package com.wanted.socialintegratefreed.global.config.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wanted.socialintegratefreed.global.error.ErrorCode;
import com.wanted.socialintegratefreed.global.format.response.ApiResponse;
import io.jsonwebtoken.io.IOException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

/**
 * 전반적인 권한 및 토큰에 대한 검증을 실시하는 예외 class
 */
@Component
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

  private final ObjectMapper mapper;

  /**
   * AccessDeniedHandler : security가 서블릿 자체 내에 있기때문에 ControllerAdvice까지 가지못하는 상황 AccessDeniedHandler
   * 를 통해 response 뱉는 걸 확인 이후 override 이후 커스텀
   */
  @Override
  public void handle(HttpServletRequest request, HttpServletResponse response,
      AccessDeniedException accessDeniedException) throws IOException, java.io.IOException {
    ErrorCode exceptionCode;
    exceptionCode = ErrorCode.ACCESS_DENIED_EXCEPTION;
    setResponse(response, exceptionCode);
  }

  /**
   * @method setResponse : 원래 맨처음에 바로 BusinessException 사용할려하였으나, security 가 Controller 오지 않기때문에 예외가
   * 터지지 않음을 인지. 그 후 Servlet 기반 response 전달
   */
  private void setResponse(HttpServletResponse response, ErrorCode exceptionCode)
      throws IOException, java.io.IOException {
    response.setStatus(exceptionCode.getHttpStatus().value());
    response.setContentType("application/json; charset=UTF-8");
    ResponseEntity<ApiResponse> errorResponse =
        ResponseEntity.status(ErrorCode.ACCESS_DENIED_EXCEPTION.getHttpStatus())
            .body(ApiResponse.toErrorForm(
                exceptionCode.getMessage()));
    try {
      response.getWriter().write(mapper.writeValueAsString(errorResponse));
    } catch (IOException e) {
      e.printStackTrace();
    }

  }
}
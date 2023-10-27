package com.wanted.socialintegratefreed.domain.user.jwt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wanted.socialintegratefreed.global.error.ErrorCode;
import com.wanted.socialintegratefreed.global.format.response.ApiResponse;
import io.jsonwebtoken.io.IOException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.ErrorResponse;

@Component
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper mapper;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
            AccessDeniedException accessDeniedException) throws IOException, java.io.IOException {
        ErrorCode exceptionCode;
        exceptionCode = ErrorCode.ACCESS_DENIED_EXCEPTION;
        setResponse(response, exceptionCode);

    }

    private void setResponse(HttpServletResponse response, ErrorCode exceptionCode)
            throws IOException, java.io.IOException {
        response.setStatus(exceptionCode.getHttpStatus().value());
        response.setContentType("application/json; charset=UTF-8");
        ResponseEntity<ApiResponse> errorResponse =
                ResponseEntity.status(ErrorCode.ACCESS_DENIED_EXCEPTION.getHttpStatus()).body(ApiResponse.toErrorForm(
                        exceptionCode.getMessage()));
        try {
            response.getWriter().write(mapper.writeValueAsString(errorResponse));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
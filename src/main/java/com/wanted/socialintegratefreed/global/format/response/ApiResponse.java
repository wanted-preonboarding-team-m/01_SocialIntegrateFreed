package com.wanted.socialintegratefreed.global.format.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Response JSON 포맷팅 형식
 */
@Getter
@AllArgsConstructor
public class ApiResponse {

  private static final String STATUS_SUCCESS = "success";
  private static final String STATUS_FAIL = "fail";
  private static final String STATUS_ERROR = "error";

  private String status;
  private String message;
  private Object data;

  /**
   * 성공했을 때, 반환하는 APIResponse
   *
   * @param data 데이터 내용
   * @return 성공 APIResponse
   */
  public static ApiResponse successForm(Object data) {
    return new ApiResponse(STATUS_SUCCESS, null, data);
  }

  /**
   * 실패했을 때, 반환하는 APIResponse
   *
   * @param message 실패 메시지
   * @return 실패 APIResponse
   */
  public static ApiResponse failForm(String message) {
    return new ApiResponse(STATUS_FAIL, message, null);
  }

  /**
   * 예외처리가 됐을 때, 반환하는 APIResponse
   *
   * @param message 에러 메시지
   * @return 에러 APIResponse
   */
  public static ApiResponse errorForm(String message) {
    return new ApiResponse(STATUS_ERROR, message, null);
  }
}

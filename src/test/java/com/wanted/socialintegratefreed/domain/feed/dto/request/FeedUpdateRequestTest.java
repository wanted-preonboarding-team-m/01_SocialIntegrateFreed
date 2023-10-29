package com.wanted.socialintegratefreed.domain.feed.dto.request;

import static com.wanted.socialintegratefreed.domain.feed.constant.FeedType.FACEBOOK;
import static com.wanted.socialintegratefreed.domain.feed.constant.FeedType.INSTAGRAM;
import static com.wanted.socialintegratefreed.domain.feed.constant.FeedType.THREADS;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.wanted.socialintegratefreed.domain.feed.constant.FeedType;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.PositiveOrZero;
import java.util.Set;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FeedUpdateRequestTest {

  private Validator validator;

  @BeforeEach
  void setUp() {
    validator = Validation.buildDefaultValidatorFactory().getValidator();
  }
  @Test
  @DisplayName("userId,title,content,type에 null 을 허용하지 않습니다.")
  void 널값_허용_안함() {
    //Given
    FeedUpdateRequest userDto = FeedUpdateRequest.builder()
        .userId(null)
        .title(null)
        .content(null)
        .type(null)
        .build();

    //When
    Set<ConstraintViolation<FeedUpdateRequest>> violations = validator.validate(userDto);

    //Then
    violations.forEach(i -> System.out.println(i.getMessage()));
    assertThat(violations.size()).isEqualTo(5);
    /*
    * 예상 결과
    * 내용을 입력해주세요.
      제목은 공백을 허용하지 않습니다.
      제목을 입력해주세요.
      사용자 아이디를 입력해주세요.
      게시글 유형을 입력해주세요.
    * */
  }

  @Test
  @DisplayName("제목은 공백을 허용하지 않습니다.")
  void 제목_공백_허용_안함() {
    //Given
    FeedUpdateRequest nullDto = FeedUpdateRequest.builder()
        .userId(1L)
        .title(" ")
        .content("내용")
        .type(INSTAGRAM)
        .build();

    //When
    Set<ConstraintViolation<FeedUpdateRequest>> violations = validator.validate(nullDto);

    //Then
    violations.forEach(i -> System.out.println(i.getMessage()));
    assertThat(violations.size()).isEqualTo(1);

  }

  @Test
  @DisplayName("올바른 사용자 아이디를 입력해주세요.(양수 허용)")
  void 사용자_아이디_확인() {
    //Given
    FeedUpdateRequest userDto = FeedUpdateRequest.builder()
        .userId(-30L)
        .title("제목")
        .content("내용")
        .type(FACEBOOK)
        .build();

    //When
    Set<ConstraintViolation<FeedUpdateRequest>> violations = validator.validate(userDto);

    //Then
    violations.forEach(i -> System.out.println(i.getMessage()));
    assertThat(violations.size()).isEqualTo(1);
  }

  @Test
  @DisplayName("제목의 크기가 0에서 128 사이여야 합니다")
  void 제목_128자_이상_오류() {
    //Given
    StringBuilder title = new StringBuilder();
    IntStream.range(0,200).forEach(i -> title.append("A"));

    FeedUpdateRequest userDto = FeedUpdateRequest.builder()
        .userId(1L)
        .title(title.toString())
        .content("내용")
        .type(FACEBOOK)
        .build();

    //When
    Set<ConstraintViolation<FeedUpdateRequest>> violations = validator.validate(userDto);


    //Then
    violations.forEach(i -> System.out.println(i.getMessage()));
    assertThat(violations.size()).isEqualTo(1);
  }

  @Test
  @DisplayName("제목은 100자이기 때문에 정상이다.")
  void 제목_128자_이하() {
    //Given
    StringBuilder title = new StringBuilder();
    IntStream.range(0,100).forEach(i -> title.append("A"));

    FeedUpdateRequest userDto = FeedUpdateRequest.builder()
        .userId(1L)
        .title(title.toString())
        .content("내용")
        .type(FACEBOOK)
        .build();

    //When
    Set<ConstraintViolation<FeedUpdateRequest>> violations = validator.validate(userDto);

    //Then
    violations.forEach(i -> System.out.println(i.getMessage()));
    assertThat(violations.size()).isEqualTo(0);
  }
}
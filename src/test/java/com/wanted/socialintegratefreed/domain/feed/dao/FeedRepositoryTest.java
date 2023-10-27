package com.wanted.socialintegratefreed.domain.feed.dao;

import static org.assertj.core.api.Assertions.assertThat;

import com.wanted.socialintegratefreed.domain.feed.constant.FeedType;
import com.wanted.socialintegratefreed.domain.feed.dto.request.FeedCreateRequest;
import com.wanted.socialintegratefreed.domain.feed.entity.Feed;
import com.wanted.socialintegratefreed.domain.user.dao.UserRepository;
import com.wanted.socialintegratefreed.domain.user.entity.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class FeedRepositoryTest{

  @Autowired
  private FeedRepository feedRepository;

  @Autowired
  private UserRepository userRepository;

  private Feed feed;
  private User user;

  @AfterEach
  public void cleanup() {
    feedRepository.deleteAll();
    userRepository.deleteAll();
  }

  @DisplayName("게시글이 성공적으로 저장된다.")
  @Test
  public void 게시글_저장() {
    // Given
    User user = createUser("test@example.com", "1234");
    FeedCreateRequest request = createFeedRequest("Test title", "Test content", FeedType.INSTAGRAM , user);

    // When
    Feed feed = feedRepository.save(request.toEntity(user));

    // Then
    Feed foundFeed = feedRepository.findById(feed.getFeedId()).orElse(null);
    assertThat(foundFeed).isNotNull();
    assertThat(foundFeed.getTitle()).isEqualTo(request.getTitle());
    assertThat(foundFeed.getContent()).isEqualTo(request.getContent());
    assertThat(foundFeed.getType()).isEqualTo(request.getType());
  }


  /**
   * 사용자 생성
   *
   * @param email 사용자 id
   * @param password 사용자 비밀번호
   * @return
   */
  private User createUser(String email, String password) {
    User user = new User(null, email, password);
    return userRepository.save(user);
  }


  /**
   * 게시물 생성 요청 dto
   *
   * @param title 저장할 게시물 제목
   * @param content 저장할 게시물 내용
   * @param type 저장할 게시물 타입
   * @param user 게시물 작성 사용자
   * @return FeedCreateRequest dto
   */
  private FeedCreateRequest createFeedRequest(String title, String content, FeedType type, User user) {
    return FeedCreateRequest.builder()
        .userId(user.getUserId())
        .title(title)
        .content(content)
        .type(type)
        .build();
  }


}

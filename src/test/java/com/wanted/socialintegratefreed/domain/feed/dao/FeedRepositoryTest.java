package com.wanted.socialintegratefreed.domain.feed.dao;

import static org.assertj.core.api.Assertions.assertThat;

import com.wanted.socialintegratefreed.domain.feed.constant.FeedType;
import com.wanted.socialintegratefreed.domain.feed.dto.request.FeedCreateRequest;
import com.wanted.socialintegratefreed.domain.feed.entity.Feed;
import com.wanted.socialintegratefreed.domain.user.dao.UserRepository;
import com.wanted.socialintegratefreed.domain.user.entity.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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

  @DisplayName("게시글이 성공적으로 수정된다.")
  @Test
  public void 게시물_수정() {
    // Given
    User user = createUser("test@example.com", "1234");
    Feed feed = createFeed("원래 제목", "원래 내용", FeedType.INSTAGRAM, user);

    // When
    feed.update(new Feed("수정 제목", "수정 내용", 0, 0, 0, FeedType.FACEBOOK, user));
    feedRepository.save(feed);

    // Then
    Feed updatedFeed = feedRepository.findById(feed.getFeedId()).orElse(null);
    assertThat(updatedFeed).isNotNull();
    assertThat(updatedFeed.getTitle()).isEqualTo("수정 제목");
    assertThat(updatedFeed.getContent()).isEqualTo("수정 내용");
    assertThat(updatedFeed.getType()).isEqualTo(FeedType.FACEBOOK);
  }

  @DisplayName("게시글이 성공적으로 삭제된다.")
  @Test
  public void 게시물_삭제() {
    // Given
    User user = createUser("test@example.com", "1234");
    Feed feed = createFeed("제목", "내용", FeedType.FACEBOOK, user);

    // When
    feedRepository.delete(feed);

    // Then
    Feed foundFeed = feedRepository.findById(feed.getFeedId()).orElse(null);
    assertThat(foundFeed).isNull();
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
   * 게시물 생성
   *
   * @param title 게시물 제목
   * @param content 게시물 내용
   * @param type 게시글의 유형
   * @param user 게시물을 작성한 사용자
   * @return Feed
   */
  private Feed createFeed(String title, String content, FeedType type, User user) {
    Feed feed = new Feed(title, content, 0, 0, 0, type, user);
    return feedRepository.save(feed);
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

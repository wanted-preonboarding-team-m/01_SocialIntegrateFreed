package com.wanted.socialintegratefreed.domain.feed.dao;

import static com.wanted.socialintegratefreed.domain.feed.constant.SearchType.DATE;
import static org.assertj.core.api.Assertions.assertThat;

import com.wanted.socialintegratefreed.domain.feed.constant.FeedType;
import com.wanted.socialintegratefreed.domain.feed.dto.request.FeedCreateRequest;
import com.wanted.socialintegratefreed.domain.feed.dto.request.FeedSearchCond;
import com.wanted.socialintegratefreed.domain.feed.entity.Feed;
import com.wanted.socialintegratefreed.domain.hashtag.dao.HashtagRepository;
import com.wanted.socialintegratefreed.domain.hashtag.entity.Hashtag;
import com.wanted.socialintegratefreed.domain.tagmatching.dao.TagMatchingRepository;
import com.wanted.socialintegratefreed.domain.tagmatching.entity.TagMatching;
import com.wanted.socialintegratefreed.domain.user.config.JpaConfig;
import com.wanted.socialintegratefreed.domain.user.constant.UserEnable;
import com.wanted.socialintegratefreed.domain.user.dao.UserRepository;
import com.wanted.socialintegratefreed.domain.user.entity.User;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@Import(JpaConfig.class) // Auditing 기능을 사용하기 위해 JpaConfig를 import
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class FeedRepositoryTest{

  @Autowired
  private FeedRepository feedRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private HashtagRepository hashtagRepository; // 통계 결과 테스트를 위해 추가

  @Autowired
  private TagMatchingRepository tagMatchingRepository; // 통계 결과 테스트를 위해 추가

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
    feedRepository.findById(feed.getFeedId())
            .orElseThrow(() -> new NoSuchElementException());

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

  @DisplayName("게시글의 상세 내용이 성공적으로 조회된다.")
  @Test
  public void 게시물_상세_조회() {
    // Given
    User user = createUser("test@example.com", "1234");
    Feed createFeed = createFeed("제목", "내용", FeedType.FACEBOOK, user);
    // When
    Feed searchFeed = feedRepository.findById(createFeed.getFeedId()).orElse(null);

    // Then
    assertThat(searchFeed).isNotNull();
    assertThat(searchFeed.getTitle()).isEqualTo(createFeed.getTitle());
    assertThat(searchFeed.getContent()).isEqualTo(createFeed.getContent());
    assertThat(searchFeed.getType()).isEqualTo(createFeed.getType());
  }

  @DisplayName("게시글 통계 결과가 성공적으로 반환된다.")
  @Test
  void makeStatisticsSuccess() {
    // 테스트용 게시물과 해시태그를 연결
    Feed feed = createFeed("제목1", "내용1", FeedType.FACEBOOK, user);
    Hashtag hashtag = createHashtag("해시태그1");
    createTagMatching(feed, hashtag);

    // search 메소드의 파라미터들
    FeedSearchCond searchCond = FeedSearchCond.builder()
        .hashtag("해시태그1")
        .type(DATE)
        .start(feed.getCreatedAt().minusDays(5))
        .end(feed.getCreatedAt().plusDays(5))
        .value("count")
        .build();
    LocalDateTime date = feed.getCreatedAt();

    Long count = feedRepository.search(date, searchCond);

    assertThat(count).isEqualTo(1);
  }

  /**
   * 사용자 생성
   *
   * @param email 사용자 id
   * @param password 사용자 비밀번호
   * @return
   */
  private User createUser(String email, String password) {
    User user = new User(null, email, password, UserEnable.USER_ENABLED);
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

  private Hashtag createHashtag(String name) {
    Hashtag hashtag = new Hashtag(name);
    return hashtagRepository.save(hashtag);
  }

  private TagMatching createTagMatching(Feed feed, Hashtag hashtag) {
    TagMatching tagMatching = new TagMatching(feed, hashtag);
    return tagMatchingRepository.save(tagMatching);
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

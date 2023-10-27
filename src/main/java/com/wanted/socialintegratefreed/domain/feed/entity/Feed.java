package com.wanted.socialintegratefreed.domain.feed.entity;

import com.wanted.socialintegratefreed.domain.feed.constant.FeedType;
import com.wanted.socialintegratefreed.domain.tagmatching.entity.TagMatching;
import com.wanted.socialintegratefreed.domain.user.entity.User;
import com.wanted.socialintegratefreed.global.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

/**
 * 게시물 엔티티
 */
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "feed")
public class Feed extends BaseTimeEntity {

  // 게시물 ID
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "feed_id", nullable = false)
  private Long feedId;

  // 게시글 제목
  @Column(name = "title", length = 128, nullable = false)
  private String title;

  // 게시글 내용
  @Column(name = "content", columnDefinition = "TEXT", nullable = false)
  private String content;

  // 조회 수
  @Column(name = "view_count", nullable = false)
  @ColumnDefault(value = "0")
  private Integer viewCount;

  // 좋아요 수
  @Column(name = "like_count", nullable = false)
  @ColumnDefault(value = "0")
  private Integer likeCount;

  // 공유 수
  @Column(name = "share_count", nullable = false)
  @ColumnDefault(value = "0")
  private Integer shareCount;

  // 게시물 타입
  @Column(name = "type", nullable = false)
  @Enumerated(EnumType.STRING)
  private FeedType type;

  // 'User'엔티티 와의 다대일 관계 설정
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  // 'TagMatching'엔티티와 일대다 관계 설정
  @OneToMany(mappedBy = "feed")
  private List<TagMatching> tagMatchings = new ArrayList<>();


  @Builder
  public Feed(String title, String content, Integer viewCount, Integer likeCount, Integer shareCount, FeedType type, User user) {
    this.title = title;
    this.content = content;
    this.viewCount = viewCount;
    this.likeCount = likeCount;
    this.shareCount = shareCount;
    this.type = type;
    this.user = user;
  }

  /**
   *
   * @param feed 수정할 feed 객체
   *
   */
  public void update(Feed feed) {
    if (feed.getTitle() != null) {
      this.title = feed.getTitle();
    }
    if (feed.getContent() != null) {
      this.content = feed.getContent();
    }
    if (feed.getType() != null) {
      this.type = feed.getType();
    }
  }
}

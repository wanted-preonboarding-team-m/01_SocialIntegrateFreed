package com.wanted.socialintegratefreed.domain.tagmatching.entity;

import com.wanted.socialintegratefreed.domain.feed.entity.Feed;
import com.wanted.socialintegratefreed.domain.hashtag.entity.Hashtag;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;


/**
 * `hashtag`, `feed` 엔티티와의 다대다 관계를 일대다 - 다대일 관계로 매핑하기 위한 중간 엔티티
 */
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "tag_matching")
public class TagMatching {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "tag_matching_id", nullable = false)
  private Long tagMatchingId;

  // 부모 엔티티인 'Feed'와의 관계 설정
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "feed_id")
  @Cascade(CascadeType.ALL)
  private Feed feed;

  // 부모 엔티티인 'Hashtag'와의 관계 설정
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "hashtag_id")
  @Cascade(CascadeType.ALL)
  private Hashtag hashtag;

  @Builder
  public TagMatching(Feed feed, Hashtag hashtag) {
    this.feed = feed;
    this.hashtag = hashtag;
  }
}

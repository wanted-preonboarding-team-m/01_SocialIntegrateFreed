package com.wanted.socialintegratefreed.domain.hashtag.entity;

import com.wanted.socialintegratefreed.domain.tagmatching.entity.TagMatching;
import com.wanted.socialintegratefreed.global.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Set;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 해시태그 엔티티
 */
@Entity
@NoArgsConstructor
@Getter
@Table(name = "hashtag")
public class Hashtag extends BaseTimeEntity {

  // 해시태그 ID
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "hashtag_id", nullable = false)
  private Long hashtagId;

  // 해시태그 이름
  @Column(name = "name", length = 30, nullable = false)
  private String name;

  // 'TagMatching'엔티티와 일대다 관계 설정
  @OneToMany(mappedBy = "hashtag")
  private Set<TagMatching> tagMatchings = new HashSet<>();

  @Builder
  public Hashtag(String name) {
    this.name = name;
  }
}

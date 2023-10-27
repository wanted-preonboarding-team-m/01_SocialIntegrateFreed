package com.wanted.socialintegratefreed.domain.feed.dto.response;

import com.wanted.socialintegratefreed.domain.feed.constant.FeedType;
import com.wanted.socialintegratefreed.domain.feed.entity.Feed;
import com.wanted.socialintegratefreed.domain.user.entity.User;
import lombok.Getter;

@Getter
public class FeedDetailResponse {
  private Long feedId;
  private String title;
  private String content;
  private FeedType type;
  private Integer viewCount;
  private Integer likeCount;
  private Integer shareCount;
  private User user;

  public FeedDetailResponse(Feed feed) {
    this.feedId = feed.getFeedId();
    this.title = feed.getTitle();
    this.content = feed.getContent();
    this.type = feed.getType();
    this.viewCount = feed.getViewCount();
    this.likeCount = feed.getLikeCount();
    this.shareCount = feed.getShareCount();
    this.user = feed.getUser();
  }
}
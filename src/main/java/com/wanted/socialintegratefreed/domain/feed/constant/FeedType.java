package com.wanted.socialintegratefreed.domain.feed.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 게시물 타입  Enum
 */
@RequiredArgsConstructor
@Getter
public enum FeedType {
  FACEBOOK("https://www.facebook.com"),
  TWITTER("https://www.twitter.com"),
  INSTAGRAM("https://www.instagram.com"),
  THREADS("https://www.threads.net");

  // 외부 api 호출시 사용할 url
  private final String url;
}

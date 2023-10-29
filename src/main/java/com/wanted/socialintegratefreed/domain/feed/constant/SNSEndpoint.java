package com.wanted.socialintegratefreed.domain.feed.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum SNSEndpoint {
  FACEBOOK("https://www.facebook.com"),
  TWITTER("https://www.twitter.com"),
  INSTAGRAM("https://www.instagram.com"),
  THREADS("https://www.threads.net");

  private final String url;
}

package com.wanted.socialintegratefreed.domain.feed.application;

import com.wanted.socialintegratefreed.domain.feed.dao.FeedRepository;
import com.wanted.socialintegratefreed.domain.feed.entity.Feed;
import com.wanted.socialintegratefreed.domain.user.entity.User;
import com.wanted.socialintegratefreed.domain.feed.dto.request.FeedCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FeedService {

  private final FeedRepository feedRepository;

  /**
   * 게시물 생성
   *
   * @param request 게시물 생성 dto
   * @param user 로그인 한 사용자
   * @return 생성된 게시글 Id
   */
  @Transactional
  public Long createFeed(FeedCreateRequest request, User user){
    Feed feed = feedRepository.save(request.toEntity(user));
    return feed.getFeedId();

  }
}

package com.wanted.socialintegratefreed.domain.feed.application;

import com.wanted.socialintegratefreed.domain.feed.dao.FeedRepository;
import com.wanted.socialintegratefreed.domain.feed.dto.request.FeedUpdateRequest;
import com.wanted.socialintegratefreed.domain.feed.dto.response.FeedDetailResponse;
import com.wanted.socialintegratefreed.domain.feed.entity.Feed;
import com.wanted.socialintegratefreed.domain.user.entity.User;
import com.wanted.socialintegratefreed.domain.feed.dto.request.FeedCreateRequest;
import com.wanted.socialintegratefreed.global.error.BusinessException;
import com.wanted.socialintegratefreed.global.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FeedService {

  private final FeedRepository feedRepository;

  /**
   * 게시물 상세 조회
   *
   * @param feedId 조회할 게시물 Id
   * @return 조회한 게시물
   */
  @Transactional(readOnly = true)
  public FeedDetailResponse getFeedById(Long feedId) {
    Feed feed = feedRepository.findById(feedId)
        .orElseThrow(() -> new BusinessException(feedId, "feedId", ErrorCode.FEED_NOT_FOUND));

    return new FeedDetailResponse(feed);
  }

}

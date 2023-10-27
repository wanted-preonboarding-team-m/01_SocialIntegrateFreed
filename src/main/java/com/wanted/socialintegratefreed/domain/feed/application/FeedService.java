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
   * 게시물 수정
   *
   * @param feedId 수정할 게시글 Id
   * @param request 수정할 게시글 내용 dto
   */
  @Transactional
  public void updateFeed(Long feedId, FeedUpdateRequest request, User user){

    //게시글 조회 없으면 예외 발생
    Feed feed = feedRepository.findById(feedId)
        .orElseThrow(() -> new BusinessException(feedId, "feedId", ErrorCode.FEED_NOT_FOUND));

    feed.update(request.toEntity(feed));
  }

  /**
   * 게시물 삭제
   *
   * @param feedId
   */
  @Transactional
  public void deleteFeed(Long feedId){
    //게시글 조회 없으면 예외 발생
    Feed feed = feedRepository.findById(feedId)
        .orElseThrow(() -> new BusinessException(feedId, "feedId", ErrorCode.FEED_NOT_FOUND));

    feedRepository.deleteById(feedId);
  }

}

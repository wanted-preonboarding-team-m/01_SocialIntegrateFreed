package com.wanted.socialintegratefreed.domain.feed.dao;

import com.wanted.socialintegratefreed.domain.feed.entity.Feed;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedRepository extends JpaRepository<Feed, Long>, FeedQueryRepository {

}

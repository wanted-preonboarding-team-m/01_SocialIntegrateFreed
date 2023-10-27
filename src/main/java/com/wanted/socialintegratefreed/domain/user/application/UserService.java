package com.wanted.socialintegratefreed.domain.user.application;

import com.wanted.socialintegratefreed.domain.user.dao.UserRepository;
import com.wanted.socialintegratefreed.domain.user.entity.User;
import com.wanted.socialintegratefreed.global.error.BusinessException;
import com.wanted.socialintegratefreed.global.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;

  /**
   * id로 사용자 조회
   *
   * @param userId 사용자 id
   * @return 조회된 사용자 Entity
   */
  public User getUserById(Long userId) {
    User user = userRepository.findById(userId).orElseThrow(
        () -> new BusinessException(userId, "companyId", ErrorCode.USER_NOT_FOUND)
    );

    return user;
  }

}

package com.wanted.socialintegratefreed.domain.user.application;


import com.wanted.socialintegratefreed.domain.user.dao.UserRepository;
import com.wanted.socialintegratefreed.domain.user.dto.UserSaveRequestDto;
import com.wanted.socialintegratefreed.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserService {


    private final UserRepository userRepository;


    public User createUser(UserSaveRequestDto userSaveRequestDto) {
        User user = User.builder()
                .email(userSaveRequestDto.getEmail())
                .password(userSaveRequestDto.getPassword())
                .build();
        return userRepository.save(user);
    }
}

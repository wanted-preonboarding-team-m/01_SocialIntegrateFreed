package com.wanted.socialintegratefreed.domain.user.dao;


import com.wanted.socialintegratefreed.domain.user.entity.User;
import jakarta.persistence.Id;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Id> {

}

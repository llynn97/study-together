package com.project.studytogether.repository;

import com.project.studytogether.entity.User;
import com.project.studytogether.entity.enums.UserMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findById(String id);

    User findByEmailAndMethod(String email, UserMethod method);

    User findByUserId(Long user_id);

    @Query("select MAX(u.userId) from user u")
    Long maxUserId();

    @Query("SELECT u.refreshToken FROM user u where u.userId=:id")
    String getRefreshTokenById(@Param("id") Long id);

    @Transactional
    @Modifying
    @Query("UPDATE user u SET u.refreshToken=:token WHERE u.userId=:id")
    void updateRefreshToken(@Param("id") Long id, @Param("token") String token);
}





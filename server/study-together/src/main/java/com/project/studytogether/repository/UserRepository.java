package com.project.studytogether.repository;

import com.project.studytogether.entity.User;
import com.project.studytogether.entity.enums.UserMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {


    Optional <User> findById(String id);
    Optional<User> findByEmailAndMethod(String email, UserMethod method);

    @Query("select MAX(u.user_id) from user u")
    Long maxUserId();

}





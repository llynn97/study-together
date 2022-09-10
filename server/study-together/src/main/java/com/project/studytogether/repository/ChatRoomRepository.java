package com.project.studytogether.repository;

import com.project.studytogether.entity.ChatRoom;
import com.project.studytogether.entity.Study;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
	Optional<ChatRoom> findByStudy(Study study);
}

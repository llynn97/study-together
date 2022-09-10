package com.project.studytogether.repository;

import com.project.studytogether.entity.Study;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudyRepository extends JpaRepository<Study, Long> {

}

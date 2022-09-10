package com.project.studytogether.repository;

import com.project.studytogether.entity.StudyCount;
import com.project.studytogether.entity.enums.StudyMethod;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudyCountRepository extends JpaRepository<StudyCount, Long> {
	Optional<StudyCount> findByStudyMethod(StudyMethod studyMethod);
}

package com.project.studytogether.repository;

import com.project.studytogether.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location, Long> {
}

package com.assessment.position.repository;

import com.assessment.position.model.Position;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PositionRepository extends JpaRepository<Position, Long> {
    @Query("SELECT p FROM Position p LEFT JOIN FETCH p.recruiter LEFT JOIN FETCH p.department")
    Page<Position> findAllPositionsWithDetails(Pageable pageable);
} 
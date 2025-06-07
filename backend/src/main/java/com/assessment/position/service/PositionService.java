package com.assessment.position.service;

import com.assessment.position.model.Position;
import com.assessment.position.repository.PositionRepository;
import com.assessment.position.repository.RecruiterRepository;
import com.assessment.position.repository.DepartmentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PositionService {
    private final PositionRepository positionRepository;
    private final RecruiterRepository recruiterRepository;
    private final DepartmentRepository departmentRepository;

    public Page<Position> getAllPositions(Pageable pageable) {
        return positionRepository.findAllPositionsWithDetails(pageable);
    }

    public Position getPosition(Long id) {
        return positionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Position not found with id: " + id));
    }

    @Transactional
    public Position createPosition(Position position) {
        // Fetch and set the recruiter
        var recruiter = recruiterRepository.findById(position.getRecruiter().getId())
                .orElseThrow(() -> new EntityNotFoundException("Recruiter not found with id: " + position.getRecruiter().getId()));
        position.setRecruiter(recruiter);

        // Fetch and set the department
        var department = departmentRepository.findById(position.getDepartment().getId())
                .orElseThrow(() -> new EntityNotFoundException("Department not found with id: " + position.getDepartment().getId()));
        position.setDepartment(department);

        return positionRepository.save(position);
    }

    @Transactional
    public Position updatePosition(Long id, Position position) {
        if (!positionRepository.existsById(id)) {
            throw new EntityNotFoundException("Position not found with id: " + id);
        }

        // Fetch and set the recruiter
        var recruiter = recruiterRepository.findById(position.getRecruiter().getId())
                .orElseThrow(() -> new EntityNotFoundException("Recruiter not found with id: " + position.getRecruiter().getId()));
        position.setRecruiter(recruiter);

        // Fetch and set the department
        var department = departmentRepository.findById(position.getDepartment().getId())
                .orElseThrow(() -> new EntityNotFoundException("Department not found with id: " + position.getDepartment().getId()));
        position.setDepartment(department);

        position.setId(id);
        return positionRepository.save(position);
    }

    @Transactional
    public void deletePosition(Long id) {
        if (!positionRepository.existsById(id)) {
            throw new EntityNotFoundException("Position not found with id: " + id);
        }
        positionRepository.deleteById(id);
    }
} 
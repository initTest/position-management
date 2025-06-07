package com.assessment.position.service;

import com.assessment.position.model.Position;
import com.assessment.position.model.Recruiter;
import com.assessment.position.model.Department;
import com.assessment.position.model.PositionStatus;
import com.assessment.position.repository.PositionRepository;
import com.assessment.position.repository.RecruiterRepository;
import com.assessment.position.repository.DepartmentRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PositionServiceTest {

    @Mock
    private PositionRepository positionRepository;

    @Mock
    private RecruiterRepository recruiterRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @InjectMocks
    private PositionService positionService;

    private Position position;
    private Recruiter recruiter;
    private Department department;

    @BeforeEach
    void setUp() {
        recruiter = new Recruiter();
        recruiter.setId(1L);
        recruiter.setName("Juan Perez");
        recruiter.setEmail("jperez@company.com");

        department = new Department();
        department.setId(1L);
        department.setName("Engineering");
        department.setCode("ENG");

        position = new Position();
        position.setId(1L);
        position.setTitle("Software Engineer");
        position.setDescription("Java Developer");
        position.setLocation("Remote");
        position.setStatus(PositionStatus.OPEN);
        position.setRecruiter(recruiter);
        position.setDepartment(department);
        position.setBudget(new BigDecimal("100000.00"));
    }

    @Test
    void getAllPositions_ShouldReturnPageOfPositions() {
        // Arrange
        List<Position> positions = List.of(position);
        Page<Position> positionPage = new PageImpl<>(positions);
        Pageable pageable = PageRequest.of(0, 10);
        when(positionRepository.findAllPositionsWithDetails(pageable)).thenReturn(positionPage);

        // Act
        Page<Position> result = positionService.getAllPositions(pageable);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(position, result.getContent().get(0));
        verify(positionRepository).findAllPositionsWithDetails(pageable);
    }

    @Test
    void getPosition_WithValidId_ShouldReturnPosition() {
        when(positionRepository.findById(1L)).thenReturn(Optional.of(position));

        Position result = positionService.getPosition(1L);

        assertNotNull(result);
        assertEquals(position.getId(), result.getId());
        assertEquals(position.getTitle(), result.getTitle());
    }

    @Test
    void getPosition_WithInvalidId_ShouldThrowException() {
        when(positionRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> positionService.getPosition(1L));
    }

    @Test
    void createPosition_WithValidData_ShouldReturnCreatedPosition() {
        when(recruiterRepository.findById(1L)).thenReturn(Optional.of(recruiter));
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));
        when(positionRepository.save(any(Position.class))).thenReturn(position);

        Position result = positionService.createPosition(position);

        assertNotNull(result);
        assertEquals(position.getTitle(), result.getTitle());
        verify(positionRepository).save(any(Position.class));
    }

    @Test
    void createPosition_WithInvalidRecruiter_ShouldThrowException() {
        when(recruiterRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> positionService.createPosition(position));
        verify(positionRepository, never()).save(any(Position.class));
    }

    @Test
    void createPosition_WithInvalidDepartment_ShouldThrowException() {
        when(recruiterRepository.findById(1L)).thenReturn(Optional.of(recruiter));
        when(departmentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> positionService.createPosition(position));
        verify(positionRepository, never()).save(any(Position.class));
    }

    @Test
    void updatePosition_WithValidData_ShouldReturnUpdatedPosition() {
        when(positionRepository.existsById(1L)).thenReturn(true);
        when(recruiterRepository.findById(1L)).thenReturn(Optional.of(recruiter));
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));
        when(positionRepository.save(any(Position.class))).thenReturn(position);

        Position result = positionService.updatePosition(1L, position);

        assertNotNull(result);
        assertEquals(position.getTitle(), result.getTitle());
        verify(positionRepository).save(any(Position.class));
    }

    @Test
    void deletePosition_WithValidId_ShouldDeletePosition() {
        when(positionRepository.existsById(1L)).thenReturn(true);

        positionService.deletePosition(1L);

        verify(positionRepository).deleteById(1L);
    }
} 
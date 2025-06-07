package com.assessment.position.controller;

import com.assessment.position.config.TestSecurityConfig;
import com.assessment.position.model.Position;
import com.assessment.position.model.Recruiter;
import com.assessment.position.model.Department;
import com.assessment.position.model.PositionStatus;
import com.assessment.position.service.PositionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PositionController.class)
@Import(TestSecurityConfig.class)
class PositionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PositionService positionService;

    @Autowired
    private ObjectMapper objectMapper;

    private Position testPosition;
    private List<Position> testPositions;
    private Page<Position> testPositionPage;

    @BeforeEach
    void setUp() {
        Recruiter recruiter = new Recruiter();
        recruiter.setId(1L);
        recruiter.setName("Juan Perez");
        recruiter.setEmail("jperez@company.com");

        Department department = new Department();
        department.setId(1L);
        department.setName("Engineering");
        department.setCode("ENG");

        testPosition = new Position();
        testPosition.setId(1L);
        testPosition.setTitle("Software Engineer");
        testPosition.setDescription("Java Developer position");
        testPosition.setLocation("Remote");
        testPosition.setStatus(PositionStatus.OPEN);
        testPosition.setRecruiter(recruiter);
        testPosition.setDepartment(department);
        testPosition.setBudget(new BigDecimal("100000.00"));

        testPositions = Arrays.asList(testPosition);
        testPositionPage = new PageImpl<>(testPositions, PageRequest.of(0, 10), 1);
    }

    @Test
    void getAllPositions_ShouldReturnPositionsList() throws Exception {
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id"));
        when(positionService.getAllPositions(any(PageRequest.class))).thenReturn(testPositionPage);

        mockMvc.perform(get("/api/positions")
                .header("X-API-KEY", "1234567890"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content[0].title").value("Software Engineer"))
                .andExpect(jsonPath("$.content[0].location").value("Remote"))
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.size").value(10))
                .andExpect(jsonPath("$.number").value(0));
    }

    @Test
    void getPosition_ShouldReturnPosition() throws Exception {
        when(positionService.getPosition(1L)).thenReturn(testPosition);

        mockMvc.perform(get("/api/positions/1")
                .header("X-API-KEY", "1234567890"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title").value("Software Engineer"));
    }

    @Test
    void createPosition_ShouldReturnCreatedPosition() throws Exception {
        when(positionService.createPosition(any(Position.class))).thenReturn(testPosition);

        mockMvc.perform(post("/api/positions")
                .header("X-API-KEY", "1234567890")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testPosition)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Software Engineer"));
    }

    @Test
    void updatePosition_ShouldReturnUpdatedPosition() throws Exception {
        when(positionService.updatePosition(eq(1L), any(Position.class))).thenReturn(testPosition);

        mockMvc.perform(put("/api/positions/1")
                .header("X-API-KEY", "1234567890")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testPosition)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Software Engineer"));
    }

    @Test
    void deletePosition_ShouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/api/positions/1")
                .header("X-API-KEY", "1234567890"))
                .andExpect(status().isNoContent());
    }
} 
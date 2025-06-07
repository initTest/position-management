package com.assessment.position.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "positions")
public class Position {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 100)
    private String title;

    @NotBlank
    @Size(max = 1000)
    @Column(length = 1000)
    private String description;

    @NotBlank
    private String location;

    @NotNull
    @Enumerated(EnumType.STRING)
    private PositionStatus status;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "recruiter_id")
    private Recruiter recruiter;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal budget;

    private LocalDate closingDate;
} 
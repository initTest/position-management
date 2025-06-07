package com.assessment.position.controller;

import com.assessment.position.model.Recruiter;
import com.assessment.position.repository.RecruiterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recruiters")
@RequiredArgsConstructor
public class RecruiterController {
    private final RecruiterRepository recruiterRepository;

    @GetMapping
    public ResponseEntity<List<Recruiter>> getAllRecruiters() {
        return ResponseEntity.ok(recruiterRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Recruiter> getRecruiter(@PathVariable Long id) {
        return recruiterRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
} 
package com.assessment.position.controller;

import com.assessment.position.model.Position;
import com.assessment.position.service.PositionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/positions")
@RequiredArgsConstructor
@Tag(name = "Position Management", description = "APIs for managing positions")
public class PositionController {
    private final PositionService positionService;

    @Operation(summary = "Get all positions with pagination",
            description = "Retrieves a paginated list of positions with optional sorting")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved positions"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing API key")
    })
    @GetMapping
    public ResponseEntity<Page<Position>> getAllPositions(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Number of items per page") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Field to sort by") @RequestParam(defaultValue = "title") String sortBy,
            @Parameter(description = "Sort direction (asc/desc)") @RequestParam(defaultValue = "asc") String direction) {
        
        Sort.Direction sortDirection = direction.equalsIgnoreCase("desc") ? 
            Sort.Direction.DESC : Sort.Direction.ASC;
        
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));
        return ResponseEntity.ok(positionService.getAllPositions(pageRequest));
    }

    @Operation(summary = "Get position by ID",
            description = "Retrieves a specific position by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved position"),
        @ApiResponse(responseCode = "404", description = "Position not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing API key")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Position> getPosition(@Parameter(description = "Position ID") @PathVariable Long id) {
        return ResponseEntity.ok(positionService.getPosition(id));
    }

    @Operation(summary = "Create new position",
            description = "Creates a new position with the provided details")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Position created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing API key")
    })
    @PostMapping
    public ResponseEntity<Position> createPosition(@Parameter(description = "Position details") @Valid @RequestBody Position position) {
        return new ResponseEntity<>(positionService.createPosition(position), HttpStatus.CREATED);
    }

    @Operation(summary = "Update position",
            description = "Updates an existing position with the provided details")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Position updated successfully"),
        @ApiResponse(responseCode = "404", description = "Position not found"),
        @ApiResponse(responseCode = "400", description = "Invalid input"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing API key")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Position> updatePosition(
            @Parameter(description = "Position ID") @PathVariable Long id,
            @Parameter(description = "Updated position details") @Valid @RequestBody Position position) {
        return ResponseEntity.ok(positionService.updatePosition(id, position));
    }

    @Operation(summary = "Delete position",
            description = "Deletes a position by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Position deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Position not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing API key")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePosition(@Parameter(description = "Position ID") @PathVariable Long id) {
        positionService.deletePosition(id);
        return ResponseEntity.noContent().build();
    }
} 
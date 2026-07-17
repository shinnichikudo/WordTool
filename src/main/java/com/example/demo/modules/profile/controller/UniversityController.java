package com.example.demo.modules.profile.controller;

import com.example.demo.modules.profile.entity.University;
import com.example.demo.modules.profile.service.UniversityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/universities")
@RequiredArgsConstructor
public class UniversityController {

    private final UniversityService universityService;

    // API lấy danh sách trường học (GET)
    @GetMapping
    public ResponseEntity<List<University>> getAllUniversities() {
        return ResponseEntity.ok(universityService.getAllUniversities());
    }

    // API thêm trường học mới (POST)
    @PostMapping
    public ResponseEntity<University> createUniversity(@RequestBody University university) {
        return ResponseEntity.ok(universityService.createUniversity(university));
    }
}
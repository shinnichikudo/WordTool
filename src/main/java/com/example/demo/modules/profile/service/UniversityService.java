package com.example.demo.modules.profile.service;

import com.example.demo.modules.profile.entity.University;
import com.example.demo.modules.profile.repository.UniversityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor // Lombok tự động tạo constructor để inject repository
public class UniversityService {

    private final UniversityRepository universityRepository;

    public List<University> getAllUniversities() {
        return universityRepository.findAll();
    }

    public University createUniversity(University university) {
        // Kiểm tra xem mã trường đã tồn tại chưa
        if (universityRepository.findByCode(university.getCode()).isPresent()) {
            throw new RuntimeException("Mã trường này đã tồn tại trong hệ thống!");
        }

        // Thêm thời gian tạo
        university.setCreatedAt(LocalDateTime.now());
        university.setUpdatedAt(LocalDateTime.now());

        return universityRepository.save(university);
    }
}
package com.example.demo.modules.profile.repository;

import com.example.demo.modules.profile.entity.University;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UniversityRepository extends JpaRepository<University, Long> {
    // Spring Boot tự động hiểu hàm này tương đương câu lệnh: SELECT * FROM universities WHERE code = ?
    Optional<University> findByCode(String code);
}
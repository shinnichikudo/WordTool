package com.example.demo.modules.profile.repository;

import com.example.demo.modules.profile.entity.FormatProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FormatProfileRepository extends JpaRepository<FormatProfile, Long> {


    List<FormatProfile> findByUniversityId(Long universityId);


    Optional<FormatProfile> findByProfileName(String profileName);
}
package com.example.demo.modules.profile.repository;

import com.example.demo.modules.profile.entity.PageSetup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PageSetupRepository extends JpaRepository<PageSetup, Long> {
    // Tương đương: SELECT * FROM page_setups WHERE profile_id = ?
    Optional<PageSetup> findByFormatProfileId(Long profileId);
}
package com.example.demo.modules.profile.repository;

import com.example.demo.modules.profile.entity.StructureRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StructureRuleRepository extends JpaRepository<StructureRule, Long> {
    Optional<StructureRule> findByFormatProfileId(Long profileId);
}
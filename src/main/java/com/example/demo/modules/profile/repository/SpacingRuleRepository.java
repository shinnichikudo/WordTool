package com.example.demo.modules.profile.repository;

import com.example.demo.modules.profile.entity.SpacingRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SpacingRuleRepository extends JpaRepository<SpacingRule, Long> {

    List<SpacingRule> findByFormatProfileId(Long profileId);

    Optional<SpacingRule> findByFormatProfileIdAndTargetType(Long profileId, String targetType);
}
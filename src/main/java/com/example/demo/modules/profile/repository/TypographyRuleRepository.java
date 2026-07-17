package com.example.demo.modules.profile.repository;

import com.example.demo.modules.profile.entity.TypographyRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TypographyRuleRepository extends JpaRepository<TypographyRule, Long> {

    // Lấy toàn bộ quy tắc font chữ của bộ hồ sơ
    List<TypographyRule> findByFormatProfileId(Long profileId);

    // Tương đương: SELECT * FROM typography_rules WHERE profile_id = ? AND target_type = ?
    // Dùng cái này để Validator check cực nhanh khi gặp một Paragraph cụ thể
    Optional<TypographyRule> findByFormatProfileIdAndTargetType(Long profileId, String targetType);
}
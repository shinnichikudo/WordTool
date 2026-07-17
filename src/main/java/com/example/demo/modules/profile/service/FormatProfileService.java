package com.example.demo.modules.profile.service;

import com.example.demo.modules.profile.entity.*;
import com.example.demo.modules.profile.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class FormatProfileService {

    private final UniversityRepository universityRepository;
    private final FormatProfileRepository formatProfileRepository;
    private final PageSetupRepository pageSetupRepository;
    private final TypographyRuleRepository typographyRuleRepository;

    /**
     * @Transactional: Đảm bảo tính ACID.
     * Nếu lưu PageSetup lỗi thì FormatProfile cũng không được lưu vào DB.
     */
    @Transactional
    public FormatProfile createFullProfile(Long universityId, String profileName,
                                           PageSetup pageSetup, TypographyRule bodyTypography) {

        //  Tìm trường học (University)
        University university = universityRepository.findById(universityId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy trường học với ID: " + universityId));

        //  Tạo và lưu FormatProfile (Vỏ)
        FormatProfile profile = FormatProfile.builder()
                .university(university)
                .profileName(profileName)
                .isActive(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        FormatProfile savedProfile = formatProfileRepository.save(profile);

        //  Gắn Profile ID vào PageSetup và lưu (Ruột 1)
        pageSetup.setFormatProfile(savedProfile);
        pageSetupRepository.save(pageSetup);

        //  Gắn Profile ID vào Typography (Body) và lưu (Ruột 2)
        bodyTypography.setFormatProfile(savedProfile);
        bodyTypography.setTargetType("BODY"); // Cố định đây là rule cho phần Body
        typographyRuleRepository.save(bodyTypography);

        // (Thực tế bạn sẽ lưu thêm SpacingRule và StructureRule ở đây)

        return savedProfile;
    }

    /**
     * Hàm này được dùng nhiều nhất bởi Validator Engine
     * Lấy cấu hình PageSetup chuẩn để kiểm tra file Word
     */
    public PageSetup getPageSetupConfig(Long profileId) {
        return pageSetupRepository.findByFormatProfileId(profileId)
                .orElseThrow(() -> new RuntimeException("Chưa có cấu hình khổ giấy cho Profile này"));
    }
}
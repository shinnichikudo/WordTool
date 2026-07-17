package com.example.demo.modules.profile.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "spacing_rules")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SpacingRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", nullable = false)
    private FormatProfile formatProfile;

    @Column(nullable = false, length = 50)
    private String targetType;

    private String lineSpacingType; // EXACTLY, MULTIPLE...
    private Double lineSpacingValue;
    private Double spacingBefore;
    private Double spacingAfter;
}
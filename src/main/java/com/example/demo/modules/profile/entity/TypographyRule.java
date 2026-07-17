package com.example.demo.modules.profile.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "typography_rules")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TypographyRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", nullable = false)
    private FormatProfile formatProfile;

    @Column(nullable = false, length = 50)
    private String targetType; // VD: BODY, HEADING_1, CAPTION

    private String fontFamily; // VD: Times New Roman
    private Double fontSize;   // VD: 13.0
    private Boolean isBold;
    private Boolean isItalic;
    private String alignment;  // VD: JUSTIFY, LEFT, CENTER
}
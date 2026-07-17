package com.example.demo.modules.profile.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "structure_rules")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StructureRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", nullable = false, unique = true)
    private FormatProfile formatProfile;

    private Boolean requireToc;
    private String imageCaptionPosition;
    private String tableCaptionPosition;
    private Integer maxHeadingLevel;
}
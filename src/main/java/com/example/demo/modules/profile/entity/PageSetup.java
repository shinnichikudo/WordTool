package com.example.demo.modules.profile.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "page_setups")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PageSetup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Quan hệ 1-1 với FormatProfile
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", nullable = false, unique = true)
    private FormatProfile formatProfile;

    private String paperSize;
    private Double marginTop;
    private Double marginBottom;
    private Double marginLeft;
    private Double marginRight;
}
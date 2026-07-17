package com.example.demo.modules.profile.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity // Đánh dấu đây là một thực thể CSDL
@Table(name = "universities") // Tên bảng trong MySQL
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class University {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 50)
    private String code;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    // Quan hệ 1-N: 1 trường có nhiều hồ sơ quy định
    // mappedBy: Trỏ tới tên biến 'university' trong class FormatProfile
    @OneToMany(mappedBy = "university", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<FormatProfile> formatProfiles;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
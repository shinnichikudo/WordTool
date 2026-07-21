package com.example.demo.modules.engine.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class AnalysisResponseDTO {
    private String fileName;
    private String status;
    private String message;

    private List<String> errors;
}
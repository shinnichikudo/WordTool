package com.example.demo.modules.engine.controller;

import com.example.demo.modules.engine.dto.AnalysisResponseDTO;
import com.example.demo.modules.engine.parser.DocxParserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/engine")
@RequiredArgsConstructor
public class DocumentAnalysisController {

    private final DocxParserService docxParserService;

    @PostMapping("/analyze")
    public ResponseEntity<AnalysisResponseDTO> analyzeDocument(@RequestParam("file") MultipartFile file) {

        //  Kiểm tra file hợp lệ
        if (file.isEmpty() || !file.getOriginalFilename().endsWith(".docx")) {
            return ResponseEntity.badRequest().body(
                    AnalysisResponseDTO.builder()
                            .status("FAILED")
                            .message("Vui lòng tải lên file Word định dạng .docx hợp lệ!")
                            .build()
            );
        }

        try {
            //  Giao cho Parser bóc tách dữ liệu
            docxParserService.parseWordFile(file);

            //  Trả về phản hồi thành công (Lúc này kết quả phân tích đang được in ra Console)
            return ResponseEntity.ok(
                    AnalysisResponseDTO.builder()
                            .fileName(file.getOriginalFilename())
                            .status("SUCCESS")
                            .message("Thuật toán đã chạy xong! ")
                            .build()
            );

        } catch (Exception e) {
            // Bắt mọi lỗi xảy ra trong quá trình đọc file để API không bị crash
            return ResponseEntity.internalServerError().body(
                    AnalysisResponseDTO.builder()
                            .fileName(file.getOriginalFilename())
                            .status("ERROR")
                            .message("Đã xảy ra lỗi kỹ thuật: " + e.getMessage())
                            .build()
            );
        }
    }
}
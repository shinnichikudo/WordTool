package com.example.demo.modules.engine.controller;

import com.example.demo.modules.engine.dto.AnalysisResponseDTO;
import com.example.demo.modules.engine.parser.DocxParserService;
import com.example.demo.modules.profile.entity.PageSetup;
import com.example.demo.modules.profile.entity.TypographyRule;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/engine")
@RequiredArgsConstructor
public class DocumentAnalysisController {

    private final DocxParserService docxParserService;

    @PostMapping("/analyze")
    public ResponseEntity<AnalysisResponseDTO> analyzeDocument(@RequestParam("file") MultipartFile file) {

        // Kiểm tra file hợp lệ
        if (file.isEmpty() || !file.getOriginalFilename().endsWith(".docx")) {
            return ResponseEntity.badRequest().body(
                    AnalysisResponseDTO.builder()
                            .status("FAILED")
                            .message("Vui lòng tải lên file Word định dạng .docx hợp lệ!")
                            .build()
            );
        }

        try {
            //  Tạo một bộ luật (Rule) giả lập để test thuật toán trước khi nối với Database
            //  dùng Times New Roman, cỡ 13, không in đậm, không in nghiêng
            PageSetup mockPageSetup = PageSetup.builder()
                    .pageWidth(21.0)
                    .pageHeight(29.7)
                    .marginTop(2.0)
                    .marginBottom(2.0)
                    .marginLeft(3.0)
                    .marginRight(2.0)
                    .build();
            // Giả lập luật Typography
            TypographyRule mockRule = TypographyRule.builder()
                    .fontFamily("Times New Roman")
                    .fontSize(13.0)
                    .isBold(false)
                    .isItalic(false)
                    .build();

            // Giao cho Parser bóc tách dữ liệu và đối chiếu với bộ luật mockRule ở trên
            List<String> errors = docxParserService.parseWordFile(file, mockRule,mockPageSetup);

            //  Trả về phản hồi cho Postman kèm theo danh sách các lỗi vi phạm
            return ResponseEntity.ok(
                    AnalysisResponseDTO.builder()
                            .fileName(file.getOriginalFilename())
                            .status(errors.isEmpty() ? "PASSED" : "FAILED") // Nếu danh sách lỗi rỗng -> PASSED
                            .message("Thuật toán đã chạy xong!")
                            .errors(errors)
                            .build()
            );

        } catch (Exception e) {
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
package com.example.demo.modules.engine.parser;

import com.example.demo.modules.engine.validator.TypographyValidator;
import com.example.demo.modules.profile.entity.TypographyRule;
import lombok.RequiredArgsConstructor;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.wml.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor // Tự động inject TypographyValidator vào
public class DocxParserService {

    //  Tiêm Validator vào đây
    private final TypographyValidator typographyValidator;

    // thêm bộ luật (rule) và trả về danh sách lỗi
    public List<String> parseWordFile(MultipartFile file, TypographyRule rule) {
        List<String> allErrors = new ArrayList<>();

        try (InputStream inputStream = file.getInputStream()) {
            WordprocessingMLPackage wordPackage = WordprocessingMLPackage.load(inputStream);
            MainDocumentPart mainDocumentPart = wordPackage.getMainDocumentPart();
            List<Object> documentElements = mainDocumentPart.getContent();

            for (Object obj : documentElements) {
                if (obj instanceof P paragraph) {
                    // Gom lỗi của từng đoạn văn lại
                    allErrors.addAll(processParagraphNode(paragraph, rule));
                }
            }
            return allErrors;

        } catch (Exception e) {
            throw new RuntimeException("Lỗi thuật toán khi phân tích file Word: " + e.getMessage());
        }
    }

    private List<String> processParagraphNode(P paragraph, TypographyRule rule) {
        List<String> paragraphErrors = new ArrayList<>();
        List<Object> runs = paragraph.getContent();

        for (Object runObj : runs) {
            if (runObj instanceof R run) {
                String text = extractTextFromRun(run);
                RPr runProperties = run.getRPr();

                if (runProperties != null && !text.trim().isEmpty()) {
                    // Chuyền dữ liệu sang hàm check
                    paragraphErrors.addAll(checkFormatting(runProperties, text, rule));
                }
            }
        }
        return paragraphErrors;
    }

    private String extractTextFromRun(R run) {
        StringBuilder text = new StringBuilder();
        for (Object obj : run.getContent()) {
            if (obj instanceof jakarta.xml.bind.JAXBElement) {
                Object unwrapped = ((jakarta.xml.bind.JAXBElement<?>) obj).getValue();
                if (unwrapped instanceof Text t) {
                    text.append(t.getValue());
                }
            } else if (obj instanceof Text t) {
                text.append(t.getValue());
            }
        }
        return text.toString();
    }

    private List<String> checkFormatting(RPr runProperties, String text, TypographyRule rule) {
        // Khởi tạo các giá trị mặc định
        String fontName = null;
        Double fontSize = null;
        Boolean isBold = false;
        Boolean isItalic = false;

        // Bóc tách Font
        if (runProperties.getRFonts() != null) {
            fontName = runProperties.getRFonts().getAscii();
        }
        // Bóc tách Cỡ chữ
        if (runProperties.getSz() != null) {
            fontSize = runProperties.getSz().getVal().doubleValue() / 2;
        }
        // Bóc tách In đậm (Trong docx4j, nếu getB() != null tức là có bật in đậm)
        if (runProperties.getB() != null) {
            isBold = true;
        }
        // Bóc tách In nghiêng
        if (runProperties.getI() != null) {
            isItalic = true;
        }

        //  GỌI VALIDATOR VÀ TRẢ VỀ LỖI
        return typographyValidator.validate(text, fontName, fontSize, isBold, isItalic, rule);
    }
}
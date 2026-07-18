package com.example.demo.modules.engine.parser;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.wml.P;
import org.docx4j.wml.R;
import org.docx4j.wml.RPr;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.docx4j.wml.Text;

import java.io.InputStream;
import java.util.List;
@Service
public class DocxParserService {
    public void parseWordFile(MultipartFile file) {
        try (InputStream inputStream = file.getInputStream()) {

            //  Dựng cây cú pháp XML từ file Word
            WordprocessingMLPackage wordPackage = WordprocessingMLPackage.load(inputStream);
            MainDocumentPart mainDocumentPart = wordPackage.getMainDocumentPart();

            //  Lấy danh sách tất cả các node con trực tiếp của Body
            List<Object> documentElements = mainDocumentPart.getContent();

            System.out.println(" BẮT ĐẦU DUYỆT CÂY TÀI LIỆU");

            //  Duyệt qua từng node
            for (Object obj : documentElements) {
                if (obj instanceof P paragraph) {
                    processParagraphNode(paragraph);
                }
                // Sau này làm tiếp: else if (obj instanceof Tbl) { processTableNode(obj); }
            }

        } catch (Exception e) {
            throw new RuntimeException("Lỗi thuật toán khi phân tích file Word: " + e.getMessage());
        }
    }
    private void processParagraphNode(P paragraph) {
        StringBuilder paragraphText = new StringBuilder();

        // Lấy danh sách các node con của Đoạn văn (Thường là các node R - Run)
        List<Object> runs = paragraph.getContent();

        for (Object runObj : runs) {
            if (runObj instanceof R run) {
                //  Rút trích nội dung chữ (Node T)
                String text = extractTextFromRun(run);
                paragraphText.append(text);

                //  Rút trích định dạng (Font, Cỡ chữ, In đậm...) từ Node RPr (Run Properties)
                RPr runProperties = run.getRPr();
                if (runProperties != null && !text.trim().isEmpty()) {
                    checkFormatting(runProperties, text);
                }
            }
        }
        if (!paragraphText.toString().trim().isEmpty()) {
            System.out.println("[Nội dung đoạn]: " + paragraphText.toString());
            System.out.println("--------------------------------------------------");
        }
    }
    private String extractTextFromRun(R run) {
        StringBuilder text = new StringBuilder();
        for (Object obj : run.getContent()) {
            // JAXBElement bọc node Text (T) bên trong
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

    private void checkFormatting(RPr runProperties, String text) {
        // Lấy Cỡ chữ (Trong Word, cỡ chữ lưu dưới dạng half-point. VD: 26 = 13pt)
        if (runProperties.getSz() != null) {
            double fontSize = runProperties.getSz().getVal().doubleValue() / 2;
            System.out.println("   -> Chữ '" + text.trim() + "' có cỡ: " + fontSize + "pt");
        }

        // Lấy In đậm
        if (runProperties.getB() != null) {
            System.out.println("   -> Chữ '" + text.trim() + "' được In đậm");
        }

        // Lấy Font chữ (Ascii font)
        if (runProperties.getRFonts() != null) {
            String fontName = runProperties.getRFonts().getAscii();
            System.out.println("   -> Chữ '" + text.trim() + "' dùng Font: " + fontName);
        }
    }
}

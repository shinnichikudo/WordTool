package com.example.demo.modules.engine.parser;

import com.example.demo.modules.engine.validator.PageSetupValidator;
import com.example.demo.modules.engine.validator.TypographyValidator;
import com.example.demo.modules.profile.entity.PageSetup;
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
@RequiredArgsConstructor
public class DocxParserService {

    private final TypographyValidator typographyValidator;
    private final PageSetupValidator pageSetupValidator;


    public List<String> parseWordFile(MultipartFile file, TypographyRule typoRule, PageSetup pageSetupRule) {
        List<String> allErrors = new ArrayList<>();

        try (InputStream inputStream = file.getInputStream()) {
            WordprocessingMLPackage wordPackage = WordprocessingMLPackage.load(inputStream);
            MainDocumentPart mainDocumentPart = wordPackage.getMainDocumentPart();

            //  KIỂM TRA CĂN LỀ VÀ KHỔ GIẤY
            allErrors.addAll(checkPageSetup(mainDocumentPart, pageSetupRule));

            // KIỂM TRA FONT CHỮ VÀ ĐỊNH DẠNG TEXT
            List<Object> documentElements = mainDocumentPart.getContent();
            for (Object obj : documentElements) {
                if (obj instanceof P paragraph) {
                    allErrors.addAll(processParagraphNode(paragraph, typoRule));
                }
            }
            return allErrors;

        } catch (Exception e) {
            throw new RuntimeException("Lỗi thuật toán khi phân tích file Word: " + e.getMessage());
        }
    }

    private List<String> checkPageSetup(MainDocumentPart mainDocumentPart, PageSetup rule) {
        List<String> errors = new ArrayList<>();
        if (rule == null) return errors;

        SectPr sectPr = mainDocumentPart.getJaxbElement().getBody().getSectPr();
        if (sectPr == null) return errors;

        Double actualWidth = null, actualHeight = null;
        Double actualTop = null, actualBottom = null, actualLeft = null, actualRight = null;
        final double TWIPS_TO_CM = 567.0;

        if (sectPr.getPgSz() != null) {
            if (sectPr.getPgSz().getW() != null) {
                actualWidth = sectPr.getPgSz().getW().doubleValue() / TWIPS_TO_CM;
            }
            if (sectPr.getPgSz().getH() != null) {
                actualHeight = sectPr.getPgSz().getH().doubleValue() / TWIPS_TO_CM;
            }
        }

        if (sectPr.getPgMar() != null) {
            if (sectPr.getPgMar().getTop() != null) {
                actualTop = sectPr.getPgMar().getTop().doubleValue() / TWIPS_TO_CM;
            }
            if (sectPr.getPgMar().getBottom() != null) {
                actualBottom = sectPr.getPgMar().getBottom().doubleValue() / TWIPS_TO_CM;
            }
            if (sectPr.getPgMar().getLeft() != null) {
                actualLeft = sectPr.getPgMar().getLeft().doubleValue() / TWIPS_TO_CM;
            }
            if (sectPr.getPgMar().getRight() != null) {
                actualRight = sectPr.getPgMar().getRight().doubleValue() / TWIPS_TO_CM;
            }
        }

        return pageSetupValidator.validate(actualWidth, actualHeight, actualTop, actualBottom, actualLeft, actualRight, rule);
    }

    private List<String> processParagraphNode(P paragraph, TypographyRule rule) {
        List<String> paragraphErrors = new ArrayList<>();
        List<Object> runs = paragraph.getContent();

        for (Object runObj : runs) {
            if (runObj instanceof R run) {
                String text = extractTextFromRun(run);
                RPr runProperties = run.getRPr();

                if (runProperties != null && !text.trim().isEmpty()) {
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
        String fontName = null;
        Double fontSize = null;
        Boolean isBold = false;
        Boolean isItalic = false;

        if (runProperties.getRFonts() != null) {
            fontName = runProperties.getRFonts().getAscii();
        }
        if (runProperties.getSz() != null) {
            fontSize = runProperties.getSz().getVal().doubleValue() / 2;
        }
        if (runProperties.getB() != null) {
            isBold = true;
        }
        if (runProperties.getI() != null) {
            isItalic = true;
        }

        return typographyValidator.validate(text, fontName, fontSize, isBold, isItalic, rule);
    }
}
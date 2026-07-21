package com.example.demo.modules.engine.validator;

import com.example.demo.modules.profile.entity.TypographyRule;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
@Component

public class TypographyValidator {
public List<String> validate (String text, String fontName, Double fontSize,
                              Boolean isBold, Boolean isItalic, TypographyRule rule) {
    List<String> errors = new ArrayList<>();
    if (text == null || text.trim().isEmpty()){
        return errors;

}
    // cat ngan log
    String shortText = text.length() > 20 ? text.substring(0, 20) + "..." : text;
    //  Kiểm tra Font chữ
    if (rule.getFontFamily() != null && fontName != null) {
        if (!rule.getFontFamily().equalsIgnoreCase(fontName)) {
            errors.add(String.format("Sai Font chữ ở đoạn '%s': Đang dùng '%s', quy định là '%s'",
                    shortText, fontName, rule.getFontFamily()));
        }
    }

    //  Kiểm tra Cỡ chữ
    if (rule.getFontSize() != null && fontSize != null) {
        // Dùng Double.compare để so sánh số thập phân cho an toàn
        if (Double.compare(rule.getFontSize(), fontSize) != 0) {
            errors.add(String.format("Sai Cỡ chữ ở đoạn '%s': Đang dùng '%s pt', quy định là '%s pt'",
                    shortText, fontSize, rule.getFontSize()));
        }
    }

    //  Kiểm tra In đậm (Bold)
    if (rule.getIsBold() != null) {
        boolean textIsBold = (isBold != null && isBold);
        if (rule.getIsBold() != textIsBold) {
            String expected = rule.getIsBold() ? "Phải in đậm" : "Không được in đậm";
            errors.add(String.format("Sai định dạng In đậm ở đoạn '%s': %s", shortText, expected));
        }
    }

    //  Kiểm tra In nghiêng (Italic)
    if (rule.getIsItalic() != null) {
        boolean textIsItalic = (isItalic != null && isItalic);
        if (rule.getIsItalic() != textIsItalic) {
            String expected = rule.getIsItalic() ? "Phải in nghiêng" : "Không được in nghiêng";
            errors.add(String.format("Sai định dạng In nghiêng ở đoạn '%s': %s", shortText, expected));
        }
    }


    return errors;
                              }
}

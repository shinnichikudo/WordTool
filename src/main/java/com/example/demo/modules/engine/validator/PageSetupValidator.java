package com.example.demo.modules.engine.validator;

import com.example.demo.modules.profile.entity.PageSetup;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PageSetupValidator {

    public List<String> validate(Double actualWidth, Double actualHeight,
                                 Double actualTop, Double actualBottom,
                                 Double actualLeft, Double actualRight, PageSetup rule) {

        List<String> errors = new ArrayList<>();
        double tolerance = 0.05; // Biên độ sai số cho phép khi làm tròn

        //  KIỂM TRA KHỔ GIẤY (Đặc biệt quan trọng)
        // Nếu database có lưu chuẩn Width/Height (ví dụ quy định A4 là 21 x 29.7)
        if (rule.getPageWidth() != null && actualWidth != null) {
            if (Math.abs(rule.getPageWidth() - actualWidth) > tolerance) {
                errors.add(String.format("Sai Chiều rộng khổ giấy: Đang là %.2f cm, quy định là %.2f cm",
                        actualWidth, rule.getPageWidth()));
            }
        }

        if (rule.getPageHeight() != null && actualHeight != null) {
            if (Math.abs(rule.getPageHeight() - actualHeight) > tolerance) {
                errors.add(String.format("Sai Chiều cao khổ giấy: Đang là %.2f cm, quy định là %.2f cm",
                        actualHeight, rule.getPageHeight()));
            }
        }

        // 2. KIỂM TRA CĂN LỀ
        if (rule.getMarginTop() != null && actualTop != null) {
            if (Math.abs(rule.getMarginTop() - actualTop) > tolerance) {
                errors.add(String.format("Sai lề TRÊN của trang: Đang là %.2f cm, quy định là %.2f cm",
                        actualTop, rule.getMarginTop()));
            }
        }

        if (rule.getMarginBottom() != null && actualBottom != null) {
            if (Math.abs(rule.getMarginBottom() - actualBottom) > tolerance) {
                errors.add(String.format("Sai lề DƯỚI của trang: Đang là %.2f cm, quy định là %.2f cm",
                        actualBottom, rule.getMarginBottom()));
            }
        }

        if (rule.getMarginLeft() != null && actualLeft != null) {
            if (Math.abs(rule.getMarginLeft() - actualLeft) > tolerance) {
                errors.add(String.format("Sai lề TRÁI của trang: Đang là %.2f cm, quy định là %.2f cm",
                        actualLeft, rule.getMarginLeft()));
            }
        }

        if (rule.getMarginRight() != null && actualRight != null) {
            if (Math.abs(rule.getMarginRight() - actualRight) > tolerance) {
                errors.add(String.format("Sai lề PHẢI của trang: Đang là %.2f cm, quy định là %.2f cm",
                        actualRight, rule.getMarginRight()));
            }
        }

        return errors;
    }
}
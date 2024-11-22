package com.flashcard.swing;

import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Font;

public class PanelTesting extends javax.swing.JPanel {
    public PanelTesting(String title, Color color) {
        // Đặt màu nền cho panel
        setBackground(color); 

        // Sử dụng BorderLayout để căn giữa chữ
        setLayout(new java.awt.BorderLayout());

        // Thêm label vào panel
        javax.swing.JLabel label = new javax.swing.JLabel(title, javax.swing.SwingConstants.CENTER);
        
        // Đặt màu chữ tương phản với màu nền
        if (isColorDark(color)) {
            label.setForeground(Color.WHITE); // Màu chữ sáng cho nền tối
        } else {
            label.setForeground(Color.BLACK); // Màu chữ tối cho nền sáng
        }

        // Đặt font cho label (tùy chỉnh thêm nếu cần)
        label.setFont(new java.awt.Font("Arial", Font.BOLD, 16)); 

        // Thêm label vào trung tâm
        add(label, java.awt.BorderLayout.CENTER);
    }

    /**
     * Kiểm tra xem màu có phải là tối hay không.
     * @param color Màu cần kiểm tra
     * @return true nếu màu tối, false nếu sáng
     */
    private boolean isColorDark(Color color) {
        // Công thức tính độ sáng dựa trên RGB
        double brightness = (0.299 * color.getRed() + 0.587 * color.getGreen() + 0.114 * color.getBlue()) / 255;
        return brightness < 0.5; // Nếu độ sáng < 0.5, coi là màu tối
    }
}

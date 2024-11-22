package com.flashcard.swing;

import javax.swing.*;
import java.awt.*;
import javax.swing.border.Border;

public class MyTextArea extends JTextArea {

    public MyTextArea() {
        // Thiết lập các thuộc tính cơ bản của TextArea
        setFont(new Font("Arial", Font.PLAIN, 14)); // Font chữ
        setForeground(new Color(67, 179, 193)); // Màu chữ
        setBackground(new Color(230, 245, 241)); // Màu nền
        setLineWrap(true); // Bật tính năng tự động xuống dòng
        setWrapStyleWord(true); // Chỉ xuống dòng khi có từ đầy đủ
        setCaretColor(new Color(67, 179, 193)); // Màu con trỏ
        setSelectionColor(new Color(67, 179, 193)); // Màu khi chọn văn bản
        setSelectionColor(new Color(67, 179, 193)); // Màu khi chọn văn bản
        
        // Thiết lập viền bo tròn cho TextArea
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10) ); // Khoảng cách giữa viền và văn bản
                
                
       
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Tùy chỉnh viền bo tròn
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(new Color(75, 175, 152));
        g2.setStroke(new BasicStroke(2));  // Độ dày viền
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15); // Viền bo tròn
    }
}

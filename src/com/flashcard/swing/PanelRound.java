package com.flashcard.swing;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JPanel;

// Lớp JPanel tùy chỉnh để tạo panel với các góc bo tròn
public class PanelRound extends JPanel {

    // Hàm khởi tạo để đặt panel thành trong suốt
    public PanelRound() {
        setOpaque(false);  // Đặt panel không mờ để có thể tùy chỉnh nền
    }

    // Phương thức vẽ nền cho panel
    @Override
    protected void paintComponent(Graphics grphcs) {
        Graphics2D g2 = (Graphics2D) grphcs;  // Chuyển Graphics thành Graphics2D để vẽ nâng cao
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);  // Kích hoạt tính năng chống răng cưa để làm mịn các cạnh
        g2.setColor(new Color(255, 255, 255));  // Đặt màu nền là màu trắng
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);  // Vẽ hình chữ nhật bo tròn với bán kính góc 20
        super.paintComponent(grphcs);  // Gọi phương thức paintComponent gốc để vẽ các thành phần con
    }
}

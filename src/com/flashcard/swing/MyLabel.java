package com.flashcard.swing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MyLabel extends JLabel {
    private Color backgroundColor = new Color(230, 245, 241); // Màu nền
    private Color hoverColor = new Color(200, 235, 221);      // Màu khi hover
    private Color textColor = new Color(67, 179, 193);        // Màu chữ
    private Color borderColor = new Color(67, 179, 193);      // Màu viền
    private boolean isHovered = false;                        // Trạng thái hover

    public MyLabel(String text) {
        super(text);
        setOpaque(false); // Cho phép vẽ nền tùy chỉnh
        setForeground(textColor); // Thiết lập màu chữ
        setFont(new Font("Arial", Font.BOLD, 14)); // Font chữ
        setHorizontalAlignment(SwingConstants.CENTER); // Căn giữa văn bản

        // Thêm sự kiện hover
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                isHovered = true;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                isHovered = false;
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Đặt màu nền tùy thuộc vào trạng thái hover
        g2.setColor(isHovered ? hoverColor : backgroundColor);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20); // Bo tròn các góc

        // Vẽ viền bo tròn
        g2.setColor(borderColor);
        g2.setStroke(new BasicStroke(2)); // Độ dày viền
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);

        // Vẽ nội dung của JLabel
        super.paintComponent(g);
    }

    // Thiết lập màu nền
    public void setBackgroundColor(Color color) {
        this.backgroundColor = color;
        repaint();
    }

    // Thiết lập màu hover
    public void setHoverColor(Color color) {
        this.hoverColor = color;
        repaint();
    }

    // Thiết lập màu chữ
    public void setTextColor(Color color) {
        setForeground(color);
        repaint();
    }

    // Thiết lập màu viền
    public void setBorderColor(Color color) {
        this.borderColor = color;
        repaint();
    }
}

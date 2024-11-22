package com.flashcard.swing;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JPasswordField;

public class MyPasswordField extends JPasswordField {

    // Các biến để lưu trữ icon và gợi ý
    private Icon prefixIcon; // Icon hiển thị phía trước trường mật khẩu
    private Icon suffixIcon; // Icon hiển thị phía sau trường mật khẩu
    private String hint = ""; // Chuỗi gợi ý (placeholder) hiển thị khi trường mật khẩu trống
    private boolean showPassword = false;

    // Getter và setter cho chuỗi gợi ý
    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    // Getter và setter cho prefix icon
    public Icon getPrefixIcon() {
        return prefixIcon;
    }

    public void setPrefixIcon(Icon prefixIcon) {
        this.prefixIcon = prefixIcon;
        initBorder(); // Cập nhật lại border khi thêm icon
    }

    // Getter và setter cho suffix icon
    public Icon getSuffixIcon() {
        return suffixIcon;
    }

    public void setSuffixIcon(Icon suffixIcon) {
        this.suffixIcon = suffixIcon;
        initBorder(); // Cập nhật lại border khi thêm icon
       
    }

    // Constructor
    public MyPasswordField() {
        setEchoChar('*'); // Ẩn mật khẩu theo mặc định
        setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Đặt padding bên trong
        setBackground(new Color(0, 0, 0, 0)); // Đặt nền trong suốt
        setForeground(Color.decode("#7A8C8D")); // Đặt màu chữ
        setFont(new java.awt.Font("sansserif", 0, 13)); // Đặt font chữ
        setSelectionColor(new Color(75, 175, 152)); // Đặt màu nền khi chọn văn bản
       
        // Thêm MouseListener cho biểu tượng eye
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (isClickOnSuffixIcon(e)) {
                    togglePasswordVisibility();
                   
                }
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                if (isClickOnSuffixIcon(e)) {
                    setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR)); // Biểu tượng bàn tay
                } else {
                	setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR)); // Trở về biểu tượng mặc định
                }
            }
        });
    }

    // Kiểm tra nếu nhấp vào khu vực của suffixIcon
    private boolean isClickOnSuffixIcon(MouseEvent e) {
        if (suffixIcon != null) {
            int x = getWidth() - suffixIcon.getIconWidth() - 10;
            return e.getX() >= x && e.getX() <= x + suffixIcon.getIconWidth();
        }
        return false;
    }

    // Chuyển đổi hiển thị mật khẩu (hiện/ẩn)
    private void togglePasswordVisibility() {
        showPassword = !showPassword;
        setEchoChar(showPassword ? (char) 0 : '*'); // Hiện hoặc ẩn mật khẩu
    }

    // Vẽ icon vào trường mật khẩu
    private void paintIcon(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        if (prefixIcon != null) { // Vẽ icon phía trước
            Image prefix = ((ImageIcon) prefixIcon).getImage();
            int y = (getHeight() - prefixIcon.getIconHeight()) / 2; // Đặt icon ở giữa chiều cao của trường
            g2.drawImage(prefix, 10, y, this); // Vẽ icon ở bên trái
        }
        if (suffixIcon != null) {
            int x = getWidth() - suffixIcon.getIconWidth() - 10;
            int y = (getHeight() - suffixIcon.getIconHeight()) / 2;
            suffixIcon.paintIcon(this, g2, x, y);
           
            
            
        }
    }

    // Hiển thị gợi ý khi trường mật khẩu trống
    @Override
    public void paint(Graphics g) {
        super.paint(g); // Gọi paint gốc
        if (getPassword().length == 0) { // Kiểm tra nếu trường mật khẩu trống
            int h = getHeight();
            ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON); // Kích hoạt khử răng cưa cho văn bản
            Insets ins = getInsets(); // Lấy khoảng cách từ biên
            FontMetrics fm = g.getFontMetrics(); // Lấy kích thước font chữ
            g.setColor(new Color(200, 200, 200)); // Đặt màu gợi ý
            g.drawString(hint, ins.left, h / 2 + fm.getAscent() / 2 - 2); // Vẽ gợi ý ở giữa chiều cao của trường mật khẩu
        }
    }

    // Thiết lập lại khoảng cách biên để tạo không gian cho icon
    private void initBorder() {
        int left = 15; // Khoảng cách biên trái
        int right = 15; // Khoảng cách biên phải
        if (prefixIcon != null) {
            left = prefixIcon.getIconWidth() + 15; // Tăng khoảng cách bên trái khi có prefix icon
        }
        if (suffixIcon != null) {
            right = suffixIcon.getIconWidth() + 15; // Tăng khoảng cách bên phải khi có suffix icon
        }
        setBorder(javax.swing.BorderFactory.createEmptyBorder(10, left, 10, right)); // Cập nhật khoảng cách biên
    }

    // Vẽ nền và icon cho thành phần
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); // Kích hoạt khử răng cưa
        g2.setColor(new Color(230, 245, 241)); // Đặt màu nền của trường mật khẩu
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 5, 5); // Vẽ nền bo góc
        paintIcon(g); // Vẽ icon nếu có
        super.paintComponent(g); // Vẽ các thành phần con
    }
}

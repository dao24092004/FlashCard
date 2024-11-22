package com.flashcard.swing;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

// Lớp JPanel tùy chỉnh với các đường viền bo tròn
public class PanelBorder extends javax.swing.JPanel {

    // Hàm khởi tạo để khởi tạo panel
    public PanelBorder() {
        initComponents();  // Khởi tạo các thành phần và bố cục của panel
        setOpaque(false);  // Đặt panel trong suốt để có thể tùy chỉnh nền
    }

    @SuppressWarnings("unchecked")
    // Phương thức khởi tạo các thành phần (tự động sinh ra bởi trình tạo giao diện)
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);  // Đặt bố cục cho panel
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 361, Short.MAX_VALUE)  // Thiết lập kích thước ngang của panel
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 197, Short.MAX_VALUE)  // Thiết lập kích thước dọc của panel
        );
    }

    // Phương thức này ghi đè phương thức paintComponent để vẽ nền cho panel
    @Override
    protected void paintComponent(Graphics grphcs) {
        Graphics2D g2 = (Graphics2D) grphcs;  // Chuyển Graphics thành Graphics2D để vẽ nâng cao
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);  // Kích hoạt tính năng chống răng cưa để làm mịn các cạnh
        g2.setColor(getBackground());  // Đặt màu vẽ bằng màu nền của panel
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);  // Vẽ hình chữ nhật bo tròn với bán kính góc 15
        super.paintComponent(grphcs);  // Gọi phương thức paintComponent gốc để vẽ các thành phần con
    }

    // Khai báo biến tự động sinh ra bởi trình tạo giao diện (không nên sửa đổi)
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}

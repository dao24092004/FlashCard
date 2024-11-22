package com.flashcard.component;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.io.File;
import javax.swing.ImageIcon;

public class PanelLoading extends javax.swing.JPanel {

    // Constructor của PanelLoading
    public PanelLoading() {
        initComponents(); // Khởi tạo các thành phần giao diện
        setOpaque(false); // Đặt panel thành trong suốt
        setFocusCycleRoot(true); // Thiết lập panel tham gia vào chuỗi điều hướng tiêu điểm
        setVisible(false); // Panel bị ẩn ban đầu, sẽ hiển thị khi cần
        setFocusable(true); // Cho phép panel nhận sự kiện bàn phím
        addMouseListener(new MouseAdapter() {
            // Thêm MouseAdapter rỗng để ngăn tương tác chuột khi panel hiển thị
        });
    }

    // Khởi tạo các thành phần giao diện
    @SuppressWarnings("unchecked")
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel(); // Tạo JLabel để hiển thị icon loading

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER); // Đặt icon ở giữa panel

        // Đường dẫn đến file icon loading
        String loadingPath = "src/com/flashcard/icon/loading5.gif";
        File loadingFile = new File(loadingPath);

        // Kiểm tra xem file icon có tồn tại hay không
        if (loadingFile.exists()) {
            jLabel1.setIcon(new ImageIcon(loadingFile.getAbsolutePath())); // Gán icon nếu tìm thấy file
        } else {
            System.out.println("Icon not found at " + loadingPath); // Thông báo lỗi nếu không tìm thấy file icon
        }

        // Thiết lập layout cho panel
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        
        // Đặt jLabel1 chiếm toàn bộ chiều ngang của panel
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 512, Short.MAX_VALUE)
        );
        
        // Đặt jLabel1 chiếm toàn bộ chiều dọc của panel
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 355, Short.MAX_VALUE)
        );
    }

    // Tùy chỉnh cách vẽ panel
    @Override
    protected void paintComponent(Graphics grphcs) {
        Graphics2D g2 = (Graphics2D) grphcs; // Ép kiểu Graphics thành Graphics2D để có thêm chức năng vẽ
        g2.setColor(new Color(255, 255, 255)); // Đặt màu trắng cho lớp phủ
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f)); // Thiết lập độ trong suốt 50%
        g2.fillRect(0, 0, getWidth(), getHeight()); // Vẽ hình chữ nhật bán trong suốt phủ lên toàn panel
        g2.setComposite(AlphaComposite.SrcOver); // Khôi phục chế độ hòa trộn ban đầu
        super.paintComponent(grphcs); // Gọi phương thức paintComponent gốc để vẽ các thành phần con
    }

    // Khai báo biến thành phần giao diện
    private javax.swing.JLabel jLabel1; // JLabel để hiển thị icon loading
}

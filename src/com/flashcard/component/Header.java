package com.flashcard.component;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;

import com.flashcard.event.SearchListener;
import com.flashcard.form.FormHome;

public class Header extends javax.swing.JPanel {
    private List<SearchListener> searchListeners = new ArrayList<>();
    private FormHome home;

    // Đăng ký SearchListener
    public void addSearchListener(SearchListener listener) {
        searchListeners.add(listener);
    }

    // Hàm khởi tạo lớp Header
    public Header() {
        initComponents(); // Khởi tạo các thành phần giao diện
        addSearchAction(); // Thêm sự kiện cho nút tìm kiếm
    }

    // Thêm sự kiện tìm kiếm vào ô tìm kiếm và icon
    private void addSearchAction() {
        searchText1.addSearchActionListener(e -> triggerSearch());
        jLabel2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("Icon tìm kiếm được bấm.");
                triggerSearch();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                jLabel2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
            }
        });
    }

    public void setCurrentForm(FormHome home) {
        this.home = home;
        System.out.println("Form_Home đã được cập nhật.");
    }

    private void triggerSearch() {
        String keyword = searchText1.getText().trim();
        System.out.println("Từ khóa tìm kiếm: " + keyword);
        
        // Truyền từ khóa tìm kiếm tới Form_Home (nếu có)
        if (home != null) {
            home.onSearch(keyword);  // Gọi phương thức trong Form_Home để cập nhật kết quả
        }
        
        // Gửi sự kiện cho các listener đã đăng ký (nếu có)
        for (SearchListener listener : searchListeners) {
            listener.onSearch(keyword);
        }
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        searchText1 = new com.flashcard.swing.SearchText(); // Thành phần tìm kiếm tùy chỉnh
        jLabel2 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255)); // Đặt màu nền trắng cho panel

        // Kiểm tra và đặt biểu tượng cho jLabel2 (icon tìm kiếm)
        String searchIconPath = "src/com/flashcard/icon/search.png";
        File searchFile = new File(searchIconPath);
        if (searchFile.exists()) {
            jLabel2.setIcon(new ImageIcon(searchFile.getAbsolutePath())); // Đặt biểu tượng nếu tệp tồn tại
        } else {
            System.out.println("Search icon not found at " + searchIconPath); // Thông báo nếu biểu tượng không tồn tại
        }

        jLabel2.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5)); // Đặt khoảng cách viền cho jLabel2

        // Thiết lập bố cục cho các thành phần trên Header panel
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup().addContainerGap()
                        .addComponent(searchText1, javax.swing.GroupLayout.DEFAULT_SIZE, 558, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel2)
                        .addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(searchText1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
                        Short.MAX_VALUE)
                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 45, Short.MAX_VALUE));
    }

    @Override
    protected void paintComponent(Graphics grphcs) {
        Graphics2D g2 = (Graphics2D) grphcs;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); // Khử răng cưa
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15); // Vẽ góc bo tròn
        g2.fillRect(0, 0, 25, getHeight()); // Hình chữ nhật bên trái
        g2.fillRect(getWidth() - 25, getHeight() - 25, getWidth(), getHeight()); // Hình chữ nhật bên phải dưới
        super.paintComponent(grphcs);
    }

    // Khai báo các biến
    private javax.swing.JLabel jLabel2; // Nhãn cho biểu tượng tìm kiếm
    private com.flashcard.swing.SearchText searchText1; // Ô tìm kiếm
}

package com.flashcard.component;

import com.flashcard.event.EventMenuSelected;
import com.flashcard.model.ModelUser;
import com.flashcard.model.Model_Menu;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class Menu extends javax.swing.JPanel {

    private EventMenuSelected event; // Event khi chọn menu
    private ModelUser user; // Đối tượng lưu thông tin người dùng
    
    // Thêm sự kiện chọn menu
    public void addEventMenuSelected(EventMenuSelected event) {
        this.event = event;
        listMenu1.addEventMenuSelected(event); // Gán sự kiện cho danh sách menu
    }

    // Constructor khởi tạo với thông tin người dùng
    public Menu(ModelUser user) {
        this.user = user;
        initComponents();
        setOpaque(false); // Làm nền trong suốt
        listMenu1.setOpaque(false); // Làm danh sách menu trong suốt
        init(); // Khởi tạo các menu
    }

    // Hàm khởi tạo các item menu
    private void init() {
        listMenu1.addItem(new Model_Menu("1", "Trang chủ ", Model_Menu.MenuType.MENU)); // Menu "Trang chủ"
        listMenu1.addItem(new Model_Menu("2", "Quản lý thẻ", Model_Menu.MenuType.MENU)); // Menu "Quản lý thẻ"
        listMenu1.addItem(new Model_Menu("3", "Quản lý chủ đề", Model_Menu.MenuType.MENU)); // Menu "Quản lý từ vựng"
        listMenu1.addItem(new Model_Menu("4", "Dịch nghĩa", Model_Menu.MenuType.MENU)); 
        
        // Thêm tiêu đề và menu trống
        listMenu1.addItem(new Model_Menu("", "", Model_Menu.MenuType.TITLE));
        listMenu1.addItem(new Model_Menu("", " test2", Model_Menu.MenuType.EMPTY));
        listMenu1.addItem(new Model_Menu("", " test3", Model_Menu.MenuType.EMPTY));

        // Thêm mục "Dữ liệu cá nhân"
        listMenu1.addItem(new Model_Menu("", "Dữ liệu cá nhân", Model_Menu.MenuType.TITLE));
        listMenu1.addItem(new Model_Menu("", " ", Model_Menu.MenuType.EMPTY));

        // Nếu người dùng đã đăng nhập, hiển thị tên người dùng
        if(user != null) {
            listMenu1.addItem(new Model_Menu("8", user.getUserName(), Model_Menu.MenuType.MENU));
        }
        else {
            listMenu1.addItem(new Model_Menu("8", "Khách ", Model_Menu.MenuType.MENU)); // Nếu chưa đăng nhập, hiển thị "Khách"
        }

        // Thêm mục "Đăng xuất"
        listMenu1.addItem(new Model_Menu("10", "Đăng xuất", Model_Menu.MenuType.MENU));
        listMenu1.addItem(new Model_Menu("", "", Model_Menu.MenuType.EMPTY)); // Thêm mục trống
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelMoving = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        listMenu1 = new com.flashcard.swing.ListMenu<>();

        panelMoving.setOpaque(false); // Làm panel di chuyển trong suốt

        // Gán font và màu cho nhãn logo
        jLabel1.setFont(new java.awt.Font("sansserif", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        // Kiểm tra và gán logo nếu có
        String logoPath = "src/com/flashcard/icon/logo.png";
        File logoFile = new File(logoPath);
        if (logoFile.exists()) {
            jLabel1.setIcon(new ImageIcon(logoFile.getAbsolutePath()));
        } else {
            System.out.println("Icon not found at " + logoPath); // Nếu không tìm thấy logo
        }
        jLabel1.setText("English");

        // Thiết lập layout cho panelMoving
        javax.swing.GroupLayout panelMovingLayout = new javax.swing.GroupLayout(panelMoving);
        panelMoving.setLayout(panelMovingLayout);
        panelMovingLayout.setHorizontalGroup(
            panelMovingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelMovingLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 203, Short.MAX_VALUE)
                .addContainerGap())
        );
        panelMovingLayout.setVerticalGroup(
            panelMovingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelMovingLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel1)
                .addContainerGap())
        );

        // Thiết lập layout cho menu
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelMoving, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(listMenu1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(panelMoving, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15)
                .addComponent(listMenu1, javax.swing.GroupLayout.DEFAULT_SIZE, 414, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    // Vẽ gradient background cho menu
    @Override
    protected void paintChildren(Graphics grphcs) {
        Graphics2D g2 = (Graphics2D) grphcs;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); // Chế độ chống răng cưa
        GradientPaint g = new GradientPaint(0, 0, Color.decode("#1CB5E0"), 0, getHeight(), Color.decode("#000046")); // Gradient màu nền
        g2.setPaint(g);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15); // Vẽ hình chữ nhật bo góc
        g2.fillRect(getWidth() - 20, 0, getWidth(), getHeight()); // Vẽ phần bên phải của menu
        super.paintChildren(grphcs); // Gọi phương thức vẽ mặc định
    }

    // Biến lưu tọa độ để di chuyển cửa sổ
    private int x;
    private int y;

    // Khởi tạo di chuyển cửa sổ
    public void initMoving(JFrame fram) {
        panelMoving.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent me) {
                x = me.getX(); // Ghi nhận tọa độ khi nhấn chuột
                y = me.getY(); // Ghi nhận tọa độ khi nhấn chuột
            }

        });
        panelMoving.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent me) {
                fram.setLocation(me.getXOnScreen() - x, me.getYOnScreen() - y); // Di chuyển cửa sổ khi kéo chuột
            }
        });
    }

    // Biến khai báo các thành phần giao diện
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private com.flashcard.swing.ListMenu<String> listMenu1;
    private javax.swing.JPanel panelMoving;
    // End of variables declaration//GEN-END:variables
}

package com.flashcard.controller;

import com.flashcard.event.EventMenuSelected;
import com.flashcard.event.SearchListener;
import com.flashcard.form.FormQuanLyThe;
import com.flashcard.form.Form_2;
import com.flashcard.form.FormDichTu;
import com.flashcard.form.FormThongTinNguoiDung;
import com.flashcard.form.FormHome;
import com.flashcard.form.FormTopic;
import com.flashcard.model.ModelUser;
import com.flashcard.model.ModelUserProFile;
import com.flashcard.service.ServiceUserProfile;
import com.flashcard.service.UserProFileImpl;
import com.flashcard.swing.WinButton;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.GroupLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;

public class ControllerAdmin extends javax.swing.JFrame {

    private FormHome home;
    private FormQuanLyThe form1;
    private FormTopic form_Topic;
    private FormDichTu form3;
    private FormThongTinNguoiDung form4;
    private static ModelUser user;
    private ConntrollerLoginAndRegister conntrollerLoginAndRegister;
    private ServiceUserProfile profile;
    private com.flashcard.component.Header header2; // Header mới cho ControllerAdmin
    public ControllerAdmin(ModelUser user, ConntrollerLoginAndRegister conntrollerLoginAndRegister) {
        this.user = user;
        this.conntrollerLoginAndRegister = conntrollerLoginAndRegister;
        this.profile = new UserProFileImpl();
        try {
            initComponents();

            // Khởi tạo các form
            home = new FormHome(user);
            form1 = new FormQuanLyThe(user);
            form_Topic = new FormTopic(user);
            form3 = new FormDichTu(user);
            form4 = new FormThongTinNguoiDung(user);
            System.out.println("Form_4 được khởi tạo: " + (form4 != null));
            // Khởi tạo Header và đăng ký SearchListener
            header2 = new com.flashcard.component.Header(); 
            header2.addSearchListener(new SearchListener() {
                @Override
                public void onSearch(String keyword) {
                    handleSearch(keyword); // Xử lý tìm kiếm khi người dùng nhập
                }
            });

            // Menu setup
            menu.initMoving(ControllerAdmin.this);
            menu.addEventMenuSelected(new EventMenuSelected() {
                @Override
                public void selected(int index) {
                    switch (index) {
                        case 0 -> setForm(home);
                        case 1 -> setForm(form1);
                        case 2 -> setForm(form_Topic);
                        case 3 -> setForm(form3);
                        
                        case 9 -> {
                        	  System.out.println("Chọn Form_4"); // Log để kiểm tra
                              setForm(form4); // Hiển thị Form_4
                        }
                        case 10 -> {
                            setVisible(false); // Ẩn ControllerAdmin hiện tại
                            conntrollerLoginAndRegister.setVisible(true); // Hiển thị màn hình đăng nhập
                        }
                        default -> System.out.println("Không có case cho index: " + index);

                    }
                }
            });

            setForm(home);
            setVisible(true); // Hiển thị JFrame

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Lỗi trong quá trình khởi tạo ControllerAdmin: " + e.getMessage());
        }
    }

    // Phương thức để xử lý khi người dùng tìm kiếm
    private void handleSearch(String keyword) {
        System.out.println("Tìm kiếm với từ khóa: " + keyword);
        
        
       
        
    }

    private void setForm(JComponent com) {
        try {
        	 if (com == null) {
                 System.out.println("Form truyền vào bị null!");
                 return;
             }
            mainPanel.removeAll();
            mainPanel.add(com, BorderLayout.CENTER);
            mainPanel.repaint();
            mainPanel.revalidate();
          

            System.out.println("setForm: Đã thêm form vào mainPanel");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Lỗi trong setForm: " + e.getMessage());
        }
    }

    private void initComponents() {
        // Đảm bảo rằng tất cả các component được khởi tạo
        panelBorder1 = new com.flashcard.swing.PanelBorder();
        menu = new com.flashcard.component.Menu(user); // Khởi tạo menu
        mainPanel = new javax.swing.JPanel(); // Khởi tạo mainPanel
        header2 = new com.flashcard.component.Header(); // Khởi tạo header2

        // Kiểm tra nếu các component không phải là null
        if (menu == null || header2 == null || mainPanel == null) {
            System.out.println("Một trong các component bị null.");
            return; // Dừng thực hiện nếu có component null
        }

        // Cài đặt JFrame
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setSize(new Dimension(1200, 800)); // Đặt kích thước cho JFrame
        mainPanel.setOpaque(false);
        mainPanel.setLayout(new BorderLayout());

        // Tạo layout GroupLayout cho panelBorder1
        GroupLayout panelBorder1Layout = new GroupLayout(panelBorder1);
        panelBorder1.setLayout(panelBorder1Layout);

        // Thêm các component vào GroupLayout
        panelBorder1Layout.setHorizontalGroup(
            panelBorder1Layout.createSequentialGroup()
                .addComponent(menu, GroupLayout.PREFERRED_SIZE, 250, GroupLayout.PREFERRED_SIZE)
                .addGroup(panelBorder1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(header2, GroupLayout.DEFAULT_SIZE, 960, Short.MAX_VALUE)
                    .addComponent(mainPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelBorder1Layout.setVerticalGroup(
            panelBorder1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(menu, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panelBorder1Layout.createSequentialGroup()
                    .addComponent(header2, GroupLayout.PREFERRED_SIZE, 60, GroupLayout.PREFERRED_SIZE)
                    .addComponent(mainPanel, GroupLayout.DEFAULT_SIZE, 493, Short.MAX_VALUE))
        );

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(panelBorder1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(panelBorder1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null); // Đặt vị trí JFrame
    }

    // Các biến khai báo
    private com.flashcard.component.Menu menu;
    private javax.swing.JPanel mainPanel;
    private com.flashcard.swing.PanelBorder panelBorder1;

}

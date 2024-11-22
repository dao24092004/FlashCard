package com.flashcard.form;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import com.flashcard.model.ModelUser;
import com.flashcard.model.ModelUserProFile;
import com.flashcard.service.ImageStorageImpl;
import com.flashcard.service.ServiceStorageImage;
import com.flashcard.service.ServiceUserProfile;
import com.flashcard.service.UserProFileImpl;
import com.flashcard.swing.MyComboBox;
import com.flashcard.swing.MyTextArea;
import com.flashcard.swing.MyTextField;
import com.flashcard.swing.ScrollBar;

public class FormThongTinNguoiDung extends JPanel {

    // Components declaration
    private JLabel lblProfilePicture;
    private MyTextField txtFullName, txtDob, txtPhone, txtAddress;
    private MyTextArea txtBio;
    private MyComboBox<String> cmbStatus;
    private Button btnSave, btnCancel;
    private String filePath;
    private ServiceStorageImage serviceStorageImage;
    private ModelUser user;
    private ModelUserProFile proFile;
    private ServiceUserProfile serviceUserProfile;

    public FormThongTinNguoiDung(ModelUser user) {
        this.serviceUserProfile = new UserProFileImpl();
        this.proFile = serviceUserProfile.getAllById(user.getUserID());
        this.user = user;
        initComponents();
        setDataUser();
    }

    private void initComponents() {
        lblProfilePicture = new JLabel();
        JLabel lblFullName = new JLabel("Họ và tên:");
        JLabel lblDob = new JLabel("Ngày sinh:");
        JLabel lblPhone = new JLabel("Số điện thoại:");
        JLabel lblAddress = new JLabel("Địa chỉ:");
        JLabel lblBio = new JLabel("Tiểu sử:");
        JLabel lblStatus = new JLabel("Trạng thái:");

        txtFullName = new MyTextField();
        txtDob = new MyTextField();
        txtPhone = new MyTextField();
        txtAddress = new MyTextField();
        txtBio = new MyTextArea();
        txtBio.setLineWrap(true);
        txtBio.setWrapStyleWord(true);

        cmbStatus = new MyComboBox<>();
        cmbStatus.addItem("Hoạt động");
        cmbStatus.addItem("Khóa tài khoản");

        btnSave = new Button("Lưu");
        btnSave.setBackground(new Color(67, 179, 193));
        btnSave.setForeground(Color.WHITE);

        btnCancel = new Button("Hủy");
        btnCancel.setBackground(new Color(67, 179, 193));
        btnCancel.setForeground(Color.WHITE);

        JScrollPane scrollBio = new JScrollPane(txtBio);

        lblProfilePicture.setHorizontalAlignment(SwingConstants.CENTER);
        lblProfilePicture.setIcon(new ImageIcon("src/com/flashcard/icon/default-profile.png"));
        lblProfilePicture.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
        lblProfilePicture.setPreferredSize(new Dimension(150, 150));

        btnSave.addActionListener(e -> saveUserData());
        btnCancel.addActionListener(e -> resetUserData());

        lblProfilePicture.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                uploadProfilePicture();
            }
        });

        JPanel contentPanel = new JPanel();
        GroupLayout layout = new GroupLayout(contentPanel);
        contentPanel.setLayout(layout);
        contentPanel.setBackground(Color.WHITE);

        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                .addComponent(lblProfilePicture, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createSequentialGroup()
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(lblFullName)
                        .addComponent(lblDob)
                        .addComponent(lblPhone)
                        .addComponent(lblAddress)
                        .addComponent(lblBio)
                        .addComponent(lblStatus))
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(txtFullName)
                        .addComponent(txtDob)
                        .addComponent(txtPhone)
                        .addComponent(txtAddress)
                        .addComponent(scrollBio, GroupLayout.PREFERRED_SIZE, 200, GroupLayout.PREFERRED_SIZE)
                        .addComponent(cmbStatus)))
                .addGroup(layout.createSequentialGroup()
                    .addComponent(btnSave, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCancel, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE))
        );

        layout.setVerticalGroup(
            layout.createSequentialGroup()
                .addComponent(lblProfilePicture, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(lblFullName)
                    .addComponent(txtFullName))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDob)
                    .addComponent(txtDob))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(lblPhone)
                    .addComponent(txtPhone))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(lblAddress)
                    .addComponent(txtAddress))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(lblBio)
                    .addComponent(scrollBio, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(lblStatus)
                    .addComponent(cmbStatus))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSave)
                    .addComponent(btnCancel))
        );

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setVerticalScrollBar(new ScrollBar());
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
    }

    private void saveUserData() {
        // Lưu dữ liệu logic
        serviceStorageImage = new ImageStorageImpl(lblProfilePicture);

        String oldImagePath = proFile.getProfilePicture();
        if (oldImagePath != null && !oldImagePath.isEmpty()) {
            File oldFile = new File(oldImagePath);
            if (oldFile.exists() && oldFile.delete()) {
                System.out.println("Ảnh cũ đã xóa: " + oldImagePath);
            }
        }

        String newFilePath = serviceStorageImage.uploadProfilePicture();
        if (newFilePath != null) {
            System.out.println("Ảnh mới đã lưu tại: " + newFilePath);
        }

        String fullName = txtFullName.getText();
        String phone = txtPhone.getText();
        String address = txtAddress.getText();
        String bio = txtBio.getText();
        String status = (String) cmbStatus.getSelectedItem();

        ModelUserProFile modelUserProFile = new ModelUserProFile(
            user.getUserID(), fullName, phone, address, bio, newFilePath);
        serviceUserProfile.saveProfile(modelUserProFile, user.getUserID());

        JOptionPane.showMessageDialog(this, "Lưu thành công!");
    }

    private void resetUserData() {
        txtFullName.setText("Nguyễn Văn A");
        txtDob.setText("01/01/1990");
        txtPhone.setText("0123456789");
        txtAddress.setText("Hà Nội");
        txtBio.setText("");
        cmbStatus.setSelectedIndex(0);
        lblProfilePicture.setIcon(new ImageIcon("src/com/flashcard/icon/default-profile.png"));
        JOptionPane.showMessageDialog(this, "Dữ liệu đã đặt lại!");
    }

    private void uploadProfilePicture() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn ảnh đại diện");
        fileChooser.setAcceptAllFileFilterUsed(false);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Image files", "jpg", "png", "jpeg");
        fileChooser.addChoosableFileFilter(filter);

        int returnValue = fileChooser.showOpenDialog(this);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            filePath = selectedFile.getAbsolutePath();
            ImageIcon imageIcon = new ImageIcon(filePath);
            lblProfilePicture.setIcon(new ImageIcon(imageIcon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH)));
            System.out.println("Ảnh đã chọn: " + filePath);
        }
    }

    private void setDataUser() {
        txtFullName.setText(proFile.getFullName());
        txtDob.setText("01/01/1990");
        txtPhone.setText(proFile.getPhone());
        txtAddress.setText(proFile.getAddress());
        txtBio.setText(proFile.getBio());
        cmbStatus.setSelectedIndex(0);

        String profilePicturePath = proFile.getProfilePicture();
        if (profilePicturePath != null && !profilePicturePath.isEmpty()) {
            ImageIcon imageIcon = new ImageIcon(profilePicturePath);
            lblProfilePicture.setIcon(new ImageIcon(imageIcon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH)));
        } else {
            lblProfilePicture.setIcon(new ImageIcon("src/com/flashcard/icon/default-profile.png"));
        }
    }
}

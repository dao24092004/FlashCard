package com.flashcard.component;
import com.flashcard.model.ModelLogin;
import com.flashcard.model.ModelUser;
import com.flashcard.swing.Button;
import com.flashcard.swing.MyPasswordField;
import com.flashcard.swing.MyTextField;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import net.miginfocom.swing.MigLayout;

public class PanelLoginAndRegister extends javax.swing.JLayeredPane {

    public ModelLogin getDataLogin() {
        return dataLogin;
    }

    public ModelUser getUser() {
        return user;
    }

    private ModelUser user;
    private ModelLogin dataLogin;

    public PanelLoginAndRegister(ActionListener eventRegister, ActionListener eventLogin) {
        initComponents();
        initRegister(eventRegister);
        initLogin(eventLogin);
        login.setVisible(false);
        register.setVisible(true);
    }

    private void initRegister(ActionListener eventRegister) {
        register.setLayout(new MigLayout("wrap", "push[center]push", "push[]25[]10[]10[]25[]push"));
        JLabel label = new JLabel("Tạo tài khoản");
        label.setFont(new Font("sansserif", 1, 30));
        label.setForeground(new Color(7, 164, 121));
        register.add(label);
        MyTextField txtUser = new MyTextField();
        String userPath = "src/com/flashcard/icon/user.png";
        File userFile = new File(userPath);
        if (userFile.exists()) {
            txtUser.setPrefixIcon(new ImageIcon(userFile.getAbsolutePath()));
        } else {
            System.out.println("Không tìm thấy icon tại " + userPath);
        }

        txtUser.setHint("Tên");
        register.add(txtUser, "w 60%");
        MyTextField txtEmail = new MyTextField();
        String EmailPath = "src/com/flashcard/icon/mail.png";
        File EmailFile = new File(EmailPath);
        if (EmailFile.exists()) {
            txtEmail.setPrefixIcon(new ImageIcon(EmailFile.getAbsolutePath()));
        } else {
            System.out.println("Không tìm thấy icon tại " + EmailPath);
        }
        txtEmail.setHint("Email");
        register.add(txtEmail, "w 60%");
        MyPasswordField txtPass = new MyPasswordField();
        String PassPath = "src/com/flashcard/icon/pass.png";
        File PassFile = new File(PassPath);
        if (PassFile.exists()) {
            txtPass.setPrefixIcon(new ImageIcon(PassFile.getAbsolutePath()));
        } else {
            System.out.println("Không tìm thấy icon tại " + PassPath);
        }
        
        String eyePath = "src/com/flashcard/icon/eye1.png";
        File eyeFile = new File(eyePath);
        if (eyeFile.exists()) {
            txtPass.setSuffixIcon(new ImageIcon(eyeFile.getAbsolutePath()));
        } else {
            System.out.println("Không tìm thấy icon tại " + eyePath);
        }
        txtPass.setHint("Mật khẩu");
        register.add(txtPass, "w 60%");
        Button cmd = new Button();
        cmd.setBackground(new Color(7, 164, 121));
        cmd.setForeground(new Color(250, 250, 250));
        cmd.addActionListener(eventRegister);
        cmd.setText("ĐĂNG KÝ");
        register.add(cmd, "w 40%, h 40");
        cmd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                String userName = txtUser.getText().trim();
                String email = txtEmail.getText().trim();
                String password = String.valueOf(txtPass.getPassword());
                user = new ModelUser(0, userName, email, password);
            }
        });
    }

    private void initLogin(ActionListener eventLogin) {
        login.setLayout(new MigLayout("wrap", "push[center]push", "push[]25[]10[]10[]25[]push"));
        JLabel label = new JLabel("Đăng nhập");
        label.setFont(new Font("sansserif", 1, 30));
        label.setForeground(new Color(7, 164, 121));
        login.add(label);
        MyTextField txtEmail = new MyTextField();
        String EmailPath = "src/com/flashcard/icon/mail.png";
        File EmailFile = new File(EmailPath);
        if (EmailFile.exists()) {
            txtEmail.setPrefixIcon(new ImageIcon(EmailFile.getAbsolutePath()));
        } else {
            System.out.println("Không tìm thấy icon tại " + EmailPath);
        }
        txtEmail.setHint("Email");
        login.add(txtEmail, "w 60%");
        MyPasswordField txtPass = new MyPasswordField();
        String PassPath = "src/com/flashcard/icon/pass.png";
        File PassFile = new File(PassPath);
        if (PassFile.exists()) {
            txtPass.setPrefixIcon(new ImageIcon(PassFile.getAbsolutePath()));
        } else {
            System.out.println("Không tìm thấy icon tại " + PassPath);
        }
        
        String eyePath = "src/com/flashcard/icon/eye1.png";
        File eyeFile = new File(eyePath);
        if (eyeFile.exists()) {
            txtPass.setSuffixIcon(new ImageIcon(eyeFile.getAbsolutePath()));
        } else {
            System.out.println("Không tìm thấy icon tại " + eyePath);
        }
        txtPass.setHint("Mật khẩu");
        login.add(txtPass, "w 60%");
        
        JButton cmdForget = new JButton("Quên mật khẩu ?");
        cmdForget.setForeground(new Color(100, 100, 100));
        cmdForget.setFont(new Font("sansserif", 1, 12));
        cmdForget.setContentAreaFilled(false);
        cmdForget.setCursor(new Cursor(Cursor.HAND_CURSOR));
        login.add(cmdForget);
        Button cmd = new Button();
        cmd.setBackground(new Color(7, 164, 121));
        cmd.setForeground(new Color(250, 250, 250));
        cmd.addActionListener(eventLogin);
        cmd.setText("ĐĂNG NHẬP");
        login.add(cmd, "w 40%, h 40");
        cmd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                String email = txtEmail.getText().trim();
                String password = String.valueOf(txtPass.getPassword());
                dataLogin = new ModelLogin(email, password);
            }
        });
    }

    public void showRegister(boolean show) {
        if (show) {
            register.setVisible(true);
            login.setVisible(false);
        } else {
            register.setVisible(false);
            login.setVisible(true);
        }
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        login = new javax.swing.JPanel();
        register = new javax.swing.JPanel();

        setLayout(new java.awt.CardLayout());

        login.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout loginLayout = new javax.swing.GroupLayout(login);
        login.setLayout(loginLayout);
        loginLayout.setHorizontalGroup(
            loginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 327, Short.MAX_VALUE)
        );
        loginLayout.setVerticalGroup(
            loginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        add(login, "card3");

        register.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout registerLayout = new javax.swing.GroupLayout(register);
        register.setLayout(registerLayout);
        registerLayout.setHorizontalGroup(
            registerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 327, Short.MAX_VALUE)
        );
        registerLayout.setVerticalGroup(
            registerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        add(register, "card2");
    }

    // Các biến khai báo - không sửa đổi
    private javax.swing.JPanel login;
    private javax.swing.JPanel register;
}

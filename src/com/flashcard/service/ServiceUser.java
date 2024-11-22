package com.flashcard.service;

import com.flashcard.connection.DatabaseConnection;
import com.flashcard.model.ModelLogin;
import com.flashcard.model.ModelUser;

import java.math.BigDecimal;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Random;

public class ServiceUser {

    private final Connection con;

    public ServiceUser() {
        con = DatabaseConnection.getInstance().getConnection();
        if (con != null) {
            System.out.println("Database connection established successfully.");
        } else {
            System.out.println("Failed to establish database connection.");
        }
    }
    
    public static String generateSHA3Hash(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA3-256");
            byte[] hashBytes = md.digest(input.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public ModelUser login(ModelLogin login) throws SQLException {
        ModelUser data = null;
        String email = login.getEmail().trim();
        String password = login.getPassword().trim();

        if (email.isEmpty() || password.isEmpty()) {
            System.out.println("Email và mật khẩu không được để trống.");
            return null;
        }

        System.out.println("Đang thực hiện đăng nhập với Email: " + email);

        String sql = "SELECT USER_ID, USERNAME, EMAIL, ROLE, VERIFYCODE FROM tbl_user WHERE EMAIL = ? AND PASSWORD = ?";
        try (PreparedStatement p = con.prepareStatement(sql)) {
            p.setString(1, email);
            p.setString(2, generateSHA3Hash(password));

            try (ResultSet r = p.executeQuery()) {
                if (r.next()) {
                    int userID = r.getInt("USER_ID");
                    String userName = r.getString("USERNAME");
                    String role = r.getString("ROLE");
                    String verifyCode = r.getString("VERIFYCODE");

                    data = new ModelUser(userID, userName, email, password, verifyCode, role);
                    System.out.println("Đã tìm thấy người dùng: " + email + ", Vai trò: " + role);

                    if ("admin".equalsIgnoreCase(role)) {
                        System.out.println("Đăng nhập với vai trò admin.");
                    } else {
                        System.out.println("Đăng nhập với vai trò người dùng.");
                    }
                } else {
                    System.out.println("Không tìm thấy người dùng với thông tin đăng nhập đã cung cấp.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Lỗi SQL trong quá trình đăng nhập: " + e.getMessage());
            e.printStackTrace();
        }

        return data;
    }

    public void insertUser(ModelUser user) throws SQLException {
        String sql = "INSERT INTO tbl_user (USERNAME, EMAIL, PASSWORD, VERIFYCODE) VALUES (?, ?, ?, ?)";
        
        // Chuẩn bị câu lệnh với tùy chọn trả về các khóa tự động
        PreparedStatement p = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
        
        String code = generateVerifyCode(); // Tạo mã xác minh
        p.setString(1, user.getUserName());
        p.setString(2, user.getEmail());
        p.setString(3, generateSHA3Hash(user.getPassword())); // Băm mật khẩu
        p.setString(4, code); // Thiết lập mã xác minh
        
        // Thực thi câu lệnh
        p.executeUpdate();

        // Lấy khóa tự động từ cột user_id
        ResultSet r = p.getGeneratedKeys();
        if (r.next()) {
        	
            user.setVerifyCode(code); // Gán mã xác minh cho đối tượng user
        }
        
        // Đóng ResultSet và PreparedStatement
        r.close();
        p.close();
    }

    private String generateVerifyCode() throws SQLException {
        DecimalFormat df = new DecimalFormat("000000");
        Random ran = new Random();
        String code = df.format(ran.nextInt(1000000));
        while (checkDuplicateCode(code)) {
            code = df.format(ran.nextInt(1000000));
        }
        return code;
    }

    private boolean checkDuplicateCode(String code) throws SQLException {
        boolean duplicate = false;
        PreparedStatement p = con.prepareStatement("SELECT USER_ID FROM tbl_user WHERE VERIFYCODE = ?");
        p.setString(1, code);
        ResultSet r = p.executeQuery();
        if (r.next()) {
            duplicate = true;
        }
        r.close();
        p.close();
        return duplicate;
    }

    public boolean checkDuplicateUser(String user) throws SQLException {
        boolean duplicate = false;
        PreparedStatement p = con.prepareStatement("SELECT USER_ID FROM tbl_user WHERE USERNAME = ? AND STATUS = 'active'");
        p.setString(1, user);
        ResultSet r = p.executeQuery();
        if (r.next()) {
            duplicate = true;
        }
        r.close();
        p.close();
        return duplicate;
    }

    public boolean checkDuplicateEmail(String email) throws SQLException {
        boolean duplicate = false;
        PreparedStatement p = con.prepareStatement("SELECT USER_ID FROM tbl_user WHERE EMAIL = ? AND STATUS = 'active'");
        p.setString(1, email);
        ResultSet r = p.executeQuery();
        if (r.next()) {
            duplicate = true;
        }
        r.close();
        p.close();
        return duplicate;
    }

    public void doneVerify(int userID) throws SQLException {
        PreparedStatement p = con.prepareStatement("UPDATE tbl_user SET VERIFYCODE = '', STATUS = 'active' WHERE USER_ID = ?");
        p.setInt(1, userID);
        p.executeUpdate();
        p.close();
    }

    public boolean verifyCodeWithUser(int userID, String code) throws SQLException {
        boolean verify = false;
        PreparedStatement p = con.prepareStatement("SELECT USER_ID FROM tbl_user WHERE USER_ID = ? AND VERIFYCODE = ?");
        p.setInt(1, userID);
        p.setString(2, code);
        ResultSet r = p.executeQuery();
        if (r.next()) {
            verify = true;
        }
        r.close();
        p.close();
        return verify;
    }
}

package com.flashcard.swing;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.print.attribute.standard.JobName;
import javax.sound.sampled.*;

public class CustomOptionPane {

    public static void showMessage(Component parent, String title, String message, MessageType messageType) {
        // Phát âm thanh
        playSound(getSoundFilePath(messageType));

        // Tạo JDialog chứa nội dung
        JDialog dialog = new JDialog((Frame) null, title, true);
        dialog.setUndecorated(true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        // Thiết kế giao diện tùy chỉnh
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
     // Thay đổi màu nền dựa trên loại thông báo
        switch (messageType) {
            case SUCCESS:
                panel.setBackground(new Color(33, 150, 243)); 
                break;
            case ERROR:
                panel.setBackground(new Color(244, 67, 54)); 
                break;
            case INFO:
                panel.setBackground(new Color(76, 175, 80)); 
                break;
        }
        panel.setBorder(BorderFactory.createLineBorder(new Color(47, 159, 173), 2)); // Viền nhẹ tối hơn

        // Tiêu đề
        JLabel lbTitle = new JLabel(title, SwingConstants.CENTER);
        lbTitle.setFont(new Font("SansSerif", Font.BOLD, 16));
        lbTitle.setForeground(Color.WHITE);
        lbTitle.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));

        // Thông điệp
        JLabel lbMessage = new JLabel(message, SwingConstants.CENTER);
        lbMessage.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lbMessage.setForeground(Color.WHITE);
        lbMessage.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));

        // Icon
        JLabel lbIcon = new JLabel(getIcon(messageType));
        lbIcon.setHorizontalAlignment(SwingConstants.CENTER);

        // Nút đóng
        Button btnClose = new Button("OK");
        btnClose.setForeground(new Color(33, 150, 243)); 
        btnClose.setFocusPainted(false);
        btnClose.setBackground(Color.WHITE);
        btnClose.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnClose.addActionListener(e -> dialog.dispose());

        // Layout các thành phần
        panel.add(lbIcon, BorderLayout.WEST);
        panel.add(lbTitle, BorderLayout.NORTH);
        panel.add(lbMessage, BorderLayout.CENTER);
        panel.add(btnClose, BorderLayout.SOUTH);

        dialog.getContentPane().add(panel);
        dialog.pack();
        dialog.setLocationRelativeTo(parent);
        
     // Thêm tính năng tự động tắt
        Timer timer = new Timer(3000, e -> dialog.dispose()); // 3000ms = 3s
        timer.setRepeats(false); // Chỉ thực hiện một lần
        timer.start();
        
        // Xử lý phím Enter
        dialog.getRootPane().setDefaultButton(btnClose); // Nhấn Enter kích hoạt nút "OK"

        
        dialog.setVisible(true);
    }

    private static Icon getIcon(MessageType messageType) {
        String path;
        switch (messageType) {
            case SUCCESS:
                path = "src/com/flashcard/icon/success1.png";
                break;
            case ERROR:
                path = "src/com/flashcard/icon/error.png";
                break;
            case INFO:
                path = "src/com/flashcard/icon/info.png";
                break;
            default:
                path = "";
        }
        return new ImageIcon(new File(path).getAbsolutePath());
    }

    private static String getSoundFilePath(MessageType messageType) {
        switch (messageType) {
            case SUCCESS:
                return "src/com/flashcard/sounds/success.wav";
            case ERROR:
                return "src/com/flashcard/sounds/error.wav";
            case INFO:
                return "src/com/flashcard/sounds/info.wav";
            default:
                return null;
        }
    }

    private static void playSound(String soundFilePath) {
        if (soundFilePath == null) return;
        try (FileInputStream fileInputStream = new FileInputStream(soundFilePath);
             BufferedInputStream bufferedStream = new BufferedInputStream(fileInputStream)) {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(bufferedStream);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
        } catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
            System.err.println("Error playing sound: " + e.getMessage());
        }
    }

    public enum MessageType {
        SUCCESS, ERROR, INFO
    }
}
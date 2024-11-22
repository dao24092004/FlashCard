package com.flashcard.service;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class ImageStorageImpl implements ServiceStorageImage {
    
    private final String uploadDir = "src/com/flashcard/images/";  // Thư mục lưu trữ ảnh
    private final JLabel displayLabel;  // JLabel để hiển thị ảnh (nếu cần)

    // Constructor để khởi tạo displayLabel
    public ImageStorageImpl(JLabel displayLabel) {
        this.displayLabel = displayLabel;
    }

    @Override
    public String uploadProfilePicture() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn ảnh đại diện");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Ảnh JPEG, PNG, GIF", "jpg", "png", "gif"));
        
        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            
            // Tạo tên file duy nhất để tránh trùng lặp
            String uniqueFileName = UUID.randomUUID().toString() + "." + getFileExtension(selectedFile);
            
            // Tạo thư mục nếu chưa tồn tại
            File dir = new File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            // Tạo đối tượng File để lưu ảnh vào thư mục
            File destinationFile = new File(uploadDir + uniqueFileName);
            
            try {
                // Sao chép ảnh vào thư mục
                Files.copy(selectedFile.toPath(), destinationFile.toPath());

                // Lấy đường dẫn tuyệt đối của ảnh đã lưu
                String filePath = destinationFile.getAbsolutePath();
                
                // Hiển thị ảnh trên JLabel nếu có
                ImageIcon profileImage = new ImageIcon(filePath);
                displayLabel.setIcon(new ImageIcon(profileImage.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH)));

                JOptionPane.showMessageDialog(null, "Ảnh đã được tải lên và lưu thành công!");
                return filePath;  // Trả về đường dẫn file đã lưu
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Không thể tải ảnh. Vui lòng thử lại!");
            }
        }
        return null;  // Nếu không chọn file
    }

    @Override
    public String getFileExtension(File file) {
        String fileName = file.getName();
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex == -1) {
            return ""; // Không có phần mở rộng
        }
        return fileName.substring(dotIndex + 1);
    }
}

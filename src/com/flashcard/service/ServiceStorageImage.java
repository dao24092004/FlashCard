package com.flashcard.service;

import java.io.File;

public interface ServiceStorageImage {
    // Phương thức tải lên ảnh và lưu vào thư mục
    String uploadProfilePicture();

    // Phương thức lấy phần mở rộng của file
    String getFileExtension(File file);
}

package com.flashcard.form;

import javax.swing.*;

import com.flashcard.model.ModelTopic;
import com.flashcard.model.ModelVocabulary;
import com.flashcard.service.ServiceTopic;
import com.flashcard.service.ServiceVocabulary;
import com.flashcard.service.TopicImpl;
import com.flashcard.service.VocabularyImpl;
import com.flashcard.swing.Button;
import com.flashcard.swing.MyComboBox;
import com.flashcard.swing.MyLabel;
import com.flashcard.swing.MyTextArea;
import com.flashcard.swing.MyTextField;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FormEditTopic extends JFrame {
    // Enum xác định chế độ hoạt động của form
    public enum Mode { ADD, EDIT }
    private Mode mode;
    private int topic_id;
    private MyTextField nameField;
    private MyTextArea descriptionArea;
    
    private Button saveButton;
    private Button cancelButton;
    private JPanel overlayPanel; // Lớp phủ bán trong suốt
    
    private ServiceTopic serviceTopic;
    
    java.util.List<ModelTopic> topics;
    private int userId;
    
    public FormEditTopic(Mode mode,int userId) {
    	
        this.mode = mode;
        this.userId = userId;
        
        serviceTopic = new TopicImpl();
        setTitle(mode == Mode.ADD ? "Add Vocabulary" : "Edit Vocabulary");
        setSize(400, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(6, 2, 10, 10));

        initializeFields(userId); // Khởi tạo các thành phần giao diện
        initializeOverlayPanel(); // Khởi tạo lớp phủ
        initializeEventHandlers(); // Khởi tạo sự kiện cho các nút

        
    }

    // Phương thức khởi tạo các trường giao diện
    private void initializeFields(int userId) {
      
        nameField = new MyTextField();
        descriptionArea = new MyTextArea();
        
        

        saveButton = new Button("Lưu");
        saveButton.setBackground(new Color(67, 179, 193));
        saveButton.setForeground(new Color(250, 250, 250));

        cancelButton = new Button("Thoát");
        cancelButton.setBackground(new Color(67, 179, 193));
        cancelButton.setForeground(new Color(250, 250, 250));

        // Thêm các thành phần vào form
        
        add(new MyLabel("Tên chủ đề:"));
        add(nameField);
        add(new MyLabel("Mô tả:"));
        add(new JScrollPane(descriptionArea));    
        add(saveButton);
        add(cancelButton);
    }

   
    

    // Phương thức khởi tạo lớp phủ
    private void initializeOverlayPanel() {
        overlayPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setColor(new Color(255, 255, 255, 128)); // Màu trắng trong suốt
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        overlayPanel.setOpaque(false);
        overlayPanel.setVisible(false);
        overlayPanel.setLayout(new BorderLayout());

        getLayeredPane().add(overlayPanel, JLayeredPane.MODAL_LAYER);
        overlayPanel.setBounds(0, 0, getWidth(), getHeight());

        // Cập nhật kích thước lớp phủ khi cửa sổ thay đổi kích thước
        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent evt) {
                overlayPanel.setBounds(0, 0, getWidth(), getHeight());
            }
        });
    }

    // Phương thức cài đặt sự kiện cho các nút
    private void initializeEventHandlers() {
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showOverlay(true); // Hiển thị lớp phủ
                saveData(); // Lưu dữ liệu
                showOverlay(false); // Ẩn lớp phủ
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Đóng form
            }
        });
    }

    // Hiển thị hoặc ẩn lớp phủ
    private void showOverlay(boolean visible) {
        overlayPanel.setVisible(visible);
    }

    // Phương thức lưu dữ liệu
    private void saveData() {
        try {
            
            String name = nameField.getText();
            String description = descriptionArea.getText();
            

            if (mode == Mode.ADD) {
                // Thêm mới từ vựng
                ModelTopic newTopic = new ModelTopic(userId, name, description);
                serviceTopic.insertTopic(newTopic);
                JOptionPane.showMessageDialog(this, "Vocabulary added successfully.");
            } else if (mode == Mode.EDIT) {
                // Cập nhật từ vựng
                
                ModelTopic updatetTopic = new ModelTopic(topic_id, userId, name, description);
                serviceTopic.updateTopic(updatetTopic);
                JOptionPane.showMessageDialog(this, "Vocabulary updated successfully.");
            }
            dispose(); // Đóng form sau khi lưu
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid input. Please check your data.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Đặt dữ liệu vào các trường khi ở chế độ chỉnh sửa
    public void setTopicData(ModelTopic topic) {
        if (mode == Mode.EDIT) {
            this.topic_id = topic.getTopic_id();
            nameField.setText(topic.getTopicName());
            descriptionArea.setText(topic.getDescription());
          
        }
    }

    // Đặt dữ liệu mặc định khi ở chế độ thêm mới
    public void setInsertTopicData(int userId) {
        if (mode == Mode.ADD) {
        	

            
            nameField.setText("");
            descriptionArea.setText("");
            
           
        }
    }

	public Mode getMode() {
		return mode;
	}

	public void setMode(Mode mode) {
		this.mode = mode;
		 
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}
	
    
}

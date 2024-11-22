package com.flashcard.form;

import javax.swing.*;

import com.flashcard.model.ModelTopic;
import com.flashcard.model.ModelVocabulary;
import com.flashcard.service.ServiceVocabulary;
import com.flashcard.service.VocabularyImpl;
import com.flashcard.swing.Button;
import com.flashcard.swing.CustomOptionPane;
import com.flashcard.swing.MyComboBox;
import com.flashcard.swing.MyLabel;
import com.flashcard.swing.MyTextArea;
import com.flashcard.swing.MyTextField;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FormEdit extends JFrame {
    // Enum xác định chế độ hoạt động của form
    public enum Mode { ADD, EDIT }
    private Mode mode;
    private int topic_id;
    private int vocab_id;
    private MyTextField wordField;
    private MyTextArea meaningArea;
    private MyTextArea exampleArea;
    
    private MyComboBox<String> topicBox;
    private MyComboBox<String> difficultyBox;
    private Button saveButton;
    private Button cancelButton;
    private JPanel overlayPanel; // Lớp phủ bán trong suốt
    
    private ServiceVocabulary vocabService;
    
    java.util.List<ModelTopic> topics;
    private int userId;
    
    public FormEdit(Mode mode,int userId) {
    	vocabService = new VocabularyImpl();
    	topics = vocabService.getTopicByUserId(userId);
        this.mode = mode;
        this.userId = userId;
        

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
      
        wordField = new MyTextField();
        meaningArea = new MyTextArea();
        exampleArea = new MyTextArea();
      
        
        topicBox = new MyComboBox<>();
        for (ModelTopic topicName : topics) {
        	topicBox.addItem(topicName.getTopicName());
        	
		}
        
        difficultyBox = new MyComboBox<>();
        difficultyBox.addItem("easy");
        difficultyBox.addItem("medium");
        difficultyBox.addItem("hard");
        

        saveButton = new Button("Lưu");
        saveButton.setBackground(new Color(67, 179, 193));
        saveButton.setForeground(new Color(250, 250, 250));

        cancelButton = new Button("Thoát");
        cancelButton.setBackground(new Color(67, 179, 193));
        cancelButton.setForeground(new Color(250, 250, 250));

        // Thêm các thành phần vào form
        
        add(new MyLabel("Từ vựng:"));
        add(wordField);
        add(new MyLabel("Nghĩa của từ:"));
        add(new JScrollPane(meaningArea));
        add(new MyLabel("Ví dụ:"));
        add(new JScrollPane(exampleArea));
        
      
        add(new MyLabel("Chủ đề:"));
        add(topicBox);
        add(new MyLabel("Độ khó:"));
        add(difficultyBox);
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
        	
        	if (!validateInputs()) {
                return; // Nếu dữ liệu không hợp lệ, không thực hiện lưu
            }
            
            String word = wordField.getText();
            String meaning = meaningArea.getText();
            String example = exampleArea.getText();
           
            String topicName = (String) topicBox.getSelectedItem();
            for (ModelTopic modelTopic : topics) {
				if(topicName.equalsIgnoreCase(modelTopic.getTopicName())) {
					topic_id  = modelTopic.getTopic_id();
				}
			}
            String difficulty = (String) difficultyBox.getSelectedItem();

            if (mode == Mode.ADD) {
                // Thêm mới từ vựng
                ModelVocabulary newVocab = new ModelVocabulary(userId, word, meaning, example, topic_id, difficulty);
                vocabService.insertVocab(newVocab);
                CustomOptionPane.showMessage(this, "Từ vựng", "Chỉnh sửa từ mới thành công", CustomOptionPane.MessageType.SUCCESS);
            } else if (mode == Mode.EDIT) {
                // Cập nhật từ vựng
                
                ModelVocabulary updatedVocab = new ModelVocabulary(vocab_id, userId, word, meaning, example, topic_id, difficulty, null, null);
                vocabService.update(updatedVocab);
                CustomOptionPane.showMessage(this, "Từ vựng", "Chỉnh sửa từ mới thành công", CustomOptionPane.MessageType.SUCCESS);
            }
            dispose(); // Đóng form sau khi lưu
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid input. Please check your data.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Đặt dữ liệu vào các trường khi ở chế độ chỉnh sửa
    public void setVocabularyData(ModelVocabulary modelVocabulary) {
        if (mode == Mode.EDIT) {
            this.vocab_id = modelVocabulary.getVocabId();
            wordField.setText(modelVocabulary.getWord());
            meaningArea.setText(modelVocabulary.getMeaning());
            exampleArea.setText(modelVocabulary.getExample());
            this.topic_id = modelVocabulary.getTopic_id();
            
            difficultyBox.setSelectedItem(modelVocabulary.getDifficulty());
        }
    }

    // Đặt dữ liệu mặc định khi ở chế độ thêm mới
    public void setInsertVocabularyData(int userId) {
        if (mode == Mode.ADD) {
        	

            
            wordField.setText("");
            meaningArea.setText("");
            exampleArea.setText("");
           
        }
    }
    
    
 // Phương thức kiểm tra tính hợp lệ của các trường
    private boolean validateInputs() {
        String word = wordField.getText().trim();
        String meaning = meaningArea.getText().trim();
        String example = exampleArea.getText().trim();
        
        if (word.isEmpty()) {
            CustomOptionPane.showMessage(this, "Lỗi từ", "Từ vựng không được để trống", CustomOptionPane.MessageType.ERROR);
            return false;
        }
        if (meaning.isEmpty()) {
            CustomOptionPane.showMessage(this, "Lỗi từ", "Nghĩa không được để trống", CustomOptionPane.MessageType.ERROR);
            return false;
        }
        if (example.isEmpty()) {
            CustomOptionPane.showMessage(this, "Lỗi từ", "Ví dụ không được để trống", CustomOptionPane.MessageType.ERROR);
            return false;
        }
        
        // Kiểm tra nếu người dùng chưa chọn chủ đề hoặc độ khó
        if (topicBox.getSelectedItem() == null) {
            CustomOptionPane.showMessage(this, "Lỗi từ", "Chủ đề không được để trống", CustomOptionPane.MessageType.ERROR);
            return false;
        }
        if (difficultyBox.getSelectedItem() == null) {
            CustomOptionPane.showMessage(this, "Lỗi từ", "Độ khó không được để trống", CustomOptionPane.MessageType.ERROR);
            return false;
        }

        return true; // Nếu tất cả đều hợp lệ
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

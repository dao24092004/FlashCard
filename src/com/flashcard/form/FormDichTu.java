package com.flashcard.form;

import javax.swing.*;
import com.flashcard.model.ModelGoogleTranslate;
import com.flashcard.model.ModelUser;
import com.flashcard.model.ModelVocabulary;
import com.flashcard.service.ServiceVocabulary;
import com.flashcard.service.VocabularyImpl;
import com.flashcard.swing.Button;
import com.flashcard.swing.MyComboBox;
import com.flashcard.swing.MyLabel;
import com.flashcard.swing.MyTextArea;
import com.flashcard.swing.MyTextField;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class FormDichTu extends javax.swing.JPanel {

    private MyLabel jLabel1;
    private MyTextField inputField;
    private MyTextArea resultArea; // Hiển thị bản dịch
    private MyTextArea examplesArea; // Hiển thị ví dụ
    private Button translateButton, suggestButton;
    private MyComboBox<String> languageComboBox;
    private JList<String> suggestionsList; // Gợi ý các từ liên quan
    private DefaultListModel<String> listModel; // Model để lưu gợi ý
    private String sourceLang = "en";
    private String targetLang = "vi";
    private Button saveButton;
    private ModelUser user;
    public FormDichTu(ModelUser user) {
    	this.user = user;
        initComponents();
    }

    private void initComponents() {
        jLabel1 = new MyLabel("Google Translate Form");
        inputField = new MyTextField();
        resultArea = new MyTextArea();
        examplesArea = new MyTextArea();
        translateButton = new Button();
        suggestButton = new Button();
        languageComboBox = new MyComboBox<>();
        listModel = new DefaultListModel<>();
        suggestionsList = new JList<>(listModel);

        // Thiết lập giao diện
        translateButton.setText("Dịch");
        translateButton.setBackground(new Color(67, 179, 193));
        translateButton.setForeground(new Color(250, 250, 250));
        translateButton.setPreferredSize(new java.awt.Dimension(120, 40)); // Kích thước 120px x 40px
        translateButton.setFont(new java.awt.Font("sansserif", java.awt.Font.BOLD, 16)); // Font chữ to hơn
        translateButton.setFocusPainted(false); // Bỏ viền khi nhấn
        translateButton.setBorderPainted(false); // Bỏ viền xung quanh
        suggestButton.setText("Gợi ý từ");
        suggestButton.setBackground(new Color(67, 179, 193));
        suggestButton.setForeground(new Color(250, 250, 250));
        suggestButton.setPreferredSize(new java.awt.Dimension(120, 40)); // Kích thước 120px x 40px
        suggestButton.setFont(new java.awt.Font("sansserif", java.awt.Font.BOLD, 16)); // Font chữ to hơn
        suggestButton.setFocusPainted(false); // Bỏ viền khi nhấn
        suggestButton.setBorderPainted(false); // Bỏ viền xung quanh
		
        languageComboBox.addItem("Dịch sang Tiếng Việt");
        languageComboBox.addItem("Dịch sang Tiếng Anh");
        languageComboBox.setSelectedIndex(0);

        JScrollPane scrollSuggestions = new JScrollPane(suggestionsList);
        JScrollPane scrollResult = new JScrollPane(resultArea);
        JScrollPane scrollExamples = new JScrollPane(examplesArea);

        resultArea.setEditable(false);
        examplesArea.setEditable(false);

        // Cập nhật ngôn ngữ khi thay đổi combobox
        languageComboBox.addActionListener(e -> {
            int selectedIndex = languageComboBox.getSelectedIndex();
            if (selectedIndex == 0) {
                sourceLang = "en";
                targetLang = "vi";
            } else {
                sourceLang = "vi";
                targetLang = "en";
            }
        });

        translateButton.addActionListener(evt -> {
            String keyword = inputField.getText().trim();
            System.out.println("Translate button clicked with keyword: " + keyword);

            if (!keyword.isEmpty()) {
                try {
                    String translated = ModelGoogleTranslate.translate(keyword, sourceLang, targetLang);
                    System.out.println("Translation result: " + translated);
                    resultArea.setText(translated);

                    List<String> suggestions = ModelGoogleTranslate.getSuggestions(keyword, sourceLang, targetLang);
                    System.out.println("Suggestions: " + suggestions);

                    listModel.clear();
                    examplesArea.setText("");
                    String selectedExample = suggestions.stream()
                            .filter(example -> example.contains(translated))
                            .findFirst()
                            .orElse("Không tìm thấy ví dụ phù hợp!");
                    examplesArea.setText(selectedExample);
                } catch (Exception e) {
                    resultArea.setText("Có lỗi xảy ra khi dịch!");
                    System.err.println("Error during translation process:");
                    e.printStackTrace();
                }
            } else {
                resultArea.setText("Vui lòng nhập từ cần dịch!");
                System.out.println("Input is empty!");
            }
        });



        // Xử lý nút gợi ý từ
        suggestButton.addActionListener(evt -> {
            String keyword = inputField.getText().trim();
            System.out.println("Suggest button clicked with keyword: " + keyword);

            if (!keyword.isEmpty()) {
                try {
                    List<String> suggestions = ModelGoogleTranslate.getSuggestions(keyword);
                    System.out.println("Suggestions fetched: " + suggestions);

                    listModel.clear();
                    examplesArea.setText("");
                    for (String suggestion : suggestions) {
                        listModel.addElement(suggestion);
                    }
                } catch (Exception e) {
                    resultArea.setText("Có lỗi khi lấy từ đề xuất!");
                    System.err.println("Error during suggestions fetch:");
                    e.printStackTrace();
                }
            } else {
                resultArea.setText("Vui lòng nhập từ để nhận đề xuất!");
                System.out.println("Input is empty for suggestions!");
            }
        });

        

        
        
     // Thêm MouseListener cho danh sách gợi ý
        suggestionsList.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int index = suggestionsList.locationToIndex(evt.getPoint()); // Lấy chỉ mục của mục được nhấn
                if (index != -1) { // Kiểm tra nếu có mục được chọn
                    String selectedWord = listModel.getElementAt(index); // Lấy từ được chọn
                    inputField.setText(selectedWord); // Hiển thị từ vào ô nhập
//                    try {
//                        String translated = ModelGoogleTranslate.translate(selectedWord, sourceLang, targetLang);
//                        resultArea.setText(translated); // Hiển thị bản dịch
//                    } catch (Exception e) {
//                        resultArea.setText("Có lỗi xảy ra khi dịch!");
//                        e.printStackTrace();
//                    }
                }
            }
        });

     
     // Thêm nút Lưu từ vào phương thức initComponents
        saveButton = new Button();
        saveButton.setText("Lưu từ");
        saveButton.setBackground(new Color(67, 179, 193));
        saveButton.setForeground(new Color(250, 250, 250));
        saveButton.setPreferredSize(new java.awt.Dimension(120, 40)); // Kích thước 120px x 40px
        saveButton.setFont(new java.awt.Font("sansserif", java.awt.Font.BOLD, 16)); // Font chữ to hơn
        saveButton.setFocusPainted(false); // Bỏ viền khi nhấn
        saveButton.setBorderPainted(false); // Bỏ viền xung quanh

        // Xử lý hành động khi nhấn nút Lưu từ
        saveButton.addActionListener(evt -> {
            String keyword = inputField.getText().trim();
            if (!keyword.isEmpty()) {
                try {
                    // Giả sử bạn lưu từ vào cơ sở dữ liệu hoặc file, ví dụ:
                    ModelVocabulary modelVocabulary = new ModelVocabulary(user.getUserID(),inputField.getText(),resultArea.getText(),examplesArea.getText(),41,null);
                    ServiceVocabulary serviceVocabulary = new VocabularyImpl();
                    serviceVocabulary.insertVocab(modelVocabulary);
                    resultArea.setText("Từ đã được lưu!");
                } catch (Exception e) {
                    resultArea.setText("Có lỗi khi lưu từ!");
                    System.err.println("Error during saving word:");
                    e.printStackTrace();
                }
            } else {
                resultArea.setText("Vui lòng nhập từ để lưu!");
            }
        });

        // Cập nhật layout để thêm nút Lưu từ vào giao diện
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
                .addComponent(inputField)
                .addComponent(languageComboBox)
                .addComponent(scrollSuggestions)
                .addComponent(scrollResult)
                .addComponent(scrollExamples)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(translateButton, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(suggestButton, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE))
                .addGroup(layout.createSequentialGroup()
                    .addComponent(saveButton, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE) // Thêm nút Lưu từ
                )
        );

        layout.setVerticalGroup(
            layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(inputField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(languageComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(translateButton)
                    .addComponent(suggestButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(saveButton)) // Thêm nút Lưu từ vào bố cục
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scrollSuggestions, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scrollExamples, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scrollResult, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
        );
    }
}

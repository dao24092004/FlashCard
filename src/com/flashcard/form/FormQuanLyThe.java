package com.flashcard.form;

import com.flashcard.component.Message;
import com.flashcard.event.TableActionEvent;
import com.flashcard.model.ModelUser;
import com.flashcard.model.ModelVocabulary;
import com.flashcard.service.ServiceVocabulary;
import com.flashcard.service.VocabularyImpl;
import com.flashcard.swing.Button;
import com.flashcard.swing.ScrollBar;
import com.flashcard.swing.Table;
import com.flashcard.swing.TableActionCellEditor;
import com.flashcard.swing.TableActionCellRender;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class FormQuanLyThe extends JPanel implements Refreshable {
    private JPanel cardPanel; // Panel chứa các thẻ
    private CardLayout cardLayout; // CardLayout để quản lý các thẻ
    private int currentIndex = 0; // Chỉ số thẻ hiện tại
    private Table table; // Bảng tùy chỉnh
    private FormEdit formEdit;
    private ModelUser user;

    public FormQuanLyThe(ModelUser user) {
        this.user = user;
        initComponents();
    }

    private void initComponents() {
        // Khởi tạo bảng tùy chỉnh
        table = createCustomTable();

        // Gọi phương thức tải dữ liệu sau khi bảng được khởi tạo
        loadVocabularyDataToTable();

        // Khởi tạo CardLayout và Panel chứa các thẻ
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);


        // ScrollPane để cuộn bảng
        JScrollPane tableScrollPane = new JScrollPane(table);

        // Sử dụng ScrollBar tùy chỉnh
        tableScrollPane.setVerticalScrollBar(new ScrollBar());
        tableScrollPane.setHorizontalScrollBar(new ScrollBar());

        
        // Panel chứa các nút điều khiển
        JPanel controlPanel = new JPanel();
        JButton btnPrev = new JButton("Trước");
        JButton btnNext = new JButton("Tiếp theo");
        Button btn = new Button();
        controlPanel.add(btnPrev);
        controlPanel.add(btnNext);
        JLabel imageLabel = new JLabel();
        JTextArea meaningArea = new JTextArea();
        JLabel wordLabel = new JLabel();
        
     // Cấu hình cho từng card
        imageLabel.setHorizontalAlignment(JLabel.CENTER);

        meaningArea.setEditable(false);
        meaningArea.setLineWrap(true);
        meaningArea.setWrapStyleWord(true);
        meaningArea.setFont(new Font("Arial", Font.PLAIN, 16));

        wordLabel.setHorizontalAlignment(JLabel.CENTER);
        wordLabel.setFont(new Font("Arial", Font.BOLD, 20));

        cardPanel.add(createCard(new JLabel(), new Color(91, 131, 223), "Card1"));
        cardPanel.add(createCard(new JLabel(), new Color(223, 91, 187), "Card2"));
        cardPanel.add(createCard(new JLabel(), new Color(119, 223, 91), "Card3"));

        // Thêm sự kiện cho nút "Trước"
        btnPrev.addActionListener(e -> slideToCard(currentIndex - 1));

        // Thêm sự kiện cho nút "Tiếp theo"
        btnNext.addActionListener(e -> slideToCard(currentIndex + 1));

        // Panel chứa cardPanel và controlPanel
        JPanel cardContainer = new JPanel(new BorderLayout());
        cardContainer.add(controlPanel, BorderLayout.NORTH); // Nút điều khiển
        cardContainer.add(cardPanel, BorderLayout.CENTER); // Thẻ

        
        // Sử dụng JSplitPane để chia đôi giao diện theo chiều dọc
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, cardContainer, tableScrollPane);
        splitPane.setDividerLocation(300); // Điểm chia ban đầu (300px từ trên)
        splitPane.setResizeWeight(0.5); // Chia đều hai phần

        // Layout chính
        setLayout(new BorderLayout());
        add(splitPane, BorderLayout.CENTER); // Thêm SplitPane vào Form_1
        
        
     // Chỉnh kích thước nút "Thêm"
     		btn.setPreferredSize(new java.awt.Dimension(120, 40)); // Kích thước 120px x 40px
     		btn.setFont(new java.awt.Font("sansserif", java.awt.Font.BOLD, 16)); // Font chữ to hơn
     		btn.setFocusPainted(false); // Bỏ viền khi nhấn
     		btn.setBorderPainted(false); // Bỏ viền xung quanh
     		// btn.setOpaque(true); // Hiển thị màu nền rõ hơn

     	// Thiết lập nút "Thêm"
    		btn.setBackground(new Color(67, 179, 193));
    		btn.setForeground(new Color(250, 250, 250));
    		btn.setText("Thêm");

    		// Chỉnh kích thước nút "Thêm"
    		btn.setPreferredSize(new java.awt.Dimension(120, 40)); // Kích thước 120px x 40px
    		btn.setFont(new java.awt.Font("sansserif", java.awt.Font.BOLD, 16)); // Font chữ to hơn
    		btn.setFocusPainted(false); // Bỏ viền khi nhấn
    		btn.setBorderPainted(false); // Bỏ viền xung quanh
    		// btn.setOpaque(true); // Hiển thị màu nền rõ hơn

    		
     	// Thêm nút "Thêm" vào một panel mới
     		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
     		buttonPanel.setBackground(Color.WHITE); // Đặt màu nền cho panel chứa nút
     		buttonPanel.add(btn);
     		
     		// Đảm bảo thêm đúng vào giao diện
     		cardContainer.add(buttonPanel, BorderLayout.SOUTH); // Thêm vào phía dưới cardContainer


     		btn.addActionListener(e -> {
     			if (formEdit == null) {
     				formEdit = new FormEdit(FormEdit.Mode.ADD, user.getUserID());
     			} else {
     				formEdit.setMode(FormEdit.Mode.ADD); // Đặt lại chế độ
     			}
     			formEdit.setInsertVocabularyData(user.getUserID());
     			formEdit.setVisible(true);

     			// Làm mới bảng khi đóng Form_Edit
     			formEdit.addWindowListener(new java.awt.event.WindowAdapter() {
     				@Override
     				public void windowClosed(java.awt.event.WindowEvent e) {
     					refreshTableData();
     				}
     			});
     		});
     		
     		table.addMouseListener(new MouseAdapter() {
     		    @Override
     		    public void mouseClicked(MouseEvent e) {
     		        int selectedRow = table.getSelectedRow();
     		        if (selectedRow != -1) {
     		            try {
     		                updateCardPanel(selectedRow); // Cập nhật thông tin
     		            } catch (Exception ex) {
     		                ex.printStackTrace();
     		                System.out.println("Lỗi khi cập nhật card panel: " + ex.getMessage());
     		            }
     		        }
     		    }
     		});



        
    }

    private JPanel createCard(Component content, Color bgColor, String name) {
        JPanel card = new JPanel();
        card.setBackground(bgColor);
        card.setLayout(new BorderLayout());
        card.setName(name); // Đặt tên cho card
        card.add(content, BorderLayout.CENTER);
        return card;
    }



    
    private void updateCardPanel(int selectedRow) {
        // Lấy dữ liệu từ bảng
        String word = (String) table.getValueAt(selectedRow, 1); // Cột "Từ vựng"
        String meaning = (String) table.getValueAt(selectedRow, 2); // Cột "Nghĩa của từ"
        String imagePath = "src/com/flashcard/icon/user.png"; // Đường dẫn ảnh

        // Cập nhật Card1 (Hình ảnh)
        Component card1 = findCardComponent("Card1");
        if (card1 instanceof JPanel) {
            JLabel imageLabel = (JLabel) ((JPanel) card1).getComponent(0); // Lấy phần tử đầu tiên
            imageLabel.setIcon(new ImageIcon(new ImageIcon(imagePath).getImage()
                .getScaledInstance(200, 200, Image.SCALE_SMOOTH))); // Resize ảnh
            imageLabel.setHorizontalAlignment(JLabel.CENTER);
            imageLabel.setVerticalAlignment(JLabel.CENTER);
        }

        // Cập nhật Card2 (Nghĩa)
        Component card3 = findCardComponent("Card3");
        if (card3 instanceof JPanel) {
        	JLabel meaninglabel = (JLabel) ((JPanel) card3).getComponent(0); // Lấy JTextArea
        	meaninglabel.setText( meaning);
        	meaninglabel.setFont(new Font("Arial", Font.PLAIN, 24)); // Chữ to hơn
        	meaninglabel.setHorizontalAlignment(JLabel.CENTER);
        	meaninglabel.setVerticalAlignment(JLabel.CENTER);
        }

        // Cập nhật Card3 (Từ vựng)
        Component card2 = findCardComponent("Card2");
        if (card2 instanceof JPanel) {
            JLabel wordLabel = (JLabel) ((JPanel) card2).getComponent(0); // Lấy JLabel
            wordLabel.setText( word);
            wordLabel.setFont(new Font("Arial", Font.BOLD, 28)); // Chữ to và đậm hơn
            wordLabel.setHorizontalAlignment(JLabel.CENTER);
            wordLabel.setVerticalAlignment(JLabel.CENTER);
        }
    }

    private Component findCardComponent(String cardName) {
        for (Component comp : cardPanel.getComponents()) {
            if (comp instanceof JPanel) {
                // Kiểm tra tên của card
                if (comp.getName() != null && comp.getName().equals(cardName)) {
                    return comp;
                }
            }
        }
        return null; // Không tìm thấy
    }



    private Table createCustomTable() {
        // Tạo bảng tùy chỉnh
        Table table = new Table();
        DefaultTableModel model = new DefaultTableModel(
            new String[] {"ID", "Từ vựng", "Nghĩa của từ", "Chủ đề", "Thao tác"}, 0
        );
        table.setModel(model);

        // Renderer cột thao tác
        table.getColumnModel().getColumn(4).setCellRenderer(new TableActionCellRender());

        // Editor cột thao tác
        table.getColumnModel().getColumn(4).setCellEditor(new TableActionCellEditor(new TableActionEvent() {
            @Override
            public void onDelete(int row) {
                int vocabId = (int) table.getValueAt(row, 0);
                if (table.isEditing()) {
                    table.getCellEditor().stopCellEditing();
                }

                DefaultTableModel model = (DefaultTableModel) table.getModel();
                ServiceVocabulary vocabularyService = new VocabularyImpl();
                boolean isDeleted = vocabularyService.deleteById(vocabId);

                if (isDeleted) {
                    model.removeRow(row);
                    showMessage(Message.MessageType.SUCCESS, "Xóa từ vựng", "Xóa thành công từ vựng!");
                } else {
                    showMessage(Message.MessageType.ERROR, "Xóa từ vựng", "Xóa thất bại, vui lòng thử lại.");
                }
            }

            private void showMessage(Message.MessageType messageType, String title, String message) {
                Message ms = new Message();
                ms.showMessage(messageType, title, message, 3000);
                ms.setVisible(true); // Hiển thị thông báo
            }

            @Override
            public void onEdit(int row) {
                int vocabId = (int) table.getValueAt(row, 0);
                ServiceVocabulary vocabularyService = new VocabularyImpl();
                ModelVocabulary modelVocabulary = vocabularyService.findById(vocabId);

                if (modelVocabulary != null && modelVocabulary.getVocabId() == vocabId) {
                    formEdit = new FormEdit(FormEdit.Mode.EDIT, modelVocabulary.getUserId());
                    formEdit.setVocabularyData(modelVocabulary);
                    formEdit.setVisible(true);

                    formEdit.addWindowListener(new java.awt.event.WindowAdapter() {
                        @Override
                        public void windowClosed(java.awt.event.WindowEvent e) {
                            refreshTableData();
                        }
                    });
                }
            }

            @Override
            public void onView(int row) {
                int vocabId = (int) table.getValueAt(row, 0);
                System.out.println("Xem vocab_id: " + vocabId);
            }
        }));

        table.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return table;
    }

    private void loadVocabularyDataToTable() {
        System.out.println("Đang tải dữ liệu từ vựng vào bảng...");

        try {
            ServiceVocabulary vocabularyService = new VocabularyImpl();
            List<Object[]> vocabularyData = vocabularyService.loadVocabularyData(user.getUserID(),null,null,null);

            if (vocabularyData == null) {
                System.out.println("Dữ liệu trả về từ ServiceVocabulary là null. Kiểm tra lại kết nối hoặc dịch vụ.");
                return;
            }

            DefaultTableModel model = (DefaultTableModel) table.getModel();
            model.setRowCount(0); // Xóa các hàng cũ
            for (Object[] rowData : vocabularyData) {
                model.addRow(rowData);
            }

        } catch (Exception e) {
            System.out.println("Lỗi khi tải dữ liệu từ vựng: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void slideToCard(int targetIndex) {
        if (targetIndex < 0 || targetIndex >= cardPanel.getComponentCount()) {
            return;
        }

        int step = 10;
        int direction = targetIndex > currentIndex ? 1 : -1;
        JPanel currentCard = (JPanel) cardPanel.getComponent(currentIndex);
        JPanel targetCard = (JPanel) cardPanel.getComponent(targetIndex);

        targetCard.setLocation(0, direction > 0 ? cardPanel.getHeight() : -cardPanel.getHeight());
        targetCard.setVisible(true);

        Timer timer = new Timer(10, null);
        timer.addActionListener(new ActionListener() {
            int distance = cardPanel.getHeight();

            @Override
            public void actionPerformed(ActionEvent e) {
                int moveStep = direction * step;
                currentCard.setLocation(0, currentCard.getLocation().y - moveStep);
                targetCard.setLocation(0, targetCard.getLocation().y - moveStep);

                distance -= step;
                if (distance <= 0) {
                    ((Timer) e.getSource()).stop();
                    currentCard.setVisible(false);
                    targetCard.setLocation(0, 0);
                    cardLayout.show(cardPanel, "Card" + (targetIndex + 1));
                    currentIndex = targetIndex;
                }
                repaint();
            }
        });
        timer.start();
    }

    @Override
    public void refreshTableData() {
        loadVocabularyDataToTable();
    }
}

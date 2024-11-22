	package com.flashcard.form;
	
	import com.flashcard.model.ModelTopic;
	import com.flashcard.model.ModelUser;
	import com.flashcard.model.ModelVocabulary;
	import com.flashcard.model.Model_Card;
	import com.flashcard.service.ServiceTopic;
	import com.flashcard.service.ServiceVocabulary;
	import com.flashcard.service.VocabularyImpl;
	import com.flashcard.swing.Button;
import com.flashcard.swing.CustomOptionPane;
import com.flashcard.swing.MyComboBox;
	import com.flashcard.swing.ScrollBar;
	import com.flashcard.swing.TableActionCellEditor;
	import com.flashcard.swing.TableActionCellRender;
	
	import net.miginfocom.swing.MigLayout;
	
	import com.flashcard.component.Header;
	import com.flashcard.component.Message;
	import com.flashcard.component.Message.MessageType;
	import com.flashcard.event.SearchListener;
	import com.flashcard.event.TableActionEvent;
	import com.flashcard.form.FormEdit.Mode;
	
	import java.awt.BorderLayout;
	import java.awt.Color;
	import java.awt.FlowLayout;
	import java.io.File;
	import java.util.ArrayList;
	import java.util.HashSet;
	import java.util.List;
	import java.util.Set;
	
	import javax.swing.ImageIcon;
	import javax.swing.JPanel;
	import javax.swing.JScrollPane;
	import javax.swing.Timer;
	import javax.swing.table.DefaultTableModel;
	
	import org.jdesktop.animation.timing.Animator;
	import org.jdesktop.animation.timing.TimingTarget;
	import org.jdesktop.animation.timing.TimingTargetAdapter;
	
	//Lớp Form_Home mở rộng từ JPanel để tạo giao diện chính cho ứng dụng
	public class FormHome extends javax.swing.JPanel implements Refreshable, SearchListener {
	
		private ModelUser user;
		private MigLayout layout;
		private FormEdit formEdit;
		private MyComboBox<String> comboBox1;
		private MyComboBox<String> comboBox2;
	
		private MyComboBox<String> comboBox3;
		
	
		
		public enum Mode {
			ADD, EDIT
		}
	
		private Mode mode;
		private Header header;
		private ServiceVocabulary serviceVocabulary  ;
		// Hàm khởi tạo
		public FormHome(ModelUser user) {
			this.serviceVocabulary = new VocabularyImpl();
			initComponents();
			this.user = user;
	
			this.formEdit = new FormEdit(FormEdit.Mode.ADD, user.getUserID());
			addSearchListener();
			loadVocabularyDataToTable();
			
		    
		    panelBorder1.add(jLabel1,BorderLayout.NORTH);
			// Thêm MouseListener để lắng nghe sự kiện nhấn vào hàng trong bảng
			// Thêm sự kiện vào cell editor của cột thao tác
			table1.getColumnModel().getColumn(4).setCellEditor(new TableActionCellEditor(new TableActionEvent() {
				@Override
				public void onDelete(int row) {
				    int vocabId = (int) table1.getValueAt(row, 0);
				    if (table1.isEditing()) {
				        table1.getCellEditor().stopCellEditing();
				    }
				    
				    DefaultTableModel model = (DefaultTableModel) table1.getModel();
				    ServiceVocabulary vocabularyService = new VocabularyImpl();
				    boolean isDeleted = vocabularyService.deleteById(vocabId);
				    
				    if (isDeleted) {
				        model.removeRow(row);
				        CustomOptionPane.showMessage(null,
				        	    "Thông báo thành công",
				        	    "Dữ liệu đã được lưu!",
				        	    CustomOptionPane.MessageType.SUCCESS);
				    } else {
				    	CustomOptionPane.showMessage(null,
				        	    "Thông báo thành công",
				        	    "Dữ liệu không được lưu!",
				        	    CustomOptionPane.MessageType.ERROR);
				    }
				    
				    
				}
	
				@Override
				public void onEdit(int row) {
					// Lấy vocab_id từ bảng theo hàng
					int vocabId = (int) table1.getValueAt(row, 0); // Giả sử vocab_id là cột đầu tiên
					System.out.println("Chỉnh sửa vocab_id: " + vocabId);
	
					ServiceVocabulary vocabularyService = new VocabularyImpl();
					ModelVocabulary modelVocabulary = vocabularyService.findById(vocabId);
	
					if (modelVocabulary != null && modelVocabulary.getVocabId() == vocabId) {
						formEdit = new FormEdit(FormEdit.Mode.EDIT, modelVocabulary.getUserId());
						formEdit.setVocabularyData(modelVocabulary); // Cập nhật dữ liệu vào form
						formEdit.setVisible(true); // Hiển thị Form_Edit
	
						// Làm mới bảng sau khi chỉnh sửa thành công
						formEdit.addWindowListener(new java.awt.event.WindowAdapter() {
							@Override
							public void windowClosed(java.awt.event.WindowEvent e) {
								refreshTableData();
							}
						});
					} else {
						System.out.println("Không tìm thấy từ vựng để chỉnh sửa.");
					}
				}
	
				@Override
				public void onView(int row) {
					// Lấy vocab_id từ bảng theo hàng
					int vocabId = (int) table1.getValueAt(row, 0); // Giả sử vocab_id là cột đầu tiên
					System.out.println("Xem vocab_id: " + vocabId);
					// Thực hiện xử lý logic xem từ vựng ở đây
					// Ví dụ: hiển thị popup hoặc chi tiết từ vựng
				}
	
			}));
	
			// Cài đặt render và editor cho cột thao tác
			table1.getColumnModel().getColumn(4).setCellRenderer(new TableActionCellRender());
	
			loadVocabularyDataToTable();
	
			table1.revalidate();
			table1.repaint();
	
			table1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
	
		
		}
		
		
	
	
	
	
	
		private void addSearchListener() {
			// Thêm listener để lắng nghe từ Header
			Header header = new Header();
			header.addSearchListener(new SearchListener() {
				@Override
				public void onSearch(String keyword) {
					updateSearchResults(keyword);
				}
			});
		}
	
		public void updateSearchResults(String keyword) {
			System.out.println("Tìm kiếm với từ khóa: " + keyword);
	
			// Kiểm tra đầu vào
			if (keyword == null || keyword.trim().isEmpty()) {
				System.out.println("Từ khóa rỗng, tải lại toàn bộ dữ liệu.");
				loadVocabularyDataToTable();
				return;
			}
	
			try {
				ServiceVocabulary vocabularyService = new VocabularyImpl();
				List<Object[]> filteredData = vocabularyService.findVocabularyByKey(user.getUserID(), keyword);
	
				// Kiểm tra dữ liệu trả về
				if (filteredData == null) {
					System.out.println("Dịch vụ trả về null. Kiểm tra lại ServiceVocabulary.");
					return;
				}
	
				System.out.println("Kết quả tìm kiếm nhận được từ ServiceVocabulary:");
				for (Object[] row : filteredData) {
					System.out.println(java.util.Arrays.toString(row));
				}
	
				// Cập nhật giao diện
				updateSearchDisplay(filteredData);
	
			} catch (Exception e) {
				System.out.println("Lỗi trong quá trình tìm kiếm: " + e.getMessage());
				e.printStackTrace();
			}
		}
	
		private void updateSearchDisplay(List<Object[]> results) {
			DefaultTableModel model = (DefaultTableModel) table1.getModel();
			model.setRowCount(0); // Xóa dữ liệu cũ
	
			// Cập nhật dữ liệu mới
			if (results.isEmpty()) {
				System.out.println("Không tìm thấy kết quả cho từ khóa.");
				model.addRow(new Object[] { "Không có kết quả", "", "", "", "" });
			} else {
				for (Object[] row : results) {
					model.addRow(row);
				}
			}
		}
	
		private List<String> getTopicName() {
		    Set<Integer> uniqueIds = new HashSet<>(); // Lưu trữ các topic_id để kiểm tra trùng lặp
		    List<String> uniqueTopicNames = new ArrayList<>(); // Danh sách chứa tên topic không trùng
		    List<ModelTopic> dataTopic = serviceVocabulary.getTopicByUserId(21);
	
		    for (ModelTopic modelTopic : dataTopic) {
		        if (uniqueIds.add(modelTopic.getTopic_id())) { 
		            // Nếu topic_id chưa có trong Set, thêm vào và lấy tên topic
		            uniqueTopicNames.add(modelTopic.getTopicName());
		        }
		    }
	
		    return uniqueTopicNames; // Trả về danh sách
		}
	
		
		private void applySort() {
		    // Lấy giá trị từ comboBox1 (chủ đề)
		    String selectedTopic = (String) comboBox1.getSelectedItem();
		    String topicCondition = null;
		    if (!"Sắp xếp theo chủ đề".equals(selectedTopic)) {
		        topicCondition = selectedTopic;
		    }
	
		    // Lấy giá trị từ comboBox2 (sắp xếp theo từ vựng)
		    String wordOrder = null;
		    if (comboBox2.getSelectedIndex() == 1) {
		        wordOrder = "ASC"; // Tăng dần
		    } else if (comboBox2.getSelectedIndex() == 2) {
		        wordOrder = "DESC"; // Giảm dần
		    }
	
		    // Lấy giá trị từ comboBox3 (lọc theo độ khó)
		    String selectedDifficulty = null;
		    if (comboBox3.getSelectedIndex() == 1) {
		        selectedDifficulty = "easy";
		    } else if (comboBox3.getSelectedIndex() == 2) {
		        selectedDifficulty = "medium";
		    } else if (comboBox3.getSelectedIndex() == 3) {
		        selectedDifficulty = "hard";
		    }
	
		    // Lấy userId
		    int userId = user.getUserID();
	
		    // Gọi hàm loadVocabularyData với các điều kiện
		    List<Object[]> filteredData = serviceVocabulary.loadVocabularyData(userId, wordOrder, topicCondition, selectedDifficulty);
	
		    // Hiển thị dữ liệu trên bảng
		    updateTable(filteredData);
		}
	
		
		private void updateTable(List<Object[]> data) {
		    DefaultTableModel model = (DefaultTableModel) table1.getModel();
		    model.setRowCount(0); // Xóa dữ liệu cũ
	
		    for (Object[] row : data) {
		        model.addRow(row); // Thêm từng hàng vào bảng
		    }
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
	
				System.out.println("Dữ liệu từ vựng nhận được:");
				for (Object[] row : vocabularyData) {
					System.out.println(java.util.Arrays.toString(row));
				}
	
				DefaultTableModel model = (DefaultTableModel) table1.getModel();
				model.setRowCount(0); // Xóa các hàng cũ
				for (Object[] rowData : vocabularyData) {
					model.addRow(rowData);
				}
	
			} catch (Exception e) {
				System.out.println("Lỗi khi tải dữ liệu từ vựng: " + e.getMessage());
				e.printStackTrace();
			}
		}
	
		@Override
		public void refreshTableData() {
			System.out.println("Đang làm mới dữ liệu trong bảng...");
			loadVocabularyDataToTable(); // Gọi phương thức tải lại dữ liệu
		}
	
		// Phương thức hỗ trợ để thiết lập biểu tượng cho thẻ
		private void setCardIcon(com.flashcard.component.Card card, String iconPath, String title) {
			File iconFile = new File(iconPath);
			if (iconFile.exists()) {
				card.setData(new Model_Card(new ImageIcon(iconFile.getAbsolutePath()), title, "", ""));
			} else {
				System.out.println("Icon not found at " + iconPath);
			}
		}
	
		@SuppressWarnings("unchecked")
		private void initComponents() {
			
			panel = new javax.swing.JLayeredPane();
			
			comboBox1 = new MyComboBox<>();
		    comboBox2 = new MyComboBox<>();
		    comboBox3 = new MyComboBox<>();
			panelBorder1 = new com.flashcard.swing.PanelBorder();
			jLabel1 = new javax.swing.JLabel();
			jScrollPane1 = new javax.swing.JScrollPane();
			table1 = new com.flashcard.swing.Table();
			Button btnThem = new Button();
			Button btnLamMoi = new Button();
			 
			setLayout(new java.awt.BorderLayout());
			
			
			// Lấy danh sách các chủ đề
			List<String> topics = getTopicName();
	
			// Thêm một mục mặc định vào danh sách
			topics.add(0, "Sắp xếp theo chủ đề");
	
			// Thiết lập dữ liệu cho comboBox1
			comboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(topics.toArray(new String[0])));
		    comboBox1.setBackground(new Color(67, 179, 193));
		    comboBox1.setForeground(new Color(250, 250, 250));
		    panel.add(comboBox1);
	
		    // Thêm các mục vào comboBox2
		    comboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Sắp xếp theo từ ", "A -> Z", "A <- Z" }));
		    comboBox2.setBackground(new Color(67, 179, 193));
		    comboBox2.setForeground(new Color(250, 250, 250));
		    panel.add(comboBox2);
	
		    // Thêm các mục vào comboBox3
		    comboBox3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Sắp xếp theo độ khó", "Easy", "Medium", "Hard" }));
		    comboBox3.setBackground(new Color(67, 179, 193));
		    comboBox3.setForeground(new Color(250, 250, 250));
		    panel.add(comboBox3);
			
		    
		    comboBox1.addActionListener(e -> applySort());
		    comboBox2.addActionListener(e -> applySort());
		    comboBox3.addActionListener(e -> applySort());
	
		    
			// Thiết lập panelBorder1
		    panel.setLayout(new java.awt.GridLayout(1, 3, 10, 0)); // 1 hàng, 3 cột
		    panel.setBackground(Color.WHITE);
			panelBorder1.setBackground(new java.awt.Color(255, 255, 255));
			panelBorder1.setLayout(new BorderLayout());
			
		    
			// Cài đặt tiêu đề "Quản lý từ vựng"
			jLabel1.setFont(new java.awt.Font("sansserif", 1, 18));
			jLabel1.setForeground(new java.awt.Color(127, 127, 127));
			jLabel1.setText("Quản lý từ vựng");
			panelBorder1.add(jLabel1, BorderLayout.NORTH);
	
			// Thiết lập nút "Thêm"
			btnThem.setBackground(new Color(67, 179, 193));
			btnThem.setForeground(new Color(250, 250, 250));
			btnThem.setText("Thêm");
	
			// Chỉnh kích thước nút "Thêm"
			btnThem.setPreferredSize(new java.awt.Dimension(120, 40)); // Kích thước 120px x 40px
			btnThem.setFont(new java.awt.Font("sansserif", java.awt.Font.BOLD, 16)); // Font chữ to hơn
			btnThem.setFocusPainted(false); // Bỏ viền khi nhấn
			btnThem.setBorderPainted(false); // Bỏ viền xung quanh
			// btnThem.setOpaque(true); // Hiển thị màu nền rõ hơn
	
			// Thêm nút "Thêm" vào một panel mới
			JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
			buttonPanel.setBackground(Color.WHITE);
			
	
			// Thêm buttonPanel vào panelBorder1
			panelBorder1.add(buttonPanel, BorderLayout.SOUTH);
	
			btnThem.addActionListener(e -> {
				if (formEdit == null) {
					
					formEdit = new FormEdit(FormEdit.Mode.ADD, user.getUserID());
					// Lắng nghe sự kiện đóng cửa sổ thêm mới
				    formEdit.addWindowListener(new java.awt.event.WindowAdapter() {
				        @Override
				        public void windowClosed(java.awt.event.WindowEvent e) {
				            refreshTableData(); // Làm mới dữ liệu sau khi thêm
				        }
				    });
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
			
			
			// Cài đặt nút "Làm mới"
			btnLamMoi.setBackground(new Color(67, 179, 193));
			btnLamMoi.setForeground(new Color(250, 250, 250));
			btnLamMoi.setText("Làm mới");
			btnLamMoi.setPreferredSize(new java.awt.Dimension(120, 40)); // Kích thước nút
			btnLamMoi.addActionListener(e -> refreshTableData()); // Gọi phương thức làm mới khi nhấn
			
			
			// Thêm nút vào panel
			
			buttonPanel.add(btnThem);
			buttonPanel.add(btnLamMoi);
			panelBorder1.add(buttonPanel, BorderLayout.SOUTH); // Thêm vào cuối panelBorder1
	
					
	
			// Cài đặt bảng table1
			table1.setModel(new javax.swing.table.DefaultTableModel(new Object[][] {},
					new String[] { "ID", "Từ vựng", "Nghĩa của từ", "Chủ đề", "Thao tác" }) {
				boolean[] canEdit = new boolean[] { false, false, false, false, true };
	
				public boolean isCellEditable(int rowIndex, int columnIndex) {
					return canEdit[columnIndex];
				}
			});
	
			jScrollPane1.setViewportView(table1);
			panelBorder1.add(jScrollPane1, BorderLayout.CENTER);
	
			// Thêm các phần vào Form_Home
			add(panel, BorderLayout.NORTH);
			add(panelBorder1, BorderLayout.CENTER);
		}
	
		
		private javax.swing.JLabel jLabel1;
		private javax.swing.JScrollPane jScrollPane1;
		private javax.swing.JLayeredPane panel;
		private com.flashcard.swing.PanelBorder panelBorder1;
		private com.flashcard.swing.Table table1;
		private com.flashcard.swing.Button button;
		private javax.swing.JLayeredPane bg;
	
		@Override
		public void onSearch(String keyword) {
			System.out.println("dang tim kiem");
		}
	
	}

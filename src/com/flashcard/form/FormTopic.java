package com.flashcard.form;

import com.flashcard.model.ModelTopic;
import com.flashcard.model.ModelUser;
import com.flashcard.model.ModelVocabulary;
import com.flashcard.model.Model_Card;
import com.flashcard.service.ServiceTopic;
import com.flashcard.service.ServiceVocabulary;
import com.flashcard.service.TopicImpl;
import com.flashcard.service.VocabularyImpl;
import com.flashcard.swing.Button;
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
import java.util.List;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;

import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTarget;
import org.jdesktop.animation.timing.TimingTargetAdapter;

//Lớp Form_Home mở rộng từ JPanel để tạo giao diện chính cho ứng dụng
public class FormTopic extends javax.swing.JPanel implements Refreshable, SearchListener {

	private ModelUser user;
	private MigLayout layout;
	private FormEditTopic formEditTopic;
	private Message message;
	private ServiceTopic serviceTopic;

	public enum Mode {
		ADD, EDIT
	}

	private Mode mode;
	private Header header;
	private JPanel messagePanel;

	// Hàm khởi tạo
	public FormTopic(ModelUser user) {
		initComponents();
		this.user = user;
		serviceTopic = new TopicImpl();
		this.formEditTopic = new FormEditTopic(FormEditTopic.Mode.ADD, user.getUserID());
		addSearchListener();
		loadTopicDataToTable();
		messagePanel = new JPanel(new BorderLayout());

		panelBorder1.add(messagePanel, BorderLayout.NORTH); // Adding it to the top of the panelBorder1
		panelBorder1.add(jLabel1, BorderLayout.NORTH);
		// Thêm MouseListener để lắng nghe sự kiện nhấn vào hàng trong bảng
		// Thêm sự kiện vào cell editor của cột thao tác
		table1.getColumnModel().getColumn(3).setCellEditor(new TableActionCellEditor(new TableActionEvent() {
			@Override
			public void onDelete(int row) {
			    int topicId = (int) table1.getValueAt(row, 0);
			    if (table1.isEditing()) {
			        table1.getCellEditor().stopCellEditing();
			    }

			    // Hiển thị hộp thoại xác nhận xóa
			    int confirm = JOptionPane.showConfirmDialog(
			        null, 
			        "Bạn có chắc chắn muốn xóa chủ đề này không?", 
			        "Xác nhận xóa", 
			        JOptionPane.YES_NO_OPTION, 
			        JOptionPane.WARNING_MESSAGE
			    );

			    // Nếu người dùng chọn "Yes"
			    if (confirm == JOptionPane.YES_OPTION) {
			        DefaultTableModel model = (DefaultTableModel) table1.getModel();
			        boolean isDeleted = serviceTopic.deleteById(topicId);

			        if (isDeleted) {
			            model.removeRow(row);
			            showMessage(Message.MessageType.SUCCESS, "Xóa chủ đề", "Xóa thành công chủ đề!");
			        } else {
			            showMessage(Message.MessageType.ERROR, "Xóa chủ đề", "Xóa thất bại, vui lòng thử lại.");
			        }
			    } else {
			        // Người dùng chọn "No" hoặc đóng hộp thoại
			        showMessage(Message.MessageType.INFO, "Hủy thao tác", "Thao tác xóa đã bị hủy.");
			    }
			}


			@Override
			public void onEdit(int row) {
				int topicId = (int) table1.getValueAt(row, 0);
				ModelTopic topic = serviceTopic.findById(topicId);
				System.out.println("mã chủ đề" +topicId);
				if (topic != null) {
					formEditTopic = new FormEditTopic(FormEditTopic.Mode.EDIT, user.getUserID());
					formEditTopic.setTopicData(topic);
					formEditTopic.setVisible(true);

					formEditTopic.addWindowListener(new java.awt.event.WindowAdapter() {
						@Override
						public void windowClosed(java.awt.event.WindowEvent e) {
							refreshTableData();
							showMessage(Message.MessageType.SUCCESS, "Chỉnh sửa chủ đề", "Đã lưu thay đổi.");
						}
					});
				} else {
					showMessage(Message.MessageType.ERROR, "Chỉnh sửa chủ đề", "Không tìm thấy chủ đề.");
				}
			}

			@Override
			public void onView(int row) {
				// Lấy vocab_id từ bảng theo hàng
				int vocabId = (int) table1.getValueAt(row, 0); // Giả sử vocab_id là cột đầu tiên
				System.out.println("Xem vocab_id: " + vocabId);
				// Thực hiện xử lý logic xem chủ đề ở đây
				// Ví dụ: hiển thị popup hoặc chi tiết chủ đề
			}

		}));

		// Cài đặt render và editor cho cột thao tác
		table1.getColumnModel().getColumn(3).setCellRenderer(new TableActionCellRender());

		loadTopicDataToTable();

		table1.revalidate();
		table1.repaint();

		table1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

	}

	private void showMessage(Message.MessageType messageType, String title, String message) {
		Message ms = new Message();
		ms.showMessage(messageType, title, message, 3000);
		ms.setVisible(true);

		if (messagePanel != null) {
			messagePanel.removeAll();
			messagePanel.add(ms, BorderLayout.NORTH);
			messagePanel.revalidate();
			messagePanel.repaint();
		}

		new Timer(3000, e -> {
			ms.setVisible(false);
			if (messagePanel != null) {
				messagePanel.revalidate();
				messagePanel.repaint();
			}
		}).start();
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
			loadTopicDataToTable();
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
			model.addRow(new Object[] { "Không có kết quả", "", "", "" });
		} else {
			for (Object[] row : results) {
				model.addRow(row);
			}
		}
	}

	private void loadTopicDataToTable() {
		System.out.println("Đang tải dữ liệu chủ đề vào bảng...");

		try {
			ServiceTopic topicService = new TopicImpl();
			Set<Object[]> topicData = topicService.getAll(user.getUserID());

			if (topicData == null) {
				System.out.println("Dữ liệu trả về từ ServiceVocabulary là null. Kiểm tra lại kết nối hoặc dịch vụ.");
				return;
			}

			System.out.println("Dữ liệu chủ đề nhận được:");
			for (Object[] row : topicData) {
				System.out.println(java.util.Arrays.toString(row));
			}

			DefaultTableModel model = (DefaultTableModel) table1.getModel();
			model.setRowCount(0); // Xóa các hàng cũ
			for (Object[] rowData : topicData) {
				model.addRow(rowData);
			}

		} catch (Exception e) {
			System.out.println("Lỗi khi tải dữ liệu chủ đề: " + e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public void refreshTableData() {
		System.out.println("Đang làm mới dữ liệu trong bảng...");
		loadTopicDataToTable(); // Gọi phương thức tải lại dữ liệu
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
		panelBorder1 = new com.flashcard.swing.PanelBorder();
		jLabel1 = new javax.swing.JLabel();
		jScrollPane1 = new javax.swing.JScrollPane();
		table1 = new com.flashcard.swing.Table();
		messagePanel = new JPanel(new BorderLayout()); // Panel chứa Message
		Button btnThem = new Button();
		Button btnLamMoi = new Button();
		setLayout(new java.awt.BorderLayout());

		// Cài đặt panelBorder1
		panelBorder1.setBackground(new java.awt.Color(255, 255, 255));
		panelBorder1.setLayout(new BorderLayout());

		// Tiêu đề "Quản lý chủ đề"
		jLabel1.setFont(new java.awt.Font("sansserif", 1, 18));
		jLabel1.setForeground(new java.awt.Color(127, 127, 127));
		jLabel1.setText("Quản lý chủ đề");
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

		// Thêm nút "Thêm" vào một panel mới
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER)); // Căn phải
		buttonPanel.setBackground(Color.WHITE);

		// Thêm hành động cho nút
		btnThem.addActionListener(e -> {
			if (formEditTopic == null) {
				formEditTopic = new FormEditTopic(FormEditTopic.Mode.ADD, user.getUserID());
			} else {
				formEditTopic.setMode(FormEditTopic.Mode.ADD); // Đặt lại chế độ
			}
			formEditTopic.setInsertTopicData(user.getUserID());
			formEditTopic.setVisible(true);

			// Làm mới bảng khi đóng Form_Edit
			formEditTopic.addWindowListener(new java.awt.event.WindowAdapter() {
				@Override
				public void windowClosed(java.awt.event.WindowEvent e) {
					refreshTableData();
				}
			});
		});

		// Cài đặt nút "Làm mới"
		btnLamMoi.setBackground(new Color(67, 179, 193));
		btnLamMoi.setForeground(new Color(250, 250, 250));
		btnLamMoi.setFont(new java.awt.Font("sansserif", java.awt.Font.BOLD, 16)); // Font chữ to hơn

		btnLamMoi.setText("Làm mới");
		btnLamMoi.setPreferredSize(new java.awt.Dimension(120, 40)); // Kích thước nút
		btnLamMoi.addActionListener(e -> refreshTableData()); // Gọi phương thức làm mới khi nhấn

		// Thêm nút vào panel

		buttonPanel.add(btnThem);
		buttonPanel.add(btnLamMoi);
		panelBorder1.add(buttonPanel, BorderLayout.SOUTH); // Thêm vào cuối panelBorder1

		// Thêm bảng chủ đề
		table1.setModel(new javax.swing.table.DefaultTableModel(new Object[][] {},
				new String[] { "ID", "Chủ đề", "Số lượng từ", "Thao tác" }) {
			boolean[] canEdit = new boolean[] { false, false, false, true };

			public boolean isCellEditable(int rowIndex, int columnIndex) {
				return canEdit[columnIndex];
			}
		});
		jScrollPane1.setViewportView(table1);
		panelBorder1.add(jScrollPane1, BorderLayout.CENTER);

		// Thêm nút "Thêm" vào cuối
		panelBorder1.add(buttonPanel, BorderLayout.SOUTH);

		// Thêm panelBorder1 vào giao diện
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

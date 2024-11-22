package com.flashcard.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.flashcard.connection.DatabaseConnection;
import com.flashcard.model.ModelTopic;
import com.flashcard.model.ModelUser;
import com.flashcard.model.ModelVocabulary;

public class VocabularyImpl implements ServiceVocabulary {

	private Connection connection;

	public List<Object[]> loadVocabularyData(int userId, String sortByWord, String topicName, String difficulty) {
	    List<Object[]> data = new ArrayList<>();
	    StringBuilder whereClause = new StringBuilder("WHERE v.user_id = ? ");
	    
	    // Thêm điều kiện lọc chủ đề
	    if (topicName != null) {
	        whereClause.append("AND t.topic_name = ? ");
	    }

	    // Thêm điều kiện lọc độ khó
	    if (difficulty != null) {
	        whereClause.append("AND v.difficulty = ? ");
	    }

	    // Tạo ORDER BY động
	    StringBuilder orderByClause = new StringBuilder();
	    if (sortByWord != null) {
	        orderByClause.append("ORDER BY v.word " + sortByWord);
	    } else {
	        orderByClause.append("ORDER BY v.word ASC"); // Mặc định sắp xếp theo từ
	    }

	    String sql = "SELECT v.vocab_id, v.word, v.meaning, v.topic_id, t.topic_name, v.difficulty " +
	                 "FROM tbl_Vocabulary v " +
	                 "INNER JOIN tbl_Topic t ON v.topic_id = t.topic_id " +
	                 whereClause +
	                 orderByClause;

	    try (Connection con = DatabaseConnection.getInstance().getConnection();
	         PreparedStatement stmt = con.prepareStatement(sql)) {

	        int paramIndex = 1;
	        stmt.setInt(paramIndex++, userId);

	        if (topicName != null) {
	            stmt.setString(paramIndex++, topicName);
	        }
	        if (difficulty != null) {
	            stmt.setString(paramIndex++, difficulty);
	        }

	        try (ResultSet rs = stmt.executeQuery()) {
	            while (rs.next()) {
	                int vocabId = rs.getInt("vocab_id");
	                String word = rs.getString("word");
	                String meaning = rs.getString("meaning");
	                String topic = rs.getString("topic_name");
	                String diff = rs.getString("difficulty");

	                data.add(new Object[]{vocabId, word, meaning, topic, diff});
	            }
	        }
	    } catch (SQLException e) {
	        System.err.println("Lỗi khi tải dữ liệu từ cơ sở dữ liệu: " + e.getMessage());
	    }

	    return data;
	}





	@Override
	public boolean deleteById(int vocabId) {
		String sql = "DELETE FROM tbl_Vocabulary WHERE vocab_id = ?"; // sửa tên bảng cho đúng với tên trong
																		// loadVocabularyData

		try (Connection con = DatabaseConnection.getInstance().getConnection();
				PreparedStatement stmt = con.prepareStatement(sql)) {

			// Kiểm tra nếu kết nối bị null hoặc đã đóng
			if (con == null || con.isClosed()) {
				System.err.println("Kết nối cơ sở dữ liệu đã bị đóng.");
				return false;
			}

			stmt.setInt(1, vocabId);
			int rowsAffected = stmt.executeUpdate();

			if (rowsAffected > 0) {
				System.out.println("Đã xóa thành công từ vựng với ID: " + vocabId);
				return true;
			} else {
				System.out.println("Không tìm thấy từ vựng với ID: " + vocabId);
				return false;
			}
		} catch (SQLException e) {
			System.err.println("Lỗi khi thực hiện xóa từ vựng: " + e.getMessage());
			e.printStackTrace();
		}
		return true;
	}

	@Override
	public void update(ModelVocabulary modelVocabulary) {
		String sql = "UPDATE tbl_Vocabulary SET user_id = ?, word = ?, meaning = ?, example = ?, topic_id = ?, difficulty = ? WHERE vocab_id = ?";

		try (Connection con = DatabaseConnection.getInstance().getConnection();
				PreparedStatement stmt = con.prepareStatement(sql)) {

			// Kiểm tra nếu kết nối bị null hoặc đã đóng
			if (con == null || con.isClosed()) {
				System.err.println("Kết nối cơ sở dữ liệu đã bị đóng.");
				return;
			}

			// Đặt các giá trị cho câu lệnh SQL
			stmt.setInt(1, modelVocabulary.getUserId());
			stmt.setString(2, modelVocabulary.getWord());
			stmt.setString(3, modelVocabulary.getMeaning());
			stmt.setString(4, modelVocabulary.getExample());
			stmt.setInt(5, modelVocabulary.getTopic_id());
			stmt.setString(6, modelVocabulary.getDifficulty());
			stmt.setInt(7, modelVocabulary.getVocabId());

			int rowsAffected = stmt.executeUpdate();

			if (rowsAffected > 0) {
				System.out.println("Đã cập nhật thành công từ vựng với ID: " + modelVocabulary.getVocabId());
			} else {
				System.out.println("Không tìm thấy từ vựng với ID: " + modelVocabulary.getVocabId());
			}
		} catch (SQLException e) {
			System.err.println("Lỗi khi thực hiện cập nhật từ vựng: " + e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public ModelVocabulary findById(int vocab_id) {
		String sql = "SELECT * FROM tbl_Vocabulary WHERE vocab_id = ?";
		ModelVocabulary vocab = null;
		System.out.println("có thấy id từ vựng không " + vocab_id);
		try (Connection con = DatabaseConnection.getInstance().getConnection();
				PreparedStatement stmt = con.prepareStatement(sql)) {

			// Kiểm tra nếu kết nối bị null hoặc đã đóng
			if (con == null || con.isClosed()) {
				System.err.println("Kết nối cơ sở dữ liệu đã bị đóng.");
				return null;
			}

			stmt.setInt(1, vocab_id);
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					// Lấy các giá trị từ kết quả truy vấn và tạo đối tượng ModelVocabulary
					int userId = rs.getInt("user_id");
					String word = rs.getString("word");
					String meaning = rs.getString("meaning");
					String example = rs.getString("example");
					int topic_id = rs.getInt("topic_id");
					String difficulty = rs.getString("difficulty");

					vocab = new ModelVocabulary(vocab_id, userId, word, meaning, example, topic_id, difficulty);
				}
			}
		} catch (SQLException e) {
			System.err.println("Lỗi khi tìm từ vựng theo ID: " + e.getMessage());
			e.printStackTrace();
		}

		return vocab;
	}

	@Override
	public void insertVocab(ModelVocabulary modelVocabulary) {
		
		
		String sql = "INSERT INTO tbl_vocabulary(USER_ID,WORD,meaning,example,topic_id,difficulty) VALUES(?,?,?,?,?,?)";
		try(Connection con = DatabaseConnection.getInstance().getConnection();
				PreparedStatement stmt = con.prepareStatement(sql)) {
			// Kiểm tra nếu kết nối bị null hoặc đã đóng
						if (con == null || con.isClosed()) {
							System.err.println("Kết nối cơ sở dữ liệu đã bị đóng.");
							
						}
						
						stmt.setInt(1, modelVocabulary.getUserId());
						stmt.setString(2, modelVocabulary.getWord());
						stmt.setString(3, modelVocabulary.getMeaning());
						stmt.setString(4, modelVocabulary.getExample());
						stmt.setInt(5, modelVocabulary.getTopic_id());
						stmt.setString(6, modelVocabulary.getDifficulty());
						stmt.executeUpdate();
						
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public List<ModelTopic> getTopicByUserId(int userId) {
	    List<ModelTopic> data = new ArrayList<>();
	    String sql = "SELECT t.topic_name, t.topic_id, t.description " +
	                 "FROM tbl_Topic t " +
	                 "LEFT JOIN tbl_Vocabulary v " +
	                 "ON t.topic_id = v.topic_id AND v.user_id = ? " +
	                 "WHERE t.user_id = ?";

	    try (Connection con = DatabaseConnection.getInstance().getConnection();
	         PreparedStatement stmt = con.prepareStatement(sql)) {

	        if (con != null) {
	            System.out.println("Kết nối cơ sở dữ liệu thành công.");
	        } else {
	            System.out.println("Kết nối cơ sở dữ liệu thất bại.");
	            return data;
	        }

	        stmt.setInt(1, userId); // Kiểm tra từ vựng thuộc user
	        stmt.setInt(2, userId); // Kiểm tra chủ đề thuộc user

	        try (ResultSet rs = stmt.executeQuery()) {
	            while (rs.next()) {
	                ModelTopic temp = new ModelTopic();
	                temp.setTopic_id(rs.getInt("topic_id"));
	                temp.setTopicName(rs.getString("topic_name"));
	                temp.setDescription(rs.getString("description"));

	                data.add(temp);
	                System.out.println("Dữ liệu tải về: " + temp.getTopicName());
	            }
	        }
	    } catch (SQLException e) {
	        System.err.println("Lỗi khi tải dữ liệu từ cơ sở dữ liệu: " + e.getMessage());
	        e.printStackTrace();
	    }

	    return data;
	}



	@Override
	public List<Object[]> findVocabularyByKey(int userId, String name) {
	    List<Object[]> data = new ArrayList<>();
	    
	    // Kiểm tra từ khóa tìm kiếm hợp lệ
	    if (name == null || name.trim().isEmpty()) {
	        System.out.println("Từ khóa tìm kiếm không hợp lệ.");
	        return data;  // Trả về danh sách trống nếu từ khóa không hợp lệ
	    }

	    String sql = "SELECT v.vocab_id, v.word, v.meaning, v.topic_id, t.topic_name " +
	                 "FROM tbl_Vocabulary v " +
	                 "INNER JOIN tbl_Topic t ON v.topic_id = t.topic_id " +
	                 "WHERE v.user_id = ? AND v.word LIKE ?"; // Tìm theo userId và từ khóa

	    // Kết nối và thực thi truy vấn
	    try (Connection con = DatabaseConnection.getInstance().getConnection();
	         PreparedStatement stmt = con.prepareStatement(sql)) {
	        
	        // Kiểm tra kết nối
	        if (con == null) {
	            System.out.println("Kết nối cơ sở dữ liệu thất bại.");
	            return data;
	        }

	        // Gán giá trị cho tham số truy vấn
	        stmt.setInt(1, userId);
	        stmt.setString(2, "%" + name + "%"); // Tìm kiếm theo từ khóa (chứa từ cần tìm)

	        // Thực thi truy vấn và xử lý kết quả
	        try (ResultSet rs = stmt.executeQuery()) {
	            if (!rs.isBeforeFirst()) { // Nếu không có dữ liệu trả về
	                System.out.println("Không tìm thấy từ vựng nào khớp với từ khóa: " + name);
	            } else {
	                while (rs.next()) {
	                    int vocabId = rs.getInt("vocab_id");
	                    String word = rs.getString("word");
	                    String meaning = rs.getString("meaning");
	                    String topicName = rs.getString("topic_name");

	                    // Thêm dữ liệu vào danh sách kết quả
	                    data.add(new Object[]{vocabId, word, meaning, topicName});
	                    
	                    // Ghi log kiểm tra dữ liệu tải về
	                    System.out.println("Tải dữ liệu: vocab_id=" + vocabId + ", word=" + word);
	                }
	            }
	        }
	    } catch (SQLException e) {
	        System.err.println("Lỗi khi tải dữ liệu từ cơ sở dữ liệu: " + e.getMessage());
	        e.printStackTrace();
	    }

	    return data;
	}



}

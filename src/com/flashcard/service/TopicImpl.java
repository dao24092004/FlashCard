package com.flashcard.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.flashcard.connection.DatabaseConnection;
import com.flashcard.model.ModelTopic;

public class TopicImpl implements ServiceTopic {

	@Override
	public Set<Object[]> getAll(int userId) {
		Set<Object[]> data = new HashSet<>();
		String sql = "SELECT t.topic_id, t.topic_name, COUNT(v.vocab_id) AS word_count " + "FROM tbl_Topic t "
				+ "LEFT JOIN tbl_Vocabulary v " + "ON v.topic_id = t.topic_id AND v.user_id = ? "
				+ "WHERE t.user_id = ? " + "GROUP BY t.topic_id, t.topic_name";

		try (Connection con = DatabaseConnection.getInstance().getConnection();
				PreparedStatement stmt = con.prepareStatement(sql)) {

			if (con != null) {
				System.out.println("Kết nối cơ sở dữ liệu thành công.");
			} else {
				System.out.println("Kết nối cơ sở dữ liệu thất bại.");
				return data;
			}

			// Truyền giá trị cho các tham số
			stmt.setInt(1, userId); // Kiểm tra từ vựng thuộc user
			stmt.setInt(2, userId); // Kiểm tra chủ đề thuộc user

			try (ResultSet rs = stmt.executeQuery()) {
				if (!rs.isBeforeFirst()) {
					System.out.println("Không có dữ liệu nào được trả về từ cơ sở dữ liệu.");
				}

				while (rs.next()) {
					int topicId = rs.getInt("topic_id");
					String topicName = rs.getString("topic_name");
					int wordCount = rs.getInt("word_count");
					data.add(new Object[] { topicId, topicName, wordCount });
				}
			}
		} catch (SQLException e) {
			System.err.println("Lỗi khi tải dữ liệu từ cơ sở dữ liệu: " + e.getMessage());
			e.printStackTrace();
		}

		return data;
	}

	@Override
	public void insertTopic(ModelTopic modelTopic) {
		String sql = "INSERT INTO tbl_Topic(topic_name,description,user_id) VALUES(?,?,?)";
		try (Connection con = DatabaseConnection.getInstance().getConnection();
				PreparedStatement stmt = con.prepareStatement(sql)) {
			// Kiểm tra nếu kết nối bị null hoặc đã đóng
			if (con == null || con.isClosed()) {
				System.err.println("Kết nối cơ sở dữ liệu đã bị đóng.");

			}

			stmt.setString(1, modelTopic.getTopicName());
			stmt.setString(2, modelTopic.getDescription());
			stmt.setInt(3, modelTopic.getUser_id());
			stmt.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public ModelTopic findById(int topicId) {
		ModelTopic data = null;
		String sql = "SELECT topic_id,topic_name,description FROM tbl_Topic WHERE topic_id = ?";
		try (Connection con = DatabaseConnection.getInstance().getConnection();
				PreparedStatement stmt = con.prepareStatement(sql)) {
			// Kiểm tra nếu kết nối bị null hoặc đã đóng
			if (con == null || con.isClosed()) {
				System.err.println("Kết nối cơ sở dữ liệu đã bị đóng.");

			}

			stmt.setInt(1, topicId);
			try (ResultSet rs = stmt.executeQuery()) {

				while (rs.next()) {
					int id = rs.getInt("topic_id");
					String name = rs.getString("topic_name");
					String description = rs.getString("description");

					data = new ModelTopic(id, name, description);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;
	}

	@Override
	public void updateTopic(ModelTopic modelTopic) {
		String sql = "UPDATE tbl_Topic SET topic_name = ?, description = ?, user_id = ? WHERE topic_id = ?";

		try (Connection con = DatabaseConnection.getInstance().getConnection();
				PreparedStatement stmt = con.prepareStatement(sql)) {

			stmt.setString(1, modelTopic.getTopicName());
			stmt.setString(2, modelTopic.getDescription());
			stmt.setInt(3, modelTopic.getUser_id());
			stmt.setInt(4, modelTopic.getTopic_id()); // Dùng topic_id trong WHERE

			int rowsAffected = stmt.executeUpdate();

			if (rowsAffected > 0) {
				System.out.println("Đã cập nhật thành công từ vựng với ID: " + modelTopic.getTopic_id());
			} else {
				System.out.println("Không tìm thấy từ vựng với ID: " + modelTopic.getTopic_id());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean deleteById(int topicId) {
	    String sql = "DELETE FROM tbl_Topic WHERE topic_id = ?";

	    try (Connection con = DatabaseConnection.getInstance().getConnection();
	         PreparedStatement stmt = con.prepareStatement(sql)) {

	        // Kiểm tra nếu kết nối bị null hoặc đã đóng
	        if (con == null || con.isClosed()) {
	            System.err.println("Kết nối cơ sở dữ liệu đã bị đóng.");
	            return false;
	        }

	        stmt.setInt(1, topicId);
	        int rowsAffected = stmt.executeUpdate();

	        if (rowsAffected > 0) {
	            System.out.println("Đã xóa thành công topic với ID: " + topicId);
	            return true;
	        } else {
	            System.out.println("Không tìm thấy topic với ID: " + topicId);
	            return false;
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}

}

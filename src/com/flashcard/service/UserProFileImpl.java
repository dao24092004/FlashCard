package com.flashcard.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;

import com.flashcard.connection.DatabaseConnection;
import com.flashcard.model.ModelUserProFile;

public class UserProFileImpl implements ServiceUserProfile {

	@Override
	public ModelUserProFile getAllById(int userId) {
		ModelUserProFile data = null;
		String sql = "SELECT * FROM tbl_user_profile WHERE user_id = ?";
		
		try(Connection con = DatabaseConnection.getInstance().getConnection();
				PreparedStatement stmt = con.prepareStatement(sql)) {
			stmt.setInt(1, userId);
			try(ResultSet rs = stmt.executeQuery()) {
				while(rs.next()) {
					 String fullName = rs.getString("full_name");    // Tên đầy đủ
			//	     Date dob;           // Ngày sinh
				     String phone = rs.getString("phone");       // Số điện thoại
				     String address =  rs.getString("address");     // Địa chỉ
				     String bio = rs.getString("bio");         // Thông tin tiểu sử
				     String profilePicture = rs.getString("profile_picture");
				     
				     data = new ModelUserProFile(fullName,phone,address,bio,profilePicture);
				}
				
			} catch (Exception e) {
				// TODO: handle exception
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return data;
	}

	@Override
	public void saveProfile(ModelUserProFile modelUserProFile,int userid) {
		String sql = "UPDATE tbl_user_profile SET user_id = ?, full_name = ?,phone = ?,address = ?,bio= ?,profile_picture = ? WHERE user_id = ?)";
		
		try(Connection con = DatabaseConnection.getInstance().getConnection();
				PreparedStatement stmt = con.prepareStatement(sql)) {
			
			stmt.setInt(1, modelUserProFile.getUserId());
			stmt.setString(2, modelUserProFile.getFullName());
			stmt.setString(3, modelUserProFile.getPhone());
			stmt.setString(4, modelUserProFile.getAddress());
			stmt.setString(5, modelUserProFile.getBio());
			stmt.setString(6, modelUserProFile.getProfilePicture());
			int row = stmt.executeUpdate();
			
			if(row > 0) {
				System.out.println("Cập nhật thông tin cá nhân thành công ");
			}
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	

}

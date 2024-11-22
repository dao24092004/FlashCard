package com.flashcard.model;

import java.util.Date;

public class ModelUserProFile {
	private int profileId;      // ID hồ sơ người dùng (khóa chính)
    private int userId;         // ID người dùng (khóa ngoại)
    private String fullName;    // Tên đầy đủ
    private Date dob;           // Ngày sinh
    private String phone;       // Số điện thoại
    private String address;     // Địa chỉ
    private String bio;         // Thông tin tiểu sử
    private String profilePicture; // Đường dẫn ảnh đại diện
	public ModelUserProFile() {
		super();
		// TODO Auto-generated constructor stub
	}
	public ModelUserProFile(int profileId, int userId, String fullName, Date dob, String phone, String address,
			String bio, String profilePicture) {
		super();
		this.profileId = profileId;
		this.userId = userId;
		this.fullName = fullName;
		this.dob = dob;
		this.phone = phone;
		this.address = address;
		this.bio = bio;
		this.profilePicture = profilePicture;
	}
	
	public ModelUserProFile(String fullName, Date dob, String phone, String address, String bio,
			String profilePicture) {
		super();
		this.fullName = fullName;
		this.dob = dob;
		this.phone = phone;
		this.address = address;
		this.bio = bio;
		this.profilePicture = profilePicture;
	}
	
	
	public ModelUserProFile(int userId, String fullName,  String phone, String address, String bio,
			String profilePicture) {
		super();
		this.userId = userId;
		this.fullName = fullName;
		
		this.phone = phone;
		this.address = address;
		this.bio = bio;
		this.profilePicture = profilePicture;
	}
	public ModelUserProFile(String fullName, String phone, String address, String bio, String profilePicture) {
		super();
		this.fullName = fullName;
		this.phone = phone;
		this.address = address;
		this.bio = bio;
		this.profilePicture = profilePicture;
	}
	public int getProfileId() {
		return profileId;
	}
	public void setProfileId(int profileId) {
		this.profileId = profileId;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public Date getDob() {
		return dob;
	}
	public void setDob(Date dob) {
		this.dob = dob;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getBio() {
		return bio;
	}
	public void setBio(String bio) {
		this.bio = bio;
	}
	public String getProfilePicture() {
		return profilePicture;
	}
	public void setProfilePicture(String profilePicture) {
		this.profilePicture = profilePicture;
	}
    
    
    

}

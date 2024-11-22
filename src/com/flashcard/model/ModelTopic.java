package com.flashcard.model;

public class ModelTopic {
	
	private int topic_id;
	private int user_id;
	private String topicName;
	private String description;
	public ModelTopic() {
		super();
		// TODO Auto-generated constructor stub
	}
	public ModelTopic(int topic_id,int user_id, String topicName, String description) {
		super();
		this.topic_id = topic_id;
		this.user_id = user_id;
		this.topicName = topicName;
		this.description = description;
	} 
	
	public ModelTopic(int user_id, String topicName, String description) {
		super();
		this.user_id = user_id;
		this.topicName = topicName;
		this.description = description;
	} 
	
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public int getTopic_id() {
		return topic_id;
	}
	public void setTopic_id(int topic_id) {
		this.topic_id = topic_id;
	}
	public String getTopicName() {
		return topicName;
	}
	public void setTopicName(String topicName) {
		this.topicName = topicName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	

}

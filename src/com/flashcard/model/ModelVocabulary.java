package com.flashcard.model;

import java.util.Date;

public class ModelVocabulary {

	private int vocabId;
	private int userId;
	private String word;
	private String meaning;
	private String example;
	private int topic_id;
	private ModelTopic topic;
	private String difficulty;
	private Date createdAt;
	private Date updatedAt;
	public ModelVocabulary() {
		super();
		// TODO Auto-generated constructor stub
	}
	public ModelVocabulary(int vocabId, int userId, String word, String meaning, String example, int topic_id,
			String difficulty, Date createdAt, Date updatedAt) {
		super();
		this.vocabId = vocabId;
		this.userId = userId;
		this.word = word;
		this.meaning = meaning;
		this.example = example;
		this.topic_id = topic_id;
		this.difficulty = difficulty;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}
	public int getVocabId() {
		return vocabId;
	}
	public void setVocabId(int vocabId) {
		this.vocabId = vocabId;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getWord() {
		return word;
	}
	public void setWord(String word) {
		this.word = word;
	}
	public String getMeaning() {
		return meaning;
	}
	public void setMeaning(String meaning) {
		this.meaning = meaning;
	}
	public String getExample() {
		return example;
	}
	public void setExample(String example) {
		this.example = example;
	}
	
	public String getDifficulty() {
		return difficulty;
	}
	public void setDifficulty(String difficulty) {
		this.difficulty = difficulty;
	}
	public Date getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	public Date getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}
	public ModelVocabulary(int vocabId, int userId, String word, String meaning, String example, int topic_id,
			String difficulty) {
		super();
		this.vocabId = vocabId;
		this.userId = userId;
		this.word = word;
		this.meaning = meaning;
		this.example = example;
		this.topic_id = topic_id;
		this.difficulty = difficulty;
	}
	public ModelVocabulary(int userId, String word, String meaning, String example, int topic_id, String difficulty) {
		super();
		this.userId = userId;
		this.word = word;
		this.meaning = meaning;
		this.example = example;
		this.topic_id = topic_id;
		this.difficulty = difficulty;
	}
	public int getTopic_id() {
		return topic_id;
	}
	public void setTopic_id(int topic_id) {
		this.topic_id = topic_id;
	}
	public ModelTopic getTopic() {
		return topic;
	}
	public void setTopic(ModelTopic topic) {
		this.topic = topic;
	}
	public ModelVocabulary(int vocabId, int userId, String word, String meaning, String example, int topic_id,
			ModelTopic topic, String difficulty, Date createdAt, Date updatedAt) {
		super();
		this.vocabId = vocabId;
		this.userId = userId;
		this.word = word;
		this.meaning = meaning;
		this.example = example;
		this.topic_id = topic_id;
		this.topic = topic;
		this.difficulty = difficulty;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}
	
	

}

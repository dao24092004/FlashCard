package com.flashcard.service;

import java.util.List;

import com.flashcard.model.ModelTopic;
import com.flashcard.model.ModelVocabulary;

public interface ServiceVocabulary {
	public List<Object[]> loadVocabularyData(int userId, String sortByWord, String topicName, String difficulty);
	public void insertVocab(ModelVocabulary modelVocabulary);
	public boolean deleteById(int vocabularyId);
	public void update(ModelVocabulary modelVocabulary);
	public ModelVocabulary findById(int vocab_id);
	public List<ModelTopic> getTopicByUserId(int userId);
	public List<Object[]> findVocabularyByKey(int userId,String name);
	
}

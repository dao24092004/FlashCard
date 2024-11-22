package com.flashcard.service;

import java.util.List;
import java.util.Set;

import com.flashcard.model.ModelTopic;
import com.flashcard.model.ModelVocabulary;

public interface ServiceTopic {
	public Set<Object[]> getAll(int userId);
	public void insertTopic(ModelTopic modelTopic);
	public ModelTopic findById(int topicId);
	public void updateTopic(ModelTopic modelTopic);
	public boolean deleteById(int topicId);
}

package com.flashcard.service;

import com.flashcard.model.ModelUserProFile;

public interface ServiceUserProfile {
	public ModelUserProFile getAllById(int userId);
	public void saveProfile(ModelUserProFile modelUserProFile,int userId);

}

package com.stacko.capmatch.Services;

import org.springframework.web.multipart.MultipartFile;

import com.stacko.capmatch.Models.User;

public interface StorageService {

//	void init();

	//void store(MultipartFile file);

	//Stream<Path> loadAll();

	//Path load(String filename);

	//Resource loadAsResource(String filename);

	//void deleteAll();

	void removeProfilePhoto(User user);

	void storeProfilePhoto(MultipartFile file, User user);

	void removeCV(User user);

	void storeCV(MultipartFile cv, User user);

	void store(MultipartFile file, String filename, String directory);

}
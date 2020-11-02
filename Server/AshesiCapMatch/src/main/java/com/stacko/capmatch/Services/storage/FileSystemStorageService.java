package com.stacko.capmatch.Services.storage;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import com.stacko.capmatch.Configuration.StorageConfig;
import com.stacko.capmatch.Models.User;
import com.stacko.capmatch.Repositories.UserRepository;
import com.stacko.capmatch.Services.DataValidationService;
import com.stacko.capmatch.Services.StorageService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FileSystemStorageService implements StorageService {

	private final Path rootLocation;
	
	StorageConfig storageConfig;
	
	@Autowired
	DataValidationService validationService;
	
	@Autowired
	UserRepository userRepo;
	
	@Autowired
	public FileSystemStorageService(StorageConfig storageConfig) {
		this.storageConfig = storageConfig;
		System.err.println(String.format("\n\n %s \n\n", storageConfig));
		this.rootLocation = Paths.get(storageConfig.getLocation());
		//this.rootLocation = Paths.get(URI.create(storageConfig.getLocation()));
	}

	@Override
	public void store(MultipartFile file, String filename, String directory) {
		//String filename = StringUtils.cleanPath(file.getOriginalFilename());
		try {
			if (file.isEmpty()) {
				throw new StorageException("Failed to store empty file " + filename);
			}
			if (filename.contains("..")) {
				// This is a security check
				throw new StorageException(
						"Cannot store file with relative path outside current directory "
								+ filename);
			}
			try (InputStream inputStream = file.getInputStream()) {
				Files.copy(inputStream, this.rootLocation.resolve(directory).resolve(filename),
					StandardCopyOption.REPLACE_EXISTING);
			}
		}
		catch (IOException e) {
			throw new StorageException("Failed to store file " + filename, e);
		}
	}

//	@Override
//	public Stream<Path> loadAll() {
//		try {
//			return Files.walk(this.rootLocation, 1)
//				.filter(path -> !path.equals(this.rootLocation))
//				.map(this.rootLocation::relativize);
//		}
//		catch (IOException e) {
//			throw new StorageException("Failed to read stored files", e);
//		}
//
//	}
//
//	@Override
//	public Path load(String filename) {
//		return rootLocation.resolve(filename);
//	}
//
//	@Override
//	public Resource loadAsResource(String filename) {
//		try {
//			Path file = load(filename);
//			Resource resource = new UrlResource(file.toUri());
//			if (resource.exists() || resource.isReadable()) {
//				return resource;
//			}
//			else {
//				throw new StorageFileNotFoundException(
//						"Could not read file: " + filename);
//
//			}
//		}
//		catch (MalformedURLException e) {
//			throw new StorageFileNotFoundException("Could not read file: " + filename, e);
//		}
//	}

//	@Override
//	public void deleteAll() {
//		FileSystemUtils.deleteRecursively(rootLocation.toFile());
//	}
//
//	@Override
//	public void init() {
//		try {
//			Files.createDirectories(rootLocation);
//		}
//		catch (IOException e) {
//			throw new StorageException("Could not initialize storage", e);
//		}
//	}

	@Override
	public void removeProfilePhoto(User user) {
		if (user == null || user.getProfilePhoto() == null) return;
		
		Path photo = this.rootLocation.resolve(storageConfig.getProfilePhotoDirectory()).resolve(user.getProfilePhoto());
		
		try {
			FileSystemUtils.deleteRecursively(photo);
		} catch (IOException e) {
			log.error(String.format("Removing profile photo '%s' for user '%s' failed!", user.getProfilePhoto(), user.getName()));
			e.printStackTrace();
		}
		
		user.setProfilePhoto(null);
		userRepo.save(user);
	}

	
	/**
	 * 
	 */
	@Override
	public void storeProfilePhoto(MultipartFile file, User user) {
		//Assumes the cv format has already been validated
		if (file == null) return;
		
		if (user.getProfilePhoto() != null)			// Remove existing
			removeProfilePhoto(user);
		
		String fileName = validationService.generateFileName(user, "photo", "jpg");			// Generate filename
		store(file, fileName, storageConfig.getProfilePhotoDirectory());
		
		user.setProfilePhoto(fileName);
		userRepo.save(user);
	}

	
	/**
	 * 
	 */
	@Override
	public void removeCV(User user) {
		if (user == null || user.getCV() == null) return;
		
		Path cv = this.rootLocation.resolve(storageConfig.getCVDirectory()).resolve(user.getCV());
		
		try {
			FileSystemUtils.deleteRecursively(cv);
		} catch (IOException e) {
			log.error(String.format("Removing CV '%s' for user '%s' failed!", user.getCV(), user.getName()));
			e.printStackTrace();
			return;
		}
		
		user.setCV(null); 			// Reset CV to null
		userRepo.save(user);
	}

	
	/**
	 * 
	 */
	@Override
	public void storeCV(MultipartFile cv, User user) {
		//Assumes the cv format has already been validated
		if (cv == null) return;
		
		if (user.getCV() != null)			// Before storing CV remove existing one
			removeCV(user);
		
		String fileName = validationService.generateFileName(user, "cv", "pdf");
		store(cv, fileName, storageConfig.getCVDirectory());
		
		user.setCV(fileName);
		userRepo.save(user);
	}
}
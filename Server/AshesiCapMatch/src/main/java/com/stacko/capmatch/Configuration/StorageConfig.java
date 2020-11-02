package com.stacko.capmatch.Configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@ConfigurationProperties(prefix="app.storage.config")
public class StorageConfig {
	
	/**
	 * Folder location for storing files
	 */
	@Getter
	@Setter
	private String location = "C:\\Users\\banah\\Desktop\\Mock_Store";
	
	@Getter
	private String profilePhotoDirectory = "ProfilePhotos";
	
	@Getter
	private String CVDirectory = "CVs";
	
	@Getter
	@Setter
	private String remoteStorageBasepath = "storageBasePath";
	

}

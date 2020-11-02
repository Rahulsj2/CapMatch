package com.stacko.capmatch.Services.storage;

public class StorageFileNotFoundException extends StorageException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8717018651690329661L;

	public StorageFileNotFoundException(String message) {
		super(message);
	}

	public StorageFileNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
}
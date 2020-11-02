package com.stacko.capmatch.Services.storage;

public class StorageException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7709050807784599549L;

	public StorageException(String message) {
		super(message);
	}

	public StorageException(String message, Throwable cause) {
		super(message, cause);
	}
}
package com.gzwanhong.utils;

public class WhException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public WhException() {
		super();
	}

	public WhException(String message, Throwable cause) {
		super(message, cause);
	}

	public WhException(String message) {
		super(message);
	}

	public WhException(Throwable cause) {
		super(cause);
	}

}

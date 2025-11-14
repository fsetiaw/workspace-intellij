package com.divillafajar.app.pos.pos_app_sini.exception.category;

import java.io.Serial;

public class CategoryNotEmptyException extends RuntimeException {


	@Serial
	private static final long serialVersionUID = -5438001648991282902L;

	public CategoryNotEmptyException(String message) {
		super(message);
	}

	public CategoryNotEmptyException(String message, Throwable cause) {
		super(message, cause);
	}

	public CategoryNotEmptyException(Throwable cause) {
		super(cause);
	}
}

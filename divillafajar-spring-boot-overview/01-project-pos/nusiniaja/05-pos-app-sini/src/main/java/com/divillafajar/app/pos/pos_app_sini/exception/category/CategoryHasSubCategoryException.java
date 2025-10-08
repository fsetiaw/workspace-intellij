package com.divillafajar.app.pos.pos_app_sini.exception.category;

import java.io.Serial;

public class CategoryHasSubCategoryException extends RuntimeException {


	@Serial
	private static final long serialVersionUID = -5438001648991282902L;

	public CategoryHasSubCategoryException(String message) {
		super(message);
	}

	public CategoryHasSubCategoryException(String message, Throwable cause) {
		super(message, cause);
	}

	public CategoryHasSubCategoryException(Throwable cause) {
		super(cause);
	}
}

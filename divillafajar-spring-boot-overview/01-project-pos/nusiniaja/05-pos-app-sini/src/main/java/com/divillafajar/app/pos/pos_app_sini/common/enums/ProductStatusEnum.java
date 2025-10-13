package com.divillafajar.app.pos.pos_app_sini.common.enums;

public enum ProductStatusEnum {
	ACTIVE("#198754"),   // Bootstrap .text-success / bg-success (green)
	INACTIVE("#6c757d"), // Bootstrap .text-secondary / bg-secondary (gray)
	DRAFT("#ffc107"),    // Bootstrap .text-warning / bg-warning (yellow)
	ARCHIVED("#dc3545"); // Bootstrap .text-danger / bg-danger (red)

	private final String color;

	ProductStatusEnum(String color) {
		this.color = color;
	}

	public String getColor() {
		return color;
	}
}


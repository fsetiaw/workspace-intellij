package com.divillafajar.app.pos.pos_app_sini.common.enums;

public enum BootstrapColorEnum {
	PRIMARY_BLUE("#0d6efd"),
	SECONDARY_GREY("#6c757d"),
	SUCCESS("#198754"),//hijau
	DANGER("#dc3545"),//metah
	WARNING("#ffc107"),//yellow ke orangean
	INFO("#0dcaf0"),//biru muda
	LIGHT("#f8f9fa"),//putih keabu2an
	DARK("#212529"),//abu tua
	WHITE("#ffffff"),
	BLACK("#000000"),
	ORANGE("#fd7e14");

	private final String hexCode;

	BootstrapColorEnum(String hexCode) {
		this.hexCode = hexCode;
	}

	public String getHexCode() {
		return hexCode;
	}

	/**
	 * Optional helper — kembalikan class CSS bootstrap.
	 * Contoh: DANGER → "text-danger" atau "bg-danger"
	 */
	public String toTextClass() {
		return "text-" + this.name().toLowerCase();
	}

	public String toBgClass() {
		return "bg-" + this.name().toLowerCase();
	}
}


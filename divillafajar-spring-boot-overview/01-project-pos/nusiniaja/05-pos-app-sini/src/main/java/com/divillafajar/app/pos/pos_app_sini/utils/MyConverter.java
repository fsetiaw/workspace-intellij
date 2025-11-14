package com.divillafajar.app.pos.pos_app_sini.utils;

import org.springframework.stereotype.Component;

@Component
public class MyConverter {
	public static boolean longToBoolean(Long value) {
		return value != null && value != 0L;
	}
}

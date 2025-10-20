package com.divillafajar.app.pos.pos_app_sini.global;

import com.divillafajar.app.pos.pos_app_sini.common.enums.BootstrapColorEnum;
import com.divillafajar.app.pos.pos_app_sini.config.properties.CustomDefaultProperties;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class AppGlobals {

	private final CustomDefaultProperties customDefaultProperties;
	private final Map<String, Object> globals = new ConcurrentHashMap<>();

	@PostConstruct
	public void init() {
		// Copy default dari file properties
		globals.put("colorPrimaryBlue", BootstrapColorEnum.PRIMARY_BLUE.getHexCode());
        globals.put("colorSecondaryGrey", BootstrapColorEnum.SECONDARY_GREY.getHexCode());
        globals.put("colorDanger", BootstrapColorEnum.DANGER.getHexCode());
        globals.put("colorWarning", BootstrapColorEnum.WARNING.getHexCode());
        globals.put("colorInfo", BootstrapColorEnum.INFO.getHexCode());

		System.out.println("[AppGlobals] Default values loaded from application.properties="+BootstrapColorEnum.PRIMARY_BLUE.getHexCode());
	}

	public Object get(String key) {
		return globals.get(key);
	}

	public void set(String key, Object value) {
		globals.put(key, value);
	}

	public Map<String, Object> getAll() {
		return globals;
	}
}
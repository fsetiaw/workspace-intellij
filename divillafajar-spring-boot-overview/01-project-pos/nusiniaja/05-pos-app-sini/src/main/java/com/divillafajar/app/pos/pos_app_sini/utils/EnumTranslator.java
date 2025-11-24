package com.divillafajar.app.pos.pos_app_sini.utils;

import com.divillafajar.app.pos.pos_app_sini.common.enums.LineOfBusinessEnum;
import lombok.NoArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@NoArgsConstructor
public class EnumTranslator {

	private MessageSource messageSource;

	public String getTranslated(LineOfBusinessEnum lob, Locale locale) {
		return messageSource.getMessage(lob.getI18nKey(), null, locale);
		//getTranslated(LineOfBusinessEnum.ACCOMODATION, Locale.getDefault()); --cara manggilnya
	}
}

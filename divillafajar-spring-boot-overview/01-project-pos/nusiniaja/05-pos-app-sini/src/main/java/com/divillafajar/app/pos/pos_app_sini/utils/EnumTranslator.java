package com.divillafajar.app.pos.pos_app_sini.utils;

import com.divillafajar.app.pos.pos_app_sini.common.enums.LineOfBusinessEnum;
import com.divillafajar.app.pos.pos_app_sini.common.interfaces.TranslatableEnum;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Component
@RequiredArgsConstructor
public class EnumTranslator {

	private final MessageSource messageSource;

	public String translate(TranslatableEnum lob) {
        Locale locale = LocaleContextHolder.getLocale();
		return messageSource.getMessage(lob.getI18nKey(), null, locale);
		//getTranslated(LineOfBusinessEnum.ACCOMODATION, Locale.getDefault()); --cara manggilnya
	}

    public List<String> translateAll(Class<? extends TranslatableEnum> enumClass) {
        return Arrays.stream(enumClass.getEnumConstants())
                .map(this::translate)
                .toList();
    }
}

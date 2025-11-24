package com.divillafajar.app.pos.pos_app_sini.common.enums;

import com.divillafajar.app.pos.pos_app_sini.common.interfaces.TranslatableEnum;

public enum LineOfBusinessEnum implements TranslatableEnum {
    ACCOMODATION("label.FBLocationCategory.accommodation"),
    FOODBAVERAGES("label.FBLocationCategory.fb");

    private final String i18nKey;

    LineOfBusinessEnum(String i18nKey) {
        this.i18nKey = i18nKey;
    }

    @Override
    public String getI18nKey() {
        return i18nKey;
    }
}


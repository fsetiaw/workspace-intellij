package com.divillafajar.app.pos.pos_app_sini.common.enums;

public enum LineOfBusinessEnum {
    ACCOMODATION("label.FBLocationCategory.accomodation"),
    FOODBAVERAGES("label.FBLocationCategory.fb");

    private final String i18nKey;

    LineOfBusinessEnum(String i18nKey) {
        this.i18nKey = i18nKey;
    }

    public String getI18nKey() {
        return i18nKey;
    }
}


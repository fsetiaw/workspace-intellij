package com.divillafajar.app.pos.pos_app_sini.common.enums;

import com.divillafajar.app.pos.pos_app_sini.common.interfaces.TranslatableEnum;

public enum AccommodationEntertainmentFacilities implements TranslatableEnum {
    SMARTTV("label.facility.accommodation.general.smartTv"),
    KARAOKE("label.facility.accommodation.general.karaoke"),
    BOARDGAME("label.facility.accommodation.general.boardGame");

    private final String i18nKey;

    AccommodationEntertainmentFacilities(String i18nKey) {
        this.i18nKey = i18nKey;
    }

    @Override
    public String getI18nKey() {
        return i18nKey;
    }
}


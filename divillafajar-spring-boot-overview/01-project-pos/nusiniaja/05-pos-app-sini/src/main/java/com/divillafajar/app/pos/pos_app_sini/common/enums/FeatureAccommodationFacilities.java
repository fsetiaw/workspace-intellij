package com.divillafajar.app.pos.pos_app_sini.common.enums;

import com.divillafajar.app.pos.pos_app_sini.common.interfaces.TranslatableEnum;

public enum FeatureAccommodationFacilities implements TranslatableEnum {
    HOMETHEATHER("label.facility.accommodation.entertainment.homeTheater"),
    SMARTTV("label.facility.accommodation.general.smartTv"),
    KARAOKE("label.facility.accommodation.general.karaoke"),
    BALCONY("label.facility.accommodation.general.balcony"),
    ROOFTOP("label.facility.accommodation.feature.roofTop"),
	PRIVATEPOOL("label.facility.accommodation.entertainment.pool.private"),
    PRIVATEWHIRLPOOL("label.facility.accommodation.bathroom.whirlpool.private");

    private final String i18nKey;

    FeatureAccommodationFacilities(String i18nKey) {
        this.i18nKey = i18nKey;
    }

    @Override
    public String getI18nKey() {
        return i18nKey;
    }
}


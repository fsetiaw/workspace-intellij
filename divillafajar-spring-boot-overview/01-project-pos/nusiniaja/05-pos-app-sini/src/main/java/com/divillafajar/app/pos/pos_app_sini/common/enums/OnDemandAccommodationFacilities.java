package com.divillafajar.app.pos.pos_app_sini.common.enums;

import com.divillafajar.app.pos.pos_app_sini.common.interfaces.TranslatableEnum;

public enum OnDemandAccommodationFacilities implements TranslatableEnum {

	HAIRDRYER("label.facility.accommodation.general.hairdryer"),
    WATERREFILL("label.facility.accommodation.ondemand.waterRefill");

    private final String i18nKey;

    OnDemandAccommodationFacilities(String i18nKey) {
        this.i18nKey = i18nKey;
    }

    @Override
    public String getI18nKey() {
        return i18nKey;
    }
}


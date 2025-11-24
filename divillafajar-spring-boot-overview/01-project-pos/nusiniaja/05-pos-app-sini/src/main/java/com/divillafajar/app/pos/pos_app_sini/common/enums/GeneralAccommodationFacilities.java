package com.divillafajar.app.pos.pos_app_sini.common.enums;

import com.divillafajar.app.pos.pos_app_sini.common.interfaces.TranslatableEnum;

public enum GeneralAccommodationFacilities implements TranslatableEnum {
    AC("label.facility.accommodation.general.ac"),
    WATERHEATER("label.facility.accommodation.general.waterHeater"),
    WIFI("label.facility.accommodation.general.wifi"),
    SMARTTV("label.facility.accommodation.general.smartTv"),
    BOARDGAME("label.facility.accommodation.general.boardGame"),
    FRIDGE("label.facility.accommodation.general.fridge"),
    SAFETYBOX("label.facility.accommodation.general.safetyBox"),
    KETTLE("label.facility.accommodation.general.kettle"),
    CLOSET("label.facility.accommodation.general.closet"),
    WORKAREA("label.facility.accommodation.general.workArea");

    private final String i18nKey;

    GeneralAccommodationFacilities(String i18nKey) {
        this.i18nKey = i18nKey;
    }

    @Override
    public String getI18nKey() {
        return i18nKey;
    }
}


package com.divillafajar.app.pos.pos_app_sini.common.enums;

import com.divillafajar.app.pos.pos_app_sini.common.interfaces.TranslatableEnum;

public enum GeneralAccommodationFacilities implements TranslatableEnum {
    WIFI("label.facility.accommodation.general.wifi"),
    FRIDGE("label.facility.accommodation.general.fridge"),
    SAFETYBOX("label.facility.accommodation.general.safetyBox"),
    KETTLE("label.facility.accommodation.general.kettle"),
    CLOSET("label.facility.accommodation.general.closet"),
    HAIRDRYER("label.facility.accommodation.general.hairdryer"),
    HAIRDRYERONDEMAND("label.facility.accommodation.general.onDemand.hairdryer"),
    PRIVATEPOOL("label.facility.accommodation.entertainment.pool.private"),
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


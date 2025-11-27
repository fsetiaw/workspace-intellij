package com.divillafajar.app.pos.pos_app_sini.common.enums;

import com.divillafajar.app.pos.pos_app_sini.common.interfaces.TranslatableEnum;

public enum GeneralAccommodationFacilities implements TranslatableEnum {
	AC("label.facility.accommodation.general.ac"),
	FAN("label.facility.accommodation.general.fan"),
	FRIDGE("label.facility.accommodation.general.fridge"),
	WIFI("label.facility.accommodation.general.wifi"),
    SAFETYBOX("label.facility.accommodation.general.safetyBox"),
    KETTLE("label.facility.accommodation.general.kettle"),
    CLOSET("label.facility.accommodation.general.closet"),
	TV("label.facility.accommodation.general.tv"),
    WORKAREA("label.facility.accommodation.general.workArea"),
	PRIVATEBATHROOM("label.facility.accommodation.bathroom.private"),
	SHAREDBATHROOM("label.facility.accommodation.bathroom.shared"),
	TERRACE("label.facility.accommodation.general.teras"),
	HAIRDRYER("label.facility.accommodation.general.hairdryer"),
	WATERREFILL("label.facility.accommodation.ondemand.waterRefill");



    private final String i18nKey;

    GeneralAccommodationFacilities(String i18nKey) {
        this.i18nKey = i18nKey;
    }

    @Override
    public String getI18nKey() {
        return i18nKey;
    }
}


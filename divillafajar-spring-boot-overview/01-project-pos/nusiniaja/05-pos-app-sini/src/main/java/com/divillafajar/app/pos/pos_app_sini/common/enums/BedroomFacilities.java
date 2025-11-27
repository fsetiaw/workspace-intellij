package com.divillafajar.app.pos.pos_app_sini.common.enums;

import com.divillafajar.app.pos.pos_app_sini.common.interfaces.TranslatableEnum;

public enum BedroomFacilities implements TranslatableEnum {
	AC("label.facility.accommodation.general.ac"),
	BALCONY("label.facility.accommodation.general.balcony"),
	TERRACE("label.facility.accommodation.general.teras"),
	FAN("label.facility.accommodation.general.fan"),
	PRIVATEBATHROOM("label.facility.accommodation.bathroom.private"),
	SMARTTV("label.facility.accommodation.general.smartTv"),
	TV("label.facility.accommodation.general.tv"),
	OUTSIDEVIEW("label.facility.accommodation.view.outside");

    private final String i18nKey;

    BedroomFacilities(String i18nKey) {
        this.i18nKey = i18nKey;
    }

    @Override
    public String getI18nKey() {
        return i18nKey;
    }
}


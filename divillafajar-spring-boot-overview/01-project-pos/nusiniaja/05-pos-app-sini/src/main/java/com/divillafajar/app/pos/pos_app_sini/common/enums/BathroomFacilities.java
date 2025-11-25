package com.divillafajar.app.pos.pos_app_sini.common.enums;

import com.divillafajar.app.pos.pos_app_sini.common.interfaces.TranslatableEnum;

public enum BathroomFacilities implements TranslatableEnum {
    PRIVATEBATHROOM("label.facility.accommodation.bathroom.private"),
    SHAREDBATHROOM("label.facility.accommodation.bathroom.share"),
    BATHTUB("label.facility.accommodation.bathroom.bathtub"),
    WHIRLPOOL("label.facility.accommodation.bathroom.whirlpool"),
    SHOWER("label.facility.accommodation.bathroom.shower"),
    BUCKET("label.facility.accommodation.bathroom.bucket"),
    BIDET("label.facility.accommodation.bathroom.bidet"),
    SQUAT("label.facility.accommodation.bathroom.toilet.squat"),
    TOILET("label.facility.accommodation.bathroom.toilet.sitting");

    private final String i18nKey;

    BathroomFacilities(String i18nKey) {
        this.i18nKey = i18nKey;
    }

    @Override
    public String getI18nKey() {
        return i18nKey;
    }
}


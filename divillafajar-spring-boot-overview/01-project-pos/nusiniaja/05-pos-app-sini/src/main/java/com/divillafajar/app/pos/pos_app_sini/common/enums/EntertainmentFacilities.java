package com.divillafajar.app.pos.pos_app_sini.common.enums;

import com.divillafajar.app.pos.pos_app_sini.common.interfaces.TranslatableEnum;

public enum EntertainmentFacilities implements TranslatableEnum {

    PUBLICPOOL("label.facility.accommodation.entertainment.pool.shared"),
    BOARDGAME("label.facility.accommodation.general.boardGame"),
    BILLIARD("label.facility.accommodation.entertainment.billiard");

    private final String i18nKey;

    EntertainmentFacilities(String i18nKey) {
        this.i18nKey = i18nKey;
    }

    @Override
    public String getI18nKey() {
        return i18nKey;
    }
}


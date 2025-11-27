package com.divillafajar.app.pos.pos_app_sini.common.enums;

import com.divillafajar.app.pos.pos_app_sini.common.interfaces.TranslatableEnum;

public enum AddOnAccommodationService implements TranslatableEnum {
	BF("label.facility.accommodation.addOnService.bf"),
	BONEFIRE("label.facility.accommodation.addOnService.bonFireWood"),
	CHARCOALGRILL("label.facility.accommodation.addOnService.charcoalGrill"),
	EXTRABED("label.facility.accommodation.addOnService.extraBed"),
	HAIRDRYER("label.facility.accommodation.general.hairdryer"),
	WATERREFILL("label.facility.accommodation.ondemand.waterRefill"),
    GRILLSET("label.facility.accommodation.addOnService.charcoalGrill");

    private final String i18nKey;

    AddOnAccommodationService(String i18nKey) {
        this.i18nKey = i18nKey;
    }

    @Override
    public String getI18nKey() {
        return i18nKey;
    }
}


package com.divillafajar.app.pos.pos_app_sini.io.entity.client.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ClientAddressDTO {
    private String id;
    private String pubId;
    private String locationCategory;
    private String guestCapacity;
    private String addressName;
    private String addressNickname;
    private String addressLine1;
    private String addressLine2;
    private String village;
    private String subDistrict;
    private String city;
    private String province;
    private String postalCode;
    private String country;
    private Boolean active;
    private String phone;
    private String email;
	//=======meta data ==============
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private String createdBy;
	private String updatedBy;
	private Boolean deleted;
    private Boolean usedDefaultCategory;
	private Boolean usedDefaultArea;
}

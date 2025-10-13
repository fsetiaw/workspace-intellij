package com.divillafajar.app.pos.pos_app_sini.io.entity.address.dto;

import com.divillafajar.app.pos.pos_app_sini.io.entity.user.UserEntity;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressDTO {
    private String pubId;
    private UserEntity user;
    private String addressName;
    private String addressNickname;
    private String recipientName;
    private String addressPhoneNumber;
    private String addressLine1;
    private String addressLine2;
    private String village;
    private String subDistrict;
    private String city;
    private String province;
    private String postalCode;
    private String country;
	//=======meta data ==============
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
	private String createdBy;
	private String updatedBy;
	private Boolean deleted;
}

package com.divillafajar.app.pos.pos_app_sini.io.entity.client.area.dto;

import com.divillafajar.app.pos.pos_app_sini.io.entity.client.ClientAddressEntity;
import com.divillafajar.app.pos.pos_app_sini.io.entity.client.area.GuestTableEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Time;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClientAreaDTO {

    private Long id;
    private String pubId;
    private String areaName;
    private String alias;
    private String location;
    private String reservationType;
    private String coolingSystem;
    private String room_type;
    private String roomFunction;
    private LocalTime startingOperationTime;
    private Long durationOperationHour;
    private List<GuestTableEntity> tables;
    private ClientAddressEntity clientAddress;
	//=======meta data ==============
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private String createdBy;
	private String updatedBy;
	private Boolean deleted;
}

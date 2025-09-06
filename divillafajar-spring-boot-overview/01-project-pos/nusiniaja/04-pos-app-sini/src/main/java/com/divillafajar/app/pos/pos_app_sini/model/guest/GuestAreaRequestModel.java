package com.divillafajar.app.pos.pos_app_sini.model.guest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Time;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GuestAreaRequestModel {
    private String addressId;
    private String areaName;     // input: name
    private String alias;    // input: alias
    private String location;
    private String coolingSystem;
    private String roomType;
    private String reservationType;
    private String roomFunction;
    private LocalTime startingOperationTime;
    private Long durationOperationHour;
    private Integer pax1;
    private Integer pax2;
    private Integer pax3;
    private Integer pax4;
    private Integer pax5;
    private Integer pax6;
    private Integer pax7;
    private Integer pax8;
    private Integer pax9;
    private Integer pax10;
}

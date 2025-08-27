package com.divillafajar.app.pos.pos_app_sini.exception.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GenericErrorResponse {
    public GenericErrorResponse(int status, String message) {
        this.status = status;
        this.message = message;
    };
    public GenericErrorResponse(String message) {
        this.message = message;
    };
    private int status;
    private String message;
    private long timeStamp = System.currentTimeMillis();


}

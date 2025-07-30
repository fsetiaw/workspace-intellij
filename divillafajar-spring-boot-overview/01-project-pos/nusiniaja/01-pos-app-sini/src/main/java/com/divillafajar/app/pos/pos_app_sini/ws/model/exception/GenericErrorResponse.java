package com.divillafajar.app.pos.pos_app_sini.ws.model.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GenericErrorResponse {
    private int status;
    private String message;
    private long timeStamp;
}

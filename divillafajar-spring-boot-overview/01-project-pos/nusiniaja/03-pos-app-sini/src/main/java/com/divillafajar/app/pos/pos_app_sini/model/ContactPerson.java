package com.divillafajar.app.pos.pos_app_sini.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
public class ContactPerson {
    private String nama;
    private String telp;
}

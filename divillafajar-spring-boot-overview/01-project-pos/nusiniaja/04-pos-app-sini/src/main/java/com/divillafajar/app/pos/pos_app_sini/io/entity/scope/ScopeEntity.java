package com.divillafajar.app.pos.pos_app_sini.io.entity.scope;

import com.divillafajar.app.pos.pos_app_sini.io.entity.client.ClientAddressEntity;
import com.divillafajar.app.pos.pos_app_sini.io.entity.fitur.FeatureEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Entity
@Table(name = "scopes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ScopeEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = -7663717300635127733L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relasi ke ClientAddressEntity
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "client_address_id", nullable = false)
    private ClientAddressEntity clientAddress;

    // Relasi ke FeatureEntity
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "feature_id", nullable = false)
    private FeatureEntity feature;

    // Bisa tambah kolom lain sesuai kebutuhan
    @Column(name = "active", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 1")
    private Boolean active = true;

}

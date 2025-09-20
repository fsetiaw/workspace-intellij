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
import java.util.UUID;

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

    @Column(name = "pub_id", updatable = false, nullable = false)
    private String pubId;

    @PrePersist
    public void generateId() {
        if (pubId == null) {
            pubId = UUID.randomUUID().toString(); // UUID jadi string
        }
    }

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

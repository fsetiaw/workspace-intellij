package com.divillafajar.app.pos.pos_app_sini.io.entity.employee;

import com.divillafajar.app.pos.pos_app_sini.io.entity.auth.NamePassEntity;
import com.divillafajar.app.pos.pos_app_sini.io.entity.client.ClientAddressEntity;
import com.divillafajar.app.pos.pos_app_sini.io.entity.client.ClientEntity;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "EmploymentEntity")
@Table(name = "employment")
public class EmploymentEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 6246372901946257742L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "id")
    private Long id;

    @Column(name = "pub_id", updatable = false, nullable = false)
    private String pubId;

    @PrePersist
    public void generateId() {
        if (pubId == null) {
            pubId = UUID.randomUUID().toString(); // UUID jadi string
        }
    }

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private EmployeeEntity employee;

    @ManyToOne
    @JoinColumn(name = "client_address_id", nullable = false)
    private ClientAddressEntity clientAddress;

    @OneToOne(mappedBy = "employment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private NamePassEntity userAuthDetails;
}

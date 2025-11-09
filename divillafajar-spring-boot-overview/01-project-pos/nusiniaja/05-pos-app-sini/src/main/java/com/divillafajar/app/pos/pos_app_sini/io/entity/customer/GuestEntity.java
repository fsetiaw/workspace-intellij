package com.divillafajar.app.pos.pos_app_sini.io.entity.customer;

import com.divillafajar.app.pos.pos_app_sini.io.entity.auth.NamePassEntity;
import com.divillafajar.app.pos.pos_app_sini.io.entity.client.ClientEntity;
import com.divillafajar.app.pos.pos_app_sini.io.entity.order.OrderEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "GuestEntity")
@Table(name = "guest")
public class GuestEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = -2685398311222829268L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    /*
    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private ClientEntity client;
     */

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private CustomerEntity customer;

    @OneToOne(mappedBy = "guest", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private NamePassEntity userAuthDetails;

    @OneToMany(mappedBy = "guest", cascade = CascadeType.ALL)
    private List<OrderEntity> orders;

	// === 🔹 Metadata ===
	@Column(name = "created_at", insertable = false, updatable = false)
	private LocalDateTime createdAt;

	@Column(name = "updated_at", insertable = false, updatable = false)
	private LocalDateTime updatedAt;

	@Column(name = "created_by", length = 100)
	private String createdBy;

	@Column(name = "updated_by", length = 100)
	private String updatedBy;

	@Column(name = "deleted", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0")
	private Boolean deleted = false;


}

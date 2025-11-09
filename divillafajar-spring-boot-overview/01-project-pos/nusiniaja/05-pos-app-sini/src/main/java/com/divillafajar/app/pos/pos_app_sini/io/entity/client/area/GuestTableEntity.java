package com.divillafajar.app.pos.pos_app_sini.io.entity.client.area;

import com.divillafajar.app.pos.pos_app_sini.io.entity.order.OrderEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity(name = "GuestTableEntity")
@Table(name = "guest_table")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GuestTableEntity {

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

    @Column(name = "capacity", nullable = false)
    private Long capacity;

    @Column(name = "status",nullable = false)
    private String status;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE,
            CascadeType.PERSIST, CascadeType.REFRESH })
    @JoinColumn(name = "area_id")
    private ClientAreaEntity area;

    @OneToMany(mappedBy = "table", cascade = CascadeType.ALL)
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

package com.divillafajar.app.pos.pos_app_sini.repo.guest;

import com.divillafajar.app.pos.pos_app_sini.io.entity.customer.GuestEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GuestRepo extends CrudRepository<GuestEntity,Long> {
}

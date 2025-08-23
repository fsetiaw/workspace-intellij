package com.divillafajar.app.pos.pos_app_sini.repo;

import com.divillafajar.app.pos.pos_app_sini.io.entity.address.AddressEntity;
import org.springframework.data.repository.CrudRepository;

public interface AddressRepo extends CrudRepository<AddressEntity, Long> {
}

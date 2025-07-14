package com.divillafajar.disiniaja.possini;

import com.divillafajar.disiniaja.possini.dao.AppDAO;
import com.divillafajar.disiniaja.possini.entity.user.address.AddressEntity;
import com.divillafajar.disiniaja.possini.entity.user.auth.AuthorityEntity;
import com.divillafajar.disiniaja.possini.entity.user.User;
import com.divillafajar.disiniaja.possini.entity.user.detail.UserDetailEntity;
import com.divillafajar.disiniaja.possini.entity.user.UserEntity;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class PossiniApplication {

	public static void main(String[] args) {
		SpringApplication.run(PossiniApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(AppDAO appDAO) {
		return runner -> {
			//createUser(appDAO);
			//findUserById(appDAO);
			//addUserWithAddress(appDAO);
		};
	}

	private void addUserWithAddress(AppDAO appDAO) {
		//create user
		User nuUser = new User();
		nuUser.setFirstName("user17");
		nuUser.setLastName("user17");

		//comment out dibawah ini kalo cuma create user doan
		nuUser = appDAO.save(nuUser);
		System.out.println("DONE SAVING USER = "+nuUser.getFirstName());
		System.out.println("DONE SAVING USER = "+nuUser.getId());

		/* kalo mo buat user + entiti
		UserDetailEntity userDetailEntity = new UserDetailEntity();
		userDetailEntity.setYoutubeChannel("ruma1");
		userDetailEntity.setUser(nuUser);
		appDAO.save(userDetailEntity);
		 */

		List<AddressEntity> addressEntityList = new ArrayList<>();;
		AddressEntity nuAddress = new AddressEntity();
		nuAddress.setAddress_line1("Jl. H Samali");
		nuAddress.setAddress_name("home17");
		nuAddress.setCity("jakarta");
		nuAddress.setPhone_number("88899988");
		nuAddress.setPostal_code("77777");
		nuAddress.setRecipient_name("me");
		nuAddress.setProvince("DKI");
		addressEntityList.add(nuAddress);

		AddressEntity nuAddress1 = new AddressEntity();
		nuAddress1.setAddress_line1("Jl. H Samali");
		nuAddress1.setAddress_name("home18");
		nuAddress1.setCity("jakarta");
		nuAddress1.setPhone_number("88899988");
		nuAddress1.setPostal_code("77777");
		nuAddress1.setRecipient_name("me");
		nuAddress1.setProvince("DKI");
		addressEntityList.add(nuAddress1);

		appDAO.save(addressEntityList, nuUser.getId());
		//nuAddress.setUser(nuUser);
		//appDAO.save(addressEntityList);
		System.out.println("DONE SAVING");
		//create address

		//nuUser.setAddresses(addressEntityList);






		//save instructor


		System.out.println("DONE SAVING");
	}

	private void findUserById(AppDAO appDAO) {
		int id = 19;
		System.out.println("start find");
		UserDetailEntity userDetailEntity = appDAO.findUserDetailById(id);
		System.out.println("userDetail  = "+ userDetailEntity.getId());
		System.out.println("user first name  = "+ userDetailEntity.getUser().getFirstName());
	}

	private void createUser(AppDAO appDAO) {
		//create instructor detail
/*
		AuthorityEntity authorityEntity = new AuthorityEntity();
		authorityEntity.setAuthority("ROLE_ADMIN");
		authorityEntity.setUsername("user1");

		UserEntity users = new UserEntity();
		users.setUsername("user1");
		users.setEnabled(true);
		users.setPassword("{noop}user3");
		users.setAuthDetail(authorityEntity);
		//appDAO.save(users);

 */




		//create user
		User nuUser = new User();
		nuUser.setFirstName("user1");
		nuUser.setLastName("user1");

		//comment out dibawah ini kalo cuma create user doan
		//nuUser = appDAO.save(nuUser);
		System.out.println("DONE SAVING USER = "+nuUser.getFirstName());
		System.out.println("DONE SAVING USER = "+nuUser.getId());

		/* kalo mo buat user + entiti
		UserDetailEntity userDetailEntity = new UserDetailEntity();
		userDetailEntity.setYoutubeChannel("ruma1");
		userDetailEntity.setUser(nuUser);
		appDAO.save(userDetailEntity);
		 */



		System.out.println("DONE SAVING");
	}
}

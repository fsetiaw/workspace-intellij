package com.divillafajar.disiniaja.possini;

import com.divillafajar.disiniaja.possini.dao.AppDAO;
import com.divillafajar.disiniaja.possini.entity.user.auth.AuthorityEntity;
import com.divillafajar.disiniaja.possini.entity.user.User;
import com.divillafajar.disiniaja.possini.entity.user.detail.UserDetailEntity;
import com.divillafajar.disiniaja.possini.entity.user.UserEntity;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class PossiniApplication {

	public static void main(String[] args) {
		SpringApplication.run(PossiniApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(AppDAO appDAO) {
		return runner -> {
			//createUser(appDAO);
			findUserById(appDAO);
		};
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

		AuthorityEntity authorityEntity = new AuthorityEntity();
		authorityEntity.setAuthority("ROLE_ADMIN");
		authorityEntity.setUsername("user3");

		UserEntity users = new UserEntity();
		users.setUsername("user3");
		users.setEnabled(true);
		users.setPassword("{noop}user3");
		users.setAuthDetail(authorityEntity);
		//appDAO.save(users);

		UserDetailEntity userDetailEntity = new UserDetailEntity();
		userDetailEntity.setYoutubeChannel("ruma3");



		//create user
		User nuUser = new User();
		nuUser.setFirstName("user3");
		nuUser.setLastName("user3");
		nuUser.setUserDetail(userDetailEntity);
		nuUser.setUserAuthDetails(users);






		//save instructor
		appDAO.save(nuUser);

		System.out.println("DONE SAVING");
	}
}

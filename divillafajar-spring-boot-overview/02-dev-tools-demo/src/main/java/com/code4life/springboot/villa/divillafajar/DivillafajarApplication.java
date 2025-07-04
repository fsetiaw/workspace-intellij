package com.code4life.springboot.villa.divillafajar;

import com.code4life.springboot.villa.divillafajar.dao.StudentDAO;
import com.code4life.springboot.villa.divillafajar.entity.Student;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

/*
@SpringBootApplication(
		//packages bisa elaborate per packages/sub packages
		scanBasePackages = {"com.code4life"}
)

 */

@SpringBootApplication
public class DivillafajarApplication {

	public static void main(String[] args) {
		SpringApplication.run(DivillafajarApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(String[] args, StudentDAO studentDAO) {
		return runner -> {
			System.out.println("Hello Runner");
			createStudent(studentDAO);
			queryForStudent(studentDAO);
		};
	}

	private void queryForStudent(StudentDAO studentDAO) {
		List<Student> students = studentDAO.findAll();
		for(Student stu : students) {
			System.out.println(stu);
		}
	}

	private void createStudent(StudentDAO studentDAO) {
		Student nuStu = new Student("unyil", "bond","ss@ss.com");

		studentDAO.save(nuStu);
		findStudent(studentDAO);
	}

	private void findStudent(StudentDAO studentDAO) {
		studentDAO.findById(1);
	}


}

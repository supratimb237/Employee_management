package com.wings.employee_management;

import com.wings.employee_management.model.Authority;
import com.wings.employee_management.model.Employee;
import com.wings.employee_management.repository.EmployeeRepository;
import com.wings.employee_management.service.JwtService;
import io.jsonwebtoken.Jwt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Set;

@SpringBootApplication
public class EmployeeManagementApplication {


	public static void main(String[] args) {
		SpringApplication.run(EmployeeManagementApplication.class, args);
		/*Employee admin = new Employee(

				"alice",
				"admin123",
				"IT",
				Set.of(Authority.ADMIN)
		);

		Employee user = new Employee(

				"bob",
				"user123",
				"HR",
				Set.of(Authority.USER)
		);
		JwtService jwtService = new JwtService();

		String s = jwtService.generateToken(admin.getUsername());
		String sub = jwtService.getClaims(s).getSubject();
		System.out.println(s);
		System.out.println(sub);*/



	}

}

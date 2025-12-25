package com.wings.employee_management.dataloader;

import com.wings.employee_management.model.Authority;
import com.wings.employee_management.model.Employee;
import com.wings.employee_management.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
public class DataLoader implements CommandLineRunner{

    @Autowired
    EmployeeRepository repository;

   /* @Bean
    CommandLineRunner loadEmployees(EmployeeRepository repository, PasswordEncoder encoder) {
        return args -> {
            Employee admin = new Employee(
                    null,
                    "alice",
                    encoder.encode("admin123"),
                    "IT",
                    Set.of(Authority.ADMIN)
            );

            Employee user = new Employee(
                    null,
                    "bob",
                    encoder.encode("user123"),
                    "HR",
                    Set.of(Authority.USER)
            );

            repository.save(admin);
            repository.save(user);
        };
    }*/

    @Override
    public void run(String... args) throws Exception {

        Employee admin = new Employee(

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

        repository.save(admin);
        repository.save(user);

    }
}

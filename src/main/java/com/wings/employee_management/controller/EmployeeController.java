package com.wings.employee_management.controller;

import com.wings.employee_management.model.Authority;
import com.wings.employee_management.model.Employee;
import com.wings.employee_management.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
public class EmployeeController {

    @Autowired
    EmployeeRepository employeeRepository;

    @PostMapping("/api/employees")
    public Object addEmployee(@RequestBody Employee employee){
        try{
            Employee emp = new Employee(
                    employee.getUsername(),
                    employee.getPassword(),
                    employee.getDepartment(),
                    employee.getRoles()
            );

            Employee save = employeeRepository.save(emp);
            return ResponseEntity.ok(save);
        }catch (Exception e){
            return ResponseEntity.status(400).build();
        }
    }

    @GetMapping("/api/employees")
    public Object getAllEmployees(){
        try{
            List<Employee> all = employeeRepository.findAll();
            return ResponseEntity.ok(all);
        }catch (Exception e){
            return ResponseEntity.status(400).build();
        }
    }

    @GetMapping("/api/employees/{id}")
    public Object getEmployees(@PathVariable("id") int id){
        try{
            Optional<Employee> all = employeeRepository.findById(id);
            if(!all.isPresent()){
                return ResponseEntity.status(404).build();
            }
            return ResponseEntity.ok(all.get());
        }catch (Exception e){
            return ResponseEntity.status(400).build();
        }
    }


    @PutMapping("/api/employees/{id}")
    public Object updateEmployees(@PathVariable("id") int id, @RequestBody Employee employee){
        try{
            boolean employeeExists = employeeRepository.existsById(id);

            if(!employeeExists){
                return ResponseEntity.status(404).build();
            }

            Optional<Employee> byId = employeeRepository.findById(id);

            byId.get().setUsername(employee.getUsername());
            byId.get().setDepartment(employee.getDepartment());

            Employee save = employeeRepository.save(byId.get());
            return ResponseEntity.ok(save);
        }catch (Exception e){
            return ResponseEntity.status(400).build();
        }
    }


    @DeleteMapping("/api/employees/{id}")
    public Object deleteEmployees(@PathVariable("id") int id){
        try{
            boolean employeeExists = employeeRepository.existsById(id);

            if(!employeeExists){
                return ResponseEntity.status(404).build();
            }
            Optional<Employee> byId = employeeRepository.findById(id);

            employeeRepository.delete(byId.get());
            return ResponseEntity.status(200).build();
        }catch (Exception e){
            return ResponseEntity.status(400).build();
        }
    }









}

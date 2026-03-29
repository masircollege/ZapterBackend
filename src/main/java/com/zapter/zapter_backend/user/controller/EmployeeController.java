package com.zapter.zapter_backend.user.controller;

import com.zapter.zapter_backend.user.domain.Employee;
import com.zapter.zapter_backend.user.repository.EmployeeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/zapter/employee")
public class EmployeeController {

    final private EmployeeRepository employeeRepository;

    public EmployeeController(EmployeeRepository employeeRepository){
        this.employeeRepository = employeeRepository;
    }

    @PostMapping("/new")
    public ResponseEntity<?> newEmployee(@RequestBody Employee employee){
        try {
            employeeRepository.save(employee);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

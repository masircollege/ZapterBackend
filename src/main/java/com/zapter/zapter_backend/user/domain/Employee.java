package com.zapter.zapter_backend.user.domain;

import com.zapter.zapter_backend.user.enums.EmployeeRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "employee")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long eid;

    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private EmployeeRole role = EmployeeRole.SYSTEM_ADMIN;

    @Column(name = "phone_no", length = 15)
    private String phoneNumber;

}

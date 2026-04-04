package com.zapter.zapter_backend.user.service;

import com.zapter.zapter_backend.security.AdminPasswordGenerator;
import com.zapter.zapter_backend.user.domain.Admin;
import com.zapter.zapter_backend.user.domain.Employee;
import com.zapter.zapter_backend.user.enums.Role;
import com.zapter.zapter_backend.user.repository.AdminRepository;
import com.zapter.zapter_backend.user.repository.EmployeeRepository;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.*;

@Service
public class AdminService {

    private final AdminRepository adminRepository;
    private final EmployeeRepository employeeRepository;
    private final TransactionTemplate transactionTemplate;
    private final AdminPasswordGenerator adminPasswordGenerator;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender javaMailSender;

    public AdminService(
            EmployeeRepository employeeRepository,
            TransactionTemplate transactionTemplate,
            AdminRepository adminRepository,
            AdminPasswordGenerator adminPasswordGenerator,
            PasswordEncoder passwordEncoder,
            JavaMailSender javaMailSender
            ){
        this.employeeRepository = employeeRepository;
        this.transactionTemplate = transactionTemplate;
        this.adminRepository = adminRepository;
        this.adminPasswordGenerator = adminPasswordGenerator;
        this.passwordEncoder = passwordEncoder;
        this.javaMailSender = javaMailSender;
    }

    public void createAdmin(Long employeeId, Role role){
        transactionTemplate.execute(status -> {
            try {
                Admin admin = new Admin();
                admin.setAdminId(generateAdminId(employeeId));
                admin.setEmployeeId(employeeId);
                admin.setPassword(passwordEncoder.encode(adminPasswordGenerator.generatePassword()));
                admin.setRole(role);
                adminRepository.save(admin);
                sendMail(admin,employeeId);
                return null;
            } catch (Exception e) {
                status.setRollbackOnly();
                throw new RuntimeException("Admin creation failed : " + e);
            }
        });
    }

    public String generateAdminId(Long employeeId) throws NoSuchAlgorithmException {
        StringBuilder rand = new StringBuilder(); // The 4 suffix random number we wanted in the admin_id. Eg : `adm-MHAK-3248`
        SecureRandom secureRandom = SecureRandom.getInstanceStrong();
        for (int i=0; i<=4; i++){
            rand.append(secureRandom.nextInt(9));
        }
        String adminLoginId;
        StringBuilder firstName = new StringBuilder(), lastName = new StringBuilder();
        String[] fullnameObject = employeeRepository.findFirstLastNameById(employeeId);
        String fullnameString = Arrays.toString(fullnameObject);
        StringBuilder fullname = new StringBuilder(fullnameString.replaceAll("[\\[\\]]",""));
        boolean flag = false;
        for (int i=0;i<fullname.length();i++){
            String currentCharacter = String.valueOf(fullname.charAt(i));
            boolean checkDividerCharacter = currentCharacter.equals(",");

            if (checkDividerCharacter || flag){
                lastName.append(currentCharacter);
                flag = true;
            } else {
                firstName.append(currentCharacter);
            }
        }

        lastName.replace(0,1,"");
//        System.out.println("firstname : "+ firstName + " lastname : " + lastName); // This line is here in case we want to debug..
        String firstCombination = String.valueOf(firstName.charAt(0)) + String.valueOf(lastName.charAt(lastName.length()-1));
        String lastCombination = String.valueOf(firstName.charAt(1)) + String.valueOf(lastName.charAt(lastName.length()-2));
        adminLoginId = "adm-"+ firstCombination.toUpperCase() + lastCombination.toUpperCase() +"-"+ rand.toString();
        return adminLoginId;

    }

    public void setPassword(String id, String oldPassword, String password, String confirmPassword) {
        updatePassword(id, oldPassword, password, confirmPassword);
    }

    public void updatePassword(String adminId, String oldPassword, String password, String confirmPassword) {
        try {
            Optional<Admin> adminById = adminRepository.findById(adminId);
            Admin admin = adminById.orElseThrow(RuntimeException::new);
            if (oldPassword.equals(admin.getPassword())) {
                if (password.equals(confirmPassword)) {
                    admin.setPassword(passwordEncoder.encode(password));
                    adminRepository.save(admin);
                }
            }

        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    private String sendMail(Admin admin, Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId).get();
        String email = employee.getEmail();
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject("Successfully Registered! "+employee.getFirstName()+" "+employee.getLastName());
        mailMessage.setText("Here is your login credentials for the admin dashboard, " +
                "admin-id : "+admin.getAdminId()+" "+"password : "+admin.getPassword()+"(NOTE : This password is meant to be changed later)");
        javaMailSender.send(mailMessage);
        return "";
    }
}

package com.leaveapi.config;

import com.leaveapi.model.Employee;
import com.leaveapi.model.Role;
import com.leaveapi.repository.EmployeeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final EmployeeRepository employeeRepository;

    public DataInitializer(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public void run(String... args) {
        if (employeeRepository.count() == 0) {
            Employee admin = new Employee("Admin", "admin@example.com");
            admin.setRole(Role.ADMIN);
            employeeRepository.save(admin);
        }
    }
}

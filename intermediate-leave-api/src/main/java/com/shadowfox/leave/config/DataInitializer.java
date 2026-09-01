package com.shadowfox.leave.config;

import com.shadowfox.leave.model.Employee;
import com.shadowfox.leave.model.Role;
import com.shadowfox.leave.repository.EmployeeRepository;
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
            Employee admin = new Employee("Admin", "admin@shadowfox.local");
            admin.setRole(Role.ADMIN);
            employeeRepository.save(admin);
        }
    }
}

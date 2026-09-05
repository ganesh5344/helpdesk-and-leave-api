package com.leaveapi.service;

import com.leaveapi.model.Employee;
import com.leaveapi.model.Role;
import com.leaveapi.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public Employee register(String name, String email) {
        employeeRepository.findByEmail(email).ifPresent(e -> {
            throw new IllegalArgumentException("An employee with this email already exists.");
        });
        return employeeRepository.save(new Employee(name, email));
    }

    public Employee getById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Employee not found: " + id));
    }

    public List<Employee> listAll() {
        return employeeRepository.findAll();
    }

    public Employee requireAdmin(String email) {
        Employee employee = employeeRepository.findByEmail(email)
                .orElseThrow(() -> new SecurityException("No employee found for admin email: " + email));
        if (employee.getRole() != Role.ADMIN) {
            throw new SecurityException("Employee '" + email + "' does not have ADMIN privileges.");
        }
        return employee;
    }
}

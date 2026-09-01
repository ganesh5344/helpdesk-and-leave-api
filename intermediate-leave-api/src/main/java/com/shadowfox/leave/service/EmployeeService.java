package com.shadowfox.leave.service;

import com.shadowfox.leave.model.Employee;
import com.shadowfox.leave.repository.EmployeeRepository;
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
}

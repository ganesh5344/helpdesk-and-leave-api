package com.shadowfox.leave.controller;

import com.shadowfox.leave.dto.CreateEmployeeRequest;
import com.shadowfox.leave.model.Employee;
import com.shadowfox.leave.service.EmployeeService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Employee register(@Valid @RequestBody CreateEmployeeRequest request) {
        return employeeService.register(request.getName(), request.getEmail());
    }

    @GetMapping
    public List<Employee> listAll() {
        return employeeService.listAll();
    }

    @GetMapping("/{id}")
    public Employee getById(@PathVariable Long id) {
        return employeeService.getById(id);
    }
}

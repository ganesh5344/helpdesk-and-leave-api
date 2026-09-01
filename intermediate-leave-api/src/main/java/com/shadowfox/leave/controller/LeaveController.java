package com.shadowfox.leave.controller;

import com.shadowfox.leave.dto.CreateLeaveRequest;
import com.shadowfox.leave.dto.ReviewLeaveRequest;
import com.shadowfox.leave.model.LeaveRequest;
import com.shadowfox.leave.service.EmployeeService;
import com.shadowfox.leave.service.LeaveService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class LeaveController {

    private final LeaveService leaveService;
    private final EmployeeService employeeService;

    public LeaveController(LeaveService leaveService, EmployeeService employeeService) {
        this.leaveService = leaveService;
        this.employeeService = employeeService;
    }

    @PostMapping("/api/employees/{employeeId}/leave-requests")
    @ResponseStatus(HttpStatus.CREATED)
    public LeaveRequest submit(@PathVariable Long employeeId,
                                @Valid @RequestBody CreateLeaveRequest request) {
        return leaveService.submit(employeeId, request.getStartDate(), request.getEndDate(), request.getReason());
    }

    @GetMapping("/api/employees/{employeeId}/leave-requests")
    public List<LeaveRequest> history(@PathVariable Long employeeId) {
        return leaveService.historyFor(employeeId);
    }

    @DeleteMapping("/api/employees/{employeeId}/leave-requests/{leaveId}")
    public LeaveRequest cancel(@PathVariable Long employeeId, @PathVariable Long leaveId) {
        return leaveService.cancel(leaveId, employeeId);
    }

    @GetMapping("/api/admin/leave-requests")
    public List<LeaveRequest> listAll(@RequestHeader("X-Admin-Email") String adminEmail) {
        employeeService.requireAdmin(adminEmail);
        return leaveService.listAll();
    }

    @PatchMapping("/api/admin/leave-requests/{leaveId}")
    public LeaveRequest review(@PathVariable Long leaveId,
                                @RequestHeader("X-Admin-Email") String adminEmail,
                                @Valid @RequestBody ReviewLeaveRequest request) {
        employeeService.requireAdmin(adminEmail);
        return leaveService.review(leaveId, request.getDecision(), request.getComment());
    }
}

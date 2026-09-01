package com.shadowfox.leave.service;

import com.shadowfox.leave.model.Employee;
import com.shadowfox.leave.model.LeaveRequest;
import com.shadowfox.leave.model.LeaveStatus;
import com.shadowfox.leave.repository.LeaveRequestRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class LeaveService {

    private final LeaveRequestRepository leaveRequestRepository;
    private final EmployeeService employeeService;

    public LeaveService(LeaveRequestRepository leaveRequestRepository, EmployeeService employeeService) {
        this.leaveRequestRepository = leaveRequestRepository;
        this.employeeService = employeeService;
    }

    public LeaveRequest submit(Long employeeId, LocalDate startDate, LocalDate endDate, String reason) {
        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("End date cannot be before start date.");
        }
        Employee employee = employeeService.getById(employeeId);
        LeaveRequest request = new LeaveRequest(employee, startDate, endDate, reason);
        long days = request.getDurationInDays();
        if (days > employee.getAnnualLeaveBalance()) {
            throw new IllegalArgumentException(
                "Requested " + days + " day(s) exceeds remaining balance of "
                    + employee.getAnnualLeaveBalance() + " day(s).");
        }
        return leaveRequestRepository.save(request);
    }

    public List<LeaveRequest> historyFor(Long employeeId) {
        employeeService.getById(employeeId);
        return leaveRequestRepository.findByEmployeeId(employeeId);
    }

    public List<LeaveRequest> listAll() {
        return leaveRequestRepository.findAll();
    }

    public LeaveRequest review(Long leaveId, LeaveStatus decision, String comment) {
        if (decision != LeaveStatus.APPROVED && decision != LeaveStatus.REJECTED) {
            throw new IllegalArgumentException("Decision must be APPROVED or REJECTED.");
        }
        LeaveRequest request = getById(leaveId);
        if (request.getStatus() != LeaveStatus.PENDING) {
            throw new IllegalStateException("Only PENDING requests can be reviewed.");
        }
        request.setStatus(decision);
        request.setReviewComment(comment);
        if (decision == LeaveStatus.APPROVED) {
            Employee employee = request.getEmployee();
            employee.setAnnualLeaveBalance(
                (int) (employee.getAnnualLeaveBalance() - request.getDurationInDays()));
        }
        return leaveRequestRepository.save(request);
    }

    public LeaveRequest cancel(Long leaveId, Long employeeId) {
        LeaveRequest request = getById(leaveId);
        if (!request.getEmployee().getId().equals(employeeId)) {
            throw new IllegalStateException("You can only cancel your own leave requests.");
        }
        if (request.getStatus() != LeaveStatus.PENDING) {
            throw new IllegalStateException("Only PENDING requests can be cancelled.");
        }
        request.setStatus(LeaveStatus.CANCELLED);
        return leaveRequestRepository.save(request);
    }

    private LeaveRequest getById(Long id) {
        return leaveRequestRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Leave request not found: " + id));
    }
}

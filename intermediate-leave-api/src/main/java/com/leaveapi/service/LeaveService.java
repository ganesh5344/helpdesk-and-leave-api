package com.leaveapi.service;

import com.leaveapi.model.Employee;
import com.leaveapi.model.LeaveRequest;
import com.leaveapi.model.LeaveStatus;
import com.leaveapi.repository.LeaveRequestRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

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

        boolean overlaps = leaveRequestRepository.findByEmployeeId(employeeId).stream()
                .filter(existing -> existing.getStatus() == LeaveStatus.PENDING
                        || existing.getStatus() == LeaveStatus.APPROVED)
                .anyMatch(existing -> !endDate.isBefore(existing.getStartDate())
                        && !startDate.isAfter(existing.getEndDate()));
        if (overlaps) {
            throw new IllegalArgumentException("You already have a pending or approved leave request that overlaps these dates.");
        }

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

    public int balanceFor(Long employeeId) {
        return employeeService.getById(employeeId).getAnnualLeaveBalance();
    }

    public List<LeaveRequest> listAll(LeaveStatus statusFilter) {
        List<LeaveRequest> all = leaveRequestRepository.findAll();
        if (statusFilter == null) {
            return all;
        }
        return all.stream()
                .filter(request -> request.getStatus() == statusFilter)
                .collect(Collectors.toList());
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

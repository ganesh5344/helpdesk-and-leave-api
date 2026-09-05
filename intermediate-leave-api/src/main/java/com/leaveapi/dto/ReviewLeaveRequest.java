package com.leaveapi.dto;

import com.leaveapi.model.LeaveStatus;
import javax.validation.constraints.NotNull;

public class ReviewLeaveRequest {

    @NotNull(message = "Decision is required (APPROVED or REJECTED)")
    private LeaveStatus decision;

    private String comment;

    public LeaveStatus getDecision() { return decision; }
    public String getComment() { return comment; }

    public void setDecision(LeaveStatus decision) { this.decision = decision; }
    public void setComment(String comment) { this.comment = comment; }
}

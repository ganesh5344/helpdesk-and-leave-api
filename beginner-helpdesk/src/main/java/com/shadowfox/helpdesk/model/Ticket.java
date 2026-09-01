package com.shadowfox.helpdesk.model;

public class Ticket {
    private final int id;
    private String studentName;
    private String title;
    private String description;
    private String category;
    private Priority priority;
    private TicketStatus status;

    public Ticket(int id, String studentName, String title, String description,
                  String category, Priority priority) {
        this.id = id;
        this.studentName = studentName;
        this.title = title;
        this.description = description;
        this.category = category;
        this.priority = priority;
        this.status = TicketStatus.OPEN;
    }

    public int getId() { return id; }
    public String getStudentName() { return studentName; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getCategory() { return category; }
    public Priority getPriority() { return priority; }
    public TicketStatus getStatus() { return status; }

    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setCategory(String category) { this.category = category; }
    public void setPriority(Priority priority) { this.priority = priority; }
    public void setStatus(TicketStatus status) { this.status = status; }

    @Override
    public String toString() {
        return String.format(
            "#%d [%s] %s | Student: %s | Category: %s | Priority: %s | Status: %s",
            id, status, title, studentName, category, priority, status
        );
    }

    public String toDetailedString() {
        return toString() + "\nDescription: " + description;
    }
}

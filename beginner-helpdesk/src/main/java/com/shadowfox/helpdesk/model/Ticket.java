package com.shadowfox.helpdesk.model;

public class Ticket {
    private final int id;
    private final Student student;
    private String title;
    private String description;
    private Category category;
    private Priority priority;
    private TicketStatus status;

    public Ticket(int id, Student student, String title, String description,
                  Category category, Priority priority) {
        this.id = id;
        this.student = student;
        this.title = title;
        this.description = description;
        this.category = category;
        this.priority = priority;
        this.status = TicketStatus.OPEN;
    }

    public int getId() { return id; }
    public Student getStudent() { return student; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public Category getCategory() { return category; }
    public Priority getPriority() { return priority; }
    public TicketStatus getStatus() { return status; }

    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setCategory(Category category) { this.category = category; }
    public void setPriority(Priority priority) { this.priority = priority; }
    public void setStatus(TicketStatus status) { this.status = status; }

    @Override
    public String toString() {
        return String.format(
            "#%d [%s] %s | Student: %s | Category: %s | Priority: %s",
            id, status, title, student.getName(), category, priority
        );
    }

    public String toDetailedString() {
        return toString() + "\nDescription: " + description;
    }
}

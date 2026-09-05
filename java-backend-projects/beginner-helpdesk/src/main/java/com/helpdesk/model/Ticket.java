package com.helpdesk.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Ticket implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final DateTimeFormatter TS_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private final int id;
    private final Student student;
    private String title;
    private String description;
    private Category category;
    private Priority priority;
    private TicketStatus status;
    private final List<String> history = new ArrayList<>();

    public Ticket(int id, Student student, String title, String description,
                  Category category, Priority priority) {
        this.id = id;
        this.student = student;
        this.title = title;
        this.description = description;
        this.category = category;
        this.priority = priority;
        this.status = TicketStatus.OPEN;
        log("Ticket created");
    }

    public int getId() { return id; }
    public Student getStudent() { return student; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public Category getCategory() { return category; }
    public Priority getPriority() { return priority; }
    public TicketStatus getStatus() { return status; }
    public List<String> getHistory() { return Collections.unmodifiableList(history); }

    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setCategory(Category category) { this.category = category; }
    public void setPriority(Priority priority) { this.priority = priority; }
    public void setStatus(TicketStatus status) { this.status = status; }

    public void log(String message) {
        history.add(LocalDateTime.now().format(TS_FORMAT) + " - " + message);
    }

    @Override
    public String toString() {
        return String.format(
            "#%d [%s] %s | Student: %s | Category: %s | Priority: %s",
            id, status, title, student.getName(), category, priority
        );
    }

    public String toDetailedString() {
        StringBuilder sb = new StringBuilder(toString());
        sb.append("\nDescription: ").append(description);
        sb.append("\nHistory:");
        for (String entry : history) {
            sb.append("\n  - ").append(entry);
        }
        return sb.toString();
    }
}

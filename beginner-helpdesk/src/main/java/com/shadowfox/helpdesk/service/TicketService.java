package com.shadowfox.helpdesk.service;

import com.shadowfox.helpdesk.model.Category;
import com.shadowfox.helpdesk.model.Priority;
import com.shadowfox.helpdesk.model.Student;
import com.shadowfox.helpdesk.model.Ticket;
import com.shadowfox.helpdesk.model.TicketStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TicketService {
    private final List<Ticket> tickets = new ArrayList<>();
    private final List<Student> students = new ArrayList<>();
    private int nextTicketId = 1;
    private int nextStudentId = 1;

    public Student findOrCreateStudent(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Student name cannot be empty.");
        }
        return students.stream()
                .filter(s -> s.getName().equalsIgnoreCase(name.trim()))
                .findFirst()
                .orElseGet(() -> {
                    Student created = new Student(nextStudentId++, name.trim());
                    students.add(created);
                    return created;
                });
    }

    public Ticket createTicket(Student student, String title, String description,
                                Category category, Priority priority) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be empty.");
        }
        Ticket ticket = new Ticket(nextTicketId++, student, title, description, category, priority);
        tickets.add(ticket);
        return ticket;
    }

    public List<Ticket> listAll() {
        return new ArrayList<>(tickets);
    }

    public Optional<Ticket> findById(int id) {
        return tickets.stream().filter(t -> t.getId() == id).findFirst();
    }

    public List<Ticket> search(String keyword) {
        String lower = keyword.toLowerCase();
        List<Ticket> results = new ArrayList<>();
        for (Ticket t : tickets) {
            if (t.getTitle().toLowerCase().contains(lower)
                    || t.getStudent().getName().toLowerCase().contains(lower)
                    || t.getCategory().name().toLowerCase().contains(lower)) {
                results.add(t);
            }
        }
        return results;
    }

    public void updateTicket(int id, String title, String description,
                              Category category, Priority priority) {
        Ticket ticket = requireNotClosed(id);
        if (title != null && !title.trim().isEmpty()) {
            ticket.setTitle(title);
        }
        if (description != null) {
            ticket.setDescription(description);
        }
        if (category != null) {
            ticket.setCategory(category);
        }
        if (priority != null) {
            ticket.setPriority(priority);
        }
    }

    public void changeStatus(int id, TicketStatus newStatus) {
        Ticket ticket = findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Ticket not found: " + id));

        if (ticket.getStatus() == TicketStatus.CLOSED) {
            throw new IllegalStateException("Cannot change status of a closed ticket.");
        }
        if (newStatus == TicketStatus.CLOSED && ticket.getStatus() != TicketStatus.RESOLVED) {
            throw new IllegalStateException("A ticket must be RESOLVED before it can be CLOSED.");
        }
        ticket.setStatus(newStatus);
    }

    private Ticket requireNotClosed(int id) {
        Ticket ticket = findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Ticket not found: " + id));
        if (ticket.getStatus() == TicketStatus.CLOSED) {
            throw new IllegalStateException("Cannot modify a closed ticket.");
        }
        return ticket;
    }
}

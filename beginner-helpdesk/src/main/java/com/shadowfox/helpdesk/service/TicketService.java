package com.shadowfox.helpdesk.service;

import com.shadowfox.helpdesk.model.Priority;
import com.shadowfox.helpdesk.model.Ticket;
import com.shadowfox.helpdesk.model.TicketStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TicketService {
    private final List<Ticket> tickets = new ArrayList<>();
    private int nextId = 1;

    public Ticket createTicket(String studentName, String title, String description,
                                String category, Priority priority) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be empty.");
        }
        if (studentName == null || studentName.trim().isEmpty()) {
            throw new IllegalArgumentException("Student name cannot be empty.");
        }
        Ticket ticket = new Ticket(nextId++, studentName, title, description, category, priority);
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
                    || t.getStudentName().toLowerCase().contains(lower)
                    || t.getCategory().toLowerCase().contains(lower)) {
                results.add(t);
            }
        }
        return results;
    }

    public void updateTicket(int id, String title, String description,
                              String category, Priority priority) {
        Ticket ticket = requireOpenOrInProgress(id);
        if (title != null && !title.trim().isEmpty()) {
            ticket.setTitle(title);
        }
        if (description != null) {
            ticket.setDescription(description);
        }
        if (category != null && !category.trim().isEmpty()) {
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

    private Ticket requireOpenOrInProgress(int id) {
        Ticket ticket = findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Ticket not found: " + id));
        if (ticket.getStatus() == TicketStatus.CLOSED) {
            throw new IllegalStateException("Cannot modify a closed ticket.");
        }
        return ticket;
    }
}

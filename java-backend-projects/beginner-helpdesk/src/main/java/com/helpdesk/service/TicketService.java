package com.helpdesk.service;

import com.helpdesk.model.Category;
import com.helpdesk.model.Priority;
import com.helpdesk.model.Student;
import com.helpdesk.model.Ticket;
import com.helpdesk.model.TicketStatus;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class TicketService {
    private final TicketRepository repository = new TicketRepository();
    private final List<Ticket> tickets;
    private final List<Student> students;
    private int nextTicketId;
    private int nextStudentId;

    public TicketService() {
        TicketRepository.StoredData data = repository.load();
        this.tickets = data.tickets;
        this.students = data.students;
        this.nextTicketId = data.nextTicketId;
        this.nextStudentId = data.nextStudentId;
    }

    private void persist() {
        repository.save(tickets, students, nextTicketId, nextStudentId);
    }

    public Student findOrCreateStudent(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Student name cannot be empty.");
        }
        Student existing = students.stream()
                .filter(s -> s.getName().equalsIgnoreCase(name.trim()))
                .findFirst()
                .orElse(null);
        if (existing != null) {
            return existing;
        }
        Student created = new Student(nextStudentId++, name.trim());
        students.add(created);
        persist();
        return created;
    }

    public Ticket createTicket(Student student, String title, String description,
                                Category category, Priority priority) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be empty.");
        }
        Ticket ticket = new Ticket(nextTicketId++, student, title, description, category, priority);
        tickets.add(ticket);
        persist();
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
        ticket.log("Ticket details updated");
        persist();
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
        TicketStatus previous = ticket.getStatus();
        ticket.setStatus(newStatus);
        ticket.log("Status changed: " + previous + " -> " + newStatus);
        persist();
    }

    public Map<TicketStatus, Long> statusSummary() {
        Map<TicketStatus, Long> summary = new EnumMap<>(TicketStatus.class);
        for (TicketStatus status : TicketStatus.values()) {
            summary.put(status, 0L);
        }
        for (Ticket ticket : tickets) {
            summary.merge(ticket.getStatus(), 1L, Long::sum);
        }
        return summary;
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
